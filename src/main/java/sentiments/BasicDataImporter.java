package sentiments;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.Language;



/**
 * @author Paw
 *
 */
@Service
public class BasicDataImporter {

	@Autowired
	Environment env;
	
	@Autowired
	TweetRepository tweetRepository; 
	
	public BasicDataImporter() {
		super();
	}

	final LanguageDetector detector = LanguageDetectorBuilder.fromAllBuiltInSpokenLanguages().build();


	public void importFromJson() {
		String jsonPath = this.env.getProperty("localTweetJson");
		try {
			InputStream stream = new FileInputStream(jsonPath);
			JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
	        Gson gson = new GsonBuilder().create();
	        reader.setLenient(true);

	        int i = 0;
	        List<Tweet> tweets = new LinkedList();
	        while (reader.hasNext()) {
	            // Read data into object model
	        	try {
	        		if (reader.peek() == JsonToken.END_DOCUMENT) {
	        			break;
	        		}
	        		JsonElement element = gson.fromJson(reader, JsonElement.class);
	        		JsonObject object = element.getAsJsonObject();
	        		Tweet tweet = this.mapJsonToTweet(object);
	        		if (tweet != null && tweet.getText() != null) {
	        			i++;
	        			tweets.add(tweet);
	        		}
	        	} catch (IllegalStateException | JsonSyntaxException e) {
	        		reader.skipValue();
	        	}
	        	// persist tweets in batch (256 per insert)
	        	if (i % 256 == 0) {
	        		this.tweetRepository.save(tweets);
	        		tweets.clear();
	        	} 
	        }
	        this.tweetRepository.save(tweets);
	        reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private Tweet mapJsonToTweet(JsonObject object) throws IOException {
		Tweet tweet = new Tweet();
		if (object.has("text")) {
			tweet.setText(object.get("text").getAsString());
			final Language detectedLanguage = detector.detectLanguageOf(tweet.getText().replaceAll("(((RT )?@[\\w_-]+[:]?)|((https?:\\/\\/)[\\w\\d.-\\/]*))",""));
			tweet.setLanguage(detectedLanguage.getIsoCode());
		}
		if (object.has("created_at")) {
			LocalDateTime dateTime;
			try {
				dateTime = LocalDateTime.parse(object.get("created_at").getAsString());
				tweet.setCrdate(Timestamp.valueOf(dateTime));
			} catch (DateTimeParseException | NullPointerException  e) {
				Parser parser = new Parser();
		        List<DateGroup> groups = parser.parse(object.get("created_at").getAsString());

		        //Get Natty's interpreted Date
		        Instant targetDate = groups.get(0).getDates().get(0).toInstant();
		        tweet.setCrdate(Timestamp.from(targetDate));
			}
		}		
		tweet.setTmstamp(Timestamp.valueOf(LocalDateTime.now()));
		return tweet;
	}
}
