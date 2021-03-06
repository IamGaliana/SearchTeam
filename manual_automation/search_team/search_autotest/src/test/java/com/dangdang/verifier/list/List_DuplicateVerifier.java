package com.dangdang.verifier.list;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2014年11月3日 下午3:44:44  
 * 类说明    分类页查重，逻辑等同搜索页查重
 */
public class List_DuplicateVerifier {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(List_DuplicateVerifier.class);
	
	public boolean Verifier(ProdIterator iterator, Map<String, String> map) {
		Map<String,Integer> pidmap = new HashMap<String,Integer>();
		String serverity= "";
		while(iterator.hasNext()){
			String prod_id = XMLParser.product_id(iterator.next());
			if(pidmap.keySet().contains(prod_id)){
				int fpage = pidmap.get(prod_id);
				int spage = iterator.getpageIndex();
				if(fpage==spage && fpage<10){
					serverity="level1";
				}else if(spage-fpage<2 && fpage<10){
					serverity="level2";
				}else if (fpage-fpage<5 && fpage <10){
					serverity="level3";
				}else{
					serverity="level4";
				}
				logger.error(" - [DUPLICATE] - ["+serverity+"] - "+"Porduct id:"+prod_id+" Page:"+fpage+" and "+spage);
				if(spage<5){
					logger.error(" - [LOG_FAILED] - ["+serverity+"] - "+"Duplicate product is found in search result.");
					return false;
				}
			}else{
				pidmap.put(prod_id, iterator.getpageIndex());
			}
		}
		
		
		
		
		
		
		
		
		
		return true;
	}
	
	

}
