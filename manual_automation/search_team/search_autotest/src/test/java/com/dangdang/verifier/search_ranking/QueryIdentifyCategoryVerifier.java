package com.dangdang.verifier.search_ranking;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

import com.dangdang.client.DBAction;
import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncCatePath;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
/**
 * 说明：店铺识别验证方法
 * @author gaoyanjun
 * @version 创建时间：2015/05/11 16:35 
 * 
 */
public class QueryIdentifyCategoryVerifier {

	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(QueryIdentifyCategoryVerifier.class);
			
	/**
	 * 店铺识别验证方法
	 * @param iterator	结果集
	 * @param catPaths	预期分类
	 * @param query		query词
	 * @param vp		验证点
	 * @return	如果结果集中所有单品的cat_paths的前两位和预期的cat_paths相同；
	 * 或单品的classcode中包含预期的cat_paths(实际是catID)，则pass；否则fail
	 */
	public boolean Verifier(ProdIterator iterator, String catPaths, String query, String vp){	
		String expectedCatPaths = "";
		String subExpectedCatPaths = "";
		String actualCatPaths = "";
		if(!catPaths.contains(".")){ // 如果url中传递的catPaths信息是category_id，需要获得对应的category_path
			DBAction dba = new DBAction();
			dba.setFuncCondition(String.format("1 = 1 and category_id = %s",catPaths));			
			List<FuncCatePath> CatPathsList = dba.getCatePathFromFC();			
			expectedCatPaths = (!CatPathsList.isEmpty()) ? CatPathsList.get(0).getCat_path() : "";
			// 如果url中的cat_Paths是category_id，那么验证时就要取到二级分类，比如58.61.17.11.00.00，就取58.61
			subExpectedCatPaths = expectedCatPaths.substring(0, 5); 
		} else {
			expectedCatPaths = catPaths; // 如果url中传递的catPaths就是category_path，直接用作期望值
			// 如果url中的cat_Paths是category_path，那么验证时就要取到二级分类，比如58.00.00.00.00.00，就取58
			subExpectedCatPaths = expectedCatPaths.substring(0, 2); 
		}
		while(iterator.hasNext()){
			Node node = iterator.next();
			String productId = XMLParser.product_id(node);
			List<Node> products = URLBuilder.porductSearch(productId, false);
			Node singleProduct = (!products.isEmpty())? products.get(0): null;
			boolean isMatchedCategory = false;
			
			// 根据product_id找到对应品了：
			if (singleProduct!=null){
				// 得到该单品的cat_paths节点值
				actualCatPaths = XMLParser.product_catelogPath(singleProduct);
				// 有可能是多个分类，由“|”分隔
				String[] paths = actualCatPaths.split("\\|");
				// 遍历单品的分类，如果其中任意一个与预期的分类（一级或二级）想匹配，则pass，如果都没有匹配的，则fail
				for(String path : paths){
					if(path.startsWith(subExpectedCatPaths)){
						isMatchedCategory = true;
						break;
					}
				}
				if(!isMatchedCategory){
					logger.info(String.format(" - [LOG FAILED] - verify function [%s] for query [%s], "
							+ "pid [%s] - failed, expected catPaths is [%s], actual catPaths is [%s]", 
							vp, query, productId, expectedCatPaths, actualCatPaths));
					return false;
				}
			}
		}
		return true;
	}
}
