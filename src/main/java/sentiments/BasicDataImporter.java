package sentiments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class BasicDataImporter {

	@Autowired
	Environment env;
	
	public BasicDataImporter() {
		super();
		
		
	}

	public void importFromJson() {
		String jsonPath = this.env.getProperty("localTweetJson");
				
	}
}
