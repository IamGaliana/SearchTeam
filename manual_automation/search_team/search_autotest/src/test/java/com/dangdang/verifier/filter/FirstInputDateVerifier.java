package com.dangdang.verifier.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.sql.Timestamp;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IFilterVerifier;

/**   
 * @author gaoyanjun@dangdang.com  
 * @version 创建时间：2015/07/20 
 * 类说明   按照上架时间筛选
 */
public class FirstInputDateVerifier extends IFilterVerifier {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(FirstInputDateVerifier.class);	
	
	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map, boolean hasResult) {
		if(!iterator.hasNext()){
			iterator.reSet();
		}
		
		// 获取url中的first_input_date筛选值，参数格式为&-first_input_date=开始时间戳~结束时间戳
		String[] timearray = null;
		int lowerTime = 0;
		int higherTime = 0;
		//String stampScope = map.get("-first_input_date");
		String stampScope = "1262275200~1483200000"; // hard coded, this value must be the same as value in method URLBuiler.converURLPars
		
		if(!stampScope.isEmpty()){
			timearray = stampScope.split("~");
			if (timearray.length == 2){
				lowerTime = (!timearray[0].isEmpty()) ? Integer.parseInt(timearray[0]) : 0;
				higherTime = (!timearray[1].isEmpty()) ? Integer.parseInt(timearray[1]) : 0;
			}
		}
		
		while (iterator.hasNext()){
			Node node = iterator.next();
			if (!isCorrectInputDate(node, map, hasResult,lowerTime, higherTime)){
				return false;
			}
		}
		logger.debug(String.format(" -  [LOG_PASS] check pass for 【first_input_date】 filter"));
		return true;		
	}
	
	/**
	 * 验证以下两点：
	 * 有结果时，上架时间是否在筛选条件范围内
	 * 无结果时，是否每个单品的上架时间都不在筛选条件范围内
	 */
	public boolean isCorrectInputDate(Node node, Map<String, String> map,boolean hasResult, int lowertime, int highertime) {			
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		String product_id = XMLParser.product_id(node);
		String first_input_date = XMLParser.product_firstinputDate(node);
		Date date = null;
		int timeStamp = (int)((Timestamp.valueOf(first_input_date).getTime())/1000);				
		if (hasResult && (timeStamp < lowertime | timeStamp > highertime)){		
			logger.error(String.format(" -  [LOG_FAILED] %s : actual: %s; expect: %s",  product_id, timeStamp,lowertime + "~" + highertime));
			return false;
		}else if (!hasResult && (timeStamp >= lowertime && timeStamp <= highertime)){
			logger.error(String.format(" -  [LOG_FAILED] %s : expeted no result, actual: %s; expect: %s",  product_id, timeStamp,lowertime + "~" + highertime));
			return false;
		}
		return true;
	}
}