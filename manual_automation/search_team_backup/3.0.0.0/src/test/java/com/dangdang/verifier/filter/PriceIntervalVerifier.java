package com.dangdang.verifier.filter;

import java.util.Map;

import org.dom4j.Node;

import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.IFilterVerifier;

public class PriceIntervalVerifier extends IFilterVerifier
{

	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map, boolean hasResult) {
		if(!iterator.hasNext()){
			iterator.reSet();
		}
		while(iterator.hasNext()){

			Node node = iterator.next();
			if(!isInterval(node, hasResult, map)){
				return false;
			}
		}
		return true;
	}
	public boolean isInterval(Node node,boolean hasResult,Map<String,String> map){
	

		// 商品赋值
		Product product = new Product();
		product.setProduct_id(XMLParser.product_id(node));
		product.setDd_sale_price(XMLParser.product_dd_sale_price(node));
		Double price = Double.valueOf(product.getDd_sale_price());
//		System.out.println(map.get("priceInterval"));
		Double min = Double.valueOf(map.get("priceInterval").split("~")[0]);
		Double max = Double.valueOf(map.get("priceInterval").split("~")[1]);
		if(hasResult && !(price<=max && price >= min)){
			logger.error(" - [PRICEINT] - "+"This product's price is not in the price interval;product price = "+price);
			logger.error(" - [PRICEINT] - "+"min price = "+min+" max price = "+max);
			return false;
		}else if(!hasResult && (price<=max && price >= min)){
			logger.error(" - [PRICEINT-NORESULT] - "+"This product's price is in the price interval;product price = "+price);
			logger.error(" - [PRICEINT-NORESULT] - "+"min price = "+min+" max price = "+max);
			return false;
		}
	return true;
	}

}
