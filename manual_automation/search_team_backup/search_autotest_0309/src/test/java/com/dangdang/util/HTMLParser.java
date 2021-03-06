package com.dangdang.util;

import java.util.HashMap;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
/*
 * 目前废弃不用
 */
public class HTMLParser {

	public static Config config = new Config();
	public static final String QueryAnalysisURL = config.get_AnayURL();

	public static Map<String,String> getCategoryPriority(String keyWord){
		Map<String,String> result = new HashMap<String,String>();
		try {
			Parser parser = new Parser(QueryAnalysisURL+"?key="+keyWord);
			NodeList  nd = parser.parse(new TagNameFilter("table"));
			Node table = nd.elementAt(2);
			NodeList tds = new NodeList();
			table.collectInto(tds, new TagNameFilter("td"));
			if(tds.size()>3&& tds.size()%3==0){
				Node [] td_array = tds.toNodeArray();
				System.out.println(td_array.length);
				for(int i=1;i<td_array.length/3;i++){
					result.put(td_array[i*3].getChildren().asString(), td_array[i*3+2].getChildren().asString());
				}
			return result;
			}
			
		} catch (ParserException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		Map<String,String> map = getCategoryPriority("手机");
	}
}
