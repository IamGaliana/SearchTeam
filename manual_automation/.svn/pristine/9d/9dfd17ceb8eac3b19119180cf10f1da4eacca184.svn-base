package com.dangdang.verifier.search_ranking;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
/**
 * 说明：店铺识别验证方法
 * @author gaoyanjun
 * @version 创建时间：2015/05/11 16:35 
 * 
 */
public class QueryIdentifyShopVerifier {

	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(QueryIdentifyShopVerifier.class);
		
	/**
	 * 店铺识别验证方法
	 * @param iterator 	结果集
	 * @param shopid	预期的店铺id
	 * @param query		query词
	 * @param vp		验证点
	 * @return			如果结果集中所有单品的shopid和预期shopid一致，则pass，否则fail
	 */
	public boolean Verifier(ProdIterator iterator,String shopid, String query, String vp){	
		while(iterator.hasNext()){
			Node node = iterator.next();
			String productId = XMLParser.product_id(node);
			String actualShopId = XMLParser.product_shopID(node);
			String expectedShopId = shopid;
			
			//结果集中单品的shopid和预期的shopid不一致，则fail
			if(!actualShopId.equals(expectedShopId)){
				logger.info(String.format(" - [LOG FAILED] - verify function [%s] for query [%s], pid [%s] failed, expected shop_id is [%s], actual shop_id is [%s]", 
						vp, query, productId, shopid, actualShopId));
				return false;
			}
		}
		return true;
	}	
}
