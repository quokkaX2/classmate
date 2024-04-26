package com.quokka.classmate.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.quokka.classmate.repository")
public class ElasticConfig extends ElasticsearchConfiguration {

//    @Value("${spring.data.elasticsearch.url}")
    @Value("${spring.elasticsearch.uris}")
    private String elasticUrl;

    @Value("{spring.elasticsearch.username")
    private String username;

    @Value("{spring.elasticsearch.password")
    private String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticUrl)
                .withBasicAuth(username, password)
                .build();
    }
}
