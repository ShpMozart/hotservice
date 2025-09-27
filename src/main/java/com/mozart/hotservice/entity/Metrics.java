package com.mozart.hotservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Metrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String groupName;
    private String serviceName;
    private String apiPath;
    private long duration_ms;
    private int statusCode;
    private double processCpu;
    private double systemCpu;
    private double usedMemMB;
    private double totalMemMB;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Metrics(String groupName, String serviceName, String apiPath, long duration_ms, int statusCode, double processCpu, double systemCpu, double usedMemMB, double totalMemMB) {
        this.groupName = groupName;
        this.serviceName = serviceName;
        this.apiPath = apiPath;
        this.duration_ms = duration_ms;
        this.statusCode = statusCode;
        this.processCpu = processCpu;
        this.systemCpu = systemCpu;
        this.usedMemMB = usedMemMB;
        this.totalMemMB = totalMemMB;
    }

    public Metrics() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(long duration_ms) {
        this.duration_ms = duration_ms;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public double getProcessCpu() {
        return processCpu;
    }

    public void setProcessCpu(int processCpu) {
        this.processCpu = processCpu;
    }

    public double getSystemCpu() {
        return systemCpu;
    }

    public void setSystemCpu(int systemCpu) {
        this.systemCpu = systemCpu;
    }

    public double getUsedMemMB() {
        return usedMemMB;
    }

    public void setUsedMemMB(int usedMemMB) {
        this.usedMemMB = usedMemMB;
    }

    public double getTotalMemMB() {
        return totalMemMB;
    }

    public void setTotalMemMB(int totalMemMB) {
        this.totalMemMB = totalMemMB;
    }


}
