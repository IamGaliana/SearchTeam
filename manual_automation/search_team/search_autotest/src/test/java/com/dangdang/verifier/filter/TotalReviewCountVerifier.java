package com.dangdang.verifier.filter;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IFilterVerifier;

/**   
 * @author gaoyanjun@dangdang.com  
 * @version 创建时间：2014年11月3日 下午3:44:44  
 * 类说明    按评论数筛选
 */
public class TotalReviewCountVerifier  extends IFilterVerifier {
	
private final static org.slf4j.Logger logger = LoggerFactory.getLogger(TotalReviewCountVerifier.class);	
	
	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map, boolean hasResult) {
		if(!iterator.hasNext()){
			iterator.reSet();
		}
		
		// 获取url中的total_review_count筛选值，参数格式为&-total_review_count=1~，意思是大于等于1	
		//String reviewCount = map.get("-total_review_count").replace("~", "");
		String reviewCount = "1";
		while (iterator.hasNext()){
			Node node = iterator.next();
			if (!isCorrectReviewCount(node, map, hasResult,reviewCount)){
				return false;
			}
		}
		logger.debug(String.format(" -  [LOG_PASS] check pass for 【SaleMonth】 filter"));
		return true;		
	}
	
	/**
	 * 验证以下两点：
	 * 有结果时，评论数是否比筛选值大
	 * 无结果时，是否每个单品的评论数都小于筛选值
	 */
	public boolean isCorrectReviewCount(Node node, Map<String, String> map,boolean hasResult,String expected_reviewcount) {		
		String product_id = XMLParser.product_id(node);
		String actual_reviewcount = XMLParser.product_total_review_count(node);
		if (hasResult && (Integer.parseInt(actual_reviewcount) < Integer.parseInt(expected_reviewcount))){		
			logger.error(String.format(" -  [LOG_FAILED] %s : actual sale_month: %s; expect sale_month: %s; should be [actual>=expected]",  product_id, actual_reviewcount ,expected_reviewcount));
			return false;
		}else if (!hasResult && Integer.parseInt(actual_reviewcount) >= Integer.parseInt(expected_reviewcount)){
			logger.error(String.format(" -  [LOG_FAILED] %s : actual sale_month: %s; should be [no result]",  product_id, actual_reviewcount ,expected_reviewcount));
			return false;
		}
		return true;
	}

}
