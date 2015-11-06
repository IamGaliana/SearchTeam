package com.dangdang.verifier.iVerifier;

import java.util.Map;

import org.apache.log4j.Logger;

import com.dangdang.client.URLBuilder;
import com.dangdang.util.ProdIterator;

public abstract class IFilterVerifier {

	public Logger logger = Logger.getLogger(IFilterVerifier.class);
			
	
	public boolean Verifier(ProdIterator iterator,Map<String,String> map){
		if (iterator.getTotalCount()<1){
			return this.noResultVerifier(URLBuilder.getDefaultIterator(map.get("query")), map);
		}
		int preCount = Integer.valueOf(map.get("totalCount"));
		int Count = iterator.getTotalCount();
		logger.fatal(String.format(" -  [CHECK-HAS-RESULT] check has result"));
		return this.NumVerifier(Count, preCount) && this.doVerify(iterator, map, true);
	};
	
	public boolean noResultVerifier(ProdIterator iterator,Map<String,String> map){
		logger.fatal(String.format(" -  [CHECK-NO-RESULT] check no result"));
		return this.doVerify(iterator, map, false);
	};
	
	public boolean NumVerifier(int Count,int preCount){
		if(Count>preCount){
			logger.error(String.format(" -  [CHECK-INFO] check fail for product num, Count: %s, preCount: %s", Count, preCount));
			return false;
		}
		return true;
	};

	public abstract boolean doVerify(ProdIterator iterator, Map<String, String> map,boolean hasResult);
}
