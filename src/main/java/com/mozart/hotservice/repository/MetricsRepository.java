package com.mozart.hotservice.repository;

import com.mozart.hotservice.entity.Metrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricsRepository extends JpaRepository<Metrics,Long> {
}
