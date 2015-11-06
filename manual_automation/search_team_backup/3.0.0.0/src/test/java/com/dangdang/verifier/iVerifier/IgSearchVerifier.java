package com.dangdang.verifier.iVerifier;

import java.util.Map;

import org.slf4j.LoggerFactory;

import com.dangdang.util.ProdIterator;

public interface IgSearchVerifier {
	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(IgSearchVerifier.class);
	public boolean Verifier(ProdIterator iterator, Map<String, String> map, String[] ta);
	public Map<String, String> getSearchParamters(ProdIterator iterator, int flag);
}
