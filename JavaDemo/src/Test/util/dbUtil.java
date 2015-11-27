package Test.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.LoggerFactory;
import Test.util.configUtil;

public class dbUtil {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(dbUtil.class);
	
	public static configUtil config = new configUtil();
	public static String db_url = config.getdb_url();
	public static String db_user = config.getdb_user();
	public static String db_password = config.getdb_password();
	public static String base_url = config.getbase_url();
	
	public dbUtil(){
		
	}
	
	public static Connection GetConnetion(){
		logger.debug("- [dbUtil] - try to connect to DB");
		try {
			Driver driver = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
			DriverManager.registerDriver(driver);
			Connection conn = DriverManager.getConnection(db_url,db_user,db_password);
			logger.debug("- [dbUtil] - Success to connect to DB");
			return conn;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public void CloseConnection(Connection conn){
		try {
			conn.close();
			logger.debug("- [dbUtil] - close connection");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ResultSet ExecuteQuery(Connection conn, String query){
		ResultSet rs;
		try {
			logger.debug("- [dbUtil] execute the query : " + query);
			Statement statement = conn.createStatement();
			rs = statement.executeQuery(query);
			logger.debug("- [dbUtil] execute succeed-");			
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("- [dbUtil] execute failed-");
			return null;
		} 
	}
	
	public static boolean ExecuteUpdate(Connection conn, String query){
		Statement statement;
		try {
			logger.debug("- [dbUtil] execute the query : " + query);
			statement = conn.createStatement();
			statement.executeUpdate(query);
			logger.debug("- [dbUtil] execute succeed-");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("- [dbUtil] execute failed-");
			return false;
		}
	}
}
