package mongo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/rest/tweets")
public class TweetResource {


    private TweetRepository tweetRepository;

    public TweetResource(TweetRepository tweetRepository){
        this.tweetRepository = tweetRepository;
    }

    @GetMapping("/all")
    public List<Tweet> getAll(){
        return tweetRepository.findAll();
    }

}