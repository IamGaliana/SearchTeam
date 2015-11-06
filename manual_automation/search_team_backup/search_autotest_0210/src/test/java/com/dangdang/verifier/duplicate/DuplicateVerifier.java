package com.dangdang.verifier.duplicate;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

public class DuplicateVerifier {
	public static Logger logger = Logger.getLogger(DuplicateVerifier.class);
	
	public static boolean doVerifier(ProdIterator iterator){
		Map<String,Integer> map = new HashMap<String,Integer>();
		String serverity= "";
		while(iterator.hasNext()){
			String prod_id = XMLParser.product_id(iterator.next());
			if(map.keySet().contains(prod_id)){
				int fpage = map.get(prod_id);
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
				map.put(prod_id, iterator.getpageIndex());
			}
		}
		return true;
	}
}
