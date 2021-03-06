package com.dangdang.verifier.gsearch;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IgSearchVerifier;

public class VedioSearchVerifier implements IgSearchVerifier{
	private static Logger logger = Logger.getLogger(BookSearchVerifier.class);
	
	@Override
	public boolean Verifier(ProdIterator iterator, Map<String, String> map, String[] ta){
		Map<String, String> searchParameters = this.getSearchParamters(iterator, 1);
		if(searchParameters==null) return true;
		return doVerifySearch(searchParameters);
	}

	@Override
	public Map<String, String> getSearchParamters(ProdIterator iterator, int flag) {
		while (iterator.hasNext()){
			Map<String, String> map = XMLParser.get_vedio_search_info(iterator.next());
			if(!map.get("product_name").toString().equals("")&&!map.get("director").toString().equals("")
					&&!map.get("actors").toString().equals("")){
				return map;
			}
		} 
		return null;
	}
	
	//st=pub&-cat_paths=05.00.00.00.00.00&um=search_ranking&product_name=%BD%F0%B8%D5&actors=&director=
	public boolean doVerifySearch(Map<String, String> searchParameters){
		boolean result = true;
		logger.debug(String.format("  - [CHECK-INFO] gSearch-search's searchParameters: %s", searchParameters));
		Map<String, String> tm = new HashMap<String, String>();
		Map<String, String> newmap = new HashMap<String, String>();
		ProdIterator iterator = null;
		tm.put("st", "pub");
		tm.put("um", "search_ranking");
		tm.put("_new_tpl", "1");//search_ranking必加
		tm.put("-cat_paths", "05.00.00.00.00.00");
		
		//只有product name
		newmap.clear();
		try {
			newmap.put("product_name", URLEncoder.encode(searchParameters.get("product_name"), "GBK"));
		} catch (UnsupportedEncodingException e1) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e1.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.put("director", "");
		newmap.put("actors", "");
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.productNameVerifier(iterator, searchParameters);
		
		
		//只有director
		newmap.clear();
		newmap.put("product_name", "");
		try {
			newmap.put("director", URLEncoder.encode(searchParameters.get("director"), "GBK"));
		} catch (UnsupportedEncodingException e1) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e1.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.put("actors", "");
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.directorVerifier(iterator, searchParameters);
		
		
		//只有actors
		newmap.clear();
		newmap.put("product_name", "");
		newmap.put("director", "");
		try {
			newmap.put("actors", URLEncoder.encode(searchParameters.get("actors"), "GBK"));
		} catch (UnsupportedEncodingException e1) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e1.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.actorsVerifier(iterator, searchParameters);
		
		//综合
		newmap.clear();		
		try {
			newmap.put("director", URLEncoder.encode(searchParameters.get("director"), "GBK"));
			newmap.put("actors", URLEncoder.encode(searchParameters.get("actors"), "GBK"));
			newmap.put("product_name", URLEncoder.encode(searchParameters.get("product_name"), "GBK"));
		} catch (UnsupportedEncodingException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.vedioSearchAllVerifier(iterator, searchParameters);
		return result;
	}
}
