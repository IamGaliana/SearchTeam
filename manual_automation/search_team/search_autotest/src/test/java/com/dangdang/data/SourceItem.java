package com.dangdang.data;

public class SourceItem {

	private String catPath;
	private String catName;
	private String item;

	public SourceItem(String catPath, String catName, String item) {
		this.catPath = catPath;
		this.catName = catName;
		this.item = item;
	}

	public String getCatPath() {
		return catPath;
	}

	public void setCatPath(String catPath) {
		this.catPath = catPath;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String cathName) {
		this.catName = cathName;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
}
