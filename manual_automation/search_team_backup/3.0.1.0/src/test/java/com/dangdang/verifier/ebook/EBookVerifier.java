package com.dangdang.verifier.ebook;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.list.Book_strategy;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2015年1月19日 下午7:38:57  
 * 类说明  电子书推广验证类
 */
public class EBookVerifier {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(EBookVerifier.class);

	public boolean Verifier(ProdIterator iterator, Map<String, String> map) {
		
		Node fNode = iterator.next();
		if (XMLParser.product_is_first_ebook(fNode).equals("1")) {
			String product_id = XMLParser.product_id(fNode);
			if (isValid(fNode)) {
				logger.info(" - [LOG_SUCCESS] - This query " + map.get("query").toString() + " has first ebook id: " + product_id);
				return true;
			}else{
				logger.info(" - [LOG_FAILED] - This query " + map.get("query").toString() + " has first ebook id: " + product_id + "is Invalid");
				return false;
			}
		}else {
			Map<String, String> urlMap = new HashMap<String, String>();
			urlMap.put("st", "full");
			urlMap.put("um", "search_ranking");
			urlMap.put("_new_tpl", "1");
			urlMap.put("-cat_paths", "98.00.00.00.00.00");
			try {
				urlMap.put("q", URLEncoder.encode(map.get("query"), "GBK"));
			} catch (UnsupportedEncodingException e) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
			}
			// 得到品的迭代器
			ProdIterator ebookiterator = new ProdIterator(urlMap);
			if (ebookiterator.getTotalCount()==0) {
				logger.info(" - [LOG_SKIP] - This query no ebook");
				return true;
			} else {
				while (ebookiterator.hasNext()) {
					Node eNode = ebookiterator.next();
					String eproduct_id = XMLParser.product_id(eNode);
					if (isValid(fNode)) {
						logger.info(" - [LOG_SUCCESS] - This query " + map.get("query").toString() + " has first ebook id: " + eproduct_id);
						return false;
					}
				}
				logger.info(" - [LOG_SUCCESS] - This query has no Valid ebook");
				return true;
			}
			
		}
	}
	
	
	
	
	
	private boolean isValid(Node fNode){
		int score = TextRelation(XMLParser.product_scope(fNode));
//		String catepath = XMLParser.product_catelogPath(fNode);//部分电子书没有catpath字段
		String medium = XMLParser.product_medium(fNode);
		int imagenum = Integer.valueOf(XMLParser.product_numImage(fNode));
		String dd_sell = XMLParser.product_is_dd_sell(fNode);
		if (score>=5&&medium.equals("22")&&imagenum>0&&dd_sell.equals("1")) {
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 得到文本相关性得分
	 * @param score
	 * @return
	 */
	public static int TextRelation(String score) {
		int iscore = Integer.valueOf(score);
		// 转成二进制
		String b_scope_full = Integer.toBinaryString(iscore);
		// 拿到文本相关性部分的得分
		String text_rel_full = b_scope_full.substring(b_scope_full.length() - 27, b_scope_full.length() - 24);
		// 从二进制重新转换成10进制
		int tr = Integer.parseInt(text_rel_full, 2);
		return tr;
	}
	
	/**
	 * 判断是否属于电子书分类
	 * @param catePath
	 * @return
	 */
	public boolean isEbookCategory(String catePath) {

		String catePath_head2 = org.apache.commons.lang3.StringUtils.substring(catePath, 0, 2);
		if (catePath_head2.equals("98")) {
			return true;
		}
		return false;
	}
	
}
