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

public class BookSearchVerifier implements IgSearchVerifier{
	
	@Override
	public boolean Verifier(ProdIterator iterator, Map<String, String> map, String[] ta){
		Map<String, String> searchParameters = new HashMap<String, String>();
		if  (ta.length==2){  ///检查所有图书搜索功能			
			searchParameters = this.getSearchParamters(iterator, 3);
		}else if (ta.length==3){  //检查特定的图书搜索功能
			searchParameters = getSearchParamters(ta, iterator);
			searchParameters.put("q", map.get("query"));
			searchParameters.put("term", map.get("term"));
			return doVerify(ta, searchParameters);
		}else if (ta.length==4){ //检查特定图书搜索的特定参数
			searchParameters = getSearchParamters(ta, iterator);
		} 
		return true;
	}

	@Override
	public Map<String, String> getSearchParamters(ProdIterator iterator, int flag) {
		if (iterator.hasNext()){
			return XMLParser.get_book_search_info(iterator.next(), flag);
		} 
		return null;
	}
	
	public Map<String, String> getSearchParamters(String[] ta, ProdIterator iterator){
		Map<String, String> searchParameters = new HashMap<String, String>();
		if (ta[2].equals("search")){
			searchParameters = this.getSearchParamters(iterator, 1);				
		}else if (ta[2].equals("list")){
			searchParameters = this.getSearchParamters(iterator, 2);
		}else if (ta[2].equals("merge")){
			searchParameters = this.getSearchParamters(iterator, 3);
		}
		return searchParameters;
	}

	public boolean doVerify(String[] ta, Map<String, String> searchParameters){
		if (ta[2].equals("search")){
			return doVerifySearch(searchParameters);				
		}else if (ta[2].equals("list")){
			return doVerifyList(searchParameters);
		}else if (ta[2].equals("merge")){
			return doVerifyMerge(searchParameters);
		}
		return true;
	}

	public boolean doVerifySearch(Map<String, String> searchParameters){
		boolean result = true;
		logger.debug(String.format("  - [CHECK-INFO] gSearch-search's searchParameters: %s", searchParameters));
		Map<String, String> tm = new HashMap<String, String>();
		Map<String, String> newmap = new HashMap<String, String>();
		ProdIterator iterator = null;
		tm.put("st", "pub");
		tm.put("um", "search_ranking");
		tm.put("_new_tpl", "1");//search_ranking必加
		tm.put("-cat_paths", "01.00.00.00.00.00");
		tm.put("-product_medium", "0");
		
		
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
		newmap.put("isbn_search", "");
		newmap.put("author_name", "");
		newmap.put("publisher", "");
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.productNameVerifier(iterator, searchParameters);
		
		
		//只有isbn
		newmap.clear();
		newmap.put("product_name", "");
		newmap.put("isbn_search", searchParameters.get("isbn_search"));
		newmap.put("author_name", "");
		newmap.put("publisher", "");
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.isbnVerifier(iterator, searchParameters);
		
		
		//只有author
		newmap.clear();
		newmap.put("product_name", "");
		newmap.put("isbn_search", "");
		newmap.put("publisher", "");
		try {
			newmap.put("author_name", URLEncoder.encode(searchParameters.get("author_name"), "GBK"));
		} catch (UnsupportedEncodingException e1) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e1.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.authorVerifier(iterator, searchParameters);
		
		
		//只有 publisher
		newmap.clear();
		newmap.put("product_name", "");
		newmap.put("isbn_search", "");
		newmap.put("author_name", "");
		try {
			newmap.put("publisher", URLEncoder.encode(searchParameters.get("publisher"), "GBK"));
		} catch (UnsupportedEncodingException e1) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e1.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.publisherVerifier(iterator, searchParameters);
		
		
		//综合
		newmap.clear();		
		try {
			newmap.put("isbn_search", searchParameters.get("isbn_search"));
			newmap.put("author_name", URLEncoder.encode(searchParameters.get("author_name"), "GBK"));
			newmap.put("publisher", URLEncoder.encode(searchParameters.get("publisher"), "GBK"));
			newmap.put("product_name", URLEncoder.encode(searchParameters.get("product_name"), "GBK"));
			newmap.put("q", URLEncoder.encode(searchParameters.get("q"), "GBK"));
		} catch (UnsupportedEncodingException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.bookSearchAllVerifier(iterator, searchParameters);
		return result;
	}

	public boolean doVerifyList(Map<String, String> searchParameters){
		boolean result = true;
		logger.debug(String.format("  - [CHECK-INFO] gSearch-list's searchParameters: %s", searchParameters));
		Map<String, String> tm = new HashMap<String, String>();
		Map<String, String> newmap = new HashMap<String, String>();
		ProdIterator iterator = null;
		tm.put("st", "pub");
		tm.put("um", "list_ranking");
		tm.put("cat_paths", "01.00.00.00.00.00");
		
		
		//只有binding
		logger.debug(String.format("  - [CHECK-INFO] gSearch-list's binding_id: %s", searchParameters.get("binding_id")));
		newmap.clear();
		newmap.put("-binding_id", searchParameters.get("binding_id"));
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.bindingVerifier(iterator, searchParameters);
		
		
		//只有cat_paths
		logger.debug(String.format("  - [CHECK-INFO] gSearch-list's cat_paths: %s", searchParameters.get("cat_paths")));
		newmap.clear();
		newmap.putAll(tm);
		newmap.put("cat_paths", searchParameters.get("cat_paths"));
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.categoryVerifier(iterator, searchParameters);
		
		
		//只有dd_sale_price
		logger.debug(String.format("  - [CHECK-INFO] gSearch-list's dd_sale_price: %s", searchParameters.get("dd_sale_price")));
		newmap.clear();
		newmap.put("-dd_sale_price", searchParameters.get("dd_sale_price")+"~");
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.ddSalePriceVerifier(iterator, searchParameters, "gt");
		
		
		//只有publish_date
		logger.debug(String.format("  - [CHECK-INFO] gSearch-list's publish_date: %s", searchParameters.get("publish_date")));
		newmap.clear();
		newmap.put("-publish_date", SearchVerifier.getTimestampFromDate(searchParameters.get("publish_date"))+"~"); //转换成时间戳
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.publishDateVerifier(iterator, searchParameters, "gt");
		
		
		//只有 stock_status
		logger.debug(String.format("  - [CHECK-INFO] gSearch-list's stock_status: %s", searchParameters.get("stock_status")));
		newmap.clear();
		newmap.put("-stock_status", "1~");
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.stockVerifier(iterator, searchParameters);
		
		
		//综合
		logger.debug(String.format("  - [CHECK-INFO] gSearch-list's all: %s", searchParameters));
		newmap.clear();		
		newmap.put("-binding_id", searchParameters.get("binding_id"));		
		newmap.put("-dd_sale_price", searchParameters.get("dd_sale_price")+"~");
		newmap.put("-publish_date", SearchVerifier.getTimestampFromDate(searchParameters.get("publish_date"))+"~");
		newmap.put("-stock_status", searchParameters.get("stock_status"));
		newmap.putAll(tm);
		newmap.put("cat_paths", searchParameters.get("cat_paths"));
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.bookListAllVerifier(iterator, searchParameters);
		return result;
	}
	
	public boolean doVerifyMerge(Map<String, String> searchParameters){
		boolean result = true;
		logger.debug(String.format("  - [CHECK-INFO] gSearch-merge's searchParameters: %s", searchParameters));
		Map<String, String> tm = new HashMap<String, String>();
		Map<String, String> newmap = new HashMap<String, String>();
		ProdIterator iterator = null;
		tm.put("st", "pub");
		tm.put("um", "search_ranking");
		tm.put("-product_medium", "0");
		//综合
		newmap.clear();		
		try {
			newmap.put("isbn_search", searchParameters.get("isbn_search"));
			newmap.put("author_name", URLEncoder.encode(searchParameters.get("author_name"), "GBK"));
			newmap.put("publisher", URLEncoder.encode(searchParameters.get("publisher"), "GBK"));
			newmap.put("product_name", URLEncoder.encode(searchParameters.get("product_name"), "GBK"));
			newmap.put("q", URLEncoder.encode(searchParameters.get("q"), "GBK"));
		} catch (UnsupportedEncodingException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		newmap.put("-publish_date", SearchVerifier.getTimestampFromDate(searchParameters.get("publish_date"))+"~");
		newmap.put("-binding_id", searchParameters.get("binding_id"));		
		newmap.put("-dd_sale_price", searchParameters.get("dd_sale_price")+"~");  //市场价
		newmap.put("-stock_status", searchParameters.get("stock_status"));
		newmap.put("-cat_paths", searchParameters.get("cat_paths"));
		newmap.putAll(tm);
		iterator = new ProdIterator(newmap);
		result = result && SearchVerifier.bookMergeAllVerifier(iterator, searchParameters);
		return result;
	}
}
