package com.dangdang.data;

public class ForcePidCategory {

	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getProducuId() {
		return producuId;
	}
	public void setProducuId(String producuId) {
		this.producuId = producuId;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryPath() {
		return categoryPath;
	}
	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	private String query;
	private String producuId;
	private String weight;
	private String categoryId;
	private String categoryPath;
	private String categoryName;
	
}
