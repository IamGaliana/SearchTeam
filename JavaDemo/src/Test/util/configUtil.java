package Test.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class configUtil {
	public static final String configPath = "conf/config.properties";
	private Properties properties = new Properties();
	public String DB_URL = "db_url";
	public String BASE_URL = "base_url";
	public String DB_USER = "db_user";
	public String DB_PASSWORD = "db_password";
	
	public configUtil(){
		try{
			InputStream fs = new FileInputStream(configPath);
			properties.load(fs);
		}catch(Exception e){
			
		}
	}
	
	public String getdb_url(){
		return properties.getProperty(DB_URL);
	}
	
	public String getdb_user(){
		return properties.getProperty(DB_USER);
	}
	
	public String getdb_password(){
		return properties.getProperty(DB_PASSWORD);
	}
	
	public String getbase_url(){
		return properties.getProperty(BASE_URL);
	}
}
