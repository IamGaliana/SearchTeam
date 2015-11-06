package com.dangdang.client;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dangdang.data.BlackListQuery;
import com.dangdang.data.FuncCatePath;
import com.dangdang.data.FuncPermReco;
import com.dangdang.data.FuncQuery;
import com.dangdang.data.FuncQueryPool;
import com.dangdang.data.FuncVP;
import com.dangdang.data.PidCatPaths;
import com.dangdang.data.SearchLog;
import com.dangdang.data.VerticalSearchQuery;
import com.dangdang.util.DBConnUtil;


public class DBAction {

	private static Logger logger = Logger.getLogger(DBAction.class);
	
	//table function_query
	private static final String ID = "id";
	private static final String QUERY = "query";
	private static final String MEDIUM = "medium";
	private static final String VERIFY_POINT= "verify_point";
	private static final String CAT_PATH = "category_path";
	private static final String DESC = "desc"; 
	
	//table function_category
	private static final String C_ID = "category_id";
	private static final String C_NAME = "path_name";
	private static final String IS_DELETE = "is_delete";
	private static final String C_VERIFY_POINT= "verify_point";
	private static final String CATEGORY_PATH = "category_path";
	private static final String HAS_SUB_PATH = "has_sub_path";
	private static final String C_QUERY = "query";
	private static final String MODULE = "module";
	
	//table fvp
	private static final String FVP_ID = "fvp_id";
	private static final String FVP = "verify_point";
	private static final String MEMO = "memo";
	
	//table query_pool
	private static final String QP_ID = "id";
	private static final String QP_QUERY = "query";
	
	//table cat_paths_filter
	private static final String CP_PID = "pid";
	private static final String CP_CID = "category_id";
	private static final String CP_CATPATHS = "cat_paths";
	
	//table vertical_search_query
	private static final String VS_ID = "id";
	private static final String VS_QUERY = "query";
	private static final String VS_QUERY_TYPE = "query_type";
	private static final String VS_STD_PRODUCT = "std_product";
	private static final String VS_SHOP_PRODUCTS = "shop_products";
	private static final String VS_HOT_PRODUCTS = "hot_products";
	private static final String VS_SHOP_ID = "shop_id";
	private static final String VS_BRAND_ID = "brand_id";
	private static final String VS_SHOP_URL = "shop_url";
	private static final String VS_BANNER1_CONTENT = "banner1_content";
	private static final String VS_BANNER1_URL = "banner1_url";
	private static final String VS_BANNER2_CONTENT = "banner2_content";
	private static final String VS_BANNER2_URL = "banner2_url";
	
	
	//table perm_reco
	private static final String PERM_ID = "perm_id";
	private static final String BRAND_RECO = "brand_reco";
	
	//table Search_log_sample
	private static String SL_QUERY = "Query";
	private static String SL_SEARCHCOUNT = "查询次数";
	private static String SL_PID="pid";
	private static String SL_POSITION="位置";
	private static String SL_SHOWTIMES="展示次数";
	private static String SL_CLICKCOUNT = "点击次数";
	
	
	private String func_condition = "";
	private String fvp_condition = "";
	private String qp_condition = "";
	private String vsq_condition = "";
	private String catpath_condition = "";
	private String perm_condition = "";
	private String sl_condition="";
	
	public void setSl_condition(String sl_condition) {
		this.sl_condition = sl_condition;
	}

	public String getPerm_condition() {
		return perm_condition;
	}

	public void setPerm_condition(String perm_condition) {
		this.perm_condition = perm_condition;
	}

	public String getVsq_condition() {
		return vsq_condition;
	}

	public void setVsq_condition(String vsq_condition) {
		this.vsq_condition = vsq_condition;
	}

	public String getQp_condition() {
		return qp_condition;
	}

	public void setQp_condition(String qp_condition) {
		this.qp_condition = qp_condition;
	}

	public void setFuncCondition(String condition){
		this.func_condition = condition;
	}

	public void setFvpCondition(String condition){
		this.fvp_condition = condition;
	}

	public void setCatCondition(String condition){
		this.catpath_condition = condition;
	}
	
	public List<SearchLog> getSearchLogRecord(){
		List<SearchLog> list = new ArrayList<SearchLog>();
		Connection conn = DBConnUtil.getConnection();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * ");
		buffer.append("FROM Search_log_sample ");
		buffer.append("WHERE "); 
		buffer.append(this.sl_condition); 
		buffer.append(" group by pid");
		
		ResultSet result = DBConnUtil.exeQuery(buffer.toString(), conn);
		try {
			while(result.next()){
				SearchLog query = new SearchLog();
				query.setQuery(result.getString(SL_QUERY));
				query.setPid(result.getString(SL_PID));
				query.setSearchCount(result.getString(SL_SEARCHCOUNT));
				query.setClickCount(result.getString(SL_CLICKCOUNT));
				query.setShowTimes(result.getString(SL_SHOWTIMES));
				query.setPosition(result.getString(SL_POSITION));
				list.add(query);
			}
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return list;
		}
		
	}
	
	
	
	public List<FuncPermReco> getFuncPermReco(){
		List<FuncPermReco> list = new ArrayList<FuncPermReco>();
		Connection conn = DBConnUtil.getConnection();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * ");
		buffer.append("FROM perm_reco ");
		buffer.append("WHERE "); 
		buffer.append(this.perm_condition); 
		buffer.append(" group by perm_id");
		
		ResultSet result = DBConnUtil.exeQuery(buffer.toString(), conn);
		try {
			while(result.next()){
				FuncPermReco query = new FuncPermReco();
				query.setPerm_id(result.getString(PERM_ID));
				query.setBrand_reco(result.getString(BRAND_RECO));
				list.add(query);
			}
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return list;
		}
		
	}
	
	
	public List<FuncQuery> getFuncQuery(){
		List<FuncQuery> list = new ArrayList<FuncQuery>();
		Connection conn = DBConnUtil.getConnection();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * ");
		buffer.append("FROM function_query ");
		buffer.append("WHERE "); 
		buffer.append(this.func_condition); 
		buffer.append(" group by query");
		
		ResultSet result = DBConnUtil.exeQuery(buffer.toString(), conn);
		try {
			while(result.next()){
				FuncQuery query = new FuncQuery();
				query.setFq_id(Integer.valueOf(result.getString(ID)));
				query.setFquery(result.getString(QUERY));
				query.setMedium(result.getString(MEDIUM));
				query.setVerify_point(result.getString(VERIFY_POINT));
				query.setCat_path(result.getString(CAT_PATH));
				query.setDesc(result.getString(DESC));
				list.add(query);
			}
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return list;
		}
		
	}
	
	/*
	 * @return 分类测试数据
	 */
	public List<FuncCatePath> getFuncCatePath(){
		List<FuncCatePath> list = new ArrayList<FuncCatePath>();
		Connection conn = DBConnUtil.getConnection();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * ");
		buffer.append("FROM function_category ");
		buffer.append("WHERE "); 
		buffer.append(this.func_condition); 
//		buffer.append(" group by category_path");
		
		ResultSet result = DBConnUtil.exeQuery(buffer.toString(), conn);
		try {
			while(result.next()){
				FuncCatePath category = new FuncCatePath();
				category.setFc_id(Integer.valueOf(result.getString(C_ID)));
				category.setCat_path(result.getString(CATEGORY_PATH));
				category.setFcate_name(result.getString(C_NAME));
				category.setVerify_point(result.getString(C_VERIFY_POINT));
				category.setHas_sub_path(result.getString(HAS_SUB_PATH));
				category.setIs_delete(result.getString(IS_DELETE));
				category.setQuery(result.getString(C_QUERY));
				category.setModule(result.getString(MODULE));
				list.add(category);
			}
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return list;
		}
		
	}
	
	public List<FuncVP> getFVP(){
		List<FuncVP> list = new ArrayList<FuncVP>();
		Connection conn = DBConnUtil.getConnection();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * ");
		buffer.append("FROM fvp ");
		buffer.append("WHERE ");
		buffer.append(this.fvp_condition);
		
		ResultSet result = DBConnUtil.exeQuery(buffer.toString(), conn);
		try {
			while(result.next()){
				FuncVP fvp = new FuncVP();
				fvp.setFvp(result.getString(FVP));
				fvp.setFvp_id(Integer.valueOf(result.getString(FVP_ID)));
				fvp.setFvpname(result.getString(MEMO));
				list.add(fvp);
			}
			return list;
		} catch (SQLException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return list;
		}
		
	}

	public List<VerticalSearchQuery> getVerticalSearchQuery(){
		List<VerticalSearchQuery> list = new ArrayList<VerticalSearchQuery>();
		Connection conn = DBConnUtil.getConnection();
		StringBuffer buffer = new StringBuffer();
		//SELECT * FROM (SELECT * FROM vertical_search_query  ORDER BY id DESC ) table GROUP BY table.query 
		buffer.append("SELECT * ");
		buffer.append("FROM (SELECT * FROM vertical_search_query  ORDER BY id DESC ) t ");
		//buffer.append("WHERE id=1804 and "); 
		buffer.append("WHERE ");
		buffer.append(this.vsq_condition); 
		
		ResultSet result = DBConnUtil.exeQuery(buffer.toString(), conn);
		try {
			while(result.next()){
				VerticalSearchQuery query = new VerticalSearchQuery();
				query.setId(Integer.valueOf(result.getString(VS_ID)));
				query.setPerm_id(result.getString(PERM_ID));
				query.setQuery(result.getString(VS_QUERY));
				query.setQuery_type(result.getString(VS_QUERY_TYPE));
				query.setStd_product(result.getString(VS_STD_PRODUCT));
				query.setShop_products(result.getString(VS_SHOP_PRODUCTS));
				query.setHot_products(result.getString(VS_HOT_PRODUCTS));
				query.setShop_id(result.getString(VS_SHOP_ID));
				query.setBrand_id(result.getString(VS_BRAND_ID));
				query.setShop_url(result.getString(VS_SHOP_URL));
				query.setBanner1_content(result.getString(VS_BANNER1_CONTENT));
				query.setBanner1_url(result.getString(VS_BANNER1_URL));
				query.setBanner2_content(result.getString(VS_BANNER2_CONTENT));
				query.setBanner2_url(result.getString(VS_BANNER2_URL));
				list.add(query);
			}
			return list;
		} catch (SQLException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return list;
		}
		
	}
	
	public List<BlackListQuery> getBlackListQuery(){
		List<BlackListQuery> list = new ArrayList<BlackListQuery>();
		Connection conn = DBConnUtil.getConnection();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * ");
		buffer.append("FROM black_list ");
		buffer.append("WHERE "); 
		buffer.append(this.vsq_condition); 
		
		ResultSet result = DBConnUtil.exeQuery(buffer.toString(), conn);
		try {
			while(result.next()){
				BlackListQuery query = new BlackListQuery();
				query.setQuery(result.getString("query"));
				query.setIdType(result.getString("id_type"));
				query.setBlackId(result.getString("ids"));
				query.setStartDate(result.getString("start_date"));
				query.setEndDate(result.getString("end_date"));
				query.setPunishType(result.getString("punish_type"));
				query.setOptype(result.getString("op_type"));
				query.setCategoryPath(result.getString("category_path"));
				query.setCommons(result.getString("commons"));
				list.add(query);
			}
			return list;
		} catch (SQLException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return list;
		}
		
	}
	
	public List<FuncQueryPool> getQueryPool(){
		List<FuncQueryPool> list = new ArrayList<FuncQueryPool>();
		Connection conn = DBConnUtil.getConnection();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * ");
		buffer.append("FROM query_pool ");
		buffer.append("WHERE ");
		buffer.append(this.qp_condition);
		
		ResultSet result = DBConnUtil.exeQuery(buffer.toString(), conn);
		try {
			while(result.next()){
				FuncQueryPool qp = new FuncQueryPool();
				qp.setId(Integer.valueOf(result.getString(QP_ID)));
				qp.setQuery(result.getString(QP_QUERY));
				list.add(qp);
			}
			return list;
		} catch (SQLException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return list;
		}
		
	}
	
	public List<PidCatPaths> getCatpathsByPid(){
		List<PidCatPaths> list = new ArrayList<PidCatPaths>();
		Connection conn = DBConnUtil.getConnection();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * ");
		buffer.append("FROM cat_paths_filter ");
		buffer.append("WHERE ");
		buffer.append(this.catpath_condition);
		
		ResultSet result = DBConnUtil.exeQuery(buffer.toString(), conn);
		try {
			while(result.next()){
				PidCatPaths cp = new PidCatPaths();
				cp.setPid(Integer.valueOf(result.getString(CP_PID)));
				cp.setCategoryId(Integer.valueOf(result.getString(CP_CID)));
				cp.setCatPaths(result.getString(CP_CATPATHS));
				list.add(cp);
			}
			return list;
		} catch (SQLException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return list;
		}
		
	}
}