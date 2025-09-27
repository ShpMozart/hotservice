package com.mozart.hotservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_metrics_agg")
public class ServiceMetricsAgg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;
    private String serviceName;
    private String apiPath;
    private LocalDateTime windowStart;
    private LocalDateTime windowEnd;

    private Double avgDurationMs;
    private Double p95DurationMs;
    private Long requestCount;
    private Double errorRate;
    private Double avgProcessCpu;
    private Double avgSystemCpu;
    private Double avgUsedMem;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public LocalDateTime getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(LocalDateTime windowStart) {
        this.windowStart = windowStart;
    }

    public LocalDateTime getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(LocalDateTime windowEnd) {
        this.windowEnd = windowEnd;
    }

    public Double getAvgDurationMs() {
        return avgDurationMs;
    }

    public void setAvgDurationMs(Double avgDurationMs) {
        this.avgDurationMs = avgDurationMs;
    }

    public Double getP95DurationMs() {
        return p95DurationMs;
    }

    public void setP95DurationMs(Double p95DurationMs) {
        this.p95DurationMs = p95DurationMs;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public Double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(Double errorRate) {
        this.errorRate = errorRate;
    }

    public Double getAvgProcessCpu() {
        return avgProcessCpu;
    }

    public void setAvgProcessCpu(Double avgProcessCpu) {
        this.avgProcessCpu = avgProcessCpu;
    }

    public Double getAvgSystemCpu() {
        return avgSystemCpu;
    }

    public void setAvgSystemCpu(Double avgSystemCpu) {
        this.avgSystemCpu = avgSystemCpu;
    }

    public Double getAvgUsedMem() {
        return avgUsedMem;
    }

    public void setAvgUsedMem(Double avgUsedMem) {
        this.avgUsedMem = avgUsedMem;
    }

}
