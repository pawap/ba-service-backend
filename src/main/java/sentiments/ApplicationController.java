package sentiments;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Paw, 6runge
 * 
 * Dummy App-Controller for determining the initial project architecture
 *
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
public class ApplicationController implements SentimentAnalysisWebInterface{
	private static TweetClassifier tc;

	@Autowired
	Environment env;
	
	@Autowired
	BasicDataImporter basicDataImporter;

    @RequestMapping("/sentiments")
	public ResponseEntity<String> home(@RequestParam(value = "tweet", defaultValue = "") String tweet, @RequestParam(value = "format", defaultValue = "text") String format) {
        String cleanTweet = tweet.replace("\r", " ").replace("\n", " ").trim();
        System.out.println("tweet:" + cleanTweet);
        String cleanFormat = format.replace("\r", " ").replace("\n", " ").trim();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        String response;
        if (cleanFormat.compareTo("json") == 0) {
        	response = generateJSONResponse(cleanTweet);
        } else {
        	response = generateTextResponse(cleanTweet);
        }
       
        return new ResponseEntity<String>(response, responseHeaders,HttpStatus.CREATED);
    }

    @RequestMapping("/html")
	public ResponseEntity<String> html() {
    	String htmlFile = "html-tester/Server-Test-Sentiments.html";
        BufferedReader br = null;
        String line = "";
        String response = "";
        try {
            br = new BufferedReader(new FileReader(htmlFile));
            while ((line = br.readLine()) != null) {
            	//System.out.println(line);
                response += "\n" + line; 

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    	
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
       
        return new ResponseEntity<String>(response, responseHeaders,HttpStatus.CREATED);
    }
    
    @RequestMapping("/import")
	public ResponseEntity<String> tweetimport() {
    	
    	this.basicDataImporter.importFromJson();
    	
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
       
        return new ResponseEntity<String>("finished", responseHeaders,HttpStatus.CREATED);
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

	@Override
	public ResponseEntity<String> offensivityStatistics() {
        JSONObject response = new JSONObject();
        response.put("offensive", Math.random() * 100);
		
        return new ResponseEntity<String>(response.toString(), HttpStatus.CREATED);
	}

}
