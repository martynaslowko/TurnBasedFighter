package org.mslowko.turnbasedfighter.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.mslowko.turnbasedfighter.model")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Getter(AccessLevel.PROTECTED)
    @Value("${spring.data.mongodb.database}")
    protected String databaseName;

}
