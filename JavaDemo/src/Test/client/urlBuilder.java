package Test.client;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import Test.client.httpRequester;

import Test.util.configUtil;

public class urlBuilder {
	
	private String STR_EQUAL = "=";
	private String STR_QUES = "?";
	private String STR_SLASH = "/";
	
	@SuppressWarnings("deprecation")
	public Map<String,String> getUrlMap(String query){		
		Map<String, String> urlMap = new HashMap<String,String>();
		urlMap.put("um", "search_ranking");
		urlMap.put("st", "full");
		urlMap.put("ps", "60");
		urlMap.put("pg","1");
		urlMap.put("q", URLEncoder.encode(query));
		
		return urlMap;		
	}
	
	public String buildUrl(String query){
		configUtil config = new configUtil();
		String baseurl = config.getbase_url();
		StringBuilder requestUrl = new StringBuilder();
		Map<String, String> map = getUrlMap(query);
		
		requestUrl.append(baseurl);
		requestUrl.append(STR_QUES);
		for(Map.Entry<String, String> entry : map.entrySet()){
			requestUrl.append(entry.getKey());
			requestUrl.append(STR_EQUAL);
			requestUrl.append(entry.getValue());
		}
		return requestUrl.toString();
	}
	
	public String GetResult(String query){
		String url = buildUrl(query);
		return httpRequester.sendRequest(url);
	}
}
