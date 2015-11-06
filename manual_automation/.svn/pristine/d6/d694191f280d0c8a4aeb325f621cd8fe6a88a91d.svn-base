package com.dangdang;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.slf4j.LoggerFactory;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**
 * @author gaoyanjun@dangdang.com
 * @version 
 */
public class IndexDataHandler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(IndexDataHandler.class);

	/**
	 * 获取指定模块的full_tm 和 inc_num
	 * @param module_name		specified module
	 * @return					Map with full tm and inc num
	 * @throws MalformedURLException
	 * @throws DocumentException
	 */
	public static Map<String, String> GetFulltmAndIncNum(String module_name) throws MalformedURLException, DocumentException{
		Map<String, String> retMap = new HashMap<String, String>();
		Map<String, String> paramMap = new HashMap<String, String>();
		
		// 获取  http://10.255.254.188:8390/?_show_module=1 的结果
		paramMap.put("_show_module", "1");
		String url = URLBuilder.buildUrl(paramMap);
		String xml = SearchRequester.get(url);
		
		// 解析得到full_tm和inc_num
		Document doc = XMLParser.read(xml);		
		String full_timestamp = XMLParser.Index_fulltm(doc, module_name);
		String inc_num = XMLParser.Index_IncNum(doc, module_name);
		
		// 放到map中传递
		retMap.put("_full_timestamp", full_timestamp);
		retMap.put("_inc_num", Integer.toString((Integer.parseInt(inc_num) + 1)));
		return retMap;
	}
	
	/**
	 * clear up virtual data
	 * @throws MalformedURLException
	 * @throws DocumentException
	 */
	public static void ClearUpTestData(List<String> pidList) throws MalformedURLException, DocumentException{		
		Map<String, String> delMap = new HashMap<String, String>();		
		Map<String, String> tmAndIncnum = new HashMap<String, String>();
		
		for(String pid: pidList){
			tmAndIncnum = GetFulltmAndIncNum("searcher");
			// 要调用的删除API，需要的参数
			// http://10.255.254.71:8390/?_inc_num=61626&product_id=33333333&_index_del=1&_full_timestamp=1438606629
			delMap.put("product_id", pid);
			delMap.put("_index_del", "1");
			delMap.putAll(tmAndIncnum);
			
			// 请求API
			String url = URLBuilder.buildURL(delMap);
			// 如果结果返回OK，说明删除成功
			String ret = SearchRequester.get(url);
			if(ret.isEmpty() || !ret.contains("OK")){
				logger.error("clear up pid [" + pid + "] failed");
			}
		}
	}
	
	/**
	 * query = test，取到第一页的60个品的pid
	 * @return  pid list
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> PidList(){
		List<String> list = new ArrayList<String>();
		Map<String, String> requestMap = new HashMap<String, String>();
		String query = "test";
		requestMap.put("um", "search_ranking");
		requestMap.put("st", "full");
		requestMap.put("pg", "1");
		requestMap.put("ps", "60");
		//requestMap.put("q", URLEncoder.encode(query,"GBK"));
		requestMap.put("q", query);
		ProdIterator iterator = new ProdIterator(requestMap,60);
		
		while(iterator.hasNext()){
			String pid = XMLParser.product_id(iterator.next());
			list.add(pid);
		}
		
		return list;
	}

}
