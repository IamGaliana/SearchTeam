package com.dangdang.verifier.blacklist;


import java.util.HashMap;
import java.util.Map;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**
 * 对单品置顶黑名单功能进行验证
 * 
 * @author zhangxiaojing
 * 
 */
public class TopProductBlackListVerifier{

	// 日志记录器
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(TopProductBlackListVerifier.class);
	
	public static boolean doVerify(FuncQuery query) {

		Map<String, String> urlp = URLBuilder.converURLPars("", query.getFquery(),null);
		logger.debug(" - SearchInfo: " + urlp.toString());
		ProdIterator iterator = new ProdIterator(urlp,3000);	
		
		Map<String,Integer> positionMap = new HashMap<String,Integer>();
		
		while(iterator.hasNext()){
			Node prod = iterator.next();
			String pid = XMLParser.product_id(prod);
			String catPaths = XMLParser.product_catelogPath(prod);
			//获取单品反馈得分
			String score = XMLParser.product_score(prod);
			String feedbackScore = getScore(score);
			int positon = iterator.getPoint()+1;
			
			//验证招商图书商品单品反馈分为0
			if(!XMLParser.product_shopID(prod).equals("0")&&catPaths.startsWith("01")){
				//验证黑名单商品的单品反馈得分为0(从高位向低位数0到2位)
				if(feedbackScore.length()==31){
					if(!feedbackScore.substring(0, 3).equals("000")){
						logger.error(String.format("query: %s,the product feedback score of merchants book product %s is not 0!", query.getFquery(), pid));
						return false;
					}
				}
			}else{
				//验证黑名单分类下的商品单品反馈得分为0
				String[] blackCatPath = new String[]{"01.43.00.00.00.00", "01.45.00.00.00.00", "01.47.00.00.00.00","01.49.00.00.00.00","01.50.00.00.00.00"}; 
				for(String path:blackCatPath){
					if(catPaths.length()>0){
						if(catPaths.substring(0, 5).equals(path.substring(0, 5))){
							//验证黑名单商品的单品反馈得分为0(从高位向低位数0到2位)
							if(feedbackScore.length()==31){
								if(!feedbackScore.substring(0, 3).equals("000")){
									logger.error(String.format("query: %s,the product feedback score of top black product %s is not 0!", query.getFquery(), pid));
									return false;
								}
							}

							//取第一个黑名单商品位置
							if(positionMap.size()==0){
								positionMap.put(pid, positon);
								logger.info(pid+" "+positon);
							}
						}
					}
				}
			}
		}
		
		//计算query黑名单商品平均位置
		double avgPositon = 0;
		if(positionMap.size()==1&&iterator.getTotalCount()>0){
			avgPositon = Double.parseDouble(positionMap.get(positionMap.keySet().iterator().next()).toString())/iterator.getTotalCount();
		}
		if(positionMap.size()==1&&avgPositon<0.5){//设置阈值
			logger.error(String.format("query: %s, the average postion of top black product %s is %s!", query.getFquery(), 
					positionMap.keySet().iterator().next(),avgPositon));
			//return false;
		}
		return true;
	}
	
	private static String getScore(String score){
		//不满31位，高位补零
		score = Integer.toBinaryString(Integer.parseInt(score));
		int length = score.length();
		if(score.length()<31){
			for(int i=0;i<31-length;i++){
				score="0"+score;
			}
		}
		return score;
	}		 
}