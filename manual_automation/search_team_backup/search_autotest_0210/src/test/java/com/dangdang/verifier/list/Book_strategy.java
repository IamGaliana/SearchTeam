package com.dangdang.verifier.list;

import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Node;

import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2014年11月1日 下午3:26:28  
 * 类说明  
 */
public class Book_strategy {
	
	public static Logger logger = Logger.getLogger(Book_strategy.class);
	
	private int totalCount = 0;
	
	private int dd_sell = 0;
	
	
	public boolean Verifier(ProdIterator iterator, Map<String, String> map) {
		
		Product product_dd = new Product();//用于比较自营
		Product product = new Product();//用于比较非自营
		
		product_dd.setScore("99999999");
		product.setScore("99999999");
		
		int totalCountXml = iterator.getTotalCount();
		if (totalCountXml==0) {
			logger.fatal(" - [LOG_EXCEPTIO] - This category no result");
		}
		
		while (iterator.hasNext()) {
			Node node = iterator.next();
			if (XMLParser.product_is_dd_sell(node).equals("1")) {
				dd_sell++;//获取自营数量
			}
			totalCount++;
		}
		
		//小于等于60，只有一页
		if (totalCount<=60 && totalCount>0) {
			if (iterator.reSet()) {
				if (dd_sell>=0 && dd_sell<totalCount) {
					return dd_sell_sort(iterator, dd_sell, product_dd) && not_dd_sell_sort(iterator, dd_sell, product);
				}else{
					return all_sort(iterator, totalCount, product);
				} 
			}else {
				logger.fatal(" - [LOG_EXCEPTIO] - Iterator reset error");
			}
		//大于60，多页
		}else {
			if (iterator.reSet()) {
				if (dd_sell>=0 && dd_sell<=60) {
					return dd_sell_sort(iterator, dd_sell, product_dd) && not_dd_sell_sort(iterator, dd_sell, product);
				}
				else { 
				return dd_sell_sort(iterator, 60, product_dd) && not_dd_sell_sort(iterator, 60, product);
				}
			}else {
				logger.fatal(" - [LOG_EXCEPTIO] - Iterator reset error");
			}
		}
		return true;
	}
	
	public boolean dd_sell_sort(ProdIterator iterator, int dd_sell, Product product){
		if (dd_sell==0) {
			return true;
		}
		for (int i = 0; i < dd_sell; i++) {
			Node node = iterator.next();
			int a = Integer.valueOf(product.getScore());
			product.setScore(XMLParser.product_scope(node));
			int b = Integer.valueOf(product.getScore());
			
			if (a<b) {
				logger.fatal(" - [LOG_EXCEPTIO] - Wrong sort:"+XMLParser.product_id(node));
				return false;
			}
			if (!XMLParser.product_is_dd_sell(node).equals("1")) {
				logger.fatal(" - [LOG_EXCEPTIO] - Is not dd_sell:"+XMLParser.product_id(node));
				return false;
			}
		}
		return true;
	}
	
	public boolean not_dd_sell_sort(ProdIterator iterator, int dd_sell, Product product){
		for (int i = dd_sell; i < totalCount; i++) {
			Node node = iterator.next();
			int c = Integer.valueOf(product.getScore());
			product.setScore(XMLParser.product_scope(node));
			int d = Integer.valueOf(product.getScore());
			if (c<d) {
				logger.fatal(" - [LOG_EXCEPTIO] - Wrong sort:"+XMLParser.product_id(node));
				return false;
			}
		}
		return true;
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
