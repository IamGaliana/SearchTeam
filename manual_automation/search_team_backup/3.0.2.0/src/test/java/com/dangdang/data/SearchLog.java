package com.dangdang.data;

public class SearchLog {

	/**
	 * @param args
	 */

	public  String getQuery() {
		return Query;
	}
	public  void setQuery(String query) {
		Query = query;
	}
	public  String getSearchCount() {
		return SearchCount;
	}
	public  void setSearchCount(String searchCount) {
		SearchCount = searchCount;
	}
	public  String getPid() {
		return pid;
	}
	public  void setPid(String pid) {
		this.pid = pid;
	}
	public  String getPosition() {
		return position;
	}
	public  void setPosition(String position) {
		this.position = position;
	}
	public  String getShowTimes() {
		return ShowTimes;
	}
	public  void setShowTimes(String showTimes) {
		ShowTimes = showTimes;
	}
	public  String getClickCount() {
		return ClickCount;
	}
	public  void setClickCount(String clickCount) {
		ClickCount = clickCount;
	}	
	private  String Query = "Query";
	private  String SearchCount = "查询次数";
	private  String pid="pid";
	private  String position="位置";
	private  String ShowTimes="展示次数";
	private  String ClickCount = "点击次数";
	

}
