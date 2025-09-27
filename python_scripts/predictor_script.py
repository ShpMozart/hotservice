import joblib
import pandas as pd
import time
from sqlalchemy import create_engine
from datetime import datetime, timedelta

DB_USER = "postgres"
DB_PASSWORD = ""
DB_HOST = "localhost"
DB_PORT = "5432"
DB_NAME = "hsp"

db_connection_str = f'postgresql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}'
db_engine = create_engine(db_connection_str)


def fetch_latest_minute_data(engine):
    query = """
        SELECT * FROM service_metrics_agg
        WHERE window_start >= NOW() - INTERVAL '2 minutes'
    """
    try:
        df = pd.read_sql_query(query, engine)
        return df
    except Exception as e:
        print(f"Error fetching data from database: {e}")
        return pd.DataFrame()

try:
    model = joblib.load('hotspot_model.joblib')
    model_features = model.feature_names_in_
    print("✅ Model loaded successfully.")
except FileNotFoundError:
    print("❌ Error: Model file 'hotspot_model.joblib' not found. Make sure the trainer has run at least once.")
    exit()

while True:
    latest_data = fetch_latest_minute_data(db_engine)

    if not latest_data.empty:
        print(f"Fetched {len(latest_data)} new records for prediction.")

        features_to_predict = latest_data.copy()

        live_features = pd.get_dummies(features_to_predict[['request_count', 'avg_duration_ms', 'p95duration_ms', 'error_rate', 'avg_process_cpu', 'avg_system_cpu', 'avg_used_mem', 'api_path', 'service_name', 'group_name']])

        live_features = live_features.reindex(columns=model_features, fill_value=0)

        probabilities = model.predict_proba(live_features)

        for i in range(len(live_features)):
            row = latest_data.iloc[i]

            hotspot_prob = 0.0

            if probabilities.shape[1] == 2:
                hotspot_prob = probabilities[i][1]
            elif model.classes_[0] == 1:
                hotspot_prob = probabilities[i][0]

            service = row['service_name']
            api = row['api_path']
            print(f"[{row['window_start']}] Service: {service}, API: {api}, Hotspot Probability: {hotspot_prob:.2%}")

            if hotspot_prob > 0.70:
                print(f"🚨 WARNING: High probability ({hotspot_prob:.2%}) of hotspot for service '{service}' on api '{api}' in the next 15 mins!")
    else:
        print("No new data to predict. Waiting...")

    time.sleep(5)