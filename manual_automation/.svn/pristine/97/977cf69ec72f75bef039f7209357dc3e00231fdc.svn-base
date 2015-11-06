package com.dangdang.verifier.compare;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.SearchCustomScheduler;
import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.ISortVerifier;
import com.dangdang.verifier.list.Score;

/**
 * 不同server之间，结果集的验证
 * 
 * @author gaoyanjun
 * 
 */
public class DifferentServerCmpVerifier {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SearchCustomScheduler.class);
	
	private static String baseUrl1 = "http://10.255.254.174:8390/";
	private static String baseUrl2 = "http://10.255.254.71:8390/";
		

	public boolean Verifier(String keyword) {	
		
		// 两个server， 同一keyword对应同样的map
		Map<String, String> map = URLBuilder.converURLPars("", keyword, null);
		logger.debug(" - SearchInfo1: " + map.toString());
		
		// 搜索结果不相同
		ProdIterator prod1 = new ProdIterator(map,1200,baseUrl1);
		ProdIterator prod2 = new ProdIterator(map,1200,baseUrl2);
		
		
//		if(prod1.getTotalCount() ==0 || prod2.getTotalCount() == 0){	// 如果结果集有一个为空，则不继续比较
//			logger.error("total count equals 0");
//			return false;
//		} else 
		if(prod1.getTotalCount() != prod2.getTotalCount()){		// 如果两个结果集totalcount不同，则不继续比较
			logger.error("total count of two servers are different!");
			return false;
		} else {														// 除以上情况，比较两个结果集中的每个product_id,记录个数和不同pid
			int sameCount = 0;
			//int differentCount = 0;
			boolean failTag = false;
			StringBuffer diffPids = new StringBuffer();
			
			while(prod1.hasNext() && prod2.hasNext()){
				String product_id1 = XMLParser.product_id(prod1.next(baseUrl1));
				String product_id2 = XMLParser.product_id(prod2.next(baseUrl2));
				if(product_id1.equals(product_id2)){
					sameCount ++;
				}else{
					// 有不同pid直接跳出
					//differentCount++;
					diffPids.append(product_id2 + ",");
					failTag = true;
					break;
				}
			}
			if(failTag){
				logger.error("different pids start at: "+ diffPids.toString());
				return false;
			}else{
				logger.info("two result sets have same pids, total count is: " + sameCount);
				return true;
			}
		}
		
	}
}
