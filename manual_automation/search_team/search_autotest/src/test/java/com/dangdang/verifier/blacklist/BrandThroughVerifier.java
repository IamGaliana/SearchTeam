package com.dangdang.verifier.blacklist;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.BlackListQuery;
import com.dangdang.util.DBConnUtil;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**
 * @author liuzhipengjs@dangdang.com
 * @version 创建时间：2015年2月4日 下午2:41:35 
 * 类说明：品牌直达验证类，需要在测试数据表black_list中设计数据，提前手动查询设计好直达的词，因为这个品牌直达词涉及到权重计算
 */
public class BrandThroughVerifier {

	// 日志记录器
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BrandThroughVerifier.class);

	public boolean doVerify(BlackListQuery queryInfo) {
		// TODO Auto-generated method stub
		return isBrandThrough(queryInfo, true);
	}

	
	
	/*
	 * 判断是否品牌直达
	 */
	private boolean isBrandThrough(BlackListQuery queryInfo, boolean b) {
		// TODO Auto-generated method stub
		String[] queryArray = queryInfo.getQuery().trim().split(",");
		for (String query : queryArray) {
			Map<String, String> urlMap = this.getUrlMap(query);
			String urlString = URLBuilder.buildURL(urlMap);
			String xml = SearchRequester.get(urlString);

			try {
				Document doc = XMLParser.read(xml);
				String directBrand = XMLParser.DirectBrand(doc);//获取XML结果中直达的品牌节点
				List<String> brands = XMLParser.getBrandNames(doc);
				//如果数据表中commons中写的“直达”，说明该条数据会触发直达逻辑
				if (queryInfo.getCommons().equals("直达")) {
					//XML结果中直达的品牌节点为设计好的品牌，且品牌汇总节点包含该品牌，并且结果集中每个品是否都属于该品牌商品
					return directBrand.equals(queryInfo.getOptype())&&brands.contains(queryInfo.getOptype())&&resultBrand(urlMap,queryInfo);
				} else {
					return directBrand.equals("");
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return b;
	}
	/*
	 * 判断结果集中每个品是否都属于该品牌商品
	 */
	private boolean resultBrand(Map<String, String> urlMap, BlackListQuery queryInfo) {
		// TODO Auto-generated method stub
		ProdIterator iterator = new ProdIterator(urlMap,179);
		while (iterator.hasNext()) {
			Node node = iterator.next();
			String brandId = XMLParser.product_brand(node);
			if (!brandId.equals(queryInfo.getBlackId())) {
				return false;
			}
		}
		return true;
	}

	private Map<String, String> getUrlMap(String query) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("st", "full");
		map.put("ps", "60");
		map.put("um", "search_ranking");
		map.put("_new_tpl", "1");// search_ranking必加
		try {
			map.put("q", URLEncoder.encode(query, "GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("_mod_ver", "S6");
		map.put("direct_brand", "1");
		// try {
		//
		// map.put("q", URLEncoder.encode(queryInfo.getQuery(), "GBK"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		return map;
	}

	
	
	
	
	
	
	
	// 临时统计数据使用,效果测试，统计function_query表中能够触发品牌直达的词集合
	public boolean statistics(ProdIterator iterator, Map<String, String> map) {
		// TODO Auto-generated method stub
		String query = map.get("query");
		Map<String, String> urlMap = this.getUrlMap(query);
		String urlString = URLBuilder.buildURL(urlMap);
		String xml = SearchRequester.get(urlString);

		try {
			Document doc = XMLParser.read(xml);
			String directbrand = XMLParser.DirectBrand(doc);
			if (!directbrand.equals("")) {
				logger.warn(query+">>>>>>"+directbrand+">>>>品牌筛选后结果总数："+XMLParser.totalCount(doc));
			}else {
//				logger.warn(">>>>未进入品牌直达>>>>："+query);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
