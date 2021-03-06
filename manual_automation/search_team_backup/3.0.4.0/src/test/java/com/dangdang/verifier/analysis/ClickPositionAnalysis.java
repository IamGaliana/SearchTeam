package com.dangdang.verifier.analysis;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.client.DBAction;
import com.dangdang.data.SearchLog;
import com.dangdang.util.Config;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

public class ClickPositionAnalysis {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ClickPositionAnalysis.class);
	public static List<Map<Integer, Integer>> analysis = new ArrayList<Map<Integer, Integer>>();
	public static Map<Integer, Integer> preResult = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> actResult = new HashMap<Integer, Integer>();

	public static double Verify(String Query) {

		// 得到Query对应的一些统计信息
		DBAction action = new DBAction();
		action.setSl_condition("Query ='" + Query + "'");
		List<SearchLog> list = action.getSearchLogRecord();
		// 用来保存从统计信息中分析出来的搜索信息，例如对应prod_id的商品id,被点击次数等
		Map<String, SearchLog> preMap = new HashMap<String, SearchLog>();
		// 用来保存prod_id
		List<String> productids = new ArrayList<String>();
		// 遍历对应Query多有的信息，合并多次记录的结果
		for (SearchLog log : list) {
			String prod_id = log.getPid();
			if (preMap.containsKey(prod_id)) {
				// 合并相同Query下，相同Product_id的信息
				log.setClickCount(String.valueOf(Integer.valueOf(preMap.get(prod_id).getClickCount()) + Integer.valueOf(log.getClickCount())));

				preMap.put(prod_id, log);
			} else {
				preMap.put(prod_id, log);
				productids.add(prod_id);
			}
		}
		// url生成的键值对
		Map<String, String> urlMap = new HashMap<String, String>();
		try {
			urlMap.put("q", URLEncoder.encode(Query, "GBK"));
			urlMap.put("st", "full");
			urlMap.put("um", "search_ranking");
			urlMap.put("_new_tpl", "1");//search_ranking必加
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		}
		// 得到当次索引更新后，对应品id的位置变化
		Map<String, String> actMap = doVerify(productids, urlMap);
		analysis = doAnalysis(preMap, actMap, "BH_TEMPLATE");
		// 点击位置变化计算
		return doCalculate(preMap, actMap, Query);
	}

	/**
	 * 位置分析
	 * 
	 * @param preMap
	 *            日志中的位置统计信息
	 * @param actMap
	 *            实际结果中的统计信息
	 * @param template
	 *            搜索的什么模板（服装，百货）
	 * @return
	 */
	public static List<Map<Integer, Integer>> doAnalysis(Map<String, SearchLog> preMap, Map<String, String> actMap/*
																												 * ,
																												 * String
																												 * query
																												 */, String template) {
		// 保存结果
		List<Map<Integer, Integer>> result = new ArrayList<Map<Integer, Integer>>();
		// 读取配置
		Config config = new Config();
		// 得到分类字串
		String scope = null;

		if (template.equals("BH_TEMPLATE")) {
			scope = config.get_BHAnaScope();
		} else {
			scope = config.get_PUBAnaScope();
		}

		// 分割成位置区间
		String[] scopes = scope.split(",");
		int[] iscopes = new int[scopes.length];
		for (int i = 0; i < scopes.length; i++) {
			iscopes[i] = Integer.valueOf(scopes[i]);
		}

		// 遍历统计的结果
		for (Map.Entry<String, SearchLog> entry : preMap.entrySet()) {

			// 得到点击信息。被点击的品id，点击次数，点击位置，实际点击位置等
			String pid = entry.getKey();
			SearchLog log = entry.getValue();
			int cc = Integer.valueOf(log.getClickCount());
			int position = Integer.valueOf(log.getPosition());
			String actPosition = (actPosition = actMap.get(pid)) != null ? actPosition : "0";
			int actPos = Integer.valueOf(actPosition);
			// 为点击位置分区统计次数
			for (int j = iscopes.length - 1; j >= 0; j--) {
				if (iscopes[j] < position) {
					if (preResult.containsKey(j)) {
						preResult.put(j, preResult.get(j) + cc);
					} else {
						preResult.put(j, cc);
					}

					break;
				}
			}
			// 为实际点击位置分区统计次数
			for (int k = iscopes.length - 1; k >= 0; k--) {
				if (iscopes[k] < actPos) {
					if (actResult.containsKey(k)) {
						actResult.put(k, actResult.get(k) + cc);
					} else {
						actResult.put(k, cc);
					}
					break;
				}
			}

		}
		result.add(preResult);
		result.add(actResult);
		return result;

	}

	/**
	 * 计算实际结果中，对应product_id的位置
	 * 
	 * @param pids
	 *            商品id
	 * @param map
	 *            保存商品id与对应的位置
	 * @return
	 */
	public static Map<String, String> doVerify(List<String> pids, Map<String, String> map) {
		Map<String, String> result = new HashMap<String, String>();
		ProdIterator iterator = new ProdIterator(map);
		while (iterator.hasNext()) {
			Node node = iterator.next();
			String prod_id = XMLParser.product_id(node);
			// 不是要要查的品，跳过
			if (!pids.contains(prod_id)) {
				continue;
			}
			// 保存品信息到结果map中
			result.put(prod_id, String.valueOf((iterator.getpageIndex() - 1) * iterator.getPageSize() + iterator.getPoint() + 1));

			// 如果已经找到所有的品后，退出查询
			if (result.size() == pids.size()) {
				break;
			}
		}
		return result;

	}

	/**
	 * 计算加权位置变化的值
	 * 
	 * @param preMap
	 *            原始位置信息
	 * @param actMap
	 *            实际位置信息
	 * @param query
	 *            对应的query
	 * @return
	 */
	public static double doCalculate(Map<String, SearchLog> preMap, Map<String, String> actMap, String query) {
		double sum = 0.0; // 用来计算加权位置变化量
		double sumClick = 0.0; // 用于计算平均位置变化量
		for (Map.Entry<String, SearchLog> entry : preMap.entrySet()) {
			String pid = entry.getKey();
			SearchLog log = entry.getValue();
			String cc = log.getClickCount();
			String position = log.getPosition();
			String actPosition = (actPosition = actMap.get(pid)) != null ? actPosition : "0";
			// 加权位置变化 = 点击次数×位置变化量
			sum = Integer.valueOf(cc) * Integer.valueOf(position) - Integer.valueOf(cc) * Integer.valueOf(actPosition);
			// 累加加权，最终按照比重进行计算平均位置变化
			sumClick = sumClick + Double.valueOf(cc);
			logger.debug(" - [LOG_SINGLERESULT] - " + "Query:" + query + " Product_id:" + pid + " ClickCount:" + cc + " PrePosition:" + position
					+ " ActPosition:" + actPosition);
		}
		sum = sum / sumClick;

		return sum;

	}
}
