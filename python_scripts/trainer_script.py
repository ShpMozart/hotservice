import pandas as pd
from sqlalchemy import create_engine
from sklearn.ensemble import RandomForestClassifier
import joblib
import time
import json
import os

DB_USER = "postgres"
DB_PASSWORD = ""
DB_HOST = "localhost"
DB_PORT = "5432"
DB_NAME = "hsp"

db_connection_str = f'postgresql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}'
db_engine = create_engine(db_connection_str)


def get_db_count(engine):
    query = "SELECT COUNT(*) FROM service_metrics_agg"
    count_df = pd.read_sql_query(query, engine)
    return count_df.iloc[0, 0]

def fetch_all_data_from_db(engine):
    print("Fetching all records from the database...")
    query = "SELECT * FROM service_metrics_agg ORDER BY window_start"
    df = pd.read_sql_query(query, engine)
    print(f"Successfully fetched {len(df)} records.")
    return df

def label_data(df):
    print("Labeling data...")
    df['future_duration'] = df['avg_duration_ms'].shift(-15)

    HOTSPOT_THRESHOLD = 500
    df['label'] = (df['future_duration'] > HOTSPOT_THRESHOLD).astype(int)

    df = df.dropna(subset=['label', 'future_duration'])
    print("Labeling complete.")
    return df

def read_status_file(file_path='training_status.json'):
    try:
        with open(file_path, 'r') as f:
            status = json.load(f)
            return status.get('last_trained_count', 0)
    except FileNotFoundError:
        return 0

def update_status_file(count, file_path='training_status.json'):
    with open(file_path, 'w') as f:
        status_data = {'last_trained_count': int(count)}
        json.dump(status_data, f)
        f.flush()
        os.fsync(f.fileno())

while True:
    last_trained_count = read_status_file()
    current_db_count = get_db_count(db_engine)

    print(f"Checking for training... Current records: {current_db_count}, Last trained at: {last_trained_count}")

    if current_db_count >= last_trained_count + 50:
        print(f"Threshold reached! Starting training with {current_db_count} records...")

        all_data = fetch_all_data_from_db(db_engine)
        labeled_data = label_data(all_data)

        features = labeled_data[['request_count', 'avg_duration_ms', 'p95duration_ms', 'error_rate', 'avg_process_cpu', 'avg_used_mem']]
        labels = labeled_data['label']

        print("Training RandomForest model...")
        model = RandomForestClassifier(n_estimators=100, random_state=42, class_weight='balanced')
        model.fit(features, labels)

        joblib.dump(model, 'hotspot_model.joblib')
        update_status_file(current_db_count)

        print("Training complete and model saved!")

    else:
        print("Not enough new data for training. Checking again in 10 minutes.")

    time.sleep(600)