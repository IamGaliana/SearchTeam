package com.dangdang.data;

import java.util.HashMap;
import java.util.Map;

public class FilterMap {
	private static Map<String, String> fMap = new HashMap<String, String>();
	private static FilterMap filtermap = null;	
	{
		fMap.put("feedback", "51:"); //用户评价：质量很好
	}
	
	private FilterMap(){
		
	}
	
	public static Map<String, String> getFilterMap(){	
		if (filtermap==null){
			filtermap = new FilterMap();
		}
		return filtermap.fMap;	
	}
}
