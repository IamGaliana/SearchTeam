package com.dangdang.verifier.list;

import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Node;

import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2014年11月10日 下午3:46:25  
 * 类说明  
 */
public class Score {
	
	public static Logger logger = Logger.getLogger(Score.class);
	
	private int totalCount = 0;
	
	public boolean Verifier(ProdIterator iterator, Map<String, String> map) {
		
		Product product = new Product();//用于比较非自营
		
		product.setScore("99999999");
		
		int totalCountXml = iterator.getTotalCount();
		if (totalCountXml==0) {
			logger.fatal(" - [LOG_EXCEPTIO] - This category no result");
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
				logger.fatal(" - [LOG_EXCEPTIO] - Wrong sort:"+XMLParser.product_id(node));
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