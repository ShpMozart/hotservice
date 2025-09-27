package com.mozart.hotservice.repository;

import com.mozart.hotservice.entity.ServiceMetricsAgg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceMetricsAggRepository extends JpaRepository<ServiceMetricsAgg, Long> {
}
