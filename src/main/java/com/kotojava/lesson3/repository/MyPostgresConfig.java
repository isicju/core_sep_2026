package com.kotojava.lesson3.repository;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties(PostgresProperties.class)
public class MyPostgresConfig {

    @Bean
    DataSource loanApplicationsDatasource(PostgresProperties properties) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{properties.getHostname()});
        dataSource.setPortNumbers(new int[]{properties.getPort()});
        dataSource.setDatabaseName(properties.getDatabase());
        dataSource.setUser(properties.getUser());
        dataSource.setPassword(properties.getPassword());
        return dataSource;
    }

    @Bean
    JdbcTemplate loanApplicationTemplate(DataSource loanApplicationsDatasource) {
        return new JdbcTemplate(loanApplicationsDatasource);
    }



}
