/*
 * ClassName:	CommonCompareScheduler.java
 * Version: 	V1.0
 * Date: 		2015-07-08 18:15
 * copyright
 */

package com.dangdang;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.SearchRequester;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gaoyanjun@dangdang.com
 * @version 创建时间：2015-06-30
 * 比较推荐和单品页价格是否相同
 * 指定一堆query，得到pid
 * 根据pid分别调取推荐和单品页的接口，对比价格
 */
public class CommonCompareScheduler {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(CommonCompareScheduler.class);
	private final static ObjectMapper objectMapper = new ObjectMapper();	
	private static String XPATH_PRICE = "//resultObject/products/row/price/sale_price";
	private static String XPATH_PROMO_PRICE = "//resultObject/products/row/promo_model/row/promo_info/row/favor/direct_price";
	
	// 推荐接口
	private static String URL1 = "http://192.168.196.167:8270/product/?fields=sale_price,original_price"
			+ "&real_time=1&ref=search&pids=";
	// 单品页 价格
	private static String URL2 = "http://192.168.197.101/v2/find_products.php?by=product_id&expand=1|2|5|25|"
			+ "&result_format=xml&commfields=&searchfields=alsoview&product_status=&include_self=&keys=";
	
	// 要验证价格的页面
	List<String> RequestURLs = new ArrayList<String>();
	
	{		
//		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&-brand_id=5575&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
//		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&-brand_id=3015&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
//		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&-brand_id=4351&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
//		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&-brand_id=7440&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
//		
//		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&-brand_id=7737&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
//		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&-brand_id=7955&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
//		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&-brand_id=6979&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
//		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&-brand_id=3906&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
//		RequestURLs.add("http://10.5.24.149:8390/?ip=111.207.228.104&pid=20150817193510457101165305132223573&domain=category.dangdang.com&_url_token=6&vert=0&cate_type=0&platform=4&is_default_search=0&is_e_default=0&is_suit_default=0&-is_shop_product=0&nl=1&-brand_id=7423&st=mall&cat_paths=4002778&um=list_ranking&gp=cat_paths,brand_id,attrib,is_dd_sell,label_id");
	}

	@BeforeMethod
 	public void setup() {
	}

	@AfterMethod
	public void clearup() {
	}

	@AfterClass
	public void classclearup() {

	}
	
	@Test(enabled = true, groups = "p2")
	public void CompareResult() {		
		try{		
			Map<String, String> diffPids = new HashMap<String, String>();
			 
			// 遍历需要对比价格也页面
			for(String url : RequestURLs){
				// 把pid存入一个list中
				diffPids.clear();
				// 指定url，取120000个品
				ProdIterator initialIterator = new ProdIterator(120000,url,false);				
				while(initialIterator.hasNext()){
					Node node = initialIterator.next(url);
					// 取到每个品的pid
					String pid = XMLParser.product_id(node);
					
					// get list price
//					String price_list = "0";
//					try{
//						price_list = XMLParser.product_dd_sale_price(node);
//					}catch(Exception e){
//						// e.printStackTrace();
//					}
					
					// 拼接推荐和单品页的api
					String newURL1 = URL1.concat(pid);
					String newURL2 = URL2.concat(pid);							
					
					// send request
					String recoJson = SearchRequester.get(newURL1);		
						// 对单品api中的特殊字符进行encode处理
					@SuppressWarnings("deprecation")
					String specialChar = java.net.URLEncoder.encode("|");
					String pidXML = SearchRequester.get(newURL2.replace("|", specialChar));				
					Document doc = XMLParser.read(pidXML);
					
					// 1.get reco price
					String price_reco = reco_price(recoJson, pid);
					
					
					// 如果推荐接口取到的价格为0，从搜索接口直接取
//					if(price_reco.equals("0")){
//						List<Node> pidNode = URLBuilder.porductSearch(pid, false);
//						if(!pidNode.isEmpty()){
//							String price = XMLParser.product_dd_sale_price(pidNode.get(0));
//							if(!price.isEmpty())
//								price_list = price;
//						}
//					}
					
					// 2. 单品页接口返回的信息中，如果有促销价，且促销价<单品价，取促销价；否则取单品价
					String final_pid_price = "0";				// 最终取到的价格
					String price_pid = getPIDSalePrice(doc);	// 单品价
					String price_promo = getPromoPrice(doc);	// 促销价
					
					if(price_promo.isEmpty()){
						if(!price_pid.isEmpty()){				// 没有促销价，且当当价非空，取当当价，否则为0
							String medium_price = String.valueOf((Double.valueOf(price_pid) * 100));
							final_pid_price = medium_price.split("\\.")[0];							
						}
					}else{										// 有促销价
						if(!price_pid.isEmpty()){					// 有促销价，且当当价非空，进行比较
							// compare
							Double promo_value = Double.valueOf(price_promo);
							Double sale_value = Double.valueOf(price_pid);
							if(promo_value.compareTo(sale_value) < 0){ // 促销价格比当当价格小，取促销价
								final_pid_price = String.valueOf((promo_value * 100)).split("\\.")[0];
							}else									 // 促销价格比当当价格大，取当当价格
								final_pid_price = String.valueOf((sale_value * 100)).split("\\.")[0];							
							
						}else{										// 有促销价，当当价为空，取促销价
							final_pid_price = String.valueOf((Double.valueOf(price_promo) * 100)).split("\\.")[0];							
						}
					}
					
					// compare，价格不同，把pid写入列表
					if(!price_reco.equals(final_pid_price)){
						diffPids.put(pid, price_reco + "&" + final_pid_price);
					}
				}	
				if(!diffPids.isEmpty()){
					logger.info(" - Reco URL: " + URL1.concat(diffPids.keySet().toString().split("\\[")[1].split("\\]")[0]));			
					logger.info(" - PID URL: " + URL2.concat(diffPids.keySet().toString().split("\\[")[1].split("\\]")[0]));	
					logger.info(" - RESULT: different pids: " + diffPids.toString());
					logger.info(" - COUNT: " + diffPids.size());
				}
			}
			
						
			
		} catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		
		}
	}

	/**
	 * 解析json,获取推荐价格
	 * @param result
	 * @param pid
	 * @return
	 */
	public static String reco_price(String result, String pid) {
		String price = null;		
		try {
			JsonNode node = objectMapper.readTree(result);
			Iterator<JsonNode> nodeIterator = node.elements();
			ArrayList<JsonNode> nodeList = new ArrayList<JsonNode>();
			while (nodeIterator.hasNext()) {
				JsonNode jsonNode = (JsonNode) nodeIterator.next();
				nodeList.add(jsonNode);
			}
			price = nodeList.get(1).get(pid).get("sale_price").toString(); 				
					
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return price;
	}

	/**
	 * 获取单品api的单品价格
	 * @param doc
	 * @return
	 */
	public static String getPIDSalePrice(Document doc){
		String value = "";
		List<Node> ln = doc.selectNodes(XPATH_PRICE);		
		if (ln.size()>0){
			value = ln.get(0).getStringValue();
			logger.debug("- [XMLParser] - get the Brand sub node ; BrandID=" + value);
		}else{
			logger.debug("- [XMLParser] - get the Brand sub node ; No BrandID");
		}
		return value;
	}
	
	/**
	 * 获取单品api的促销价格
	 * @param doc
	 * @return
	 */
	public static String getPromoPrice(Document doc){
		String value = "";
		List<Node> ln = doc.selectNodes(XPATH_PROMO_PRICE);		
		if (ln.size()>0){
			value = ln.get(0).getStringValue();
			logger.debug("- [XMLParser] - get the Brand sub node ; BrandID=" + value);
		}else{
			logger.debug("- [XMLParser] - get the Brand sub node ; No BrandID");
		}
		return value;
	}
}
