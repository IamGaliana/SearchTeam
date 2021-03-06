package com.dangdang.verifier.sort;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.ISortVerifier;
import com.dangdang.verifier.list.Score;

/**
 * 对翻页功能进行验证
 * 
 * @author zhangxiaojing
 * 
 */
public class PageTurnVerifier implements ISortVerifier {

	// 日志记录器
//	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(Score.class);
	
	private int totalCount;

	@Override
	public boolean Verifier(ProdIterator iterator, Map<String, String> map) {
		int singleEbookCount = 0;
		while (iterator.hasNext()) {
			Node subNode = iterator.next();
			// 为商品赋值
			Product product = new Product();
			product.setProduct_id(XMLParser.product_id(subNode));
			product.setProduct_medium(XMLParser.product_medium(subNode));

			product.setEbook_Product_id(XMLParser.ebook_product_id(subNode));
			product.setPaper_Book_Product_id(XMLParser.paper_book_product_id(subNode));
				
			if(product.getProduct_medium().equals("22")){
				logger.info("单独电子书ID:"+product.getProduct_id()+product.getEbook_Product_id());
				singleEbookCount++;
			}
		}
		return this.Verifier(map.get("query").toString(),Integer.parseInt(map.get("resultCountEbook")),singleEbookCount);
	}
	
	public boolean Verifier(String query,int resultCountEbook,int singleEbookCount) {
		
		try {
			String xml = this.getQueryResult(query, 1);
			Document dom = XMLParser.read(xml);
			logger.info("总电子书数:"+resultCountEbook);
			logger.info("单独电子书数:"+singleEbookCount);
			totalCount = Integer.parseInt(XMLParser.totalCount(dom))- resultCountEbook +singleEbookCount;//product节点数=合并后的总商品数-（总电子书数-单独的电子书节点）
			int pageCount = Integer.parseInt(XMLParser.PageCount(dom));//页数
			int pageSize = Integer.parseInt(XMLParser.PageSize(dom));//容量
			int expectPagecount=0;//期望页数
			int Remainder = totalCount%pageSize;
			if(Remainder==0){
				expectPagecount = totalCount/pageSize;
			}else{
				expectPagecount = totalCount/pageSize+1;
			}
			
			Boolean result = true;
			if(pageCount!=expectPagecount){//验证总页数
				logger.error("总页数有误  expectPagecount:"+expectPagecount+";pageCount:"+pageCount);
				return false;
			}
			if(pageCount==1){
				int[] pageIndexs = new int[]{1};
				result = this.getVerifyResult(query,pageIndexs);
			}
			else if(pageCount==2){
				int[] pageIndexs = new int[]{1,2};
				result = this.getVerifyResult(query,pageIndexs);
			}
			else if(pageCount==3){
				int[] pageIndexs = new int[]{1,2,3};
				result = this.getVerifyResult(query,pageIndexs);
			}
			else if(pageCount>=4){
				int[] pageIndexs = new int[]{1,2,pageCount-1,pageCount};
				result = this.getVerifyResult(query,pageIndexs);
			}
			
			return result;
		} catch (MalformedURLException | DocumentException e) {
			// TODO Auto-generated catch block
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}

		return true;

	}
	
	private boolean getVerifyResult(String query,int[] pageIndexs) throws NumberFormatException, MalformedURLException, DocumentException{
		String xml = null;
		boolean result = true;
		for(int index : pageIndexs){
			xml = this.getQueryResult(query, index);
			result = result&verify(xml,index);
			if(result==false) return false;
		}
		return result;
	}
	
	private String getQueryResult(String query,int page){
		Map<String, String> map = new HashMap<String, String>();
		try {
			map.put("q", URLEncoder.encode(query, "GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("st", "full");
		map.put("um", "search_ranking");
		map.put("_new_tpl", "1");//search_ranking必加
		map.put("ps", "200");
		map.put("pg", String.valueOf(page));

		// 合成访问后台的url
		String URL = URLBuilder.buildURL(map);
		// 得到后台吐出的数据.xml格式
		String xml = SearchRequester.get(URL);
		return xml;
	}
	
	private boolean verify(String xml,int page) throws NumberFormatException, MalformedURLException, DocumentException{
		Document dom = XMLParser.read(xml);
		//totalCount = Integer.parseInt(XMLParser.totalCount(dom));//总商品数
		int pageIndex = Integer.parseInt(XMLParser.PageIndex(dom));//页码
		int pageCount = Integer.parseInt(XMLParser.PageCount(dom));//页数
		int pageSize = Integer.parseInt(XMLParser.PageSize(dom));//容量
		int productCount = XMLParser.getProductNodes(dom).size();
		int expectPagecount = totalCount/pageSize+1;//期望页数
		
		if(pageIndex!=page){//验证页码
			logger.error("页码有误 ");
			return false;
		}
		if(pageSize!=200){//验证页面容量
			logger.error("页面容量有误");
			return false;
		}
		if(pageCount!=expectPagecount){//验证总页数
			logger.error("总页数有误  expectPagecount:"+expectPagecount+";pageCount:"+pageCount);
			return false;
		}
		//验证页面商品数
		if(totalCount<pageSize&&page==1){//不满一页
			if(productCount!=totalCount){
				logger.error("第一页商品数有误");
				return false;
			}
		}else if(totalCount>pageSize&&page==pageCount){//多页，最后一页
			int expectCount = totalCount-(pageCount-1)*200;
			if(productCount != expectCount){
				logger.error("最后一页商品数有误  expectCount:"+expectCount+";productCount:"+productCount);
				return false;
			}
		}
		else{//多页，非最后一页
			if(productCount!=200){
				logger.error("中间页商品数有误");
				return false;
			}
		}
	    return true;
	}


	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean NumVerifier(int Count, int preCount) {
		// TODO Auto-generated method stub
		return false;
	}

}
