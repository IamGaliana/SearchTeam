package Test.client;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class httpRequester {
	private static final CloseableHttpClient httpClient=null;
	
	public static String sendRequest(String url){
		return doGet(url);
	}
	
	private static String doGet(String url){
		if(StringUtils.isBlank(url))
			return null;
		
		CloseableHttpResponse response =  null;
		HttpGet httpGet = new HttpGet(url);
		try {
			response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != 200){
				httpGet.abort();
				throw new RuntimeException("- [httpRequest] error status code :" + statusCode);
			}
			
			HttpEntity entity = response.getEntity();
			String result = null;
			if(entity != null){
				result = EntityUtils.toString(entity, "utf-8");
			}
			//EntityUtils.consume(entity);
			response.close();
			return result;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
