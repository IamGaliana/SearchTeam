package com.dangdang.data;

public class FuncQuery {

	private int fq_id;
	private String fquery;
	private String medium;
	private String verify_point;
	private String cat_path;
	private String desc;
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCat_path() {
		return cat_path;
	}
	public void setCat_path(String cat_path) {
		this.cat_path = cat_path;
	}
	public int getFq_id() {
		return fq_id;
	}
	public void setFq_id(int fq_id) {
		this.fq_id = fq_id;
	}
	public String getFquery() {
		return fquery;
	}
	public void setFquery(String fquery) {
		this.fquery = fquery;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public String getVerify_point() {
		return verify_point;
	}	
	public void setVerify_point(String verify_point) {
		this.verify_point = verify_point;
	}
	
}