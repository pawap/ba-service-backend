package sentiments;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
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

/**
 * @author Paw
 *
 */
@Service
public class BasicDataImporter {

	private static final int IMPORT = 0;
	private static final int TRAINING = 1;

	@Autowired
	Environment env;

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	TrainingTweetRepository trainingTweetRepository;

	TweetRepository currentRepository;

	private int dataDestination;

	public BasicDataImporter() {
		super();
	}

	public void importFromJson(String jsonPath, TweetMarker tweetMarker) {
		try {
			InputStream stream = new FileInputStream(jsonPath);
			JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
			Gson gson = new GsonBuilder().create();
			reader.setLenient(true);

			int i = 0;
			List<AbstractTweet> tweets = new LinkedList();
			while (reader.hasNext()) {
				// Read data into object model
				try {
					if (reader.peek() == JsonToken.END_DOCUMENT) {
						break;
					}
					JsonElement element = gson.fromJson(reader, JsonElement.class);
					JsonObject object = element.getAsJsonObject();
					AbstractTweet tweet = this.mapJsonToTweet(object);
					if (tweet != null && tweet.getText() != null) {
						i++;
						if (tweetMarker != null) {
							tweetMarker.mark(tweet);
						}
						tweets.add(tweet);
					}
				} catch (IllegalStateException | JsonSyntaxException e) {
					reader.skipValue();
				}
				// persist tweets in batch (256 per insert)
				if (i % 256 == 0) {
					this.currentRepository.saveAll(tweets);
					tweets.clear();
				}
			}
			this.tweetRepository.saveAll(tweets);
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

	public void importFromTsv(String tsvPath, TweetMarker tweetMarker) {
		Reader in;
        int i = 0;
        List<AbstractTweet> tweets = new LinkedList();
		try {
			FileInputStream fstream = new FileInputStream(tsvPath);
			in = new BufferedReader(new InputStreamReader(fstream));
			Iterable<CSVRecord> records = CSVFormat.TDF.withHeader().parse(in);
			for (CSVRecord record : records) {
				AbstractTweet tweet = this.mapTsvToTweet(record);
        		if (tweet != null && tweet.getText() != null) {
        			i++;
					if (tweetMarker != null) {
						tweetMarker.mark(tweet);
					}
					tweets.add(tweet);
        		}
            	if (i % 256 == 0) {
            		this.currentRepository.saveAll(tweets);
            		tweets.clear();
            	} 	
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// persist tweets in batch (256 per insert)
        this.tweetRepository.saveAll(tweets);
	}

	private AbstractTweet mapTsvToTweet(CSVRecord record) {
		Tweet tweet = new Tweet();
		tweet.setText(record.get("tweet"));
		switch (record.get("subtask_a")) {
		case "OFF":
			tweet.setOffensive(true);
			break;
		case "NOT":
			tweet.setOffensive(false);
			break;
		default:
		}
		return tweet;
	}

	private AbstractTweet mapJsonToTweet(JsonObject object) {
		Tweet tweet = new Tweet();
		if (object.has("text")) {
			tweet.setText(object.get("text").getAsString());
		}
		if (object.has("created_at")) {
			LocalDateTime dateTime;
			try {
				dateTime = LocalDateTime.parse(object.get("created_at").getAsString());
				tweet.setCrdate(Timestamp.valueOf(dateTime));
			} catch (DateTimeParseException | NullPointerException e) {
				Parser parser = new Parser();
				List<DateGroup> groups = parser.parse(object.get("created_at").getAsString());

				// Get Natty's interpreted Date
				Instant targetDate = groups.get(0).getDates().get(0).toInstant();
				tweet.setCrdate(Timestamp.from(targetDate));
			}
		}
		tweet.setTmstamp(Timestamp.valueOf(LocalDateTime.now()));
		return tweet;
	}

	public void importExampleJson() {
		setDataDestination(IMPORT);
		String jsonPath = this.env.getProperty("localTweetJson");
		importFromJson(jsonPath, null);
	}

	public void importTsvTestAndTrain() {
		setDataDestination(IMPORT);
		importFromTsv(this.env.getProperty("localTweetTsv.train"), null);
		importFromTsv(this.env.getProperty("localTweetTsv.test"), tweet -> {
			//((TrainingTweet) tweet).setTest(true);
		});
	}

	private void setDataDestination(int destination) {

		this.dataDestination = destination;

		switch (destination) {
		case IMPORT:
			this.currentRepository = this.tweetRepository;
			break;
		case TRAINING:
			this.currentRepository = this.trainingTweetRepository;
			break;
		}

	}
}
