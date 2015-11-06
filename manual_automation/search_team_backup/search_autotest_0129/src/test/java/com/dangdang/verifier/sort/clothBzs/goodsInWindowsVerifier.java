package com.dangdang.verifier.sort.clothBzs;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Node;

import com.dangdang.client.RPCClient;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

public class goodsInWindowsVerifier {

	
	public static Logger logger = Logger.getLogger(goodsInWindowsVerifier.class);
	
	
	//标记品类和shopid对应关系,list中的位置
	int p = 0;
	
	private static final String[] clothCategory = { "58.64.05.00.00.00", "58.61.00.00.00.00"};
	
	
	
	public boolean verify(FuncQuery fquery) {

		// 得到query词
		String query = fquery.getFquery();
		String pid = fquery.getDesc();

		// 拿到query词的第一个分类的权重
		String firstCategory = null;

		// 第一分类的权重
		int firstPriority = 0;
		// 从接口得到查询的分类反馈信息
		Map<String, Double> priorityMap = RPCClient.getCategoryPriority(query);

		// 找到第一分类
		for (Map.Entry<String, Double> entry : priorityMap.entrySet()) {
			int priority = entry.getValue().intValue();
			if (priority > firstPriority) {
				firstPriority = priority;
				firstCategory = entry.getKey();
			}
		}

		// 查询的url参数组装
		Map<String, String> urlMap = new HashMap<String, String>();
		urlMap.put("st", "full");
		urlMap.put("um", "search_ranking");
		urlMap.put("_mod_ver", "S5");
		urlMap.put("_new_tpl", "1");
		try {
			urlMap.put("q", URLEncoder.encode(query, "GBK"));
		} catch (UnsupportedEncodingException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
		}

		// 得到品的迭代器
		ProdIterator iterator = new ProdIterator(urlMap);
		
		// 第一分类权重>=40 && 第一分类属于服装事业部分类
		if (firstPriority >= 40 && isClothBzsCategory(firstCategory) && iterator.getTotalCount() >= 500) {
			String score_after = "";
			String score_before= "";
			
			//查找进入新排序后的品分数
			while(iterator.hasNext()){
				Node node = iterator.next();
//				if(iterator.getPoint()==5){
//				
				String product_id = XMLParser.product_id(node);
//					insertQuery(fquery.getFquery(), "goods_windows", product_id);
//					break;
//				}
				if(product_id.equals(pid)){
					score_after = XMLParser.product_scope(node);
					break;
				}
			}
			
			
			urlMap.remove("_mod_ver");
			ProdIterator iterator_before = new ProdIterator(urlMap);
			while(iterator_before.hasNext()){
				Node node_beforeNode = iterator_before.next();
				String product_id_before = XMLParser.product_id(node_beforeNode);
				if(product_id_before.equals(pid)){
					score_before = XMLParser.product_scope(node_beforeNode);
					break;
				}
			}
			
			if(compareBizScore(score_before,score_after)){
				return true;
			}
		}
		logger.error("Product_id:"+pid);
		return false;
	}
	
	/**
	 * 判断是否属于服装事业部的分类
	 * 
	 * @param catePath
	 * @return
	 */
	public boolean isClothBzsCategory(String catePath) {
		
		String catePath_head5 = org.apache.commons.lang3.StringUtils.substring(catePath,0,5);
		String catePath_head8 = org.apache.commons.lang3.StringUtils.substring(catePath,0,8);
		
		if (catePath_head8.equals(org.apache.commons.lang3.StringUtils.substring(clothCategory[0],0,8))) {
			p = 0;//p控制品类和店铺id的对应关系
			return true;
		} else if (catePath_head5.equals(org.apache.commons.lang3.StringUtils.substring(clothCategory[1],0,5))) {
			p = 1;
			return true;
		}
		return false;
	}

	/**
	 * 比较商业打分
	 * @param before
	 * @param after
	 * @return
	 */
	public static boolean compareBizScore(String before,String after){
		
		//得到score分，转换成整数
		int ibefore = Integer.valueOf(before);
		int iafter = Integer.valueOf(after);
		//转成2进制
		String b_before = Integer.toBinaryString(ibefore);
		String b_after = Integer.toBinaryString(iafter);
		
		//截取商业因素部分
		String b_biz_before = b_before.substring(b_before.length()-8,b_before.length());
		String b_biz_after = b_after.substring(b_after.length()-8,b_after.length());
		
		//转换回
		int i_biz_before = Integer.valueOf(b_biz_before, 2);
		int i_biz_after = Integer.valueOf(b_biz_after,2);
		if(i_biz_after>=255){
			return true;
		}else if(i_biz_after-i_biz_before == 30){
			return true;
		}else{
			logger.error("before:"+i_biz_before+" after:"+i_biz_after);
			logger.error("b_before:"+b_biz_before+"/"+b_before+"; b_after:"+b_biz_after+"/"+b_after);
			return false;
		}
		
		
	}
	
//	public static void insertQuery(String query,String verify_point,String pid){
//		Connection conn = DBConnUtil.getConnection();
//		String queryf = String.format("INSERT INTO function_query (`query`,`desc`,`verify_point`) VALUES ('%s','%s','%s');", query,pid,verify_point);
//		DBConnUtil.exeUpdate(queryf, conn);
//		DBConnUtil.closeConnection(conn);
//		
//	}
	
	

}
