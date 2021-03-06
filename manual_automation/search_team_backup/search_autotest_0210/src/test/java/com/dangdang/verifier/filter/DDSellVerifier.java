package com.dangdang.verifier.filter;

import java.util.Map;

import org.dom4j.Node;


import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IFilterVerifier;

public class DDSellVerifier extends IFilterVerifier {
	
	
	
	


	@Override
	public boolean NumVerifier(int Count, int preCount) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map, boolean hasResult) {
		if(!iterator.hasNext()){
			iterator.reSet();
		}
		while(iterator.hasNext()){
			Node node = iterator.next();
			if(!isDDSell(node,hasResult)){
				return false;
			}
		}
		return  true;
	}
	
	public boolean isDDSell(Node node,boolean hasResult){
		logger.debug("/****************************product**************************/");
		//商品数据类
		Product product = new Product();

		// 对product赋值
		product.setProduct_id(XMLParser.product_id(node));
		product.setShop_id(XMLParser.product_shopID(node));
		
		if(hasResult && !product.getShop_id().equals("0")){
			logger.error(" - [DDSELL] - "+product.getProduct_id()
					+ " : it is not sale by dangdang. Shop id is "
					+ product.getShop_id());
			logger.debug("/****************************end**************************/");
			return false;
			
			
		}else if(!hasResult && product.getShop_id().equals("0")){
			logger.error(" - [DDSELL-NORESULT] - "+product.getProduct_id()
					+ " : it is saled by dangdang. Shop id is "
					+ product.getShop_id());
			logger.debug("/****************************end**************************/");
			return false;
		}
		return true;
	}
	
}
