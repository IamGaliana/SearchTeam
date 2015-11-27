package Test.client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import Test.util.dbUtil;
import Test.data.*;

public class dbEntity {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(dbEntity.class);
	
	private static String ID = "id";
	private static String QUERY = "query";
	private static String VERIFY_POINT = "verify_point";

	private String func_condition;
	
	public void setFunctionQuery(String funcCondition){
		this.func_condition = funcCondition;
	}
	
	public List<functionQuery> getFunctionQuery(){
		List<functionQuery> list = new ArrayList<functionQuery>();
		Connection conn = dbUtil.GetConnetion();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM function_query ");
		buffer.append("WHERE ");
		buffer.append(this.func_condition);
		
		ResultSet rs = dbUtil.ExecuteQuery(conn, buffer.toString());
		try {
			while(rs.next()){
				functionQuery query_result = new functionQuery();
				query_result.setFuncQueryID(Integer.valueOf(rs.getString(ID)));
				query_result.setFuncQuery(rs.getString(QUERY));
				query_result.setFuncVP(rs.getString(VERIFY_POINT));
				list.add(query_result);				
			}
		} catch (SQLException e) {
			logger.error("- [dbAction] - getFunctionQuery error");
			e.printStackTrace();
		}
		return list;
	}
}
