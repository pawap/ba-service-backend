package sentiments;

import net.sf.json.JSONObject;

public class BasicDataHandler implements BasicDataHandlerInterface {

	@Override
	public Object getDBConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getLabledTweets(Integer iTime, String sLable) {
		//returns a JSON containing the number of sLable labled tweets
		JSONObject jJsonReturn = new JSONObject();

		jJsonReturn.put("number", 10);
		jJsonReturn.put("lable","offensiv");

		return jJsonReturn;
	}

}
