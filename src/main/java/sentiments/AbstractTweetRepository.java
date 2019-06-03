package sentiments;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.CrudRepository;

@NoRepositoryBean
public interface AbstractTweetRepository<T extends AbstractTweet, Long> extends CrudRepository<T, Long>{

    @Query("select e from #{#entityName} as e from equipment where e.name = equipmentName")
    public abstract T findEquipmentByName(String equipmentName);

}
