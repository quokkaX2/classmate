package com.quokka.classmate.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        String lookupKey = TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "replica" : "master";
        log.info("Current DataSource is {}", lookupKey);
        return lookupKey;
    }
}