package com.dangdang.client;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.CategoryFeedback;
import com.dangdang.data.AttribMap;
import com.dangdang.data.FilterMap;
import com.dangdang.util.Config;
import com.dangdang.util.DateTimeHandler;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.blacklist.BrandThroughVerifier;
import com.dangdang.verifier.categoryFeedback.CategoryFeedbackVerify;
import com.dangdang.verifier.ebook.EBookVerifier;
import com.dangdang.verifier.filter.AttribVerifier;
import com.dangdang.verifier.filter.BrandVerifier;
import com.dangdang.verifier.filter.CategoryVerifier;
import com.dangdang.verifier.filter.DDSellVerifier;
import com.dangdang.verifier.filter.ExclusivePrice;
import com.dangdang.verifier.filter.FeedbackVerifier;
import com.dangdang.verifier.filter.M_ExclusivePrice;
import com.dangdang.verifier.filter.OutletsVerifier;
import com.dangdang.verifier.filter.PreSaleVerifier;
import com.dangdang.verifier.filter.PriceIntervalVerifier;
import com.dangdang.verifier.filter.PromoVerifier;
import com.dangdang.verifier.filter.StockVerifier;
import com.dangdang.verifier.gsearch.B2cSearchVerifier;
import com.dangdang.verifier.gsearch.BookSearchVerifier;
import com.dangdang.verifier.gsearch.DigitalProductSearchVerifier;
import com.dangdang.verifier.gsearch.EBookSearchVerifier;
import com.dangdang.verifier.gsearch.MusicSearchVerifier;
import com.dangdang.verifier.gsearch.VedioSearchVerifier;
import com.dangdang.verifier.iVerifier.IFilterVerifier;
import com.dangdang.verifier.iVerifier.ISortVerifier;
import com.dangdang.verifier.iVerifier.M_IFilterVerifier;
import com.dangdang.verifier.list.Book_strategy;
import com.dangdang.verifier.list.Children_strategy;
import com.dangdang.verifier.filter.TotalReviewCountVerifier;
import com.dangdang.verifier.filter.SaleMonthVerifier;
import com.dangdang.verifier.filter.FirstInputDateVerifier;
import com.dangdang.verifier.list.List_DuplicateVerifier;
import com.dangdang.verifier.list.Score;
import com.dangdang.verifier.producttop.ProductTopVerify;
import com.dangdang.verifier.querymodify.QueryModifyVerify;
import com.dangdang.verifier.scenario.ScenarioSearchVerifier;
import com.dangdang.verifier.search_ranking.MobileExclusiveAddWeightVerifier;
import com.dangdang.verifier.search_ranking.NoStockDropWeightVerifier;
import com.dangdang.verifier.search_ranking.TimelessAddWeightVerifier;
import com.dangdang.verifier.sort.CommentDescVerifier;
import com.dangdang.verifier.sort.LastInputVerifier;
import com.dangdang.verifier.sort.PageTurnVerifier;
import com.dangdang.verifier.sort.PriceAscVerifier;
import com.dangdang.verifier.sort.PriceDescVerifier;
import com.dangdang.verifier.sort.SaleWeekVerifier;
import com.dangdang.verifier.sort.newprod.NewProdVerifier;
import com.dangdang.verifier.sort.specifiedpool.SpecifiedPoolAddWeightVerifier;

/*
 * URL拼接、封装
 */
public class URLBuilder {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(URLBuilder.class);
	
	private static String baseURL = new Config().get_baseURL();
	private static String SPLIT_QUES = "?";
	private static String SPLIT_AND = "&";
	private static String SPLIT_EQUAL = "=";

	/*
	 * Map转化成url q不转GBK码
	 */
	public static String buildURL(Map<String, String> map) {
		// parametersMap = map;
		Set<String> keySet = map.keySet();
		StringBuffer url_part = new StringBuffer();
		for (String keys : keySet) {
			url_part.append(keys + SPLIT_EQUAL + map.get(keys) + SPLIT_AND);
		}
		String url = baseURL + SPLIT_QUES + url_part.toString();
		logger.info(" - [URLBuilder] - URL: " + url);
		return url;
	}
	
	/*
	 * Map转化成url q不转GBK码
	 */
	public static String buildURL(Map<String, String> map, String baseurl) {
		// parametersMap = map;
		Set<String> keySet = map.keySet();
		StringBuffer url_part = new StringBuffer();
		for (String keys : keySet) {
			url_part.append(keys + SPLIT_EQUAL + map.get(keys) + SPLIT_AND);
		}
		String url = baseurl + SPLIT_QUES + url_part.toString();		
		logger.info(" - [URLBuilder] - URL: " + url);
		return url;
	}

	/*
	 * Map转化成url 小搜索baseURL=http://10.255.254.74:8390/
	 */
	public static String buildURL_mini(Map<String, String> map) {
		// parametersMap = map;
		String baseURL_mini = "http://10.255.254.74:9615/";
		Set<String> keySet = map.keySet();
		StringBuffer url_part = new StringBuffer();
		for (String keys : keySet) {
			url_part.append(keys + SPLIT_EQUAL + map.get(keys) + SPLIT_AND);
		}
		String url = baseURL_mini + SPLIT_QUES + url_part.toString();
		logger.info(" - [URLBuilder] - URL: " + url);
		return url;
	}
	/*
	 * Map转化成url q转GBK码
	 */
	public static String buildUrl(Map<String, String> map) {
		// parametersMap = map;
		Set<String> keySet = map.keySet();
		StringBuffer url_part = new StringBuffer();
		for (String keys : keySet) {
			if (keys.equals("q")) {
				try {
					url_part.append(keys + SPLIT_EQUAL + URLEncoder.encode(map.get(keys), "GBK") + SPLIT_AND);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				url_part.append(keys + SPLIT_EQUAL + map.get(keys) + SPLIT_AND);
			}
		}
		String url = (baseURL + SPLIT_QUES + url_part.toString()).replaceAll("\\+", "%20");

		logger.info(" - [URLBuilder] - URL: " + url);
		return url;

	}
	
	/*
	 * Map转化成url q转GBK码 带服务器IP参数
	 */
	public static String buildUrl(Map<String, String> map, String baseURL) {
		// parametersMap = map;
		Set<String> keySet = map.keySet();
		StringBuffer url_part = new StringBuffer();
		for (String keys : keySet) {
			if (keys.equals("q")) {
				try {
					url_part.append(keys + SPLIT_EQUAL + URLEncoder.encode(map.get(keys), "GBK") + SPLIT_AND);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				url_part.append(keys + SPLIT_EQUAL + map.get(keys) + SPLIT_AND);
			}
		}
		String url = (baseURL + SPLIT_QUES + url_part.toString()).replaceAll("\\+", "%20");

		logger.info(" - [URLBuilder] - URL: " + url);
		return url;

	}

	/**
	 * baseurl是指定形式的url，需要在该url后边拼接类似pg=1&ps=60的参数，与之前buildURL不同的是，以SPLIT_AND连接baseurl和参数
	 * e.g. 
	 * baseurl = http://10.3.255.227:8390/?shop_id=13207&_url_token=26&st=mall
	 * 分别取不同页数的结果时，需要跟上 &pg=60&ps=1 的参数
	 * @param map
	 * @param baseurl
	 * @param contactwithand
	 * @return
	 */
	public static String buildURL(Map<String, String> map, String baseurl, boolean contactwithand) {
		// parametersMap = map;
		Set<String> keySet = map.keySet();
		StringBuffer url_part = new StringBuffer();
		for (String keys : keySet) {
			url_part.append(keys + SPLIT_EQUAL + map.get(keys) + SPLIT_AND);
		}
		String url =  baseurl + SPLIT_QUES + url_part.toString();	
		// baseurl 和 map参数之间用 “&” 连接
		if(contactwithand){
			url = baseurl + SPLIT_AND + url_part.toString();	
		}	
		logger.info(" - [URLBuilder] - URL: " + url);
		return url;
	}
	
	/*
	 * 转换参数为Map并加上默认常量st=full：搜索全站，um=search_ranking：使用搜索模块
	 */
	public static Map<String, String> converURLPars(String verifyPoint, String keyWord, Map<String, String> tm) {
		String[] vps = verifyPoint.split(",");
		Map<String, String> map = new HashMap<String, String>();
		map.put("st", "full");
		map.put("ps", "60");
		map.put("um", "search_ranking");
		map.put("_new_tpl", "1");//search_ranking必加		
		
		try {
			if(tm != null && tm.get("platform")!=null){
				map.put("platform", tm.get("platform"));
			}
			if(!keyWord.isEmpty() && keyWord != ""){
				map.put("q", URLEncoder.encode(keyWord, "GBK"));
			}
		} catch (UnsupportedEncodingException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.info(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		}
		
		for (String vp : vps) {
			switch (vp) {		
			case "mbexclusive_addweight":	//无线手机专享价加权
				map.put("-mobile_exclusive", "1");
				if(tm != null & tm.get("platform") !=null ){
					if (!tm.get("platform").isEmpty()){
						map.put("platform", tm.get("platform"));
					}
				}
				break;
			case "timeless_addweight":		//时效性加权,上架时间在30天以内的品，进行加权	
				final Date finalNow = new Date();
				Date temp_date = DateTimeHandler.AddorSubtractDays(finalNow, -30);					
				Integer ts = DateTimeHandler.DateToTimestamp(temp_date);				
				map.put("-first_input_date", ts.toString()+"~");
				if(tm != null & tm.get("timelessSwitch") != null ){
					if(!tm.get("timelessSwitch").isEmpty()){
						map.put("timelessSwitch", tm.get("timelessSwitch"));
					}
				}
				break;
			case "specified_pool": 			// 指定商品池加权
				if(tm != null & tm.get("add_weight_list_switch") != null ){
					if(!tm.get("add_weight_list_switch").isEmpty()){
						map.put("add_weight_list_switch", tm.get("add_weight_list_switch"));
					}
				}
				break;
			case "first_input_date": 	//上架时间
				map.put("-first_input_date", "1262275200~1483200000"); // 2000/01/01 00:00:00 ~ 2017/01/01 00:00:00
				break;
			case "total_review_count":	//评论数
				map.put("-total_review_count", "1~");
				break;
			case "sale_month":			//商品近30天销量
				map.put("-sale_month", "1~");
				break;				
			case "new_prod": // 教育新品加权
				map.put("ip", "111.207.228.104");
				break;
			case "lin_shi": // 统计品牌直达
				map.put("_mod_ver", "S6");
				map.put("direct_brand", "1");
				break;
			case "ebook": // 电子书推广
				map.put("is_e_default", "1");
				map.put("platform", "4");
				break;
			case "search_custom": // 定制化搜索，变换map测试~~~~~~~~~~~~~~~~~
//				if (tm.get("OldQuery").contains("自营")) {
//					map.put("-is_dd_sell", "1");
//				}
				// map.put("-is_dd_sell", "1");
				//
				// map.put("outlets", "1");
				//
				// map.put("-stock_status", "1~");
				//
				// map.put("-promotion_type", "1~");
				//
				// map.put("-mobile_exclusive", "1");

				// map.put("-product_type", "60");
				// map.put("vart", "1");
//				map.put("_mod_ver", "S7");
				break;
			case "search_custom_z": // 定制化搜索，需要增加自营参数的测试词
				   map.put("-is_dd_sell", "1");
//				   map.put("_mod_ver", "S7");
//				   map.put("-is_dd_sell", "1");
				//
				// map.put("outlets", "1");
				//
				// map.put("-stock_status", "1~");
				//
				// map.put("-promotion_type", "1~");
				//
				// map.put("-mobile_exclusive", "1");

				// map.put("-product_type", "60");
				// map.put("vart", "1");
				break;
			case "search_custom_o": // 定制化搜索，需要增加海外购参数的测试词
				   //map.put("-is_overseas", "1");
				   map.put("is_overseas", "1");
				break;
			case "cate_and_query": // 分类名搜索词，变换map测试~~~~~~~~~~~~~~~~~
				// map.put("-is_dd_sell", "1");
				//
				// map.put("outlets", "1");
				//
				// map.put("-stock_status", "1~");
				//
				// map.put("-promotion_type", "1~");
				//
				// map.put("-mobile_exclusive", "1");

				// map.put("-product_type", "60");
				// map.put("vart", "1");
				break;
			case "price_asc": // 价格升序
				map.put("us", "xprice_asc");
				break;
			case "price_desc": // 价格降序
				map.put("us", "xprice_desc");
				break;
			case "sale_week_amt": // 销量排序
				map.put("fs", "sale_week");
				map.put("fa", "0");
				break;
			case "last_date": // 最新排序
				if (tm.get("medium").equals("12")) {
					map.put("fs", "first_input_date");
					map.put("fa", "0");
				} else {
					map.put("fs", "publish_date");
					map.put("fa", "0");
				}
				break;
			case "outlets": // 尾品会过滤
				map.put("outlets", "1");
				break;
			case "new": // 新品汇过滤
				break;
			case "stock": // 有库存过滤
				map.put("-stock_status", "1~");
				break;
			case "promotion": // 有促销过滤
				map.put("-promotion_type", "1~");
				break;
			case "presale": // 预售过滤
				map.put("-product_type", "60");
				map.put("vart", "1");
				break;
			case "dd_sell": // 当当自营过滤
				map.put("-is_dd_sell", "1");
				break;
			case "mobile_exclusive": // 手机专享价过滤
				map.put("-mobile_exclusive", "1");
				break;
			case "m_mobile_exclusive": // 无线端手机专享价过滤
				map.put("-mobile_exclusive", "1");
				map.put("is_mphone", "1");
				break;
			case "feedback": // 用户评价过滤
				map.put("-label_id", FilterMap.getFilterMap().get(vp));
				break;
			case "priceInterval": // 价格区间过滤
				map.put("-dd_sale_price", tm.get(vp));
				break;
			case "brand": // 品牌过滤
				map.put("-brand_id", tm.get(vp));
				break;
			case "category": // 分类过滤
				map.put("-cat_paths", tm.get(vp));
				break;
			case "comment":
				map.put("fs", "score");//好评排序
				map.put("fa", "0");
				break;
			case "vertical_search":
				map.put("vert", "1");
				if (tm != null) {
					map.put("pid", tm.get(vp));
				}
			case "season_timeness":
				map.put("ps", "60");
				break;
			default:
				if (vp.startsWith("attrib")) { // 属性过滤
					String attr = vp.split("_")[1];
					map.put("-attrib", AttribMap.getAttribMap().get(attr));
				} else if (vp.startsWith("gSearch_ebook")) {
					map.put("-product_medium", "22");
					map.put("-cat_paths", "98.00.00.00.00.00");
				} else if (vp.startsWith("gSearch")) {
					String str = vp.split("_")[1];
					if (str.equals("digital")) {
						map.put("-cat_paths", "98.00.00.00.00.00");
					}
					if (str.equals("music")) {
						map.put("-cat_paths", "03.00.00.00.00.00");
					}
					if (str.equals("vedio")) {
						map.put("-cat_paths", "05.00.00.00.00.00");
					}
				}
				break;
			}
		}
		return map;
	}

	/*
	 * 无线增加is_mphone参数 转换参数为Map并加上默认常量st=full：搜索全站，um=search_ranking：使用搜索模块
	 */
	public static Map<String, String> m_converURLPars(String verifyPoint, String keyWord, Map<String, String> tm) {
		String[] vps = verifyPoint.split(",");
		Map<String, String> map = new HashMap<String, String>();
		map.put("st", "full");
		map.put("ps", "60");
		map.put("um", "search_ranking");
		map.put("is_mphone", "1");
		map.put("_new_tpl", "1");//search_ranking必加
		try {
			map.put("q", URLEncoder.encode(keyWord, "GBK"));
		} catch (UnsupportedEncodingException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		}
		for (String vp : vps) {
			switch (vp) {
			case "price_asc": // 价格升序
				map.put("us", "xprice_asc");
				break;
			case "price_desc": // 价格降序
				map.put("us", "xprice_desc");
				break;
			case "sale_week_amt": // 销量排序
				map.put("fs", "sale_week");
				map.put("fa", "0");
				break;
			case "last_date": // 最新排序
				if (tm.get("medium").equals("12")) {
					map.put("fs", "first_input_date");
					map.put("fa", "0");
				} else {
					map.put("fs", "publish_date");
					map.put("fa", "0");
				}
				break;
			case "outlets": // 尾品会过滤
				map.put("outlets", "1");
				break;
			case "new": // 新品汇过滤
				break;
			case "stock": // 有库存过滤
				map.put("-stock_status", "1~");
				break;
			case "promotion": // 有促销过滤
				map.put("-promotion_type", "1~");
				break;
			case "presale": // 预售过滤
				map.put("-product_type", "60");
				map.put("vart", "1");
				break;
			case "dd_sell": // 当当自营过滤
				map.put("-is_dd_sell", "1");
				break;
			case "mobile_exclusive": // 手机专享价过滤
				map.put("-mobile_exclusive", "1");
				break;
			case "m_mobile_exclusive": // 无线端手机专享价过滤
				map.put("-mobile_exclusive", "1");
				map.put("is_mphone", "1");
				break;
			case "feedback": // 用户评价过滤
				map.put("-label_id", FilterMap.getFilterMap().get(vp));
				break;
			case "priceInterval": // 价格区间过滤
				map.put("-dd_sale_price", tm.get(vp));
				break;
			case "brand": // 品牌过滤
				map.put("-brand_id", tm.get(vp));
				break;
			case "category": // 分类过滤
				map.put("-cat_paths", tm.get(vp));
				break;
			case "comment":
				map.put("fs", "score");
				map.put("fa", "0");
				break;
			case "vertical_search":
				map.put("vert", "1");
				if (tm != null) {
					map.put("pid", tm.get(vp));
				}
			case "season_timeness":
				map.put("ps", "60");
				break;
			default:
				if (vp.startsWith("attrib")) { // 属性过滤
					String attr = vp.split("_")[1];
					map.put("-attrib", AttribMap.getAttribMap().get(attr));
				} else if (vp.startsWith("gSearch_ebook")) {
					map.put("-product_medium", "22");
					map.put("-cat_paths", "98.00.00.00.00.00");
				} else if (vp.startsWith("gSearch")) {
					String str = vp.split("_")[1];
					if (str.equals("digital")) {
						map.put("-cat_paths", "98.00.00.00.00.00");
					}
					if (str.equals("music")) {
						map.put("-cat_paths", "03.00.00.00.00.00");
					}
					if (str.equals("vedio")) {
						map.put("-cat_paths", "05.00.00.00.00.00");
					}
				}
				break;
			}
		}
		return map;
	}

	/**
	 * 返回只包含最基本参数的map，um和st以及类似cat_paths等其他参数可以通过tm传递
	 * @param verifyPoint 	验证点
	 * @param keyword		关键词
	 * @param tm			辅助map
	 * @return
	 */	
	public static Map<String, String> convertURLParsTotalBasic(String verifyPoint, String keyword, Map<String, String> tm){
		String[] vps = verifyPoint.split(",");
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("ps", "60");
		map.put("_new_tpl", "1");
		
		if (!keyword.isEmpty())
			try {
				map.put("q", URLEncoder.encode(keyword, "GBK"));
			} catch (UnsupportedEncodingException e) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.info(" - [LOG_EXCEPTION] - " + exception);
				e.printStackTrace();
			}
		
		// tm中的所有参数对，都增加到map中，如st=full、um=search_ranking等
		if(tm!=null && !tm.isEmpty()){
			if(!tm.get("um").isEmpty())
				map.put("um", tm.get("um"));
			if(!tm.get("st").isEmpty())
				map.put("st", tm.get("st"));
		}
		
		for(String vp : vps){
			switch(vp){
				case "query_identify_category":
					if(tm.get("-cat_paths")!=null)
						map.put("-cat_paths",tm.get("-cat_paths"));
					break;
//				case "query_identify_shop":
//					if(tm.get("-shop_id")!=null)
//						map.put("-shop_id",tm.get("-shop_id"));
//					break;
				default:
					break;
			}
		}
		return map;
	}
	/*
	 * 
	 * 转换参数为Map并加上默认常量st=full：搜索全站，um=list_ranking：使用list模块
	 */
	public static Map<String, String> l_converURLPars(String verifyPoint, String catpaths, Map<String, String> tm) {
		String[] vps = verifyPoint.split(",");
		Map<String, String> map = new HashMap<String, String>();
		
		if(verifyPoint.equals("first_input_date") 
				|| verifyPoint.equals("total_review_count") 
				|| verifyPoint.equals("sale_month") ) 
			map.put("st", "mall");
		else
			map.put("st", "full");		
		
		map.put("ps", "60");
		map.put("um", "list_ranking");
		map.put("cat_paths", catpaths);

		for (String vp : vps) {
			switch (vp) {
			case "first_input_date": 	//上架时间
				map.put("-first_input_date", "1262275200~1483200000"); // 2010/01/01 00:00:00 ~ 2017/01/01 00:00:00
				break;
			case "total_review_count":	//评论数
				map.put("-total_review_count", "1~");
				break;
			case "sale_month":			//商品近30天销量
				map.put("-sale_month", "1~");
				break;	
				
			case "cate_and_query": // 分类名搜索词，变换map测试~~~~~~~~~~~~~~~~~~~~~~~~~~

				// map.put("-is_dd_sell", "1");
				//
				// map.put("outlets", "1");
				//
				// map.put("-stock_status", "1~");
				//
				// map.put("-promotion_type", "1~");
				//
				// map.put("-mobile_exclusive", "1");

				// map.put("-product_type", "60");
				// map.put("vart", "1");

				break;
			case "price_asc": // 价格升序
				map.put("us", "xprice_asc");
				break;
			case "price_desc": // 价格降序
				map.put("us", "xprice_desc");
				break;
			case "sale_week_amt": // 销量排序
				map.put("fs", "sale_week");
				map.put("fa", "0");
				break;
			case "last_date": // 最新排序
				if (tm.get("medium").equals("12")) {
					map.put("fs", "first_input_date");
					map.put("fa", "0");
				} else {
					map.put("fs", "publish_date");
					map.put("fa", "0");
				}
				break;
			case "outlets": // 尾品会过滤
				map.put("outlets", "1");
				break;
			case "new": // 新品汇过滤
				break;
			case "stock": // 有库存过滤
				map.put("-stock_status", "1~");
				break;
			case "promotion": // 有促销过滤
				map.put("-promotion_type", "1~");
				break;
			case "presale": // 预售过滤
				map.put("-product_type", "60");
				map.put("vart", "1");
				break;
			case "dd_sell": // 当当自营过滤
				map.put("-is_dd_sell", "1");
				break;
			case "mobile_exclusive": // 手机专享价过滤
				map.put("-mobile_exclusive", "1");
				break;
			case "m_mobile_exclusive": // 无线端手机专享价过滤
				map.put("-mobile_exclusive", "1");
				map.put("is_mphone", "1");
				break;
			case "feedback": // 用户评价过滤
				map.put("-label_id", FilterMap.getFilterMap().get(vp));
				break;
			case "priceInterval": // 价格区间过滤
				map.put("-dd_sale_price", tm.get(vp));
				break;
			case "brand": // 品牌过滤
				map.put("-brand_id", tm.get(vp));
				break;
			case "category": // 分类过滤
				map.put("-cat_paths", tm.get(vp));
				break;
			case "comment":
				map.put("fs", "score");
				map.put("fa", "0");
				break;
			case "vertical_search":
				map.put("vert", "1");
				if (tm != null) {
					map.put("pid", tm.get(vp));
				}
			case "season_timeness":
				map.put("ps", "60");
				break;
			default:
				if (vp.startsWith("attrib")) { // 属性过滤
					String attr = vp.split("_")[1];
					map.put("-attrib", AttribMap.getAttribMap().get(attr));
				} else if (vp.startsWith("gSearch_ebook")) {
					map.put("-product_medium", "22");
					map.put("-cat_paths", "98.00.00.00.00.00");
				} else if (vp.startsWith("gSearch")) {
					String str = vp.split("_")[1];
					if (str.equals("digital")) {
						map.put("-cat_paths", "98.00.00.00.00.00");
					}
					if (str.equals("music")) {
						map.put("-cat_paths", "03.00.00.00.00.00");
					}
					if (str.equals("vedio")) {
						map.put("-cat_paths", "05.00.00.00.00.00");
					}
				}
				break;
			}
		}
		return map;
	}

	public static boolean getIteratorWithoutFilter(Map<String, String> map, String vps) {
		try {
			String[] vp_s = vps.split(",");

			for (String vp : vp_s) {
				switch (vp) {
				case "outlets": // 尾品会过滤
					map.remove("outlets");
					break;
				case "new": // 新品汇过滤
					break;
				case "stock": // 有库存过滤
					map.remove("-stock_status");
					break;
				case "promotion": // 有促销过滤
					map.remove("-promotion_type");
					break;
				case "presale": // 预售过滤
					map.remove("-product_type");
					map.remove("vart");
					break;
				case "dd_sell": // 当当自营过滤
					map.remove("-is_dd_sell");
					break;
				case "feedback": // 用户评价过滤
					map.remove("-label_id");
					break;
				case "priceInterval": // 价格区间过滤
					map.remove("-dd_sale_price");
					break;
				case "brand": // 品牌过滤
					map.remove("-brand_id");
					break;
				case "category": // 分类过滤
					map.remove("-cat_paths");
					break;
				default:
					if (vp.startsWith("attrib_")) { // 材质过滤
					// String attr = vp.split("_")[1];
						map.remove("attrib");

					} else if (vp.startsWith("gSearch_")) { // 高级搜索

					}

					break;
				}
			}
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 功能验证方法
	 * 
	 * @param vp
	 *            本次要验证的功能
	 * @param productList
	 *            //商品列表
	 * @return
	 */
	public static boolean doMFVerify(String vps, ProdIterator iterator, Map<String, String> map) {
		try {
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			while (iterator.hasNext()) {
				Node node = iterator.next();
				boolean result = false;
				for (String vp : vps.split(",")) {
					switch (vp) {
					case "outlets": // 尾品会过滤
						OutletsVerifier outlets = new OutletsVerifier();
						result = result || outlets.idOutlets(node, false);
						break;
					case "stock": // 有库存过滤
						StockVerifier stock = new StockVerifier();
						result = result || stock.canStock(node, false);
						break;
					case "promotion": // 有促销过滤
						PromoVerifier promo = new PromoVerifier();
						result = result || promo.isPromo(node, false, format, now);
						break;
					case "mobile_exclusive": // 手机专享价过滤
						IFilterVerifier mprice = new ExclusivePrice();
						result = result || mprice.Verifier(iterator, map);
						break;
					case "presale": // 预售过滤
						PreSaleVerifier presale = new PreSaleVerifier();
						result = result || presale.isPreSale(node, false);
						break;
					case "dd_sell": // 当当自营过滤
						DDSellVerifier ddsale = new DDSellVerifier();
						result = result || ddsale.isDDSell(node, false);
						break;
					case "priceInterval": // 价格区间过滤
						PriceIntervalVerifier pricescope = new PriceIntervalVerifier();
						result = result || pricescope.isInterval(node, false, map);
						break;
					case "brand": // 品牌过滤
						BrandVerifier brand = new BrandVerifier();
						result = result || brand.isBrand(node, false, map, map.get("brand"));
						break;
					case "category": // 分类过滤
						CategoryVerifier catepath = new CategoryVerifier();
						result = result || catepath.isCategory(node, false, map, map.get("category"));
						break;
					case "feedback": // 用户反馈过滤
						FeedbackVerifier feed = new FeedbackVerifier();
						result = result || feed.isFeedback(node, false, FilterMap.getFilterMap().get("feedback"));
						break;
					default:
						if (vp.startsWith("attrib_")) { // 材质过滤
							String attr = vp.split("_")[1];
							map.put("attrib", AttribMap.getAttribMap().get(attr));
							AttribVerifier attrib = new AttribVerifier();
							result = result || attrib.isAttrib(node, map, false, map.get("attrib"));
						} else if (vp.startsWith("gSearch_")) { // 高级搜索
							gSearchVerify(iterator, vp, map);
						}
						break;
					// case "":
					}
				}
				if (result == false) {
					return false;
				}
			}
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			return false;
		}
		return true;
	}

	/**
	 * 功能验证方法
	 * 
	 * @param vp
	 *            本次要验证的功能
	 * @param productList
	 *            //商品列表
	 * @return
	 */
	public static boolean doVerify(String vps, ProdIterator iterator, Map<String, String> map) {
		boolean result = true;
		try {
			for (String vp : vps.split(",")) {
				switch (vp) {	
				case "nostock_dropweight"://无库存降权
					NoStockDropWeightVerifier NoStockDropWeight = new NoStockDropWeightVerifier();
					result = result && NoStockDropWeight.Verifier(iterator,map);
					break;
				case "first_input_date":  //上架时间
					IFilterVerifier firstInput = new FirstInputDateVerifier();				
					firstInput.Verifier(iterator, map);
					break;
				case "total_review_count": //评论数
					IFilterVerifier totalCount = new TotalReviewCountVerifier();
					totalCount.Verifier(iterator, map);
					break;
				case "sale_month": 	//近30天销量
					IFilterVerifier saleMonth = new SaleMonthVerifier();
					saleMonth.Verifier(iterator, map);
					break;
				case "new_prod": //教育新品加权
					NewProdVerifier newProd = new NewProdVerifier();
//					NewProdCount newProd = new NewProdCount();//统计使用
					result = result && newProd.Verifier(iterator, map);
					break;
				case "lin_shi": //临时统计数据
					BrandThroughVerifier brandcount = new BrandThroughVerifier();
					result = result && brandcount.statistics(iterator, map);
					break;
				case "ebook": //电子书推广
					EBookVerifier ebook = new EBookVerifier();
					result = result && ebook.Verifier(iterator, map);
					break;
				case "book_strategy": // 临时用一个多月，图书分类
					Book_strategy book_strategy = new Book_strategy();
					result = result && book_strategy.Verifier(iterator, map);
					break;
				case "children_strategy": // 临时用一个多月，童书分类
					Children_strategy children_strategy = new Children_strategy();
					result = result && children_strategy.Verifier(iterator, map);
					break;
				case "Score": // 其他分类默认按照Score排序
					Score other_score = new Score();
					result = result && other_score.Verifier(iterator, map);
					break;
				case "l_duplicate": // 列表页查重
					List_DuplicateVerifier list_duplicateVerifier = new List_DuplicateVerifier();
					result = result && list_duplicateVerifier.Verifier(iterator, map);
					break;
				case "price_asc": // 价格升序
					ISortVerifier priceASC = new PriceAscVerifier();
					result = result && priceASC.Verifier(iterator, map);
					break;
				case "price_desc": // 价格降序
					ISortVerifier priceDESC = new PriceDescVerifier();
					result = result && priceDESC.Verifier(iterator, map);
					break;
				case "sale_week_amt": // 销量排序
					ISortVerifier saleweek = new SaleWeekVerifier();
					result = result && saleweek.Verifier(iterator, map);
					break;
				case "last_date":
					if (map.get("medium").equals("12")) {
						ISortVerifier lastinput = new LastInputVerifier();
						result = result && lastinput.Verifier(iterator, map);
					} else { // 默认是图书
						// TODO:
					}
					break;
				case "outlets": // 尾品会过滤
					IFilterVerifier outlets = new OutletsVerifier();
					result = result && outlets.Verifier(iterator, map);
					break;
				case "stock": // 有库存过滤
					IFilterVerifier stock = new StockVerifier();
					result = result && stock.Verifier(iterator, map);
					break;
				case "promotion": // 有促销过滤
					IFilterVerifier promo = new PromoVerifier();
					result = result && promo.Verifier(iterator, map);
					break;
				case "mobile_exclusive": // 手机专享价过滤
					IFilterVerifier mprice = new ExclusivePrice();
					result = result && mprice.Verifier(iterator, map);
					break;
				case "m_mobile_exclusive": // 无线端手机专享价过滤
					M_IFilterVerifier m_mprice = new M_ExclusivePrice();
					result = result && m_mprice.Verifier(iterator, map);
					break;
				case "presale": // 预售过滤
					IFilterVerifier presale = new PreSaleVerifier();
					result = result && presale.Verifier(iterator, map);
					break;
				case "dd_sell": // 当当自营过滤
					IFilterVerifier ddsale = new DDSellVerifier();
					result = result && ddsale.Verifier(iterator, map);
					break;
				case "priceInterval": // 价格区间过滤
					IFilterVerifier pricescope = new PriceIntervalVerifier();
					result = result && pricescope.Verifier(iterator, map);
					break;
				case "brand": // 品牌过滤
					IFilterVerifier brand = new BrandVerifier();
					result = result && brand.Verifier(iterator, map);
					break;
				case "category": // 分类过滤
					IFilterVerifier catepath = new CategoryVerifier();
					result = result && catepath.Verifier(iterator, map);
					break;
				case "feedback": // 用户反馈过滤
					IFilterVerifier feed = new FeedbackVerifier();
					result = result && feed.Verifier(iterator, map);
					break;
				case "comment":
					ISortVerifier comment = new CommentDescVerifier();
					result = result && comment.Verifier(iterator, map);
					break;
				case "page_turn": // 翻页功能
					PageTurnVerifier pageturn = new PageTurnVerifier();
					result = result && pageturn.Verifier(iterator, map);
					break;
				case "cbook_pi":
					// ChildBookPriceIntervalVerifier cbookpi = new
					// ChildBookPriceIntervalVerifier();
					// result = result && cbookpi.Verifier(iterator, map);
					// break;
				case "scenario_search": //场景化搜索
					ScenarioSearchVerifier scenario = new ScenarioSearchVerifier();
					result = result & scenario.Verifier(map);
					break;
				case "category_feedback": //分类反馈
					CategoryFeedbackVerify cf = new CategoryFeedbackVerify();
					boolean flag=cf.verify(map);
					System.out.println("验证结果"+flag);
					result = result & flag ;
					break;
				case "product_top": //单品置顶
					ProductTopVerify pt = new ProductTopVerify();
					boolean flag2=pt.verify(map);
					System.out.println("验证结果"+flag2);
					result = result & flag2 ;
					break;
				case "query_modify": //query改写
					QueryModifyVerify qm = new QueryModifyVerify();
					boolean flag3=qm.verify(map);
					System.out.println("验证结果"+flag3);
					result = result & flag3 ;
					break;
				default:
					if (vp.startsWith("attrib_")) { // 材质过滤
						String attr = vp.split("_")[1];
						map.put("attrib", AttribMap.getAttribMap().get(attr));
						IFilterVerifier attrib = new AttribVerifier();
						result = result && attrib.Verifier(iterator, map);
					} else if (vp.startsWith("gSearch_")) { // 高级搜索
						result = result && gSearchVerify(iterator, vp, map);
					}
					break;
				}
			}
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			return false;
		}
		return result;
	}
	
	/**
	 * 用于验证两个iterator之间的差异
	 * @param vps			验证点
	 * @param query			query词
	 * @param old_iterator	没加参数前的结果集
	 * @param new_iterator	加了参数后的结果集
	 * @return				验证结果
	 */
	public static boolean doVerifyWith2Iterator(String vps, String query, ProdIterator old_iterator,ProdIterator new_iterator) {
		boolean result = true;
		try {
			for (String vp : vps.split(",")) {
				switch (vp) {					
				case "timeless_addweight":  //时效性加权
					TimelessAddWeightVerifier Timeless = new TimelessAddWeightVerifier();
					result = result && Timeless.Verifier(old_iterator, new_iterator, query, vps);
					break;
				case "mbexclusive_addweight": //无线端手机专享价加权
					MobileExclusiveAddWeightVerifier MobileExclusive = new MobileExclusiveAddWeightVerifier();
					result = result && MobileExclusive.Verifier(old_iterator, new_iterator, query, vps);
					break;	
				case "specified_pool":
					SpecifiedPoolAddWeightVerifier specifiedVerifier = new SpecifiedPoolAddWeightVerifier();
					result = specifiedVerifier.Verifier(old_iterator,new_iterator, query, vps);
					break;
				default:				
					break;
				}
			}
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			return false;
		}
		return result;
	}

	
	/**
	 * 验证逻辑：
	 * 按query搜索，得到第一个单品，以该单品的product_name、cat_paths、publisher等作为高级搜索的条件
	 * 例如，百货，以第一次得到的product_name为搜索条件时，只需要验证高级搜索结果的单品名是否为product_name的一部分即可
	 */
	public static boolean gSearchVerify(ProdIterator iterator, String vp, Map<String, String> map) {
		String[] ta = vp.split("_");
		switch (ta[1]) {
		case "book":
			BookSearchVerifier bsv = new BookSearchVerifier();
			return bsv.Verifier(iterator, map, ta);
		case "ebook":
			EBookSearchVerifier ebsv = new EBookSearchVerifier();
			return ebsv.Verifier(iterator, map, ta);
		case "digital":
			DigitalProductSearchVerifier dpsv = new DigitalProductSearchVerifier();
			return dpsv.Verifier(iterator, map, ta);
		case "music":
			MusicSearchVerifier msv = new MusicSearchVerifier();
			return msv.Verifier(iterator, map, ta);
		case "vedio":
			VedioSearchVerifier vsv = new VedioSearchVerifier();
			return vsv.Verifier(iterator, map, ta);
		case "b2c":
			B2cSearchVerifier b2csv = new B2cSearchVerifier();
			return b2csv.Verifier(iterator, map, ta);
		}
		return false;
	}

	/**
	 * 根据关键词获取XML结果，并且转换成Map
	 */
	public static Map<String, String> getPreSearchInfo(String keyWord) {
		String xml = getXML("", keyWord, null);
		return getPreSearchInfoWithXml(xml);
	}
	
	/**
	 * 根据关键词和其他不固定的参数，如平台等，获取XML结果，并且转换成Map
	 */
	public static Map<String, String> getPreSearchInfo(String keyWord, Map<String, String> addParams) {
		String xml = getXML("", keyWord, addParams);
		return getPreSearchInfoWithXml(xml);
	}

	/**
	 * list_根据关键词获取XML结果，并且转换成Map
	 */
	public static Map<String, String> l_getPreSearchInfo(String catepath) {
		String xml = l_getXML("", catepath, null);
		return getPreSearchInfoWithXml(xml);
	}

	public static Map<String, String> getPreSearchInfo(Map<String, String> map) {
		String url = buildURL(map);
		return getPreSearchInfoWithXml(SearchRequester.get(url));
	}

	/**
	 * 从XML中获取一些基本信息节点，totalCount、pageCount、priceInterval、brand、category、template
	 * 、term、resultCountEbook等
	 */
	public static Map<String, String> getPreSearchInfoWithXml(String xml) {
		Map<String, String> tm = new HashMap<String, String>();
		try {
			Document doc = XMLParser.read(xml);
			tm.put("totalCount", XMLParser.totalCount(doc));
			tm.put("pageCount", XMLParser.PageCount(doc));
			tm.put("priceInterval", XMLParser.getPriceInterval(doc));
			tm.put("brand", XMLParser.getBrand(doc));
			tm.put("category", XMLParser.getCategory(doc));
			tm.put("template", XMLParser.getWebTemplete(doc));
			tm.put("term", XMLParser.term(doc));
			tm.put("webTemplete", XMLParser.getWebTemplete(doc));
			tm.put("resultCountEbook", XMLParser.resultCountEbook(doc));
			tm.put("productCount", ((Integer)XMLParser.getProductNodes(doc).size()).toString());
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		}
		return tm;
	}

	/*
	 * 通过 verifyPoint、关键词和Map获取String格式的XML结果
	 */
	public static String getXML(String verifyPoint, String keyWord, Map<String, String> map) {
		String url = buildURL(converURLPars(verifyPoint, keyWord, map));
		return SearchRequester.get(url);
	}

	/*
	 * list_通过 verifyPoint、关键词和Map获取String格式的XML结果
	 */
	public static String l_getXML(String verifyPoint, String catapath, Map<String, String> map) {
		String url = buildURL(l_converURLPars(verifyPoint, catapath, map));
		return SearchRequester.get(url);
	}

	// PC
	public static ProdIterator getDefaultIterator(String keyWord) {
		Map<String, String> map = URLBuilder.converURLPars("", keyWord, null);
		return new ProdIterator(map);
	}

	// PC list
	public static ProdIterator l_getDefaultIterator(String catepath) {
		Map<String, String> map = URLBuilder.l_converURLPars("", catepath, null);
		return new ProdIterator(map);
	}

	// 无线增加is_mphone参数
	public static ProdIterator getMDefaultIterator(String keyWord) {
		Map<String, String> map = URLBuilder.m_converURLPars("", keyWord, null);
		return new ProdIterator(map);
	}

	@SuppressWarnings("unchecked")
	public static List<Node> porductSearch(String product_ids, boolean withUM) {

		Map<String, String> urlmap = new HashMap<String, String>();
		if (withUM) {
			urlmap.put("st", "full");
			urlmap.put("um", "search_ranking");
		}

		urlmap.put("product_id", product_ids);

		String url = buildURL(urlmap);

		String xml = SearchRequester.get(url);

		Document doc;
		try {
			if(!xml.isEmpty()){
				doc = XMLParser.read(xml);
				return XMLParser.getProductNodes(doc);
			}else
				return new ArrayList<Node>();
		} catch (MalformedURLException | DocumentException e) {
			// TODO Auto-generated catch block
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			e.printStackTrace(new PrintWriter(buf, true));
			String expMessage = buf.toString();
			logger.error(" - [LOG_EXCEPTION] - " + expMessage);
			return new ArrayList<Node>();
		}

	}

	// 小搜索查询商品数据
	public static List<Node> porductSearch_mini(String product_ids, boolean withUM) {

		Map<String, String> urlmap = new HashMap<String, String>();
		if (withUM) {
			urlmap.put("st", "full");
			urlmap.put("um", "search_ranking");
		}

		urlmap.put("product_id", product_ids);

		String url = buildURL_mini(urlmap);

		String xml = SearchRequester.get(url);

		Document doc;
		try {
			doc = XMLParser.read(xml);
			return XMLParser.getProductNodes(doc);
		} catch (MalformedURLException | DocumentException e) {
			// TODO Auto-generated catch block
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			e.printStackTrace(new PrintWriter(buf, true));
			String expMessage = buf.toString();
			logger.error(" - [LOG_EXCEPTION] - " + expMessage);
			return new ArrayList<Node>();
		}

	}
}
