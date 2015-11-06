package com.dangdang.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2015年5月29日 下午3:32:41  
 * 类说明  
 */
public class JsonParser {
	
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	/**   
	 * 类说明    解析运营工具中教育品类新品json数据
	 */
	public static Map<String, List<String>> newProdData(String result) {
		Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
		
		try {
			JsonNode node = objectMapper.readTree(result);
			if (node.isArray()) {
				Iterator<JsonNode> nodeIterator = node.elements();
				ArrayList<JsonNode> nodeList = new ArrayList<JsonNode>();
				while (nodeIterator.hasNext()) {
					JsonNode jsonNode = (JsonNode) nodeIterator.next();
					nodeList.add(jsonNode);
//					System.out.println(jsonNode);//挨个打印节点String
				}
				//倒序，需要新覆盖旧。。。原http接口json顺序是从新到旧，需要从旧到新
				for (int i = nodeList.size()-1; i >= 0; i--) {
					String pids = nodeList.get(i).get("product_id").toString().replace("\"", "");//去掉双引号
					String[] newProdArray = pids.split(",");
					List<String> newProdList = Arrays.asList(newProdArray);
					String a = nodeList.get(i).get("keyword").toString().replace("\"", "");//去掉双引号
//					System.out.println(a);
					dataMap.put(a, newProdList);
				}
				
			}else {
				
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
