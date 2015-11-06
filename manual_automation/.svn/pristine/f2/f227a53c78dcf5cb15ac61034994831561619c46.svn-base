package com.dangdang.verifier.blacklist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.BlackListQuery;
import com.dangdang.util.XMLParser;

/**
 * @author liuzhipengjs@dangdang.com
 * @version 创建时间：2014年12月20日 上午10:21:14 类说明 店铺直达验证
 */
public class ShopThroughVerifier {

	// 日志记录器
	private static Logger logger = Logger.getLogger(ShopThroughVerifier.class);

	public boolean doVerify(BlackListQuery queryInfo) {
		// TODO Auto-generated method stub
		return isShopThrough(queryInfo, true);
	}

	private boolean isShopThrough(BlackListQuery queryInfo, boolean b) {
		// TODO Auto-generated method stub
		String[] queryArray = queryInfo.getQuery().trim().split(",");
		for (String query : queryArray) {
			Map<String, String> urlMap = this.getUrlMap(query);
			String urlString = URLBuilder.buildUrl(urlMap);
			String xml = SearchRequester.get(urlString);

			try {
				Document doc = XMLParser.read(xml);
				String type = XMLParser.shopinfotype(doc);
				String shopurl = XMLParser.shopinfourl(doc);
				String shopname = XMLParser.shopinfoname(doc);
				String shopimage = XMLParser.shopinfoimage(doc);
				if (type.equals("NULL")) {
					return !b;
				}// 没有更新数据库字段名，数据库里：CategoryPath存的是店铺ID，Commons存的是图片url,op_type存的是店铺名称
				else if (type.equals("1") && shopurl.contains(queryInfo.getCategoryPath()) && shopname.equals(queryInfo.getOptype())
						&& shopimage.equals(queryInfo.getCommons())) {
					continue;
				} else {
					return !b;
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

	private Map<String, String> getUrlMap(String queryInfo) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("st", "full");
		map.put("ps", "60");
		map.put("um", "search_ranking");
		map.put("_new_tpl", "1");//search_ranking必加
		map.put("q", queryInfo);
		// try {
		//
		// map.put("q", URLEncoder.encode(queryInfo.getQuery(), "GBK"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		return map;
	}

}
