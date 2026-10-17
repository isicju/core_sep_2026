package com.kotojava.lesson3.repository;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class MyPostgresConfig {

    @Bean
    DataSource loanApplicationsDatasource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{"158.220.122.4"});
        dataSource.setPortNumbers(new int[]{5439});
        dataSource.setDatabaseName("mortgage");
        dataSource.setUser("mortgage");
        dataSource.setPassword("mortgage");
        return dataSource;
    }

    @Bean
    JdbcTemplate loanApplicationTemplate(DataSource loanApplicationsDatasource) {
        return new JdbcTemplate(loanApplicationsDatasource);
    }

}
