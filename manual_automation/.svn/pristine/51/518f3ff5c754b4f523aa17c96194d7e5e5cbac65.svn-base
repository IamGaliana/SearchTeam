package com.dangdang.verifier.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IFilterVerifier;

/**   
 * @author gaoyanjun@dangdang.com  
 * @version 创建时间：2015/07/20 
 * 类说明    按月销量筛选
 */
public class SaleMonthVerifier extends IFilterVerifier {
	
private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SaleMonthVerifier.class);	
	
	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map, boolean hasResult) {
		if(!iterator.hasNext()){
			iterator.reSet();
		}
		
		// 获取url中的saleMonth筛选值，参数格式为&-sale_Month=1~	，意思是大于等于1	
		//String saleMonth = map.get("-sale_month").replace("~", "");
		String saleMonth = "1";
		
		while (iterator.hasNext()){
			Node node = iterator.next();
			if (!isCorrectSaleMonth(node, map, hasResult,saleMonth)){
				return false;
			}
		}
		logger.debug(String.format(" -  [LOG_PASS] check pass for 【SaleMonth】 filter"));
		return true;		
	}
	
	/**
	 * 验证以下两点：
	 * 有结果时，月销量是否比筛选值大
	 * 无结果时，是否每个单品的上架时间小于筛选值
	 */
	public boolean isCorrectSaleMonth(Node node, Map<String, String> map,boolean hasResult,String expected_salemonth) {		
		String product_id = XMLParser.product_id(node);
		String sale_month = XMLParser.product_sale_month(node);
		if (hasResult && (Integer.parseInt(sale_month) < Integer.parseInt(expected_salemonth))){		
			logger.error(String.format(" -  [LOG_FAILED] %s : actual sale_month: %s; expect sale_month: %s; should be [actual>=expected]",  product_id, sale_month ,expected_salemonth));
			return false;
		}else if (!hasResult && Integer.parseInt(sale_month) >= Integer.parseInt(expected_salemonth)){
			logger.error(String.format(" -  [LOG_FAILED] %s : actual sale_month: %s; should be [no result]",  product_id, sale_month ,expected_salemonth));
			return false;
		}
		return true;
	}

}