package com.dangdang.data;
/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2014年10月30日 下午4:48:08  
 * 类说明 
 */
public class FuncCatePath {

	private int fc_id;
	private String fcate_name;
	private String cat_path;
	private String verify_point;
	private String is_delete;
	private String has_sub_path;
	private String query;
	private String module;
	
	
	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	
	public int getFc_id() {
		return fc_id;
	}
	public void setFc_id(int fc_id) {
		this.fc_id = fc_id;
	}
	public String getFcate_name() {
		return fcate_name;
	}
	public void setFcate_name(String fcate_name) {
		this.fcate_name = fcate_name;
	}
	public String getCat_path() {
		return cat_path;
	}
	public void setCat_path(String cat_path) {
		this.cat_path = cat_path;
	}
	public String getVerify_point() {
		return verify_point;
	}
	public void setVerify_point(String verify_point) {
		this.verify_point = verify_point;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public String getHas_sub_path() {
		return has_sub_path;
	}
	public void setHas_sub_path(String has_sub_path) {
		this.has_sub_path = has_sub_path;
	}
	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}
	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}
	
	
	
	
	
}
