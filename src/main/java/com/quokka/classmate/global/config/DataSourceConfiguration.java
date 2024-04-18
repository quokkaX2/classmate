package com.quokka.classmate.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Configuration
public class DataSourceConfiguration {

    private static final String MASTER_SERVER = "MASTER";
    private static final String REPLICA_SERVER = "REPLICA";
    private static final String REPLICA2_SERVER = "REPLICA2";

    @Bean
    @Qualifier(MASTER_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(REPLICA_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.replica")
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(REPLICA2_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.replica2")
    public DataSource replica2DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(MASTER_SERVER) DataSource masterDataSource,
            @Qualifier(REPLICA_SERVER) DataSource replicaDataSource,
            @Qualifier(REPLICA2_SERVER) DataSource replica2DataSource
    ) {
        CloudWatchMetricsFetcher metricsFetcher = new CloudWatchMetricsFetcher();
        RoutingDataSource routingDataSource = new RoutingDataSource(metricsFetcher);

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource);
        dataSourceMap.put("replica", replicaDataSource);
        dataSourceMap.put("replica2", replica2DataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource(masterDataSource(), replicaDataSource(), replica2DataSource()));
    }

}