package com.quokka.classmate.replication;

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
            double replica1Cpu = metricsFetcher.getAverageCPUUtilization("replica1", 300);
            double replica2Cpu = metricsFetcher.getAverageCPUUtilization("replica2", 300);
            double replica3Cpu = metricsFetcher.getAverageCPUUtilization("replica3", 300);

            log.info("CPU Utilization - Replica1: {}, Replica2: {}, Replica3: {}", replica1Cpu, replica2Cpu, replica3Cpu);

            if (replica1Cpu <= replica2Cpu && replica1Cpu <= replica3Cpu) {
                log.info("use - replica1");
                return "replica1";
            } else if (replica2Cpu <= replica1Cpu && replica2Cpu <= replica3Cpu) {
                log.info("use - replica2");
                return "replica2";
            } else {
                log.info("use - replica3");
                return "replica3";
            }
        }
        log.info("use - master");
        return "master";
    }
}