package com.dangdang.verifier.iVerifier;

import java.util.Map;

import com.dangdang.util.ProdIterator;

public interface IgSearchVerifier {
	public boolean Verifier(ProdIterator iterator, Map<String, String> map, String[] ta);
	public Map<String, String> getSearchParamters(ProdIterator iterator, int flag);
}
