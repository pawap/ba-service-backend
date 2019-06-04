package sentiments;

import mongo.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TweetRepositoryMongo extends MongoRepository<Tweet, Integer> {


}
