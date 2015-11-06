package com.dangdang.verifier.iVerifier;

import java.util.Map;

import org.apache.log4j.Logger;

import com.dangdang.util.ProdIterator;

public interface ISortVerifier {
	
	public Logger logger = Logger.getLogger(ISortVerifier.class);

	public boolean Verifier(ProdIterator iterator,Map<String,String> map);
	
	public boolean doVerify(ProdIterator iterator,Map<String,String> map);  
	
	public boolean NumVerifier(int Count,int preCount);
	
}
