package com.kotojava.lesson3.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.net.URI;

@Configuration
public class S3Config {

    public static final String BUCKET_NAME = "applications";

    @Bean
    public S3Client s3Client() {
        S3Client client = S3Client.builder()
                .endpointOverride(URI.create("http://158.220.122.4:9000"))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("minioadmin", "minioadmin")))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();

        createBucketIfNotExists(client);
        return client;
    }

    private void createBucketIfNotExists(S3Client client) {
        try {
            client.headBucket(b -> b.bucket(BUCKET_NAME));
        } catch (NoSuchBucketException e) {
            client.createBucket(b -> b.bucket(BUCKET_NAME));
        }
    }
}