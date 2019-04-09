package sentiments;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * @author Paw, 6runge
 * 
 * Dummy App-Controller for determining the initial project architecture
 *
 */
@RestController
@EnableAutoConfiguration
public class ApplicationController {
	private static TweetClassifier tc;

    @RequestMapping("/sentiments")
    String home(@RequestParam(value = "tweet", defaultValue = "") String tweet, @RequestParam(value = "format", defaultValue = "text") String format) {

        String cleanTweet = tweet.replace("\r", " ").replace("\n", " ").trim();
        System.out.println("tweet:" + cleanTweet);
        String cleanFormat = format.replace("\r", " ").replace("\n", " ").trim();

        if (cleanFormat.compareTo("json") == 0) {
            return generateJSONResponse(cleanTweet);
        } else {
            return generateTextResponse(cleanTweet);
        }
    }

    private String generateJSONResponse(String input) {
        JSONObject out = new JSONObject();
        out.put("input", input);

        JSONArray sentiments = new JSONArray();

        sentiments.add(tc.classifyTweet(input));
        
        out.put("sentiments", sentiments);

        return out.toString();
    }

    private String generateTextResponse(String input) {
        StringBuilder output = new StringBuilder();

        output.append("input: " + input);
        output.append("\nsentiments:");
        output.append(tc.classifyTweet(input));
 
        return output.toString();
    }

    /**
     * Runs the RESTful server.
     *
     * @param args execution arguments
     */
    public static void main(String[] args) {
    	tc = new TweetClassifier();
        SpringApplication.run(ApplicationController.class, args);
    }

}