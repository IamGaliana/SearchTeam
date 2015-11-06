package com.dangdang.verifier.categoryFeedback;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.scenario.ScenarioSearchVerifier;

public class CategoryFeedbackVerify{
	
	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryFeedbackVerify.class);
	

	
	//直接用query+category_path访问后台xml，得到pid_list，称这个pid_list为默认的产品pid列表
	public static List<String> getDefaultPidList(Map<String,String> map) throws Exception{
		List<String> pid_list_default=new ArrayList<String> ();
		Map<String,String> urlMap = new HashMap<String,String>();
		String category_path=map.get("category_path");
		urlMap.put("st", "full");
		urlMap.put("um", "search_ranking");
		urlMap.put("ps", "60");
		urlMap.put("pg", "1");
		urlMap.put("_new_tpl", "1");
		urlMap.put("-cat_paths", category_path.trim());
		urlMap.put("q", URLEncoder.encode(map.get("query"), "GBK"));
		String url = URLBuilder.buildURL(urlMap);
		String xml = SearchRequester.get(url);
		Document doc = XMLParser.read(xml);
		pid_list_default=XMLParser.getPidList(doc);
		System.out.println("query+categoryPath"+pid_list_default);
		return pid_list_default;
		
	}
	/*
	 * pid_list_categoty_feedback 存储的是通过query得到的xml文件中的商品id
	 */
	public static List<String> getCategoryFeedbackPidList(Map<String,String> map) throws Exception{
		List<String> pid_list_categoty_feedback=new ArrayList<String> ();
		Map<String,String> urlMap = new HashMap<String,String>();
		urlMap.put("st", "full");
		urlMap.put("um", "search_ranking");
		urlMap.put("ps", "60");
		urlMap.put("pg", "1");
		urlMap.put("_new_tpl", "1");
		urlMap.put("q", URLEncoder.encode(map.get("query"), "GBK"));
		String url = URLBuilder.buildURL(urlMap);
		String xml = SearchRequester.get(url);
		Document doc = XMLParser.read(xml);
		pid_list_categoty_feedback=XMLParser.getPidList(doc);
		System.out.println("query+pid_list_categoty_feedback"+pid_list_categoty_feedback);
		return pid_list_categoty_feedback;
	}
	public static boolean verify(Map<String,String> map) throws Exception{
		
		//pid_list_default 存储的是通过query+category_path得到的xml文件中的商品id，这些商品id会排在通过query查询的结果集前面
		List<String> pid_list_default=getDefaultPidList(map);
		
		//pid_list_categoty_feedback 存储的是通过query得到的xml文件中的商品id
		List<String> pid_list_categoty_feedback=getCategoryFeedbackPidList(map);
		//比较前七个pid是否都相等
		//if(pid_list_default.size()>6&&pid_list_categoty_feedback.size()>6){
			for(int i=0;i<7;i++){
				if(i<pid_list_default.size()){
					if(!pid_list_categoty_feedback.contains(pid_list_default.get(i))){

						return false;
					}
				}
				
			}
			//return true;
				
		//}
		return true;
//		else{
//			logger.info("分类反馈的商品数量小于六个！！！");
//			for(int i=1;i<pid_list_default.size();i++){
//				if(!pid_list_categoty_feedback.contains(pid_list_default.get(i))){
//					return false;
//				}
//			}
//			return true;
//		}
		
	}
	
}
	