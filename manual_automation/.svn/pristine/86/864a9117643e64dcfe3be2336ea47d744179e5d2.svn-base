package com.dangdang.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.execchain.RequestAbortedException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import com.dangdang.client.SearchRequest;
import com.dangdang.client.URLBuilder;

/**
 * @author liuzhipengjs@dangdang.com
 * @version 创建时间：2015年5月29日 下午2:32:38 类说明 获取运营工具数据接口
 */
public class StoolData {

	// public static final String CHARSET = "utf-8";
	// public static final String CHARSET = "GBK";

	public static String query4json(String baseurl, String type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("op_type", "query");
		map.put("ret_type", "json");
		map.put("type", type);
		String url = URLBuilder.buildUrl(map, baseurl);
		String result4json = SearchRequest.doGetToolData(url, null, "GBK");// 运营工具统一使用GBK编码。。。。
		return result4json;
	}



	// 调试使用
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String baseurl = "http://10.255.254.72:8898/";
		String type = "new_product_category_weight";
		String a = query4json(baseurl, type);

		System.out.println(a);

	}
}
