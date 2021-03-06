package com.dangdang.verifier.filter;

import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Node;

import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IFilterVerifier;

public class AttribVerifier extends IFilterVerifier {

	private static Logger logger = Logger.getLogger(AttribVerifier.class);

	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map, boolean hasResult) {
		String eattrib = ".*" + map.get("attrib") + ".*";
		if(!iterator.hasNext()){
			iterator.reSet();
		}
		while (iterator.hasNext()){
			Node prod = iterator.next();
			if (!isAttrib(prod, map, hasResult,eattrib)){
				return false;
			}
		}
		logger.debug(String.format(" -  [CHECK-PASS-INFO] check pass for 【attrib】 filte : %s", eattrib));
		return true;
	}
	
	public boolean isAttrib(Node prod ,Map<String,String> map,boolean hasResult,String eattrib){
		logger.debug("/****************************product**************************/");
		String pid = XMLParser.product_id(prod);
		String attrib = XMLParser.product_attrib(prod);
		logger.debug("/****************************end**************************/");
		if (hasResult && !attrib.matches(eattrib)){
			logger.error(String.format(" -  [CHECK-FAIL-INFO] %s : actual: %s; expect: %s", pid, attrib, eattrib));
			return false;
		}else if (!hasResult && attrib.matches(eattrib)){
			logger.error(String.format(" -  [CHECK-NO-RESULT] %s : actual: %s; expect: %s", pid, attrib, eattrib));
			return false;
		}
		return true;
	}

}
