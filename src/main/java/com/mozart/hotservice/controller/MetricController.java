package com.mozart.hotservice.controller;


import com.mozart.hotservice.dto.MetricsDTO;
import com.mozart.hotservice.service.MetricsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricController {

    private final MetricsService metricsService;

    public MetricController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @PostMapping()
    public void saveMetric(@RequestBody MetricsDTO metricsDTO) {
        metricsService.saveMetrics(metricsDTO);
    }

}
