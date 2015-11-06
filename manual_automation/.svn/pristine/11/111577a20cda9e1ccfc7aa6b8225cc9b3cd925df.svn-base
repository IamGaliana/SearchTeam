package com.dangdang.verifier.querymodify;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.util.XMLParser;

public class QueryModifyVerify {

	public static boolean verify(Map<String, String> map) throws Exception {
		Map<String, String> urlMap = new HashMap<String, String>();
		urlMap.put("st", "full");
		urlMap.put("um", "search_ranking");
		urlMap.put("ps", "60");
		urlMap.put("pg", "1");
		urlMap.put("_new_tpl", "1");
		urlMap.put(
				"q",
				URLEncoder.encode(map.get("query"), "GBK").replace("++++",
						"%20%20%20%20"));

		System.out.println("##########################################");
		System.out.println(URLEncoder.encode(map.get("query"), "GBK"));
		System.out.println("##########################################");
		// query改写之后的query
		String test_data = map.get("test_data");

		String url = URLBuilder.buildURL(urlMap);
		String xml = SearchRequester.get(url);
		Document doc = XMLParser.read(xml);
		String term = XMLParser.term(doc);

		// 如果无结果，则认为是正确的
		String totalCnt = XMLParser.getSingleNode(doc, "//TotalCnt");
		if (totalCnt.equals("0")) {
			return true;
		}

		// 去掉term中的逗号
		String[] termL = term.split(",");
		term = "";
		for (int i = 0; i < termL.length; i++) {
			term += termL[i];
		}

		System.out.println("应该被替换后的query:" + test_data);

		// 被程序改写之后的query
		String Term = XMLParser.getSingleNode(doc, "//Term");

		System.out.println("替换后的query:" + Term.toString());
		
		// Term中包含逗号
		if(Term.contains(",")){
			String[] TermList = Term.split(",");
			for (int i = 0; i < TermList.length; i++) {
				if(test_data.trim().contains(TermList[i])){
					continue;
				}else{
					return false;
				}
			}
			return true;
		}
		
		// if(test_data.trim().equals(original_query.trim())){
		// return true;
		// }
		if (test_data.trim().contains(Term.toString())) {
			return true;
		}
		return false;

	}
}
