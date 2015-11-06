package com.dangdang.verifier.filter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.dom4j.Node;

import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IFilterVerifier;

public class OutletsVerifier extends IFilterVerifier {

	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map, boolean hasResult) {
			
			// 日期刷
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 当前日期
			Date now = new Date();
			if(!iterator.hasNext()){
				iterator.reSet();
			}
		while(iterator.hasNext()){
			Node node = iterator.next();
			if(!idOutlets(node,hasResult)){
				return false;
			}
		
		}
		return true;
	}
	
	public boolean idOutlets(Node node,boolean hasResult){
		// 日期刷
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 当前日期
		Date now = new Date();
		
		logger.debug("/****************************product**************************/");
		//商品数据类
		Product product = new Product();
		// 对product赋值
		product.setProduct_id(XMLParser.product_id(node));
		product.setProduct_action_id(XMLParser.product_ActionID(node));
		product.setAction_start_date(XMLParser
				.product_ActionStartDate(node));
		product.setAction_end_date(XMLParser
				.product_ActionEndDate(node));
		product.setIs_delete(XMLParser.product_is_delete(node));
		
		try{
			if(hasResult && !isAvailable(product.getProduct_action_id(),product.getIs_delete() ,product.getAction_start_date(), product.getAction_end_date(), format, now)){
				logger.error(" - [OUTLETS] - "+product.getProduct_id()
						+ " : No available outlets activity is setting on it.");
				return false;
				
			}else if(!hasResult && isAvailable(product.getProduct_action_id(),product.getIs_delete(), product.getAction_start_date(), product.getAction_end_date(), format, now)){
				logger.error(" - [OUTLETS-NORESULT] - "+product.getProduct_id()
						+ " : An available outlets activity is setting on it.");
				return false;
			}
		}catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return false;
		}
		return true;
	}
	
	public boolean isAvailable(String id,String isDelete,String startDate,String endDate,SimpleDateFormat format,Date now) throws ParseException{
		//int outlets_id = 11000983;
		//int activity_outlets_id = 12000639;
		// 是否有活动设置在此商品上
	 if(id.equals("11000983") && isDelete.equals("0")) {
			Date action_start_time = format.parse(startDate);
			Date action_end_time = format.parse(endDate);
			
			// 现有活动是否还有效
			if (now.after(action_start_time)
					&& now.before(action_end_time)) {
			} else {
				logger.debug(" - [OUTLETS] - "+"start_date:"+startDate+" end_date:"+endDate);
				return false;
			}
		}else {
			logger.debug(" - [OUTLETS] - "+id
					+ " : No available outles activity is setting on it.");
			// System.out.println(product.getProduct_id()+" : No available outles activity is setting on it.");
			return false;
		}
		return true;
	}
	
	public boolean isOutDate(Date start,Date end){
		// 当前日期
		Date now = new Date();
		// 现有活动是否还有效
		if (now.after(start)
				&& now.before(end)) {
			return false;
		} else {
			return true;
		}
	}
	
	
}

