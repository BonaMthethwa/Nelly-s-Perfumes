package com.perfumes.perfumeswebapp.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @SuppressWarnings("null")
    @Override
    protected String getDatabaseName() {
        return "db";
    }

    @SuppressWarnings("null")
    @Override
    public MongoClient mongoClient() {
        return MongoClients
                .create("mongodb+srv://user:123@db.ekiqbhl.mongodb.net/?retryWrites=true&w=majority&appName=db");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName()));
    }
}
