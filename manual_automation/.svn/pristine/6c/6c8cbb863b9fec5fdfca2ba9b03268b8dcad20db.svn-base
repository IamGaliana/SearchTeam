package com.dangdang.verifier.filter;

import java.util.Map;

import org.dom4j.Node;

import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IFilterVerifier;

public class AttribVerifier extends IFilterVerifier {

	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map, boolean hasResult) {
		// 验证单品attrib中是否包含如下格式的属性值，完全匹配15553:1，排除15553:12、15553:13等格式的值
		String eattrib = ".*" + map.get("attrib") + "[^0-9].*";
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
