package com.dangdang.verifier.sort;

import java.util.Map;

import org.dom4j.Node;

import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.ISortVerifier;

public class CommentDescVerifier implements ISortVerifier {

	@Override
	public boolean Verifier(ProdIterator iterator, Map<String, String> map) {
		// 上一个商品
		Product pre_Product = null;
		Product pre_Product_reco = null;
		//暂时不验总商品数量
//		int totalCount = iterator.getTotalCount();
//		int preTotalCount = Integer.valueOf(map.get("totalCount"));
//		if (!NumVerifier(totalCount, preTotalCount)) {
//			return false;
//		}
		
		// 循环所有的商品节点。比较两个相邻的商品是否符合相应规则。（按评论得分递减排序）
		while (iterator.hasNext()) {
			logger.debug("/****************************product**************************/");
			Node subNode = iterator.next();
			// 为商品赋值
			Product product = new Product();
			product.setProduct_id(XMLParser.product_id(subNode));
			product.setTotal_review_count(XMLParser
					.product_total_review_count(subNode));
			product.setScore(XMLParser.product_score(subNode));
			product.setStype(XMLParser.product_stype(subNode));
			if(product.getStype().equals("")){
				// 判断当前商品是否为第一个
				if (pre_Product == null) {
					pre_Product = product;

				} else {
					// 比较商品的评论得分
					Double preScore = getFinalScore(
							pre_Product.getTotal_review_count(),
							pre_Product.getScore());
					Double score = getFinalScore(product.getTotal_review_count(),
							product.getScore());
					if (preScore >= score) {
						pre_Product = product;

					} else {
						logger.error(" - [COMMDESC] - " + "pre_product_id:"
								+ pre_Product.getProduct_id() + ";" + "preScore:"
								+ preScore);
						logger.error(" - [COMMDESC] - " + "product_id:"
								+ product.getProduct_id() + ";" + "score:" + score);
						return false;

					}
				}
			}else if(product.getStype().equals("reco")){//推荐品排序
				// 判断当前商品是否为第一个
				if (pre_Product_reco == null) {
					pre_Product_reco = product;

				} else {
					// 比较商品的评论得分
					Double preScore = getFinalScore(
							pre_Product_reco.getTotal_review_count(),
							pre_Product_reco.getScore());
					Double score = getFinalScore(product.getTotal_review_count(),
							product.getScore());
					if (preScore >= score) {
						pre_Product_reco = product;

					} else {
						logger.error(" - [COMMDESC] - " + "pre_product_id:"
								+ pre_Product_reco.getProduct_id() + ";" + "preScore:"
								+ preScore);
						logger.error(" - [COMMDESC] - " + "product_id:"
								+ product.getProduct_id() + ";" + "score:" + score);
						return false;

					}
				}
			}

			logger.debug("/****************************end**************************/");
		}
		return true;

	}

	private Double getFinalScore(String comment,String score){
		if(comment.equals("")){
			comment="0";
		}
		if(score.equals("")){
			score="0";
		}

		//计算公式：最终得分=(平均评论数*平均得分+实际评论数*实际得分)/(平均评论数+实际评论数)，其中 平均评论数=30  平均得分=3.5
		Double finalScore = (30*3.5+Double.valueOf(comment)*Double.valueOf(score))/(30+Double.valueOf(comment));
		return finalScore;
	}

	@Override
	public boolean NumVerifier(int Count, int preCount) {
		// 本次结果中商品数量比预查询中的结果数量大
		if (Count != preCount) {
			logger.error(" - [COMMDESC] - "
					+ "The total count different with the total count of the result before it was sorted by comments desc!");
			logger.error(" - [COMMDESC] - " + "Total Count:" + Count
					+ " Pre Total Count:" + preCount);
			return false;
		} else {
			logger.debug("Correct!");
			return true;
		}
	}

	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map) {
		// TODO Auto-generated method stub
		return false;
	}

//	public static void main(String[] args) {
//		PropertyConfigurator.configure("conf/log4j.properties");
//		Map<String,String> urlmap = URLBuilder.getMap("comment", "连衣裙");
//		Map<String,String> urlmap2 = URLBuilder.getMap("", "连衣裙");
//		Map<String,String> argsmap = new HashMap<String, String>();
//		argsmap.put("outlets","1");
//		argsmap.put("TotalCnt", "34541");
//		ProdIterator iterator = new ProdIterator(urlmap);
//		ProdIterator iterator2 = new ProdIterator(urlmap2);
//		CommentDescVerifier com = new CommentDescVerifier();
//		System.out.println(com.Verifier(iterator, argsmap));
//	}
}
