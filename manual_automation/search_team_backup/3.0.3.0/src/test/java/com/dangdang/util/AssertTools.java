package com.dangdang.util;

import java.util.List;
import java.util.Map;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2015年4月1日 下午1:39:51  
 * 类说明  
 */
public class AssertTools {
	
	
	/**
	 * 得到总库存状态
	 * @param score  商品的score得分
	 * @return 是否有库存
	 */
	public static boolean AllStockStatus(String score) {

		int iscore = Integer.valueOf(score);
		// 转成二进制
		String b_scope_full = Integer.toBinaryString(iscore);
		// 拿到库存标志位的得分
		String text_rel_full = b_scope_full.substring(b_scope_full.length() - 16, b_scope_full.length() - 15);
		int tr = Integer.parseInt(text_rel_full);
		if (tr==1) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 得到是否单品置顶
	 * @param score  商品的score得分
	 * @return 是否单品置顶
	 */
	public static boolean singleTopStatus(String score) {

		int iscore = Integer.valueOf(score);
		if (iscore>=268435456) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 得到区域库存状态
	 * @param score  商品的score得分
	 * @return 是否区域有库存
	 */
	public static boolean StockStatus(String score) {

		int iscore = Integer.valueOf(score);
		// 转成二进制
		String b_scope_full = Integer.toBinaryString(iscore);
		// 拿到库存标志位的得分
		String text_rel_full = b_scope_full.substring(b_scope_full.length() - 9, b_scope_full.length() - 8);
		int tr = Integer.parseInt(text_rel_full);
		if (tr==1) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 得到图片状态
	 * @param score  商品的score得分
	 * @return 是否有图片
	 */
	public static boolean HasImages(String score) {

		int iscore = Integer.valueOf(score);
		// 转成二进制
		String b_scope_full = Integer.toBinaryString(iscore);
		// 拿到库存标志位的得分
		String text_rel_full = b_scope_full.substring(b_scope_full.length() - 15, b_scope_full.length() - 14);
		int tr = Integer.parseInt(text_rel_full);
		if (tr==1) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * 得到文本相关性得分
	 * @param score  商品的score得分
	 * @return 文本相关性得分
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
	
	
	
	// 调试使用
		public static void main(String[] args) {
			String aString = "430736255";
			
			System.out.println(singleTopStatus(aString));

		}


}
