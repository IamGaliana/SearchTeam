package com.dangdang.verifier.scenario;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.util.XMLParser;
/**
 * 说明：【场景化搜索】 - 非精确书名-考研英语&注册会计师
 *  验证xml中是否有对应的scenario_name节点并且节点值正确
 * @author gaoyanjun
 * @version 创建时间：2015/05/11 16:35 
 * 
 */
public class ScenarioSearchVerifier {

	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(ScenarioSearchVerifier.class);
			
	/*
	 * 入口
	 */
	public boolean Verifier(Map<String,String> map){	
		Map<String,String> urlMap = new HashMap<String,String>();
		urlMap.put("st", "full");
		urlMap.put("um", "search_ranking");
		urlMap.put("ps", "1");
		urlMap.put("pg", "60");
		urlMap.put("_new_tpl", "1");		
		try {
			urlMap.put("q", URLEncoder.encode(map.get("query"), "GBK"));
		} catch (UnsupportedEncodingException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
		}
		
		String url = URLBuilder.buildURL(urlMap);
		String xml = SearchRequester.get(url);
		String scenario_tag = null;
		
		try{
			Document doc = XMLParser.read(xml);
			scenario_tag = XMLParser.scenario_name(doc);
		}
		catch(MalformedURLException | DocumentException e)
		{
			 ByteArrayOutputStream buf = new ByteArrayOutputStream();
			 e.printStackTrace(new PrintWriter(buf, true));
			 String expMessage = buf.toString();
			 logger.error(" - [LOG_EXCEPTION] - "+expMessage);
			 return false;
		}
		
//		logger.info(String.format(" -  [CHECK-HAS-RESULT] check has scenario_name"));
		return this.ScenarioVerifier(map.get("query"), scenario_tag);
	};
	
		
	/*
	 *  场景化 - 考研英语&注册会计师验证
	 */
	public boolean ScenarioVerifier(String query,String scenario_tag){
		if(scenario_tag == "" || scenario_tag == null){
			logger.error(" -  [CHECK-INFO] check fail for scenario_tag: scenario_name tag is null or empty");
			return false;
		}
		if(query.indexOf("英语")>-1 && scenario_tag != "nsearch_english" )
		{
			logger.error(String.format(" -  [CHECK-INFO] check fail for scenario_tag: scenario_name tag should be [nsearch_english]for query [%s], but it is %s", query, scenario_tag));
			return false;
		}			
		if(query.indexOf("会计")>-1 && scenario_tag != "nsearch_accountant")
		{
			logger.error(String.format(" -  [CHECK-INFO] check fail for scenario_tag: scenario_name tag should be [nsearch_accountant] for query [%s], but it is %s", query, scenario_tag));
			return false;
		}
		
		return true;
	};
}
