package com.dangdang.verifier.cateforecast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.client.RPCClient;
import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.Config;
import com.dangdang.util.XMLParser;


public class CategoryForecastVerifier {

	//日志记录
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryForecastVerifier.class);
	
	
	
	/**
	 * 验证Query中带有标点符号的情况，预测分类是否一致
	 * @param query     原始的Query词
	 * @param isSearchBG     是否通过搜索后台验证（置否的话会通过开发的接口进行验证，默认为：http://192.168.85.133:12345/query？key=xxxx）
	 * @return  
	 */
	public static boolean Verify(FuncQuery query,boolean isSearchBG){
		//得到Query词
		String sourceQuery = query.getFquery();
		
		//是否搜索后台验证
		if(isSearchBG){
			//想原始的query词中插入标点符号，位置随机
			String [] targetQuerys = InsertSymbol(sourceQuery);
			//构造url的参数map
			Map<String,String> urlMap = new HashMap<String,String>();
			urlMap.put("st", "full");
			urlMap.put("um", "search_ranking");
//			urlMap.put("gp", "cat_paths");   //必须
			urlMap.put("_new_tpl", "1");
			urlMap.put("q",sourceQuery);
			
			//访问后台，得到返回的xml结果
			String url = URLBuilder.buildUrl(urlMap);
			String xml = SearchRequester.get(url);
			
			//得到结果中返回的分类预测列表
			List<String> sourceCateList = getCateList(xml);
			
			//循环生成的包含标点符号的query,得到各自的分类预测结果与原始的进行比较
			for(String targetQuery:targetQuerys){
				urlMap.put("q", targetQuery);
				String url_temp = URLBuilder.buildUrl(urlMap);
				String xml_temp = SearchRequester.get(url_temp);
				List<String> tempCateList = getCateList(xml_temp);
				
				//得到的分类数量不相同
				if(sourceCateList.size()!=tempCateList.size()){
					logger.error(" - [LOG_FAILED] - "+"Category forecast number is not same! Source:"+sourceCateList.size()+" Target:"+tempCateList.size());
					logger.error(" - Source Query:"+sourceQuery+" Target Query:"+targetQuery);
					return false;
				}
				
				
				for(int i=0;i<sourceCateList.size();i++){
					//分类的顺序不同
					if(!sourceCateList.get(i).equals(tempCateList.get(i))){
						logger.error(" - [LOG_FAILED] - "+"Category forecast is not same! Pos:"+i+" Source:"+sourceCateList.get(i)+" Target:"+tempCateList.get(i));
						logger.error("Source Query:"+sourceQuery+" Target Query:"+targetQuery);
						return false;
					}
				}
			}
			
			
			//通过开发接口进行验证
		}else{
			//保存分类预测的结果。  分类路径+分类权重
			Map<String,Double> sourceCatePath = RPCClient.getCategoryPriority(sourceQuery);
			//插入标点符号
			String [] targetQuerys = InsertSymbol(sourceQuery);
			
			
			for(String targetQuery : targetQuerys){
				Map<String,Double> targetCatePath  = RPCClient.getCategoryPriority(targetQuery);
				//比较分类预测结果
				if(!MapCompare(sourceCatePath,targetCatePath)){
					logger.error("Source Query:"+sourceQuery+" Target Query:"+targetQuery);
				}
				
			}
		}
		logger.debug(" - [LOG_SUCCESS] - "+"Test Successed! Query:"+sourceQuery);
		return true;
	}
	
//	public static boolean doVerify(String sourceQuery,String targetQuery){
//		
//	}
	
	/**
	 * 比较分类预测结果的2个map
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean MapCompare(Map<String,Double> source,Map<String,Double> target){
		
		//遍历
		for(String key:source.keySet()){
			String sourceValue = source.get(key).toString();
			//是否分类都相同
			if(!target.containsKey(key)){
				logger.error(" - [LOG_FAILED] - "+"The category forecast is not same! The catepath is not contained:"+key);
//				logger.error("Source Query:"+sourceQuery+" Target Query:"+targetQuery);
				return false;
			}
			String targetValue = target.get(key).toString();
			//是否分类的权重都相同
			if(!sourceValue.equals(targetValue)){
				logger.error(" - [LOG_FAILED] - "+"The category weight is not same! Source Weight:"+sourceValue+" Target Weight:"+targetValue);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 得到从搜索结果的xml中得到分类预测的分类列表
	 * @param xml   搜索结果
	 * @return
	 */
	public static List<String> getCateList(String xml){
		try {
			Document doc = XMLParser.read(xml);
			List<String> result = new ArrayList<String>();
			List<Node> categorys = XMLParser.cateInfo(doc);
			for(Node node: categorys){
				result.add(XMLParser.catePath(node));
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/**
	 * 向Query中随机位置上插入标点符号
	 * @param sourceQuery
	 * @return
	 */
	public static String [] InsertSymbol(String sourceQuery){
		Config config = new Config();
		String symbolSetting = config.get_SymbolSetting();
		String [] symbols = symbolSetting.split(",");
		String [] targetQuerys = new String[symbols.length];
		
		for(int i=0;i<symbols.length;i++){
			switch(symbols[i]){
			//空格
			case "\\0":
				symbols[i]=" ";
				break;
			}
			Date d = new Date();
			Random ran = new Random(d.getTime());
			int pos  = ran.nextInt(sourceQuery.length());
			String targetQuery = StringUtils.substring(sourceQuery, 0, pos)+symbols[i]+StringUtils.substring(sourceQuery, pos, sourceQuery.length());
			targetQuerys[i]=targetQuery;
		}
		
		return targetQuerys;
	}
	
	public static void main(String[] args) {
		FuncQuery fquery = new FuncQuery();
		fquery.setFquery("手机");
		System.out.println(Verify(fquery,true));
	}

}
