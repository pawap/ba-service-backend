package sentiments;

import java.util.Locale;

/**
 * This class lacks functionality. Its sole purpose is to present some structure. 
 * @author 6runge
 *
 */
public class DummyPreprocessor implements BasicPreprocessorInterface {
	private String[] tweets;

	public DummyPreprocessor() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * @see sentiments.BasicPreprocessorInterface#prepareTweets()
	 * 
	 * The order of the steps is probably good but some steps might have to be removed (e.g. we have to choose
	 * either stemming or lemmatization) while additional steps enhance the whole process.
	 */
	@Override
	public void prepareTweets() {
		for (String tweet : tweets) {
			tweet.toLowerCase(Locale.ENGLISH);
			removePunctuation(tweet);
			removeStopWords(tweet);
			standardize(tweet);
			correctSpelling(tweet);
			tokenize(tweet);
			performStemming(tweet);
			lemmatize(tweet);
		}
	}

	/*
	 * Removes punctuation reduce to help reduce the size of the data and increase computational efficiency
	 */
	private void removePunctuation(String tweet) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Removes very common words that carry no meaning or less meaning compared to other keywords.
	 */
	private void removeStopWords(String tweet) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Translates common shorthands and abbreviations into their respective origins. 
	 * This may help the downstream process to easily understand and resolve the semantics of the text.
	 */
	private void standardize(String tweet) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Spell correction needs to happen after standardization so that, for example
	 * 'ur' is changed into your before spell correction can treat it as a typo for 'or'
	 */
	private void correctSpelling(String tweet) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Split the text into meaningful bits (sentences, words,...)
	 */
	private void tokenize(String tweet) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Extract root words by stemming.
	 */
	private void performStemming(String tweet) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Extract root words by lemmatization.
	 */
	private void lemmatize(String tweet) {
		// TODO Auto-generated method stub
		
	}
	
	

}
