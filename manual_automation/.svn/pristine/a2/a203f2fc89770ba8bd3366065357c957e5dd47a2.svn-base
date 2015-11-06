package com.dangdang.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.dangdang.client.SearchRequest;
import com.dangdang.client.URLBuilder;

/**
 * @author liuzhipengjs@dangdang.com
 * @version 创建时间：2015年5月29日 下午2:32:38 类说明 获取运营工具数据接口
 */
public class StoolData {

	// public static final String CHARSET = "utf-8";
	// public static final String CHARSET = "GBK";

	public static String query4json(String baseurl, String type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("op_type", "query");
		map.put("ret_type", "json");
		map.put("type", type);
		String url = URLBuilder.buildUrl(map, baseurl);
		String result4json = SearchRequest.doGetToolData(url, null, "GBK");// 运营工具统一使用GBK编码。。。。
		return result4json;
	}
	
	/*
	 * add by gaoyanjun @ 2015/06/16
	 * 说明： 请求运营工具，增加了一个参数map，并返回结果
	 * baseurl = 10.255.254.72:8898/
	 * 不同业务逻辑，type不同
	 * map中传递其他请求参数，如weight = 100 
	 * 最终拼成的url串：http://10.255.254.72:8898/?type=key_cat&op_type=query&ret_type=json&weight=100
	 */
	public static String query4json(String baseurl, String type, Map<String,String> map){
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("op_type","query");
		queryMap.put("ret_type","json");
		queryMap.put("type", type);
		if(map.size() > 0){
			for(Entry<String, String> entry: map.entrySet()){
				queryMap.put(entry.getKey(), entry.getValue());
			}
		}
		String url = URLBuilder.buildUrl(queryMap, baseurl);
		String result4json = SearchRequest.doGetToolData(url, null, "GBK");
		return result4json;
	}



	// 调试使用
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String baseurl = "http://10.255.254.72:8898/";
		String type = "new_product_category_weight";
		String a = query4json(baseurl, type);

		System.out.println(a);

	}
}
