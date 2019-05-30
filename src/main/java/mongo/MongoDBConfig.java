package mongo;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.sql.Timestamp;

@EnableMongoRepositories(basePackageClasses = TweetRepository.class)
@Configuration
public class MongoDBConfig {

    @Bean
    CommandLineRunner commandLineRunner(TweetRepository tweetRepository) {
        return strings -> {
            tweetRepository.save(new Tweet(1,"ein tweet",new Timestamp(100),new Timestamp(200)));
            tweetRepository.save(new Tweet(2,"noch ein tweet",new Timestamp(200),new Timestamp(300)));
        };

       }
}
