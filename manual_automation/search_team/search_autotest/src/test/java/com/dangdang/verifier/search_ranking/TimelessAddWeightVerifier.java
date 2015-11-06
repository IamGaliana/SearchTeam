package com.dangdang.verifier.search_ranking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.dom4j.Node;

import com.dangdang.util.AssertTools;
import com.dangdang.util.DateTimeHandler;
import com.dangdang.util.FileUtil;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
/**
 * 说明：百货商品时效性加权策略
 * @author gaoyanjun
 * @version 创建时间：2015/05/11 16:35 
 * 
 */
public class TimelessAddWeightVerifier {

	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(TimelessAddWeightVerifier.class);
		
	/**
	 * 商品时效性加权
	 * @param old_iterator
	 * @param new_iterator
	 * @param query
	 * @param vps
	 * @return
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public boolean Verifier(ProdIterator old_iterator,ProdIterator new_iterator,String query, String vps) throws NumberFormatException, IOException{	
		boolean fail = false;
		boolean hasResult = false;
		int unmatchedCount = 0;
		String fileName = FileUtil.getRemoteFile("http://10.4.32.86:9030/timeless_data/pid_time_pro.txt");
		List<String> addWeightPids = FileUtil.readTxtFile(fileName);
		
		Map<String, Integer> mapOldResult = new HashMap<String, Integer>();
		Map<String, Integer> mapNewResult = new HashMap<String, Integer>();
		
		// 增加timelessSwitch=0参数时的结果集，只取在addWeightPids文件中的的pid 和 score， 放到mapOldResult中
		while (old_iterator.hasNext()){
			Node old_node = old_iterator.next();
			String old_productId = XMLParser.product_id(old_node);
			Integer beforeScore = Integer.parseInt(XMLParser.product_scope(old_node));	
			if(addWeightPids.contains(old_productId))
				mapOldResult.put(old_productId, beforeScore);
		}
		
		
		// 增加timelessSwitch=1参数后的结果集，取pid 和 score 放到mapNewResult中
		while(new_iterator.hasNext()){
			Node new_node = new_iterator.next();
			String new_product_id = XMLParser.product_id(new_node);
			Integer afterScore = Integer.parseInt(XMLParser.product_scope(new_node));
			mapNewResult.put(new_product_id, afterScore);
		}
		
		// 以mapOldResult为基准，遍历每个pid；
		// 如果mapNewResult中包含该pid，则对比score值是否正确增加
		// 如果不包含该pid，则跳过
		Iterator<String> it = mapOldResult.keySet().iterator();
		while(it.hasNext()){
			hasResult = true;
			Object key = it.next();
			if(mapNewResult.containsKey(key)){
				Integer beforeScore = mapOldResult.get(key);
				Integer afterScore = mapNewResult.get(key);
				
				// 加权，商业因素分加上5分		
				if(AssertTools.BusinessScore(afterScore)  != AssertTools.BusinessScore(beforeScore) + 5){					
					logger.error("query -["+ query +"], Score of product [" + key + "] was not set to right value: "
							+ "old score is [" +beforeScore +"], new score is ["+ afterScore +"]");
					fail = true;
					unmatchedCount ++;
				} 
			}
		}
		
		if (!hasResult){
			logger.error(" - [LOG_SKIP] - verify function ["+ vps +"], query - [" + query+ "] - NO RESULT" );
			return true;
		}
			
		if (fail){ 
			logger.error(" - [LOG_FAILED] - verify function ["+ vps +"], query - [" + query+ "]"
					+ "- unMatched product count is [" + unmatchedCount +"]");
			return false;
		}
		else {
			logger.info(" - [LOG_PASS] - verify function ["+ vps +"], query - [" + query + "]");
			return true;
		} 
		
		/*// 原来的方法，废弃，有bug：new_iterator每次遍历后会记录单品位置，下次遍历会从该位置向下取，之前的品会被忽略掉，比较不全面
		while(old_iterator.hasNext()){
			hasResult = true;
			Node old_node = old_iterator.next();
			String old_productId = XMLParser.product_id(old_node);
			Integer beforeScore = Integer.parseInt(XMLParser.product_scope(old_node));				
			
			// TODO : 判断该pid是否在时效性加权的文本文件里，如果在才加权，否则skip
			if(addWeightPids.contains(old_productId)){
				String new_productId = null;
				Integer afterScore = null;
				while(new_iterator.hasNext()){
					Node new_node = new_iterator.next();
					new_productId = XMLParser.product_id(new_node);
					if (new_productId.equals(old_productId)){
						afterScore = Integer.parseInt(XMLParser.product_scope(new_node));
						break;
					}else
						continue;
				}
				
				if(afterScore != null){	
					// 加权，商业因素分加上5分
					if(AssertTools.BusinessScore(afterScore)  != AssertTools.BusinessScore(beforeScore) + 5){					
						logger.error("query -["+ query +"], Score of product [" + new_productId + "] was not set to right value: "
								+ "old score is [" +beforeScore +"], new score is ["+ afterScore +"]");
						fail = true;
						unmatchedCount ++;
					} 
				}else{
					logger.error("query -["+ query +"], did not find pid [" + old_productId + "] in new result set");
					fail = true;
					unmatchedCount ++;
				}
			}
		}	*/		
	}		
}
