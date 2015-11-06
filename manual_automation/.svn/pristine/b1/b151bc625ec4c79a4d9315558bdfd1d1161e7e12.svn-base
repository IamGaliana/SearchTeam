package com.dangdang.verifier.search_ranking;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.util.AssertTools;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
/**
 * 说明：无库存降权策略，图书无库存商品，排到有库存商品的后面，但排除掉以下品：预售、电子书、第一个无库存的品；
 * @author gaoyanjun
 * @version 创建时间：2015/05/11 16:35 
 * 
 */
public class NoStockDropWeightVerifier {

	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(NoStockDropWeightVerifier.class);
		
	/**
	 * 无库存商品靠后验证
	 * @param iterator	结果集
	 * @param map		
	 * @return
	 */
	public boolean Verifier(ProdIterator iterator, Map<String, String> map){	
		boolean fail = false;
		Node node1 = null;
		//Node node2 = null;
		boolean isFirstNoStock = true;
		boolean isInvalid = false;
		String invalidPid = null;
		
		while(iterator.hasNext()){		
			node1 = iterator.next();
			/*int position = iterator.getPoint();			
			if(position == -1){				// 如果当前位置是第一个品，node1等于第一个品，node2等于第二个品
				node1 = iterator.next();
				node2 = iterator.next();
			}else{							// 如果当前位置不是第一个品，node1是前一个品，node2是后一个品
				node1 = node2;
				node2 = iterator.next();
			}*/
							
			String productid1 = XMLParser.product_id(node1);
			//String productid2 = XMLParser.product_id(node2);
			
			String stockstatus1 = XMLParser.product_StockStatus(node1);
			//String stockstatus2 = XMLParser.product_StockStatus(node2);
			
			String is_ebook1 = XMLParser.prodSpecifiedAttrib(node1, "is_ebook");
			String pre_sale1 = XMLParser.prodSpecifiedAttrib(node1, "pre_sale");
			
			// 1.电子书忽略，2.预售书忽略，3.第一个无库存忽略
			// bad case: 没考虑 00001这种情况下，前面三个0是否都可以忽略
/*			if(!productid1.equals("0") && !productid2.equals("0")){
				if(stockstatus1.equals("0") && !stockstatus2.equals("0")){ 	// 如果出现无库存在前，有库存在后：
					if(isFirstNoStock){								// 如果是第一个无库存品，则跳过
						logger.info(String.format("- function [NoStockDropWeight] - the 1st time is acceptable, stock status of product [%s] is '0', stock status of product [%s] is '1',"
								+ " [%s] is in front of [%s]", productid1, productid2, productid1, productid2));
						isFirstNoStock = false;
					}else if(is_ebook1.equals("1") || pre_sale1.equals("1")){		// 如果是电子书或预售，则跳过
						logger.info(String.format("- function [NoStockDropWeight] - product [%s] is ebook or presale: is_ebook '[%s]', pre_sale '[%s]'", productid1, is_ebook1, pre_sale1));
					}else {													// 不满足以上条件的，视为fail
						fail = true;
						logger.error(String.format("- function [NoStockDropWeight] - error: the 2nd time is not acceptable, stock status of product [%s] is '0', stock status of product [%s] is '1',"
								+ "but [%s] is in front of [%s]", productid1, productid2, productid1, productid2));
						break;
					}
					
				}
			}*/
			
			if(!productid1.equals("0")){
				if(stockstatus1.equals("0")){
					if(isFirstNoStock){
						logger.info(String.format("- function [NoStockDropWeight] - the 1st no stock, pid: %s", productid1));
						isFirstNoStock = false;
						continue;
					}
					if(is_ebook1.equals("1") || pre_sale1.equals("1")){
						logger.info(String.format("- function [NoStockDropWeight] - product %s is_ebook or pre_sale", productid1));
						continue;
					}else{
						// WARN: current product stock status is 0, but if all stock status after this product are not 1, this should be right
						logger.warn(String.format("- function [NoStockDropWeight] - product  %s - stock status is 0,  not the 1st, not ebook or pre_sale", productid1));
						isInvalid = true;
						invalidPid = productid1;
						continue;
					}					
				}else{
					if(isInvalid){
						fail = true;
						logger.error(String.format("- function [NoStockDropWeight] - error: stock status of product [%s] is '0', after it, stock status of product %s is 1", invalidPid, productid1));
						break;
					}
				}
			}
		}

		
		if (fail){ 
			logger.error(String.format(" - [LOG_FAILED] - verify function [NoStockDropWeight], query - [%s]", map.get("query").toString()));
			return false;
		}
		else {
			logger.info(String.format(" - [LOG_SUCCESS] - verify function [NoStockDropWeight], query - [%s]", map.get("query").toString()));
			return true;
		} 
	}	
}
