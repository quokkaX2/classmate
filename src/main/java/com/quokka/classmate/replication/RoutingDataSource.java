package com.quokka.classmate.replication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.Random;

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
        //     double replica1Cpu = metricsFetcher.getAverageCPUUtilization("replica1", 300);
        //     double replica2Cpu = metricsFetcher.getAverageCPUUtilization("replica2", 300);
        //     double replica3Cpu = metricsFetcher.getAverageCPUUtilization("replica3", 300);

            log.info("CPU Utilization - Replica1: {}, Replica2: {}, Replica3: {}", replica1Cpu, replica2Cpu, replica3Cpu);

            Random random = new Random();
            int randomReplica = random.nextInt(3) + 1; // 1, 2, 3 중에서 랜덤하게 선택
    
            switch (randomReplica) {
                case 1:
                    log.info("use - replica1");
                    return "replica1";
                case 2:
                    log.info("use - replica2");
                    return "replica2";
                case 3:
                    log.info("use - replica3");
                    return "replica3";
                default:
                    // 예기치 못한 경우에는 기본값으로 master를 사용합니다.
                    log.warn("use - master");
                    return "master";
            }
        }
        log.info("use - master");
        return "master";
    }
}
