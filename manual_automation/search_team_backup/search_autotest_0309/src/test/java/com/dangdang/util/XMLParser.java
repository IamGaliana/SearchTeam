package com.dangdang.util;

//import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
//import org.dom4j.io.SAXReader;
import org.slf4j.LoggerFactory;

/**
 * xml的解析类
 * 
 * @author zhangyamin
 * 
 */
public class XMLParser {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(XMLParser.class);
	
	private static String XPATH_PRODUCT = "//result/Body/Product";
	private static String XPATH_CATEPATHS = "//result/ranking_info/QueryAnalysisPart/CategoryFeedback/item";
	private static String XPATH_PRODUCT_ID = "product_id";
	private static String XPATH_PRODUCT_MEDIUM = "product_medium";
	private static String XPATH_EBOOK_PRODUCT_ID = "ebook_product_id";
	private static String XPATH_PAPER_BOOK_PRODUCT_ID = "paper_book_product_id";
	private static String XPATH_PRODUCT_TYPE = "product_type";
	private static String XPATH_PRMOTION_ID = "promotion_id";
	private static String XPATH_PRODUCT_NAME = "product_name";
	private static String XPATH_PRICE = "price";
	private static String XPATH_DD_SALE_PRICE = "dd_sale_price";
	private static String XPATH_IS_FIRST_EBOOK = "is_first_book_epromotion";//是否电子书
	private static String XPATH_EXCLUSIVE_REDUCED_PRICE = "exclusive_reduced_price";//手机专享价差价
	private static String XPATH_EXCLUSIVE_BEGIN_DATE = "exclusive_begin_date";//手机专享价开始时间
	private static String XPATH_EXCLUSIVE_END_DATE = "exclusive_end_date";//手机专享价结束时间
	private static String XPATH_IS_DD_SELL = "is_dd_sell";//当当自营
	
	private static String XPATH_PROMOTION_SALEPRICE = "promo_saleprice";
	private static String XPATH_SHOP_ID = "shop_id";
	private static String XPATH_PROMOTION_TYPE = "promotion_type";
	private static String XPATH_REAL_DD_SALE_PRICE = "Real_DD_Sale_Price";
	private static String XPATH_REDUCED_PRICE = "Reduced_Price";
	private static String XPATH_DISCOUNT = "discount";
	private static String XPATH_COLLECTION_PROMO_ID = "collection_promo_id";
	private static String XPATH_CAT_PROMO_END_DATE = "cat_promo_end_date";
	private static String XPATH_CAT_PROMO_START_DATE = "cat_promo_start_date";
	private static String XPATH_PROMOTION_LABEL = "promotion_label";

	private static String XPATH_SALE_WEEK = "sale_week";
	private static String XPATH_SALE_WEEK_AMT = "sale_week_amt";
	private static String XPATH_FIRST_INPUT_DATE = "first_input_date";
	private static String XPATH_PUBLISH_DATE = "publish_date";
	private static String XPATH_PROMO_START_TIME = "promo_start_date";
	private static String XPATH_PROMO_END_TIME = "promo_end_date";
	private static String XPATH_IS_SOLD_OUT = "is_sold_out";
	private static String XPATH_STOCK_STATUS = "stock_status";
	private static String XPATH_PRE_SALE = "pre_sale";
	private static String XPATH_PRODUCT_ACTION_ID = "product_action_id";
	private static String XPATH_ACTION_START_DATE = "action_start_date";
	private static String XPATH_ACTION_END_DATE = "action_end_date";
	private static String XPATH_WEBTEMPLETE = "//result/Header/Summary/WebTemplate";
	private static String XPATH_IS_PUBLICATION = "is_publication";
	private static String XPATH_TOTALCNT = "//result/Header/Summary/TotalCnt";
	private static String ResultCountEbook = "//result/Header/Summary/ResultCountEbook";
	private static String XPATH_TERM = "//result/Header/Term";
	private static String XPATH_CAT_PATHS = "cat_paths";
	private static String XPATH_SCORE = "Score";
	private static String XPATH_MUTI_PROMO_TYPE = "muti_promotion_type";
	private static String XPATH_MUTI_PROMO_START_DATE = "muti_promo_start_date";
	private static String XPATH_MUTI_PROMP_END_DATE = "muti_promo_end_date";
	private static String XPATH_PAGE_SIZE = "//result/Header/Summary/Page/PageSize";
	private static String XPATH_PAGE_COUNT = "//result/Header/Summary/Page/PageCount";
	private static String XPATH_PAGE_INDEX = "//result/Header/Summary/Page/PageIndex";
	private static String XPATH_Total_review_count = "total_review_count";
	private static String XPATH_Score = "score";
	
	private static String XPATH_DIRECTBRAND = "//result/StatInfo/direct_brand/name";
	private static String XPATH_BRAND = "//result/StatInfo/Brands/items/item/ID";
	private static String XPATH_BRANDNAME = "//result/StatInfo/Brands/items/item/Name";
	private static String XPATH_CATEGORY = "//result/Body/Product/cat_paths";
	private static String XPATH_PRICEMIN = "//result/StatInfo/PriceInterval/items/item/Min";
	private static String XPATH_PRICEMAX = "//result/StatInfo/PriceInterval/items/item/Max";
	private static String XPATH_PRICEINTERVAL = "//result/StatInfo/PriceInterval";
	
	private static String XPATH_METAATTR = "//result/StatInfo/MetaAttr";//面包屑
	private static String XPATH_BRAND_ID = "brand_id";
	private static String XPATH_ATTRIB = "attrib";
	private static String XPATH_LABEL_ID ="label_id";
	private static String XPATH_IS_DELETE = "is_delete";

	private static String XPATH_ISBN_SEARCH ="isbn_search";
	private static String XPATH_AUTHOR_NAME ="author_name";
	private static String XPATH_PUBLISHER ="publisher";
	private static String XPATH_DEVICE_ID = "device_id";
	private static String XPATH_BINDING_ID = "binding_id";
	private static String XPATH_NUMIMAGES = "num_images";
	private static String XPATH_DISPLAYSTATUS = "display_status";
	
	private static String XPATH_TEMPLATE_TYPE = "//result/StatInfo/template_info/template_type";
	private static String XPATH_VS_DDSALE = "//result/StatInfo/template_info/dd_sale";
	private static String XPATH_VS_SHOPSALE = "//result/StatInfo/template_info/shop_sale";
	private static String XPATH_VS_SHOPSALE_ITEM = "products/item";
	
	private static String XPATH_VS_ITEM = "//result/StatInfo/template_info/products/item";
	private static String XPATH_VS_SHOP_ID = "//result/StatInfo/template_info/shop_id";
	private static String XPATH_IS_RECO = "is_reco";
	
	//搜索直达、店铺直达
	private static String XPATH_VS_SHOP_INFO_TYPE = "//result/StatInfo/ShopInfo/type";
	private static String XPATH_VS_SHOP_INFO_URL = "//result/StatInfo/ShopInfo/shop_url";
	private static String XPATH_VS_SHOP_INFO_NAME = "//result/StatInfo/ShopInfo/shop_name";
	private static String XPATH_VS_SHOP_INFO_IMAGE = "//result/StatInfo/ShopInfo/image_url";
	private static String XPATH_VS_SHOP_INFO_PROMO = "//result/StatInfo/ShopInfo/promotion";
	
	private static String XPATH_STYPE = "_stype";
	
	private static String XPATH_CATEINFO = "//StatInfo/cat_paths/level1/items/CategoryInfo";
	private static String XPATH_CATEPATH = "CatPath";
	private static String XPATH_PATH = "//StatInfo/Path/items/item";
	
	
	//店铺直达、搜索直达
	public static String shopinfotype(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_VS_SHOP_INFO_TYPE).get(0);
		if (subNode==null) {
			return "NULL";
		}else {
			String value = subNode.getStringValue();
			return value;
		}
	}
	public static String shopinfourl(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_VS_SHOP_INFO_URL).get(0);
		if (subNode==null) {
			return "NULL";
		}else {
			String value = subNode.getStringValue();
			return value;
		}
	}
	public static String shopinfoname(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_VS_SHOP_INFO_NAME).get(0);
		if (subNode==null) {
			return "NULL";
		}else {
			String value = subNode.getStringValue();
			return value;
		}
	}
	public static String shopinfoimage(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_VS_SHOP_INFO_IMAGE).get(0);
		if (subNode==null) {
			return "NULL";
		}else {
			String value = subNode.getStringValue();
			return value;
		}
	}
	public static String shopinfopromo(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_VS_SHOP_INFO_PROMO).get(0);
		if (subNode==null) {
			return "NULL";
		}else {
			String value = subNode.getStringValue();
			return value;
		}
	}
	
	
	public static String catePath(Node node){
		List<Node> nodes = node.selectNodes(XPATH_CATEPATH);
		try{
			Node catepath = (Node)nodes.get(0);
			String value = catepath.getStringValue();
			return value;
		}catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return "";
		}
	}
	
	public static List<Node> cateInfo(Document doc){
		try{
			List<Node> nodes = doc.selectNodes(XPATH_CATEINFO);
			return nodes;
			
		}catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return null;
		}
	}
	
	public static List<Node> pathInfo(Document doc){
		try{
			List<Node> nodes = doc.selectNodes(XPATH_PATH);
			return nodes;
			
		}catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return null;
		}
	}
	
	public static String product_isReco(Node node) {
		
		String value="";
		try{
		Node is_reco = (Node)node.selectNodes(XPATH_IS_RECO).get(0);
		value = is_reco.getStringValue();
		}catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return "0";
		}
//		logger.debug("- [XMLParser] - get the numImage nodes; numImage="+value);
		return value;
	}
	
	public static Node PriceInterval(Document doc){
		
		List<Node> list = doc.selectNodes(XPATH_PRICEINTERVAL);
		return list.get(0);
	}
	
	public static String product_displayStatus(Node node) {
		
		String value="";
		try{
		Node display = (Node)node.selectNodes(XPATH_DISPLAYSTATUS).get(0);
		value = display.getStringValue();
		}catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return "0";
		}
//		logger.debug("- [XMLParser] - get the display_status node; display_status="+value);
		return value;
	}
	
	public static String product_numImage(Node node) {
	
		String value="";
		try{
		Node numImage = (Node)node.selectNodes(XPATH_NUMIMAGES).get(0);
		value = numImage.getStringValue();
		}catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return "0";
		}
//		logger.debug("- [XMLParser] - get the numImage nodes; numImage="+value);
		return value;
	}
	
	public static List<Node> template_shopsale_products(Node node) {
		
		List<Node> list = node.selectNodes(XPATH_VS_SHOPSALE_ITEM);
//		logger.debug("- [XMLParser] - get the sub product nodes in shop sale node");
		return list;
	}
	
	public static String templateType(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_TEMPLATE_TYPE).get(0);
		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the templateType sub node ; templateType="
//				+ value);
		return value;
	}
	
	public static Node templateDDSale(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_VS_DDSALE).get(0);
//		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the template DD sale node;");
		return subNode;
	}
	
	public static Node templateShopSale(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_VS_SHOPSALE).get(0);
//		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the template shop sale node;");
		return subNode;
	}
	
	public static List templateItem(Document doc) {
		List list = doc.selectNodes(XPATH_VS_ITEM);
//		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the template item list;");
		return list;
	}
	
	public static String templateShopId(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_VS_SHOP_ID).get(0);
		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the templateShopId sub node ; templateShopId="
//				+ value);
		return value;
	}
	
	private static String XPATH_PRODUCT_TERM = "term";
	private static String XPATH_SINGER="singer";
	private static String XPATH_ACTORS="actors";
	private static String XPATH_DIRECTOR="director";
	
	// MetaAttr
	public static String MetaAttr(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_METAATTR).get(0);
		String value = subNode.getStringValue();
		// logger.debug("- [XMLParser] - get the PageSize sub node ; PageSize="
		// + value);
		return value;
	}

	// DIRECTBRAND
	public static String DirectBrand(Document doc) {
		String value = "";
		try {
			Node subNode = (Node) doc.selectNodes(XPATH_DIRECTBRAND).get(0);
			value = subNode.getStringValue();
			
		} catch (Exception e) {
			value = "";
		}
		// logger.debug("- [XMLParser] - get the PageSize sub node ; PageSize="
		// + value);
		return value;
	}
	
	public static String PageSize(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_PAGE_SIZE).get(0);
		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the PageSize sub node ; PageSize="
//				+ value);
		return value;
	}

	public static String PageCount(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_PAGE_COUNT).get(0);
		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the PageCount sub node ; PageCount="
//				+ value);
		return value;
	}

	public static String PageIndex(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_PAGE_INDEX).get(0);
		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the PageIndex sub node ; PageIndex="
//				+ value);
		return value;
	}

	public static Document read(String xml) throws MalformedURLException,
			DocumentException {
//		logger.debug("- [XMLParser] - Parse the xml! "/* + xml */);
		return DocumentHelper.parseText(xml);
	}

	public static String term(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_TERM).get(0);
		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the terms  ; terms = " + value);
		return value;
	}
	
	public static List getcatepaths(Document doc) {

		// String xpath = "//result/Body/Product";
		List list = doc.selectNodes(XPATH_CATEPATHS);
//		logger.debug("- [XMLParser] - get the product nodes;");
		return list;
	}
	

	public static String totalCount(Document doc) {
		Node subNode = (Node) doc.selectNodes(XPATH_TOTALCNT).get(0);
		String value = subNode.getStringValue();
//		logger.debug("- [XMLParser] - get the total count sub node ; total_Count="
//				+ value);
		return value;
	}
	
	public static String resultCountEbook(Document doc) {
		Node subNode = (Node) doc.selectNodes(ResultCountEbook).get(0);
		String value = subNode.getStringValue();
		//logger.debug("- [XMLParser] - get the ResultCountEbook sub node ; ResultCountEbook="
				//+ value);
		return value;
	}

	public static List getProductNodes(Document doc) {

		// String xpath = "//result/Body/Product";
		List list = doc.selectNodes(XPATH_PRODUCT);
//		logger.debug("- [XMLParser] - get the product nodes;");
		return list;
	}


	public static String ebook_product_id(Node node) {
		String value="";
		try {
			if( node.selectNodes(XPATH_EBOOK_PRODUCT_ID)!=null){
				if(node.selectNodes(XPATH_EBOOK_PRODUCT_ID).size()>0){
			Node subNode = (Node) node.selectNodes(XPATH_EBOOK_PRODUCT_ID).get(0);
			value = subNode.getStringValue();
//					logger.debug("- [XMLParser] - get the ebook_product_id sub node ; ebook_product_id="
//							+ value);
				}

			}

		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "0";
		}
		return value;
	}

	public static String paper_book_product_id(Node node) {
		String value="";
		try {
			if(node.selectNodes(XPATH_PAPER_BOOK_PRODUCT_ID)!=null){
				if(node.selectNodes(XPATH_PAPER_BOOK_PRODUCT_ID).size()>0){
			Node subNode = (Node) node.selectNodes(XPATH_PAPER_BOOK_PRODUCT_ID).get(0);
			value = subNode.getStringValue();
//					logger.debug("- [XMLParser] - get the paper_book_product_id sub node ; paper_book_product_id="
//							+ value);
				}
			}
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "0";
		}
		return value;
	}
	
	public static String product_medium(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PRODUCT_MEDIUM).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the product_medium sub node ; product_medium="
//					+ value);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "0";
		}
		return value;
	}
	
	public static String product_muti_promo_start_date(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_MUTI_PROMO_START_DATE)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the muti_promo_start_date sub node ; muti_promo_start_date="
//					+ value);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "1970-01-01 00:00:00";
		}
		return value;
	}

	public static String product_muti_promo_end_date(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_MUTI_PROMP_END_DATE)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the muti_promo_end_date sub node ; muti_promo_end_date="
//					+ value);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "1970-01-01 00:00:00";
		}
		return value;
	}

	public static String product_muti_promo_type(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_MUTI_PROMO_TYPE)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the product type sub node ; product_type="
//					+ value);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "0";
		}
		return value;
	}

	public static String product_name(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PRODUCT_NAME).get(0);
			value = subNode.getStringValue().replaceAll("<[\\w\\W]+?>", "").trim();  //替换html字符
			value = ChineseConvertor.toSimple(value).toLowerCase(); //中文转简体、英文转小写
//			logger.debug("- [XMLParser] - get the product name sub node ; product_name="
//					+ value);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "";
		}
		return value;
	}

	public static String product_catelogPath(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_CAT_PATHS).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the catelog path sub node ; catPath="
//					+ value);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "";
		}
		return value;
	}
	//大写Score
	public static String product_scope(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_SCORE).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the scope sub node ; scope="
//					+ value);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "0";

		}
		return value;
	}

	public static String product_isPublication(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_IS_PUBLICATION).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the is_publication sub node ; is_publication="
//					+ value);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "0";
		}
		return value;
	}

	public static String product_ActionEndDate(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_ACTION_END_DATE)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the action end date sub node ; Action_end_date="
//					+ value);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			value = "1970-01-01 00:00:00";
		}
		return value;
	}

	public static String product_ActionStartDate(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_ACTION_START_DATE)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the action start date sub node ; Action_start_date="
//					+ value);
		} catch (Exception e) {
			value = "1970-01-01 00:00:00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;

	}

	public static String product_ActionID(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PRODUCT_ACTION_ID)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the product action id sub node ; Product_action_id="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_shopID(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_SHOP_ID).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get shop id sub node ; Shop_id= "
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_preSale(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PRE_SALE).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the pre sale sub node ; Pre_sale="
//					+ value);
		} catch (Exception e) {
			value = "0.00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_type(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PRODUCT_TYPE).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the product type sub node ; Product_type="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_StockStatus(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_STOCK_STATUS).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the stock status sub node ; Stock_status="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_isSoldOut(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_IS_SOLD_OUT).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the sold out sub node ; Sold_out="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);

		}
		return value;
	}

	public static String product_firstinputDate(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_FIRST_INPUT_DATE).get(
					0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the first input date sub node ; first_input_date="
//					+ value);
		} catch (Exception e) {
			value = "1970-01-01 00:00:00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_publishDate(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PUBLISH_DATE).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the publish date sub node ; Publish_Date="
//					+ value);
		} catch (Exception e) {
			// value = "1970-01-01 00:00:00";
			value = "";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_price(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PRICE).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the price sub node ; Price="
//					+ value);
		} catch (Exception e) {
			value = "0.00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_id(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PRODUCT_ID).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the product id sub node ; Product_id="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String promotion_label(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PROMOTION_LABEL).get(0);
			value = subNode.getStringValue();
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}
	
	public static String product_sale_week(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_SALE_WEEK).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the sale week sub node ; Sale_week="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);

		}
		return value;
	}

	public static String product_sale_week_amt(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_SALE_WEEK_AMT).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the sale week amt sub node ; Sale_week_amt="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_dd_sale_price(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_DD_SALE_PRICE).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the dd sale price sub node ; dd_sale_price="
//					+ value);
		} catch (Exception e) {
			value = "0.00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}
	
	public static String product_is_first_ebook(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_IS_FIRST_EBOOK).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the dd sale price sub node ; dd_sale_price="
//					+ value);
		} catch (Exception e) {
			value = "0";
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//			e.printStackTrace(new PrintStream(baos));  
//			String exception = baos.toString();  
//			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_promo_saleprice(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PROMOTION_SALEPRICE)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the promotion sale price sub node ; Promotion_sale_price="
//					+ value);
		} catch (Exception e) {
			value = "0.00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_promotion_id(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PRMOTION_ID).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the promotion id sub node ; promotion_id="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_promo_start_date(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PROMO_START_TIME).get(
					0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the promotion start time sub node ; promotion_start_time="
//					+ value);
		} catch (Exception e) {
			value = "1970-01-01 00:00:00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_promo_end_date(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_PROMO_END_TIME).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the promotion end time sub node ; promotion_end_time="
//					+ value);
		} catch (Exception e) {
			value = "1970-01-01 00:00:00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}
	
	public static String product_exclusive_begin_date(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_EXCLUSIVE_BEGIN_DATE).get(0);
			value = subNode.getStringValue();
		} catch (Exception e) {
			value = "1970-01-01 00:00:00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}
	
	public static String product_exclusive_end_date(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_EXCLUSIVE_END_DATE).get(0);
			value = subNode.getStringValue();
		} catch (Exception e) {
			value = "1970-01-01 00:00:00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}
	
	public static String product_exclusive_reduced_price(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_EXCLUSIVE_REDUCED_PRICE).get(0);
			value = subNode.getStringValue();
		} catch (Exception e) {
			value = "-1";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}
	
	public static String product_is_dd_sell(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_IS_DD_SELL).get(0);
			value = subNode.getStringValue();
		} catch (Exception e) {
			value = "-1";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_collection_promo_id(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_COLLECTION_PROMO_ID)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the collection_promo_id sub node ; collection_promo_id="
//					+ value);
		} catch (Exception e) {
			value = "";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_cat_promo_end_date(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_CAT_PROMO_END_DATE)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the cat_promo_end_date sub node ; cat_promo_end_date="
//					+ value);
		} catch (Exception e) {
			value = "1970-01-01 00:00:00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_cat_promo_start_date(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_CAT_PROMO_START_DATE)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the cat_promo_start_date sub node ; cat_promo_start_date="
//					+ value);
		} catch (Exception e) {
			value = "1970-01-01 00:00:00";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String product_total_review_count(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_Total_review_count)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the Total_review_count sub node ; Total_review_count="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}
	
	//小写score
	public static String product_score(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_Score).get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the score sub node ; score="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}
	
	public static String product_brand(Node node) {
		return get_product_info(node, XPATH_BRAND_ID);
	}
	
	public static String product_attrib(Node node) {
		return get_product_info(node, XPATH_ATTRIB);
	}

	public static String product_label(Node node) {
		return get_product_info(node, XPATH_LABEL_ID);
	}

	//第一个品牌
	public static String getBrand(Document doc){
		String value = "";
		List<Node> ln = doc.selectNodes(XPATH_BRAND);		
		if (ln.size()>0){
			value = ln.get(0).getStringValue();
			logger.debug("- [XMLParser] - get the Brand sub node ; BrandID=" + value);
		}else{
			logger.debug("- [XMLParser] - get the Brand sub node ; No BrandID");
		}
		return value;
	}
	
	public static List<String> getBrandNames(Document doc){
//		String value = "";
		List<String> abc = new ArrayList<String>();
		List<Node> ln = doc.selectNodes(XPATH_BRANDNAME);		
		if (ln.size()>0){
			for (int i = 0; i < ln.size(); i++) {
				abc.add(ln.get(i).getStringValue());
			}
			logger.debug("- [XMLParser] - get the Brand sub node ; BrandID=" + abc.toString());
		}else{
			logger.debug("- [XMLParser] - get the Brand sub node ; No BrandID");
		}
		return abc;
	}

	public static String getCategory(Document doc){
		String value = "";
		List<Node> ln = doc.selectNodes(XPATH_CATEGORY);
		if (ln.size()>0){
			value = ln.get(0).getStringValue();
			logger.debug("- [XMLParser] - get the Category sub node ; Category=" + value);
		}else{
			logger.debug("- [XMLParser] - get the Category sub node ; No Category");
		}
		return value;
	}
	
	public static String getPriceInterval(Document doc){
		String value = "";
		List<Node> ln1 = doc.selectNodes(XPATH_PRICEMIN);
		List<Node> ln2 = doc.selectNodes(XPATH_PRICEMAX);
		if (ln1.size()>0){		
			value = ln1.get(0).getStringValue();
			value += "~" + ln2.get(0).getStringValue();
			logger.debug("- [XMLParser] - get the PriceInterval sub node ; PriceInterval=" + value);
		}else{
			logger.debug("- [XMLParser] - get the PriceInterval sub node ; No PriceInterval");
		}
		return value;
	}
	
	public static String getWebTemplete(Document doc) {
		List<Node> ln = doc.selectNodes(XPATH_WEBTEMPLETE);
		if (ln.size()>0){
			String webTemplete = ln.get(0).getStringValue();
			if (webTemplete.equals("PUB_TEMPLATE")) {
				logger.debug("- [XMLParser] - get web templete node; template="+webTemplete);
				return "0";
			} else if (webTemplete.equals("BH_TEMPLATE")) {
				logger.debug("- [XMLParser] - get web templete node; template="+webTemplete);
				return "12";
			} 
		}
		return "-1";
	}

	
	public static String get_product_info(Node node, String nodepath) {
		String value = "";
		try {
			List<Node> ln = node.selectNodes(nodepath);
			if (ln.size()>0){
				value = ln.get(0).getStringValue().toLowerCase(); //英文转小写
				value = ChineseConvertor.toSimple(value);  //中文转简体
//				logger.debug(String.format("- [XMLParser] - get the product sub node ; %s=%s", nodepath,  value));
			}else{
//				logger.debug("- [XMLParser] - get the product sub node ; No " + nodepath);
			}
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return null;
		}
		return value;
	}

	public static String product_is_delete(Node node) {
		String value;
		try {
			Node subNode = (Node) node.selectNodes(XPATH_IS_DELETE)
					.get(0);
			value = subNode.getStringValue();
//			logger.debug("- [XMLParser] - get the is_delete sub node ; is_delete="
//					+ value);
		} catch (Exception e) {
			value = "0";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
		return value;
	}

	public static String isbn_search(Node node) {
		return get_product_info(node, XPATH_ISBN_SEARCH);
	}

	public static String author_name(Node node) {
		return get_product_info(node, XPATH_AUTHOR_NAME);
	}
	
	public static String publisher(Node node) {
		return get_product_info(node, XPATH_PUBLISHER);
	}
	
	public static String singer(Node node) {
		return get_product_info(node, XPATH_SINGER);
	}
	
	public static String actors(Node node) {
		return get_product_info(node, XPATH_ACTORS);
	}

	public static String director(Node node) {
		return get_product_info(node, XPATH_DIRECTOR);
	}
	
	public static String binding_id(Node node) {
		return get_product_info(node, XPATH_BINDING_ID);
	}
	
	public static String device_id(Node node) {
		return get_product_info(node, XPATH_DEVICE_ID);
	}

	public static String product_stype(Node node) {
		return get_product_info(node, XPATH_STYPE);
	}
	
	public static Map<String,String> get_book_search_base_info(Node node) {
		Map<String, String> tm = new HashMap<String, String>();
		tm.put(XPATH_PRODUCT_ID, get_product_info(node, XPATH_PRODUCT_ID).trim());
		tm.put(XPATH_EBOOK_PRODUCT_ID, get_product_info(node, XPATH_EBOOK_PRODUCT_ID).trim());
		tm.put(XPATH_PRODUCT_NAME, get_product_info(node, XPATH_PRODUCT_NAME).replaceAll("<[\\w\\W]+?>", "").trim());
		tm.put(XPATH_ISBN_SEARCH, get_product_info(node, XPATH_ISBN_SEARCH).trim());
		tm.put(XPATH_AUTHOR_NAME, get_product_info(node, XPATH_AUTHOR_NAME).trim());
		tm.put(XPATH_PUBLISHER, get_product_info(node, XPATH_PUBLISHER).trim());
		tm.put("product_"+XPATH_PRODUCT_TERM, get_product_info(node, XPATH_PRODUCT_TERM).trim());
		return tm;
	}

	public static Map<String,String> get_book_search_extend_info(Node node) {
		Map<String, String> tm = new HashMap<String, String>();
		tm.put(XPATH_CAT_PATHS, get_product_info(node, XPATH_CAT_PATHS));
		tm.put(XPATH_PRICE, get_product_info(node, XPATH_PRICE));
		tm.put(XPATH_DD_SALE_PRICE, get_product_info(node, XPATH_DD_SALE_PRICE));
		String publish_date = get_product_info(node, XPATH_PUBLISH_DATE);
		if (publish_date.equals("")) {
			publish_date = "2013-10-22 00:00:00";		
		}
		tm.put(XPATH_PUBLISH_DATE, publish_date);
		tm.put(XPATH_BINDING_ID, get_product_info(node, XPATH_BINDING_ID));
		tm.put(XPATH_STOCK_STATUS, get_product_info(node, XPATH_STOCK_STATUS));
		return tm;
	}	
	
	public static Map<String,String> get_ebook_search_extend_info(Node node) {
		Map<String, String> tm = new HashMap<String, String>();
		tm.put(XPATH_CAT_PATHS, get_product_info(node, XPATH_CAT_PATHS));
		tm.put(XPATH_PRICE, get_product_info(node, XPATH_PRICE));
		tm.put(XPATH_DD_SALE_PRICE, get_product_info(node, XPATH_DD_SALE_PRICE));
		String publish_date = get_product_info(node, XPATH_PUBLISH_DATE);
		if (publish_date.equals("")) {
			publish_date = "2013-10-22 00:00:00";		
		}
		tm.put(XPATH_PUBLISH_DATE, publish_date);
//		tm.put(XPATH_DEVICE_ID, get_product_info(node, XPATH_DEVICE_ID));
		tm.put(XPATH_DEVICE_ID,"14");
		return tm;
	}
	
	public static Map<String,String> get_music_search_info(Node node) {
		Map<String, String> tm = new HashMap<String, String>();
		tm.put(XPATH_PRODUCT_ID, get_product_info(node, XPATH_PRODUCT_ID).trim());
		tm.put(XPATH_PRODUCT_NAME, get_product_info(node, XPATH_PRODUCT_NAME).replaceAll("<[\\w\\W]+?>", "").trim());
		tm.put(XPATH_SINGER, get_product_info(node, XPATH_SINGER).trim());
		tm.put(XPATH_ACTORS, get_product_info(node, XPATH_ACTORS).trim());
		return tm;
	}
	
	public static Map<String,String> get_b2c_search_info(Node node) {
		Map<String, String> tm = new HashMap<String, String>();
		tm.put(XPATH_PRODUCT_ID, get_product_info(node, XPATH_PRODUCT_ID).trim());
		tm.put(XPATH_PRODUCT_NAME, get_product_info(node, XPATH_PRODUCT_NAME).replaceAll("<[\\w\\W]+?>", "").trim());
		tm.put(XPATH_CAT_PATHS, get_product_info(node, XPATH_CAT_PATHS).trim());
		return tm;
	}
	
	public static Map<String,String> get_vedio_search_info(Node node) {
		Map<String, String> tm = new HashMap<String, String>();
		tm.put(XPATH_PRODUCT_ID, get_product_info(node, XPATH_PRODUCT_ID).trim());
		tm.put(XPATH_PRODUCT_NAME, get_product_info(node, XPATH_PRODUCT_NAME).replaceAll("<[\\w\\W]+?>", "").trim());
		tm.put(XPATH_DIRECTOR, get_product_info(node, XPATH_DIRECTOR).trim());
		tm.put(XPATH_ACTORS, get_product_info(node, XPATH_ACTORS).trim());
		return tm;
	}
	
	public static Map<String,String> get_book_search_info(Node node, int flag) {
		switch (flag){
		case 1:
			return get_book_search_base_info(node);
		case 2:
			return get_book_search_extend_info(node);
		case 3:
			Map<String,String> tm = get_book_search_extend_info(node);
			tm.putAll(get_book_search_base_info(node));
			return tm;
		}
		return null;
	}

	
	public static Map<String,String> get_ebook_search_info(Node node, int flag) {
		switch (flag){
		case 1:
			return get_book_search_base_info(node);
		case 2:
			return get_ebook_search_extend_info(node);
		case 3:
			Map<String,String> tm = get_ebook_search_extend_info(node);
			tm.putAll(get_book_search_base_info(node));
			return tm;
		}
		return null;
	}

}
