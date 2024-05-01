package com.quokka.classmate.replication;

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
    private static final String REPLICA1_SERVER = "REPLICA1";
    private static final String REPLICA2_SERVER = "REPLICA2";
    private static final String REPLICA3_SERVER = "REPLICA3";

    @Bean
    @Qualifier(MASTER_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(REPLICA1_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.replica1")
    public DataSource replica1DataSource() {
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
    @Qualifier(REPLICA3_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.replica3")
    public DataSource replica3DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(MASTER_SERVER) DataSource masterDataSource,
            @Qualifier(REPLICA1_SERVER) DataSource replica1DataSource,
            @Qualifier(REPLICA2_SERVER) DataSource replica2DataSource,
            @Qualifier(REPLICA3_SERVER) DataSource replica3DataSource
    ) {
        CloudWatchMetricsFetcher metricsFetcher = new CloudWatchMetricsFetcher();
        RoutingDataSource routingDataSource = new RoutingDataSource(metricsFetcher);

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource);
        dataSourceMap.put("replica1", replica1DataSource);
        dataSourceMap.put("replica2", replica2DataSource);
        dataSourceMap.put("replica3", replica3DataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource(masterDataSource(), replica1DataSource(), replica2DataSource(), replica3DataSource()));
    }

}
