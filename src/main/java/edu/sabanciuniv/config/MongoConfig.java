package edu.sabanciuniv.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import edu.sabanciuniv.model.User;

@Configuration
public class MongoConfig {

    @Bean
    CommandLineRunner initIndexes(MongoTemplate mongoTemplate) {
        return args -> {
            mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("username", org.springframework.data.domain.Sort.Direction.ASC).unique());
            mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("email", org.springframework.data.domain.Sort.Direction.ASC).unique());
        };
    }
}

