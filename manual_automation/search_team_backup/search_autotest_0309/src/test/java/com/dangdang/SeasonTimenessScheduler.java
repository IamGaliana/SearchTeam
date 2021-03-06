package com.dangdang;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.data.FuncQuery;
import com.dangdang.verifier.season.SeasonTimenessVerifier;



public class SeasonTimenessScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SeasonTimenessScheduler.class);
	private static Map<Integer, Integer> result = new HashMap<Integer,Integer>();
	public static int count = 0;
	@BeforeClass
	public void setup() {  		
	}  

	@AfterClass 
	public void clearup() { 
		logger.debug("********************"+count+"**********************");
		double Spring_1 = getresult(11); 
		logger.debug("* 第一页 春 比例:"+Spring_1+"% *");
		double Summer_1 = getresult(12); 
		logger.debug("* 第一页 夏 比例:"+Summer_1+"% *");
		double Autumn_1 = getresult(13); 
		logger.debug("* 第一页 秋 比例:"+Autumn_1+"% *");
		double Winter_1 = getresult(14); 
		logger.debug("* 第一页 冬 比例:"+Winter_1+"% *");
		logger.debug("************************************************");
		logger.debug("************************************************");
		double Spring_2 = getresult(21); 
		logger.debug("* 第二页 春 比例:"+Spring_2+"% *");
		double Summer_2 = getresult(22); 
		logger.debug("* 第二页 夏 比例:"+Summer_2+"% *");
		double Autumn_2 = getresult(23); 
		logger.debug("* 第二页 秋 比例:"+Autumn_2+"% *");
		double Winter_2 = getresult(24); 
		logger.debug("* 第二页 冬 比例:"+Winter_2+"% *");
		logger.debug("************************************************");
		logger.debug("************************************************");
		double Spring_3 = getresult(31); 
		logger.debug("* 第三页 春 比例:"+Spring_3+"% *");
		double Summer_3 = getresult(32); 
		logger.debug("* 第三页 夏 比例:"+Summer_3+"% *");
		double Autumn_3 = getresult(33); 
		logger.debug("* 第三页 秋 比例:"+Autumn_3+"% *");
		double Winter_3 = getresult(34); 
		logger.debug("* 第三页 冬 比例:"+Winter_3+"% *");
		logger.debug("************************************************");
		logger.debug("************************************************");
		double Spring_4 = getresult(41); 
		logger.debug("* 第四页 春 比例:"+Spring_4+"% *");
		double Summer_4 = getresult(42); 
		logger.debug("* 第四页 夏 比例:"+Summer_4+"% *");
		double Autumn_4 = getresult(43); 
		logger.debug("* 第四页 秋 比例:"+Autumn_4+"% *");
		double Winter_4 = getresult(44); 
		logger.debug("* 第四页 冬 比例:"+Winter_4+"% *");
		logger.debug("************************************************");
		logger.debug("************************************************");
		double Spring_5 = getresult(51); 
		logger.debug("* 第五页 春 比例:"+Spring_5+"% *");
		double Summer_5 = getresult(52); 
		logger.debug("* 第五页 夏 比例:"+Summer_5+"% *");
		double Autumn_5 = getresult(53); 
		logger.debug("* 第五页 秋 比例:"+Autumn_5+"% *");
		double Winter_5 = getresult(54); 
		logger.debug("* 第五页 冬 比例:"+Winter_5+"% *");
		logger.debug("************************************************");
	}   
	
	//季节时效性
	@Test(enabled=false)  
	public void SeasonTimenessTest_01() {  
		long d = System.currentTimeMillis() ;
		logger.info(Long.toString(d));			
//		DBAction dba = new DBAction();
//		dba.setFuncCondition("verify_point='season_timeness' and id MOD 8=0");
//		List<FuncQuery> list = dba.getFuncQuery();
		List<FuncQuery> list = new ArrayList<FuncQuery>();
		FuncQuery query = new FuncQuery();
		query.setFquery("女装");
		query.setVerify_point("season_timeness");
		list.add(query);
		for(FuncQuery fquery:list){
			Map<Integer, Integer> tmp_resultMap = SeasonTimenessVerifier.verify(fquery);
			if(tmp_resultMap.size()>0){
				result = mergeMap(result,tmp_resultMap);
				count++;
			}
		}
	}
	
	@Test(enabled=false)  
	public void SeasonTimenessTest_02() { 
		long d = System.currentTimeMillis() ;
		logger.info(Long.toString(d));			
		DBAction dba = new DBAction();
		dba.setFuncCondition("verify_point='season_timeness' and id MOD 8=1");
		List<FuncQuery> list = dba.getFuncQuery();
		for(FuncQuery fquery:list){
			Map<Integer, Integer> tmp_resultMap = SeasonTimenessVerifier.verify(fquery);
			if(tmp_resultMap.size()>0){
				result = mergeMap(result,tmp_resultMap);
				count++;
			}
		}
	} 
	
	@Test(enabled=false)  
	public void SeasonTimenessTest_03() { 
		long d = System.currentTimeMillis() ;
		logger.info(Long.toString(d));			
		DBAction dba = new DBAction();
		dba.setFuncCondition("verify_point='season_timeness' and id MOD 8=2");
		List<FuncQuery> list = dba.getFuncQuery();
		for(FuncQuery fquery:list){
			Map<Integer, Integer> tmp_resultMap = SeasonTimenessVerifier.verify(fquery);
			if(tmp_resultMap.size()>0){
				result = mergeMap(result,tmp_resultMap);
				count++;
			}	
		}
	} 
	
	@Test(enabled=false)  
	public void SeasonTimenessTest_04() { 
		long d = System.currentTimeMillis() ;
		logger.info(Long.toString(d));			
		DBAction dba = new DBAction();
		dba.setFuncCondition("verify_point='season_timeness' and id MOD 8=3");
		List<FuncQuery> list = dba.getFuncQuery();
		for(FuncQuery fquery:list){
			Map<Integer, Integer> tmp_resultMap = SeasonTimenessVerifier.verify(fquery);
			if(tmp_resultMap.size()>0){
				result = mergeMap(result,tmp_resultMap);
				count++;
			}	
		}
	} 
	
	public static Map<Integer, Integer> mergeMap(Map<Integer, Integer> map1,Map<Integer, Integer> map2){
		for(Map.Entry<Integer, Integer> entry : map2.entrySet()){
			int key = entry.getKey();
			int value = entry.getValue();
			if(map1.containsKey(key)){
				 int tmp_val = map1.get(key);
				 map1.put(key, tmp_val+value);
			}else{
				map1.put(key, value);
			}
		}
		return map1;
	}
	
	public double getresult(int index){
		if(result.containsKey(index)){
			return result.get(index)*100/(count*60.00);
		}else{
			return 0.0;
		}
		
		
	}
	
}
