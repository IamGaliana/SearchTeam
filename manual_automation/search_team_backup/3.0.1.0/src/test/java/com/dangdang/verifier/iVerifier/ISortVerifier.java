package com.dangdang.verifier.iVerifier;

import java.util.Map;

import org.slf4j.LoggerFactory;

import com.dangdang.util.ProdIterator;

public interface ISortVerifier {
	
	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(ISortVerifier.class);

	public boolean Verifier(ProdIterator iterator,Map<String,String> map);
	
	public boolean doVerify(ProdIterator iterator,Map<String,String> map);  
	
	public boolean NumVerifier(int Count,int preCount);
	
}