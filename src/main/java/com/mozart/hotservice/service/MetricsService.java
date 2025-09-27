package com.mozart.hotservice.service;

import com.mozart.hotservice.dto.MetricsDTO;
import com.mozart.hotservice.entity.Metrics;
import com.mozart.hotservice.repository.MetricsRepository;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {
    private final MetricsRepository metricsRepository;

    public MetricsService(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    public void saveMetrics(MetricsDTO metricsDTO) {
        Metrics metrics = new Metrics(
                 metricsDTO.getGroupName()
                ,metricsDTO.getServiceName()
                ,metricsDTO.getApiPath()
                ,metricsDTO.getDuration_ms()
                ,metricsDTO.getStatusCode()
                ,metricsDTO.getProcessCpu()
                ,metricsDTO.getSystemCpu()
                ,metricsDTO.getUsedMemMB()
                ,metricsDTO.getTotalMemMB()
        );
        metricsRepository.save(metrics);
    }
}
