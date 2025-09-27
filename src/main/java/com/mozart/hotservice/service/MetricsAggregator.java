package com.mozart.hotservice.service;

import com.mozart.hotservice.entity.ServiceMetricsAgg;
import com.mozart.hotservice.repository.ServiceMetricsAggRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetricsAggregator {

    private final JdbcTemplate jdbcTemplate;
    private final ServiceMetricsAggRepository repository;

    public MetricsAggregator(JdbcTemplate jdbcTemplate, ServiceMetricsAggRepository repository) {
        this.jdbcTemplate = jdbcTemplate;
        this.repository = repository;
    }

    @Scheduled(fixedRate = 60000)
    public void aggregateMetrics() {
        System.out.println("START Metrics Aggregation...");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.minusMinutes(1);

        String sql = """
            SELECT group_name,
                   service_name,
                   api_path,
                   AVG(duration_ms) as avg_duration,
                   PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY duration_ms) as p95_duration,
                   COUNT(*) as request_count,
                   SUM(CASE WHEN status_code != 200 THEN 1 ELSE 0 END)::float / COUNT(*) as error_rate,
                   AVG(process_cpu) as avg_process_cpu,
                   AVG(system_cpu) as avg_system_cpu,
                   AVG(used_memmb) as avg_used_mem
            FROM metrics
            WHERE created_at BETWEEN ? AND ?
            GROUP BY group_name, service_name, api_path
        """;

        List<ServiceMetricsAgg> result = jdbcTemplate.query(sql,
            new Object[]{windowStart, now},
            (rs, rowNum) -> {
                System.out.println("Processing row " + rowNum);
                System.out.println(rs.getString("group_name") + ", " + rs.getString("service_name") + ", " + rs.getString("api_path"));

                ServiceMetricsAgg agg = new ServiceMetricsAgg();
                agg.setGroupName(rs.getString("group_name"));
                agg.setServiceName(rs.getString("service_name"));
                agg.setApiPath(rs.getString("api_path"));
                agg.setWindowStart(windowStart);
                agg.setWindowEnd(now);
                agg.setAvgDurationMs(rs.getDouble("avg_duration"));
                agg.setP95DurationMs(rs.getDouble("p95_duration"));
                agg.setRequestCount(rs.getLong("request_count"));
                agg.setErrorRate(rs.getDouble("error_rate"));
                agg.setAvgProcessCpu(rs.getDouble("avg_process_cpu"));
                agg.setAvgSystemCpu(rs.getDouble("avg_system_cpu"));
                agg.setAvgUsedMem(rs.getDouble("avg_used_mem"));
                return agg;
            });

        repository.saveAll(result);
    }
}
