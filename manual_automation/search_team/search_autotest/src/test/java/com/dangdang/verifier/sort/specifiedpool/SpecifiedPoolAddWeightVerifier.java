package com.dangdang.verifier.sort.specifiedpool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.data.FuncQuery;
import com.dangdang.util.AssertTools;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**
 * @author gaoyanjun@dangdang.com
 * @version 创建时间：2015年7月14日 
 *  类说明 加权正确性验证
 */
public class SpecifiedPoolAddWeightVerifier {
	
	private static List<String> ProductPool =  new ArrayList<String>();
	private static List<String> BrandPool = new ArrayList<String>();
	private static List<String> ShopPool = new ArrayList<String>();	

	// 日志记录器
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SpecifiedPoolAddWeightVerifier.class);	
		
	public boolean Verifier(ProdIterator old_iterator, ProdIterator new_iterator, String query, String vps) {
		
		int matchedCount = 0;
		int unmatchedCount = 0;
		boolean fail = false;
		
		// 初始化商品池、品牌池、店铺池
		InitPools();		
		
		// 获得实施策略前后，和excel文件中匹配的product及其分数
		Map<String, Integer> beforeMap = GetMatchedProducts(old_iterator);
		Map<String, Integer> afterMap = GetMatchedProducts(new_iterator);
		
		if(beforeMap.size() != afterMap.size()){
			logger.error(" - [LOG_FAILED] - verify function [SpecifiedPoolAddWeight], query - [" + query + "]"
					+ "- total count of 2 resultsets are different,"
					+ " products count before using strategy: [" + beforeMap.size() + "], "
					+ "products count after using strategy [" + afterMap.size() + "]");
			return false;
		}
		
		if(beforeMap.size() == 0){
			logger.info(" - [LOG_SKIP] - verify function [SpecifiedPoolAddWeight] , query - [" + query+ "]"
					+ "did not find matched products");
			return true;
		}
		
		// 比较分数
		Iterator<String> it = beforeMap.keySet().iterator();
		while(it.hasNext()){
			Object key = it.next();
			if (afterMap.containsKey(key)){
				Integer beforeScore = beforeMap.get(key);
				Integer afterScore = afterMap.get(key);
				
				// 商业因素分加五分
				if(AssertTools.BusinessScore(afterScore)  != AssertTools.BusinessScore(beforeScore) + 5){					
					logger.error("query -["+ query +"], Score of product [" + key.toString() + "] was not set to right value: "
							+ "old score is [" +beforeScore +"], new score is ["+ afterScore +"]");
					fail = true;
					unmatchedCount ++;
				} else
					matchedCount ++;
				
			} else{
				logger.error("query -["+ query +"], after using new strategy, did not find product [" + key.toString() + "] in new result set!");
				fail = true;
				unmatchedCount ++;
			}
		}	
		
		if (fail){ 
			logger.error(" - [LOG_FAILED] - verify function [SpecifiedPoolAddWeight] , query - [" + query+ "]"
					+ "- unMatched product count is [" + unmatchedCount +"]");
			return false;
		}
		else {
			logger.info(" - [LOG_PASS] - verify function [SpecifiedPoolAddWeight] , query - [" + query + "]"
					+ "- matched product count is [" + matchedCount +"]");
			return true;
		} 
	}

	/*
	 * 初始化商品池、品牌池、店铺池
	 */
	public static void InitPools() {
		
		// TODO:改成读取excel
		
    	//待匹配的商品池列表
		String products = "23629643,23608680,23618525,23629646,23638096,23638097,23654501,23654498,"
				+ "23643238,23651027,23654494,23654500,23685471,23640067,23643237,23669231,"
				+ "23654499,23654496,23654502,23654503,20531088,23654504,23629645,23643240,"
				+ "23643236,23643241,23669229,23669230,23638058,23629647,23696693,23698722,"
				+ "23645663,23651050,23683391,23696690,23696382,23698639,23696692,23678792,"
				+ "23629644,23608679,23640068,23669234,23618527,23654491,23698324,23643239,"
				+ "23654493,23654492,23654495,23655301,23654497,23696691,1267486200,1377436305,"
				+ "1257643137";
		String[] prodArray = products.split(",");
		ProductPool = Arrays.asList(prodArray);

		String brands = "7907,19832,18494";
		String[] brandArray = brands.split(",");
		BrandPool = Arrays.asList(brandArray);

		String shops = "14839,3957,11257,11184";
		String[] shopArray = shops.split(",");
		ShopPool = Arrays.asList(shopArray);
		
	}
	
	/*
	 * 获取与加权池匹配的pid和score值，存入map
	 */
	public Map<String,Integer> GetMatchedProducts(ProdIterator iterator){
		Map<String, Integer> matchedProducts = new HashMap<String, Integer>();
		Node node = null;
		
		while (iterator.hasNext()){
			// 先获取结果集中的pid、score、brand、shop
			node = iterator.next();
			String PidInXml = XMLParser.product_id(node);
			int Score = Integer.parseInt(XMLParser.product_scope(node));
			String brandInXml = XMLParser.product_brand(node);
			String shopInXml = XMLParser.product_shopID(node);
			
			// 和商品池比较，找到与池中匹配的product，把pid和score作为key、value加入map
			if(!ProductPool.isEmpty()){
				for(String PidInPool: ProductPool){
					if(PidInXml != "" && PidInPool != "" && PidInXml.equals(PidInPool)){
						matchedProducts.put(PidInXml, Score);
					}
				}
			}
			
			// 和品牌池比较，找到与池中匹配的product，把pid和score作为key、value加入map
			if(!BrandPool.isEmpty()){
				for(String BrandInPool: BrandPool){
					if(brandInXml != "" && BrandInPool != "" && brandInXml.equals(BrandInPool)){
						matchedProducts.put(PidInXml, Score);
					}				
				}
			}
			
			// 和店铺池比较，找到与池中匹配的product，把pid和score作为key、value加入map
			if(!ShopPool.isEmpty()){
				for(String ShopInPool: ShopPool){
					if(shopInXml != "" && ShopInPool != "" && shopInXml.equals(ShopInPool)){
						matchedProducts.put(PidInXml, Score);
					}				
				}
			}
		}
		return matchedProducts;
	}
	
	// 调试使用
	public static void main(String[] args) {
//		String aString = StoolData.query4json(baseurl, type);
//		Map<String, List<String>> dataMap = JsonParser.newProdData(aString);
		
		
//		System.out.println(dataMap.get("01.47.93.04.00.00"));
//		System.out.println(dataMap.get(dataMap.keySet().toArray()[0]));
//		System.out.println(dataMap.toString());

	}
	
	

}
