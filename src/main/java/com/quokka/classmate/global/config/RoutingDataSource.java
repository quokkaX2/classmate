package com.quokka.classmate.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final CloudWatchMetricsFetcher metricsFetcher;

    public RoutingDataSource(CloudWatchMetricsFetcher metricsFetcher) {
        this.metricsFetcher = metricsFetcher;
    }

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            double replica1Cpu = metricsFetcher.getAverageCPUUtilization("replica-1", 300);
            double replica2Cpu = metricsFetcher.getAverageCPUUtilization("replica-2", 300);

            String selectedReplica = replica1Cpu <= replica2Cpu ? "replica" : "replica2";
            log.info("CPU Utilization - Replica1: {}, Replica2: {}", replica1Cpu, replica2Cpu);
            log.info("Selected Replica: {}", selectedReplica);  // 로그 추가

            return selectedReplica;
        }
        return "master";
    }
}