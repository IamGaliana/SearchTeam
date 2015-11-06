package com.dangdang.verifier.blacklist;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.data.BlackListQuery;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**
 * 对黑名单功能进行验证
 */
public class SearchRankingBlackListVerifier{

	// 日志记录器
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SearchRankingBlackListVerifier.class);

	public boolean doVerify(BlackListQuery queryInfo) {
	    try {
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start_date = formatDate.parse(queryInfo.getStartDate());
			Date end_date = formatDate.parse(queryInfo.getEndDate());
		    Date now = new Date();

		    if(start_date.before(now)&&end_date.after(now)){//黑名单生效
		    	logger.info("blacklist filer query ["+queryInfo.getQuery()+"] is on");
		    	return isBlackList(queryInfo,true);
		    }else{//黑名单不生效
		    	logger.info("blacklist filer query ["+queryInfo.getQuery()+"] is off");
		    	return isBlackList(queryInfo,false);
		    }
		} catch (ParseException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
			return false;
		}	
	}
	
	private boolean isBlackList(BlackListQuery queryInfo,boolean bool){
		String id_type = queryInfo.getIdType();
		String[] blackList = queryInfo.getBlackId().split(",");
		
		Map<String,String> urlMap = this.getUrlMap(queryInfo);
		ProdIterator iterator = new ProdIterator(urlMap,1139);//搜索黑名单只看前19页去除黑名单
		
		int totalCount = 0;
		while (iterator.hasNext()) {
			Node node = iterator.next();
			totalCount++;
		}
		
		if (iterator.reSet()){
			return verify(id_type,blackList,iterator,bool,totalCount);
		}
		return bool;
	}
	
	
	private boolean verify(String id_type, String[] blackList, ProdIterator iterator, boolean bool, int totalCount) {
		// TODO Auto-generated method stub
		if (id_type.equals("shop")) {
			for (int i = 0; i < totalCount; i++) {
				
				Node node = iterator.next();
				String shopId = XMLParser.product_shopID(node);
				
				for (String blackid : blackList) {
					if (shopId.equals(blackid)) {
//						logger.info("");
						return !bool;
					}
				}
			}
		}else if (id_type.equals("product")) {
			for (int i = 0; i < totalCount; i++) {
				
				Node node = iterator.next();
				String productId = XMLParser.product_id(node);
				
				for (String blackid : blackList) {
					if (productId.equals(blackid)) {
//						logger.info("");
						return !bool;
					}
				}
			}
		}
		
		return bool;
		
	}

	
	private Map<String,String> getUrlMap(BlackListQuery queryInfo){
		Map<String,String> map = new HashMap<String,String>();
		map.put("st", "full");
		map.put("ps", "60");
		map.put("um", "search_ranking");
		map.put("_new_tpl", "1");//search_ranking必加
		try {
			map.put("q", URLEncoder.encode(queryInfo.getQuery(), "GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}
	
	
	
	
	
	
//	private int getBlackProductCount(Map<String,String> urlMap,String[] blackList){
//		int count = 0;
//		ProdIterator iterator = new ProdIterator(urlMap);
//		while (iterator.hasNext()){
//			Node prod = iterator.next();
//			for(String balckId : blackList){
//				if(balckId.trim().equals(XMLParser.product_shopID(prod))){
//					count++;
//				}
//			}
//		}
//		return count;
//	}
	
//	private boolean verify(String id_type,String[] blackList,List<Node> productList,List<Node> productLimitList,boolean bool,int pagelimit){
//		if(productLimitList.size()==0){
//			logger.error("no query result error");
//			return false;
//		}
//		boolean flag = true;
//		if(bool==true){//验证黑名单生效
//			if(id_type.equals("shop")){
//				//验证前pageLimit页不出现黑名单shop
//				for(String balckId : blackList){
//					for (Node subNode : productLimitList) {
//						String shopId = XMLParser.product_shopID(subNode);
//						if(balckId.trim().equals(shopId)){
//							logger.error("The blacklist shop "+balckId+" should not exsit before page "+pagelimit);
//							return false;
//						}
//					}
//				}
//				
//				if(productList.size()==0){//delete情况
//					return true;
//				}
//				
//				//验证pageLimit页之后出现所有的黑名单shop
//				for(String balckId : blackList){
//					boolean temp = false;
//					for (Node subNode : productList) {
//						String shopId = XMLParser.product_shopID(subNode);
//						if(balckId.trim().equals(shopId)){
//							temp = true;
//						}
//					}
//					if(temp==false){
//						logger.error("The blacklist shop "+balckId+" should exsit after page "+pagelimit);
//						return false;
//					}
//					flag = flag&temp;
//				}
//			}else if(id_type.equals("product")){
//				//验证前pageLimit页不出现黑名单商品
//				for(String balckId : blackList){
//					for (Node subNode : productLimitList) {
//						String productId = XMLParser.product_id(subNode);
//						if(balckId.trim().equals(productId)){
//							logger.error("The blacklist product "+balckId+" should not exsit before page "+pagelimit);
//							return false;
//						}
//					}
//				}
//				
//				if(productList.size()==0){//delete情况
//					return true;
//				}
//				
//				//验证pageLimit页之后出现所有的黑名单商品
//				for(String balckId : blackList){
//					boolean temp = false;
//					for (Node subNode : productList) {
//						String productId = XMLParser.product_id(subNode);
//						if(balckId.trim().equals(productId)){
//							temp = true;
//						}
//					}
//					if(temp==false){
//						logger.error("The blacklist product "+balckId+" should exsit after page "+pagelimit);
//						return false;
//					}
//					flag = flag&temp;
//				}
//			}
//		}else{//验证黑名单不生效
//			if(id_type.equals("shop")){
//				//验证前pageLimit页出现所有的黑名单shop
//				for(String balckId : blackList){
//					boolean temp = false;
//					for (Node subNode : productLimitList) {
//						String shopId = XMLParser.product_shopID(subNode);
//						if(balckId.trim().equals(shopId)){
//							temp = true;
//						}
//					}
//					if(temp==false){
//						logger.error("The blacklist shop "+balckId+" should exsit before page "+pagelimit);
//						return false;
//					}
//					flag = flag&temp;
//				}
//				
//				//验证pageLimit页之后不出现黑名单shop
//				for(String balckId : blackList){
//					for (Node subNode : productList) {
//						String shopId = XMLParser.product_shopID(subNode);
//						if(balckId.trim().equals(shopId)){
//							logger.error("The blacklist shop "+balckId+" should not exsit after page "+pagelimit);
//							return false;
//						}
//					}
//				}
//			}else if(id_type.equals("product")){
//				//验证前pageLimit页出现所有黑名单商品
//				for(String balckId : blackList){
//					boolean temp = false;
//					for (Node subNode : productLimitList) {
//						String productId = XMLParser.product_id(subNode);
//						if(balckId.trim().equals(productId)){
//							temp = true;
//						}
//					}
//					if(temp==false){
//						logger.error("The blacklist product "+balckId+" should exsit before page "+pagelimit);
//						return false;
//					}
//					flag = flag&temp;
//				}
//				
//				//验证pageLimit页之后不出现黑名单商品
//				for(String balckId : blackList){
//					for (Node subNode : productList) {
//						String productId = XMLParser.product_id(subNode);
//						if(balckId.trim().equals(productId)){
//							logger.error("The blacklist product "+balckId+" should not exsit after page "+pagelimit);
//							return false;
//						}
//					}
//				}
//			}
//		}
//		
//		return flag;
//	}
	
	
}
