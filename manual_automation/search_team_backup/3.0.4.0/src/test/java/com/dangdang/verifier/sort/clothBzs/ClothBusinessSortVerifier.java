package com.dangdang.verifier.sort.clothBzs;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

public class ClothBusinessSortVerifier {

	// 日志记录器
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ClothBusinessSortVerifier.class);
	

	// 15184,14727,14339,11257,14006,15186,14222,13929,14474,14874,15187,14701,14999
	// 58.64.05.00.00.00 女士服装 店铺id:14424,13437,13480,13241,13943,13824;
	// 58.61.00.00.00.00
	// 运动户外:15184,14727,14339,11257,14006,15186,14222,13929,14474,14874,15187,14701,14999
	private static final String[] clothCategory = { "58.64.05.00.00.00", "58.61.00.00.00.00" };
	// 预设的优质商家品的位置
	private static final Integer[] pos_quality = { 1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 15, 17, 18, 19, 21, 22, 23, 25, 26, 29, 30, 33, 34, 37, 38, 41, 42, 45,
			46, 49, 50, 53, 54, 57, 58 };
	// 预设的新商家品的位置
	private static final Integer[] pos_new = { 4, 8, 12, 16, 20, 24 };
	// 预设的普通商家品的位置
	private static final Integer[] pos_common = { 27, 28, 31, 32, 35, 36, 39, 40, 43, 44, 47, 48, 51, 52, 55, 56, 59, 60 };

	public static List<Integer> temQulist = Arrays.asList(pos_quality);
	public static List<Integer> temNelist = Arrays.asList(pos_new);
	public static List<Integer> temColist = Arrays.asList(pos_common);

	// 标记品类和shopid对应关系,list中的位置
	int p = 0;

	/**
	 * 验证服装事业部新排序策略
	 * 
	 * @param fquery
	 *            输入的query对象
	 * @return
	 */
	public boolean verify(FuncQuery fquery) {
		// NewPList.clear();
		// QuliPList.clear();
		// ComPList.clear();
		//
		// 暂存新商家位置上所有品的列表
		List<Node> NewPList = new ArrayList<Node>();
		// 暂存优质商家位置上所有品的列表
		List<Node> QuliPList = new ArrayList<Node>();
		// 暂存普通商家位置上所有品的列表
		List<Node> ComPList = new ArrayList<Node>();

		// 得到query词
		String query = fquery.getFquery();

		// 拿到query词的第一个分类的权重
		String firstCategory = null;
		// 第一分类的权重
		int firstPriority = 0;
		
		// 从接口得到查询的分类反馈信息
//		Map<String, Double> priorityMap = RPCClient.getCategoryPriority(query);
		// // 找到第一分类(环境分类预测的值不一致，***************暂不用*****************)
		// for (Map.Entry<String, Double> entry : priorityMap.entrySet()) {
		// int priority = entry.getValue().intValue();
		// if (priority > firstPriority) {
		// firstPriority = priority;
		// firstCategory = entry.getKey();
		// }
		// }

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

		// 获取第一分类
		Map<String, String> urlMap4Ranking = new HashMap<String, String>();
		urlMap4Ranking.put("st", "full");
		urlMap4Ranking.put("um", "search_ranking");
		urlMap4Ranking.put("_mod_ver", "S5");
		urlMap4Ranking.put("_new_tpl", "1");
		
		urlMap4Ranking.put("q", query);
		urlMap4Ranking.put("ranking_total", "1");
		urlMap4Ranking.put("ranking_info", "1");
		
		logger.info(urlMap.toString());
//		logger.info("####################################################");
		logger.info(urlMap4Ranking.toString());
		String urlRanking = URLBuilder.buildUrl(urlMap4Ranking);
		String xmlRanking = SearchRequester.get(urlRanking);
		logger.info(urlRanking);
		Document docRanking = null;
		try {
			docRanking = XMLParser.read(xmlRanking);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		List<Node> firstPaths = XMLParser.getcatepaths(docRanking);
		if (firstPaths.size() > 0) {
			String firstPathString = firstPaths.get(0).getStringValue();
			String[] arrString = firstPathString.split(":");
			firstCategory = arrString[0];
			firstPriority = Integer.valueOf(arrString[1]);
		}else {
			logger.info(" - [LOG_EXCEPTION] - " + "该词无预测分类");
			return true;
		}

		// 第一分类权重>=40 && 第一分类属于服装事业部分类
		if (firstPriority >= 40 && isClothBzsCategory(firstCategory) && iterator.getTotalCount() >= 350) {
			logger.info(" - [LOG_INFO] - 预测第一分类：" + firstCategory);
			// 结果中去除分类直达
			String urlStr = URLBuilder.buildURL(urlMap);
			String xmlStr = SearchRequester.get(urlStr);
			Document doc = null;
			try {
				doc = XMLParser.read(xmlStr);
			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}

			List<Node> pathList = XMLParser.pathInfo(doc);
			if (pathList != null) {
				// 验证是否为分类直达(是否包含(result\StatInfo\Path节点)
				if (pathList.size() > 0) {
					logger.error(" - [INSIDE] - " + "Cate direct is enable in result. ");
					return false;
				}
			}

			// 新品店铺id记录
			List<String> newShopList = new ArrayList<String>();
			// 热卖店铺id记录
			List<String> qulShopList = new ArrayList<String>();
			// 普通店铺id记录
			List<String> comShopList = new ArrayList<String>();
			// 所有店铺记录
			List<String> allShopList = new ArrayList<String>();

			// 前五页对应位置上的品按照顺序放置到对应的列表中，方便后续使用
			while (iterator.hasNext()) {

				Node node = iterator.next();
				if (iterator.getpageIndex() > 5) {
					break;
				}
				allShopList.add(XMLParser.product_shopID(node));

				if (temQulist.contains(iterator.getPoint() + 1)) {
					QuliPList.add(node);
					qulShopList.add(XMLParser.product_shopID(node));
				} else if (temNelist.contains(iterator.getPoint() + 1)) {
					newShopList.add(XMLParser.product_shopID(node));
					NewPList.add(node);
				} else if (temColist.contains(iterator.getPoint() + 1)) {
					comShopList.add(XMLParser.product_shopID(node));
					ComPList.add(node);
				} else {
					logger.error(" - [INSIDE] - " + "位置有问题");
					return false;
				}
			}
			
			// 优质商家shop_id列表
			List<List<String>> QShopList = QualityShop();
			// 新商家shop_id列表
			List<List<String>> NShopList = NewShop();
			
			
			
			StringBuffer aBuffer = new StringBuffer("     >>>>>RESULT-allShopList: ");
			for (int a = 0; a < allShopList.size(); a++) {
				aBuffer.append(a%60+1);
				aBuffer.append(":");
				aBuffer.append(allShopList.get(a));
				aBuffer.append(", ");
			}
			logger.info(aBuffer.toString());
			
			StringBuffer bBuffer = new StringBuffer("     >>>>>RESULT-newShopList: ");
			for (int b = 0; b < newShopList.size(); b++) {
				bBuffer.append(b/6+1);
				bBuffer.append(".");
				bBuffer.append(pos_new[b%6]);
				bBuffer.append(":");
				if (NShopList.get(0).contains(newShopList.get(b))||NShopList.get(1).contains(newShopList.get(b))) {
					bBuffer.append("##");
				}
				bBuffer.append(newShopList.get(b));
				bBuffer.append(", ");
			}
			logger.info(bBuffer.toString());
			
			StringBuffer cBuffer = new StringBuffer("     >>>>>RESULT-qulShopList: ");
			for (int c = 0; c < qulShopList.size(); c++) {
				cBuffer.append(c/36+1);
				cBuffer.append(".");
				cBuffer.append(pos_quality[c%36]);
				cBuffer.append(":");
				if (QShopList.get(0).contains(qulShopList.get(c))||QShopList.get(1).contains(qulShopList.get(c))) {
					cBuffer.append("**");
				}
				cBuffer.append(qulShopList.get(c));
				cBuffer.append(", ");
			}
			logger.info(cBuffer.toString());
			
			StringBuffer dBuffer = new StringBuffer("     >>>>>RESULT-comShopList: ");
			for (int d = 0; d < comShopList.size(); d++) {
				dBuffer.append(d/18+1);
				dBuffer.append(".");
				dBuffer.append(pos_common[d%18]);
				dBuffer.append(":");
				if (NShopList.get(0).contains(comShopList.get(d))||NShopList.get(1).contains(comShopList.get(d))) {
					dBuffer.append("##");
				}
				if (QShopList.get(0).contains(comShopList.get(d))||QShopList.get(1).contains(comShopList.get(d))) {
					dBuffer.append("**");
				}
				dBuffer.append(comShopList.get(d));
				dBuffer.append(", ");
			}
			logger.info(dBuffer.toString());


			// 去除单品置顶
			if (!rmTopProduct(QuliPList)) {
				return false;
			}

			// 优质商家的位置上是优质商家的品
			int qua_split_pos = 0;
			if (!doVerify(QuliPList, qua_split_pos, QShopList.get(p), firstCategory)) {
				logger.error("[Quality]");
				return false;
			}

			// 新商家的位置上是新商家的品
			int new_split_pos = 0;
			if (!doVerify(NewPList, new_split_pos, NShopList.get(p), firstCategory)) {
				logger.error("[New]");
				return false;
			}

			// 普通商品的位置上是普通商家，并且按照Score的分数降序排列
			// int pre_score = -1; // 初始化
			for (int x = 0; x < ComPList.size(); x++) {
				Node node = ComPList.get(x);
				String score = XMLParser.product_scope(node);
				String shop_id = XMLParser.product_shopID(node);
				String comCatepath = XMLParser.product_catelogPath(node);
				// 普通品位置只能包含普通商品的品
				if ((QShopList.get(p).contains(shop_id) && isaCategory(firstCategory, comCatepath) && (TextRelation(score) >= 5))
						|| (NShopList.get(p).contains(shop_id) && isaCategory(firstCategory, comCatepath) && (TextRelation(score) >= 5))) {
					if (!noProperProduct(ComPList, QShopList.get(p), NShopList.get(p))) {
						logger.error(" - [INSIDE] - " + " - Quality/New shop Product was found at commons product's position. " + PositionCom(x));
						logger.error(" - [INSIDE] - " + " SHOP ID:" + shop_id + " Common's Position: " + ComPList.indexOf(node));
						return false;
					}

					// 是否连续
					for (int y = x; y < ComPList.size(); y++) {
						String tem_shop_id = XMLParser.product_shopID(ComPList.get(y));
						String tem_product_id = XMLParser.product_id(ComPList.get(y));
						if (QShopList.contains(tem_shop_id) || NShopList.contains(tem_shop_id)) {
							continue;
						} else {
							logger.error(" - [INSIDE] - " + "The Common product type list is discontinuity.Position:" + PositionCom(y));
							logger.error(" - [INSIDE] - " + "The product__id:" + tem_product_id);
							return false;
						}
					}
				}

			}
			// 如果不进入服装事业部重新排序的要求
		} else {
			logger.info("不满足第一分类大于40或者结果总数少于350个或者第一分类不属于预设分类");
			return true; // SortScore(iterator); 含有打散等策略，不能按照score分排序验证
		}
		logger.info(" - [INSIDE] - " + "Verify Successed!");
		return true;
	}

	/**
	 * 判断是否属于服装事业部的分类
	 * 
	 * @param catePath
	 * @return
	 */
	public boolean isClothBzsCategory(String catePath) {

		String catePath_head5 = org.apache.commons.lang3.StringUtils.substring(catePath, 0, 5);
		String catePath_head8 = org.apache.commons.lang3.StringUtils.substring(catePath, 0, 8);

		if (catePath_head8.equals(org.apache.commons.lang3.StringUtils.substring(clothCategory[0], 0, 8))) {
			p = 0;// p控制品类和店铺id的对应关系
			return true;
		} else if (catePath_head5.equals(org.apache.commons.lang3.StringUtils.substring(clothCategory[1], 0, 5))) {
			p = 1;
			return true;
		}

		return false;
	}

	/**
	 * 生成优质商家的列表
	 * 
	 * @return
	 */
	public static List<List<String>> QualityShop() {
		String QualityShop1 = "9625,9902,10793,10876,7752,10464,14072,10188,8746,8323,8205,7171,7386,14050,8862,8294,5384,7710,6575,4609,8639,7192,8754";
		String[] QualArray1 = QualityShop1.split(",");
		// List<String> qualList1 = Arrays.asList(QualArray1);
		List<String> qualList1 = new ArrayList<String>(Arrays.asList(QualArray1));

		String QualityShop2 = "9274,3957,10776,11132,11181,11868,11868,12359,12640,12909,11184,13433,15184,12300,12689,13898,14474,9957,6128,12683,9183,9181,9176,9193,9136,9154,7723";
		String[] QualArray2 = QualityShop2.split(",");
		// List<String> qualList2 = Arrays.asList(QualArray2);
		List<String> qualList2 = new ArrayList<String>(Arrays.asList(QualArray2));

		List<List<String>> qShopList = new ArrayList<List<String>>();
		qShopList.add(qualList1);
		qShopList.add(qualList2);

		return qShopList;
	}

	/**
	 * 生成新商家的列表
	 * 
	 * @return
	 */
	public static List<List<String>> NewShop() {
		String NewShop1 = "14424,13437,13480,13241,13943,13824";
		String[] NewArray1 = NewShop1.split(",");
		List<String> newList1 = Arrays.asList(NewArray1);

		String NewShop2 = "15184,14727,14339,11257,14006,15186,14222,13929,14474,14874,15187,14701,14999";
		String[] NewArray2 = NewShop2.split(",");
		List<String> newList2 = Arrays.asList(NewArray2);

		List<List<String>> nShopList = new ArrayList<List<String>>();
		nShopList.add(newList1);
		nShopList.add(newList2);

		return nShopList;
	}

	/**
	 * 验证指定商品是属于指定的商家列表
	 * 
	 * @param list
	 *            搜索结果中的商品列表
	 * @param split_pos
	 *            分割位置
	 * @param shoplist
	 *            制定的商品列表
	 * @return
	 */
	public static boolean doVerify(List<Node> list, int split_pos, List<String> shoplist, String firstCategory) {
		// 用来记录上一个品的score分数
		int qua_pre_score = -1;
		// 新品or热卖店铺id记录
		List<String> ShopList = new ArrayList<String>();
		// 循环遍历商品列表
		for (int i = 0; i < list.size(); i++) {
			// 得到商品的shop_id与score分
			String shop_id = XMLParser.product_shopID(list.get(i));
			String score = XMLParser.product_scope(list.get(i));
			String product_id = XMLParser.product_id(list.get(i));
			String catepath = XMLParser.product_catelogPath(list.get(i));
			ShopList.add(shop_id);
			// 如果是制定商铺列表的
			if (shoplist.contains(shop_id) && isaCategory(firstCategory, catepath)) {
				if (qua_pre_score == -1) {
					// 召回的品文本相关性要>=5
					if (!(TextRelation(score) >= 5)) {
						logger.error(" - [INSIDE] - " + " - The product text relative score is less than 5; product_id:" + product_id + " Score:" + score + " "
								+ PositionQul(i) + PositionNew(i));
						return false;
					}
					qua_pre_score = Integer.valueOf(score);
					continue;
				} else {
					// 召回的品文本相关性要>=5
					if (!(TextRelation(score) >= 5)) {
						logger.error(" - [INSIDE] - " + " - The product text relative score is less than 5; product_id:" + product_id + " Score:" + score + " "
								+ PositionQul(i) + PositionNew(i));
						return false;
					}
					// score分倒序
					if (qua_pre_score >= Integer.valueOf(score)) {
						qua_pre_score = Integer.valueOf(score);
						continue;

						// 没有按照score分倒序
					} else {
						logger.error(" - [INSIDE] - " + " - Sort by score failed.pre_product:" + XMLParser.product_id(list.get(i - 1)) + " cur_product:"
								+ product_id + " " + PositionQul(i) + PositionNew(i));
						logger.error(" - [INSIDE] - " + " - pre_product score:" + qua_pre_score + " cur_product score:" + score);
						return false;
					}
				}
				// 如果不是指定商铺列表中的
			} else {
				// 记录分割位置
				if (split_pos == 0) {
					split_pos = i;
				}
				// 记录上一个品的score分
				qua_pre_score = -1;

				// 遍历搜索结果中所有的品
				for (int j = i; j < list.size(); j++) {
					// 得到品的商铺信息与分数
					String tmp_shop_id = XMLParser.product_shopID(list.get(j));
					String tmp_score = XMLParser.product_scope(list.get(j));
					String catepath2 = XMLParser.product_catelogPath(list.get(j));
//					int scoreInt = Integer.valueOf(tmp_score);
					// 分割点之后发现了非普通商家的品
					if (shoplist.contains(tmp_shop_id) && isaCategory(firstCategory, catepath2) && (TextRelation(tmp_score) >= 5)) {
						logger.error(" - [INSIDE] - " + "The product type list is discontinuity.Position:" + PositionQul(j) + PositionNew(j));
						logger.error(" - [INSIDE] - " + "The product__id:" + XMLParser.product_id(list.get(j)));
						return false;
					}
				}

				break;
			}
		}

		// logger.debug(" - [INSIDE] - "+"DoVerify Successed!");
		return true;
	}
	
	
	
	//TODO
	private static boolean isaCategory(String firstCategory, String catepath) {
		return catepath.equals(firstCategory);
	}

	public static String PositionQul(int index) {
		int p_q = index / 36 + 1;
		int i_q = temQulist.get(index % 36);
		String str = String.format("[Quality Page: %d + Index: %d]", p_q, i_q);
		return str;

	}

	public static String PositionNew(int index) {
		int p_q = index / 6 + 1;
		int i_q = temNelist.get(index % 6);
		String str = String.format("[New Page: %d + Index: %d]", p_q, i_q);
		return str;

	}

	public static String PositionCom(int index) {
		int p_q = index / 18 + 1;
		int i_q = temColist.get(index % 18);
		String str = String.format("[Com Page: %d + Index: %d]", p_q, i_q);
		return str;

	}

	public static boolean noProperProduct(List<Node> nList, List<String> sListQul, List<String> sListNew) {

		for (Node node : nList) {
			String shop_id = XMLParser.product_shopID(node);
			if (sListQul.contains(shop_id) || sListNew.contains(shop_id)) {
				continue;
			} else {
				String product_id = XMLParser.product_id(node);
				logger.error("ProperProduct--->  Common product  id:" + product_id + " Shop_id: " + shop_id);
				return false;
			}
		}
		return true;
	}

	/**
	 * 未进入新排序策略的，默认搜索结果按照综合分数排序 （只验证前5页）
	 * 
	 * @param iterator
	 * @return
	 */
	public static boolean SortScore(ProdIterator iterator) {
		// 保存上一个品的综合分数
		int pre_score = -1;
		String pre_product_id = null;
		// 遍历搜索结果
		while (iterator.hasNext() && iterator.getpageIndex() <= 5) {
			Node node = iterator.next();
			String score = XMLParser.product_scope(node);
			String cur_product_id = XMLParser.product_id(node);
			// 初始
			if (pre_score == -1) {
				pre_score = Integer.valueOf(score);
				pre_product_id = XMLParser.product_id(node);
				continue;
			} else {
				// 比较综合评分，降序展示
				if (pre_score >= Integer.valueOf(score)) {
					pre_score = Integer.valueOf(score);
					pre_product_id = XMLParser.product_id(node);
					return true;
				} else {
					logger.error(" - [OUTSIDE] - " + " - Sort by score failed.pre_product:" + pre_product_id + " cur_product:" + cur_product_id);
					logger.error(" - [OUTSIDE] - " + " - pre_product score:" + pre_score + " cur_product score:" + score);
					return false;
				}
			}
		}
		logger.debug(" - [OUTSIDE] - " + "Successed");
		return true;

	}

	/**
	 * 验证结果中去除单品置顶
	 * 
	 * @param list
	 * @return
	 */
	public static boolean rmTopProduct(List<Node> list) {
		// 前3个品
		for (int i = 0; i < 3; i++) {
			String score = XMLParser.product_scope(list.get(i));
			String pidString = XMLParser.product_id(list.get(i));
			int i_score = Integer.valueOf(score);
			String b_score = Integer.toBinaryString(i_score);
			int length = b_score.length();
			// 补齐31
			if (length < 31) {
				for (int j = 0; j < 31 - length; j++) {
					b_score = "0" + b_score;
				}
			}

			String topProduct = b_score.substring(0, 3);

			// 单品加权分为0
			if (!topProduct.equals("000")) {
				logger.error(" - [INSIDE] - " + " Top product is not removed! product_id:" + pidString + " score:" + score + " bscore:" + b_score + "/"
						+ topProduct);
				return false;
			}

		}

		return true;
	}

	/**
	 * 得到文本相关性得分
	 * 
	 * @param score
	 *            商品的score得分
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

	public static void main(String[] args) {
		FuncQuery fquery = new FuncQuery();
		fquery.setFquery("风衣 女 春秋");
		ClothBusinessSortVerifier aaa = new ClothBusinessSortVerifier();
		System.out.println(aaa.verify(fquery));

	}

}
