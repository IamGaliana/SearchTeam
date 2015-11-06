package com.dangdang.verifier.producttop;

/**
 * 单品置顶验证类
 */
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

public class ProductTopVerify{
	
	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(ProductTopVerify.class);
	
	/*
	 * pid_list_categoty_feedback 存储的是通过query得到的xml文件中的商品id
	 */
	public static String getPidFromXml(Map<String,String> map) throws Exception{
		
		Map<String,String> urlMap = new HashMap<String,String>();
		urlMap.put("st", "full");
		urlMap.put("um", "search_ranking");
		urlMap.put("ps", "60");
		urlMap.put("pg", "1");
		urlMap.put("_new_tpl", "1");
		urlMap.put("q", URLEncoder.encode(map.get("query"), "GBK"));
		String url = URLBuilder.buildURL(urlMap);
		
		System.out.println("#################"+url);
		String xml = SearchRequester.get(url);
		Document doc = XMLParser.read(xml);
		List<String> pid_list=new ArrayList<String> ();
		pid_list=XMLParser.getPidList(doc);
		System.out.println("pid_list"+pid_list);
		return pid_list.get(0);
	}
	
	/*
	 * add by dongxiaobing
	 * 得到是否有货的标志位
	 */
	public static boolean getStockStatus(Map<String,String> map) throws Exception{
		String test_product_id=map.get("product_id");
		Map<String,String> urlMap = new HashMap<String,String>();
		urlMap.put("product_id", test_product_id);
		String url = URLBuilder.buildURL(urlMap);
		String xml = SearchRequester.get(url);
		Document doc = XMLParser.read(xml);
		
		String stock_status=XMLParser.product_StockStatus_dxb(doc);
		
		System.out.println("###############库存状态"+stock_status);
		
		//0表示缺货
		if(!("0".equals(stock_status))){
			//有货返回正确
			System.out.println("有货");
			return true;
		}else{
			System.out.println("没货");
			return false;
		}
		
	}
	
	/*
	 * add by dongxiaobing
	 * 得到是否是当当自营的商品
	 */
	public static boolean getDdsell(Map<String,String> map) throws Exception{
		String test_product_id=map.get("product_id");
		Map<String,String> urlMap = new HashMap<String,String>();
		urlMap.put("product_id", test_product_id);
		String url = URLBuilder.buildURL(urlMap);
		String xml = SearchRequester.get(url);
		Document doc = XMLParser.read(xml);
		
		String is_dd_sell=XMLParser.product_dd_sell_dxb(doc);
		
		//System.out.println("###############当当自营flag"+is_dd_sell);
		
		//1表示当当自营
		if("1".equals(is_dd_sell)){
			System.out.println("是当当自营");
			return true;
		}else{
			System.out.println("不是当当自营");
			return false;
		}
		
	}
	/*
	 * add by dongxiaobing
	 * 得到是否是图书，如果不是图书，不限制是否当当自营
	 */
	public static boolean getPMedium(Map<String,String> map) throws Exception{
		String test_product_id=map.get("product_id");
		Map<String,String> urlMap = new HashMap<String,String>();
		urlMap.put("product_id", test_product_id);
		String url = URLBuilder.buildURL(urlMap);
		String xml = SearchRequester.get(url);
		Document doc = XMLParser.read(xml);
		
		String product_medium=XMLParser.product_medium_dxb(doc);
		if("0".equals(product_medium)){
			//是图书
			System.out.println("是图书");
			return true;
		}else{
			System.out.println("不是图书");
			return false;
		}

		
	}
	
	public static boolean verify(Map<String,String> map) throws Exception{
		
		//得到测试数据库中需要置顶的产品id
		String test_product_id=map.get("product_id");
		System.out.println("需要置顶的pid为:"+test_product_id);
		
		//得到后台返回xml中的第一个产品节点的pid
		String xml_product_id=getPidFromXml(map);
		
		//无货，如果单品置顶了返回false，否则返回true
		if(!getStockStatus(map)){
			if(test_product_id.equals(xml_product_id)){
				return false;
			}else{
				return true;
			}
		}
		
		//是百货，不管是不是当当自营，都置顶
		if(!getPMedium(map)){
			if(test_product_id.equals(xml_product_id)){
				return true;
			}else{
				return false;
			}
		//是图书是当当自营，应该被置顶
		}else if(getPMedium(map)&&getDdsell(map)){
			if(test_product_id.equals(xml_product_id)){
				return true;
			}else{
				return false;
			}
		//是图书不是当当自营，不应该被置顶
		}else{
			if(test_product_id.equals(xml_product_id)){
				return false;
			}else{
				return true;
			}
		}
	
	}
	
}
	
