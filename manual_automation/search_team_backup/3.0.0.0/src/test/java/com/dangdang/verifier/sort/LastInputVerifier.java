package com.dangdang.verifier.sort;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.dom4j.Node;

import com.dangdang.data.Product;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;
import com.dangdang.verifier.iVerifier.ISortVerifier;

public class LastInputVerifier implements ISortVerifier {

	@Override
	public boolean Verifier(ProdIterator iterator, Map<String, String> map) {
		// TODO Auto-generated method stub

		// 相比当前的商品，它的上一个商品
		Product pre_Product = null;
		Product pre_Product_reco = null;
		// List<Product> prodList = new ArrayList<Product>();

		// 默认值器
		String defaultDate = "1970-01-01 00:00:00";

		try {

			// 日期格式刷
			SimpleDateFormat formatDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			//暂时不验总商品数量
//			//本次搜索结果中品的数量
//			int totalCount = iterator.getTotalCount();
//			//预请求结果中品的数量
//			int preTotalCount = Integer.valueOf(map.get("totalCount"));
//			
//			if(!NumVerifier(totalCount, preTotalCount)){
//				return false;
//			}
			// 遍历所有商品节点。比较他们的最后更新时间或者发布时间
			while (iterator.hasNext()) {
				logger.debug("/****************************product**************************/");
				
				Node subNode = iterator.next();
				
				Product product = new Product();
				product.setProduct_id(XMLParser.product_id(subNode));
				product.setSale_week(XMLParser.product_sale_week(subNode));
				product.setSale_week_amt(XMLParser
						.product_sale_week_amt(subNode));

					product.setLast_input_date(XMLParser.product_firstinputDate(subNode));
					product.setStype(XMLParser.product_stype(subNode));
					if(product.getStype().equals("")){
						if (pre_Product == null) {
							pre_Product = product;
						} else {
							String datetime_product = product.getLast_input_date();
							String datetime_preProduct = pre_Product.getLast_input_date();
							Date date_product = formatDate.parse(datetime_product
									.equals("") ? defaultDate : datetime_product);
							Date date_preProduct = formatDate
									.parse(datetime_preProduct.equals("") ? defaultDate
											: datetime_preProduct);
							if (date_preProduct.after(date_product)
									| date_preProduct.equals(date_product)) {
								pre_Product = product;
							} else {
								logger.error(" - [LASTINPUT] - "+"pre_product_id:"
										+ pre_Product.getProduct_id() + ";"
										+ "first_input_date:" + datetime_preProduct);
								logger.error(" - [LASTINPUT] - "+"product_id:"
										+ product.getProduct_id() + ";"
										+ "first_input_date:" + datetime_product);
								
								return false;
							}
						}
					}else if(product.getStype().equals("reco")){//推荐品排序
						if (pre_Product_reco == null) {
							pre_Product_reco = product;
						} else {
							String datetime_product = product.getLast_input_date();
							String datetime_preProduct = pre_Product_reco.getLast_input_date();
							Date date_product = formatDate.parse(datetime_product
									.equals("") ? defaultDate : datetime_product);
							Date date_preProduct = formatDate
									.parse(datetime_preProduct.equals("") ? defaultDate
											: datetime_preProduct);
							if (date_preProduct.after(date_product)
									| date_preProduct.equals(date_product)) {
								pre_Product_reco = product;
							} else {
								logger.error(" - [LASTINPUT] - "+"pre_product_id:"
										+ pre_Product_reco.getProduct_id() + ";"
										+ "first_input_date:" + datetime_preProduct);
								logger.error(" - [LASTINPUT] - "+"product_id:"
										+ product.getProduct_id() + ";"
										+ "first_input_date:" + datetime_product);
								
								return false;
							}
						}
					}

					logger.debug("/****************************End**************************/");
			}


		} catch ( ParseException e) {
			// TODO Auto-generated catch block
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}

		return true;
	}

	@Override
	public boolean NumVerifier(int Count, int preCount) {
		//本次结果中商品数量比预查询中的结果数量大
				if(Count!=preCount){
					logger.error(" - [LASTINPUT] - "+"The total count different with the total count of the result before it was sorted by last input!");
					logger.error(" - [LASTINPUT] - "+"Total Count:"+Count+" Pre Total Count:"+preCount);
					return false;
				}else{
					logger.debug("Correct!");
					return true;
				}
	}

	@Override
	public boolean doVerify(ProdIterator iterator, Map<String, String> map) {
		// TODO Auto-generated method stub
		return false;
	}
	
//	public static void main(String[] args) {
//	PropertyConfigurator.configure("conf/log4j.properties");
//	Map<String,String> urlmap = URLBuilder.getMap("last_date", "连衣裙");
//	Map<String,String> urlmap2 = URLBuilder.getMap("", "连衣裙");
//	Map<String,String> argsmap = new HashMap<String, String>();
//	argsmap.put("outlets","1");
//	argsmap.put("TotalCnt", "39889");
//	ProdIterator iterator = new ProdIterator(urlmap);
//	ProdIterator iterator2 = new ProdIterator(urlmap2);
//	LastInputVerifier lastinput = new LastInputVerifier();
//	System.out.println(lastinput.Verifier(iterator, argsmap));
//}
}
