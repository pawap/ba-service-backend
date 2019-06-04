package mongo;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import sentiments.Tweet;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Paw
 *
 */
@Service
public class BasicDataImporter {

	@Autowired
	Environment env;

	@Autowired
	mongo.TweetRepository tweetRepository;

	public BasicDataImporter() {
		super();
	}


	public void importFromJson() {
		//String jsonPath = this.env.getProperty("localTweetJson");
		String jsonPath = "C:/Users/Paddy/Desktop/tweet2018121920_17.json";
		try {
			InputStream stream = new FileInputStream(jsonPath);
			JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
	        Gson gson = new GsonBuilder().create();
	        reader.setLenient(true);

	        int i = 0;
	        List<mongo.Tweet> tweets = new LinkedList();
	        while (reader.hasNext()) {
	            // Read data into object model
	        	try {
	        		if (reader.peek() == JsonToken.END_DOCUMENT) {
	        			break;
	        		}
	        		JsonElement element = gson.fromJson(reader, JsonElement.class);
	        		JsonObject object = element.getAsJsonObject();
	        		mongo.Tweet tweet = this.mapJsonToTweet(object);
	        		if (tweet != null && tweet.getText() != null) {
	        			i++;
	        			tweets.add(tweet);
	        		}
	        	} catch (IllegalStateException | JsonSyntaxException e) {
	        		reader.skipValue();
	        	}
	        	// persist tweets in batch (256 per insert)
	        	if (i % 256 == 0) {
	        		tweetRepository.saveAll(tweets);
	        		tweets.clear();
	        	}
	        }
	        tweetRepository.saveAll(tweets);
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

	private mongo.Tweet mapJsonToTweet(JsonObject object) {
		mongo.Tweet tweet = new mongo.Tweet();
		if (object.has("text")) {
			tweet.setText(object.get("text").getAsString());
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
