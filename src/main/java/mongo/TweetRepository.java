package mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TweetRepository extends MongoRepository<Tweet, Integer> {


}
