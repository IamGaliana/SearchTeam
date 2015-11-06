package com.dangdang.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.DateTimeAtCompleted;

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
	
	/*
	 * add by gaoyanjun @ 2015/06/16
	 * 说明： 解析运营工具中分类直达模块的数据
	 */
	public static Map<String, Map<String, String>> CategoryDirectionData(String result){
		Map<String, Map<String, String>> ret = new HashMap<String, Map<String, String>>();
		
		try{
			JsonNode rootNode = objectMapper.readTree(result);
			if(rootNode.isArray()){
				Iterator<JsonNode> node = rootNode.elements();
				ArrayList<JsonNode> nodelist = new ArrayList<JsonNode>();
				while(node.hasNext()){
					JsonNode tempnode = (JsonNode)node.next();
					nodelist.add(tempnode);
				}
				
				// 同一个keyword，需要按修改时间，新数据覆盖旧数据
				// http接口返回json数据的顺序是从新到旧, 所以需要倒序读取nodelist内容，从旧到新
				// Map会用后边新的覆盖前面旧的
				for(int i = nodelist.size()-1; i >= 0; i--){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String startdate = nodelist.get(i).get("start_date").toString().replace("\"", "");
					String enddate = nodelist.get(i).get("end_date").toString().replace("\"", "");					
					
					Date start_date = df.parse(startdate);
					Date end_date = df.parse(enddate);	
					Date now = new Date();
					
					// 只取有效时间段内的数据
					if (start_date.before(now)&&end_date.after(now)){
						String keyword = nodelist.get(i).get("keyword").toString().replace("\"", "");	
						
						String categoryid = nodelist.get(i).get("category_id").toString().replace("\"", "");
						String cat_paths = nodelist.get(i).get("category_path").toString().replace("\"", "");
						String path_name = nodelist.get(i).get("path_name").toString().replace("\"", "");	
						
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("category_id", categoryid);
						dataMap.put("category_path", cat_paths);
						dataMap.put("path_name", path_name);
						
						ret.put(keyword, dataMap);
					}
				}
			}		
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
