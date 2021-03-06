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
 * @version 创建时间：2015年2月4日 下午2:41:35 类说明
 */
public class BrandThroughVerifier {

	// 日志记录器
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BrandThroughVerifier.class);

	public boolean doVerify(BlackListQuery queryInfo) {
		// TODO Auto-generated method stub
		return isBrandThrough(queryInfo, true);
	}

	private boolean isBrandThrough(BlackListQuery queryInfo, boolean b) {
		// TODO Auto-generated method stub
		String[] queryArray = queryInfo.getQuery().trim().split(",");
		for (String query : queryArray) {
			Map<String, String> urlMap = this.getUrlMap(query);
			String urlString = URLBuilder.buildURL(urlMap);
			String xml = SearchRequester.get(urlString);

			try {
				Document doc = XMLParser.read(xml);
				String directBrand = XMLParser.DirectBrand(doc);
				List<String> brands = XMLParser.getBrandNames(doc);
				
				if (queryInfo.getCommons().equals("直达")) {
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

	
	
	
	
	
	
	
	// 临时统计数据使用,效果测试
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
