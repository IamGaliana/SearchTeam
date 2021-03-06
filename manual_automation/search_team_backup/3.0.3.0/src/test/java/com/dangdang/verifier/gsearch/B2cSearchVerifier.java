package com.dangdang.verifier.gsearch;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IgSearchVerifier;

public class B2cSearchVerifier implements IgSearchVerifier{
	
	@Override
	public boolean Verifier(ProdIterator iterator, Map<String, String> map, String[] ta){
		Map<String, String> searchParameters = this.getSearchParamters(iterator, 1);
		if(searchParameters==null) return true;
		return doVerifySearch(searchParameters);
	}

	@Override
	public Map<String, String> getSearchParamters(ProdIterator iterator, int flag) {
		while (iterator.hasNext()){      
			Map<String, String> map = XMLParser.get_b2c_search_info(iterator.next());
			if(!map.get("product_name").toString().equals("")&&!map.get("cat_paths").toString().equals("")){
				return map;
			}
		} 
		return null;
	}
	
	//st=full&um=search_ranking&q=%C9%CC%C6%B7&-cat_paths=05.00.00.00.00.00
	public boolean doVerifySearch(Map<String, String> searchParameters){
		boolean result = true;
		logger.debug(String.format("  - [CHECK-INFO] gSearch-search's searchParameters: %s", searchParameters));
		Map<String, String> tm = new HashMap<String, String>();
		Map<String, String> newmap = new HashMap<String, String>();
		ProdIterator iterator = null;
		tm.put("st", "full");
		tm.put("um", "search_ranking");
		tm.put("_new_tpl", "1");//search_ranking必加
		
		//全部分类
		newmap.clear();
		try {
			newmap.put("q", URLEncoder.encode(searchParameters.get("product_name"), "GBK"));
		} catch (UnsupportedEncodingException e1) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e1.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.put("-cat_paths", "");
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.productNameVerifier(iterator, searchParameters);
		
		//综合
		newmap.clear();		
		try {
			newmap.put("q", URLEncoder.encode(searchParameters.get("product_name"), "GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.put("-cat_paths", searchParameters.get("cat_paths"));
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.b2cSearchAllVerifier(iterator, searchParameters);
		return result;
	}
}
