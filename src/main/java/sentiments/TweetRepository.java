package sentiments;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Paw
 *
 */
public interface TweetRepository extends CrudRepository<Tweet, Integer> {
	
	@Query("from Tweet where train=:train and offensive=:offensive")
	public Iterable<Tweet> findAllByTrainAndOffensive(@Param("train") Boolean train, @Param("offensive") Boolean offensive);

	@Query("select count(*) from Tweet where train=:train")
	public int count(@Param("train") boolean train);
}
