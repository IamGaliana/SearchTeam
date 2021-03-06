package com.dangdang.verifier.list;

import java.util.Map;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2014年11月10日 下午3:46:25  
 * 类说明    主要验证除自营优先策略以外的列表页，不管是不是自营，统一是按照Score从大到小排序
 */
public class Score {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(Score.class);
	
	private int totalCount = 0;
	
	public boolean Verifier(ProdIterator iterator, Map<String, String> map) {
		
		Product product = new Product();//用于比较非自营
		
		product.setScore("99999999");
		
		int totalCountXml = iterator.getTotalCount();
		if (totalCountXml==0) {
			logger.info(" - [LOG_EXCEPTIO] - This category no result");
		}
		
		while (iterator.hasNext()) {
			Node node = iterator.next();
			totalCount++;
		}
		
		if (iterator.reSet()) {
			return all_sort(iterator, totalCount, product);
		}
		
		return false;
		
	}
	
	
	public boolean all_sort(ProdIterator iterator, int totalCount, Product product){
		for (int i = 0; i < totalCount; i++) {
			Node node = iterator.next();
			int a = Integer.valueOf(product.getScore());
			product.setScore(XMLParser.product_scope(node));
			int b = Integer.valueOf(product.getScore());
			if (a<b) {
				logger.info(" - [LOG_EXCEPTIO] - Wrong sort:"+XMLParser.product_id(node));
				return false;
			}
		}
		return true;
	}
	


	public boolean doVerify(ProdIterator iterator, Map<String, String> map) {
		// TODO Auto-generated method stub
		return false;
	}

}