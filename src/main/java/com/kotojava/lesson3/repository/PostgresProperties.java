package com.kotojava.lesson3.repository;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "postgres")
public class PostgresProperties {
    private String hostname;
    private int port;
    private String database;
    private String user;
    private String password;
}