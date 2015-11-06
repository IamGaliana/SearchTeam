package com.dangdang.verifier.blacklist;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.BlackListQuery;
import com.dangdang.util.XMLParser;

/**
 * @author gaoyanjun@dangdang.com
 * @version 创建时间：2015-06-12 19:50 
 * 类说明 分类直达验证：
 * 运营工具中配置的权重为100的搜索词，在有效期内，按照最新修改时间排序，应该直达到固定分类下，
 * 验证点是xml结果中有//StatInfo/Path/items/item节点，该节点代表面包屑
 */
public class CategoryDirectionVerifier {

	// 日志记录器
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ShopThroughVerifier.class);

	/*
	 * 主方法：验证是否分类直达
	 */
	public boolean doVerify(Entry<String, Map<String, String>> entry) {
		return isCatDirection(entry, false);
	}

	/*
	 *  验证是否分类直达
	 */
	@SuppressWarnings("finally")
	private boolean isCatDirection(Entry<String, Map<String, String>> entry, boolean isPass) {
		// 运营工具中配置的关键词和分类信息
		String query = entry.getKey();
		Map<String, String> pathInfo = entry.getValue();
		String expectedCatPath = pathInfo.get("category_path");
		String expectedCatID = pathInfo.get("category_id");
		String expectedPathName = pathInfo.get("path_name");
		
		// 请求url
		Map<String, String> urlMap = this.getUrlMap(query);
		String urlString = URLBuilder.buildUrl(urlMap);
		String xml = SearchRequester.get(urlString);
			
		try {
			Document doc = XMLParser.read(xml);
			List<Node> pathList = XMLParser.pathInfo(doc);
			if(pathList!=null || !pathList.isEmpty()){
				//验证是否为分类直达(是否包含(//StatInfo/Path/items/item节点)
				if(pathList.size()>0){
					// 取  //StatInfo/Path/items/ 下的最后一个item，分类直达的最底级分类
					Node lastNode = pathList.get(pathList.size()-1);
					
					String category_id = XMLParser.catePathID(lastNode);
					String cat_paths = XMLParser.catePath(lastNode);
					String paths_name = XMLParser.catePathName(lastNode);
					
					if(expectedCatPath.equals(cat_paths) && 
							expectedCatID.equals(category_id) &&
							expectedPathName.equals(paths_name))
						isPass = true;		
					else {
						logger.info("expected category info for query: " + query + " is : [" + expectedCatID + "," + expectedCatPath + "," + expectedPathName + "]; "
								+ "actual category info is [" + category_id + "," + cat_paths + "," + paths_name + "]");
						isPass = false;
					}						
				}
				 else{
					logger.info("No path [//StatInfo/Path/items/item] for query:" + query);
					isPass = false;
				}
			}
			else{ 
				logger.info("No path [//StatInfo/Path/items/item] for query:" + query);
				isPass = false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}			
		finally{
			return isPass;
		}
	}

	/*
	 *  拼接请求的url
	 */
	private Map<String, String> getUrlMap(String queryInfo) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pg", "1");
		map.put("ps", "60");
		map.put("st", "full");		
		map.put("um", "search_ranking");
		map.put("_new_tpl", "1");//search_ranking必加
		map.put("q", queryInfo);
		return map;
	}

}
