package com.dangdang.verifier.sort.newprod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;

import com.dangdang.util.AssertTools;
import com.dangdang.util.JsonParser;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.StoolData;
import com.dangdang.util.XMLParser;

/**
 * @author liuzhipengjs@dangdang.com
 * @version 创建时间：2015年4月1日 下午2:31:04 类说明
 */
public class NewProdVerifier {
	//初始化运营工具的url参数
	private static String baseurl = "http://10.255.254.72:8898/";
	private static String type = "new_product_category_weight";

	// 日志记录器
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(NewProdVerifier.class);
	//初始化原始数据
	Map<String, List<String>> dataMap = newProdCate();
	Object[] categorys = dataMap.keySet().toArray();
	
	public boolean Verifier(ProdIterator iterator, Map<String, String> map) {
//		Map<String, List<String>> dataMap = newProdCate();
//		Object[] categorys = dataMap.keySet().toArray();

		// 结果中的配置分类下新品list,和配置的分类个数对应
		Map<Integer, List<Node>> resultmap = new HashMap<Integer, List<Node>>();
		for (int m = 0; m < categorys.length; m++) {
			List<Node> list = new ArrayList<Node>();
			resultmap.put(m, list);
		}

		if (iterator.getTotalCount() < 1) {
			return false; // 可省略，doQuery方法里有判断
		} else if (iterator.getTotalCount() > 600) {
			for (int i = 0; i < 600; i++) {
				Node node = iterator.next();
				String prodCatepath = XMLParser.product_catelogPath(node);
				//跳过单品置顶
				if (AssertTools.singleTopStatus(XMLParser.product_scope(node))) {
					logger.info("单品置顶ID："+XMLParser.product_id(node)+"所属分类："+prodCatepath);
					continue;
				}
				for (int j = 0; j < categorys.length; j++) {
					if (categoryMatch(categorys[j], prodCatepath)) {
						List<Node> temporaryList = resultmap.get(j);
						temporaryList.add(node);
						resultmap.put(j, temporaryList);
					}
				}
			}
			for (Entry<Integer, List<Node>> entry : resultmap.entrySet()) {
				List<String> pidStrings = new ArrayList<String>();
				for(Node node : entry.getValue()){
					pidStrings.add(XMLParser.product_id(node));
				}
				logger.info("配置各分类商品情况："+categorys[entry.getKey()].toString()+pidStrings);
			}
			return validMap(resultmap);
		} else {
			while (iterator.hasNext()) {
				Node node = iterator.next();
				String prodCatepath = XMLParser.product_catelogPath(node);
				//跳过单品置顶
				if (AssertTools.singleTopStatus(XMLParser.product_scope(node))) {
					logger.info("单品置顶ID："+XMLParser.product_id(node)+"所属分类："+prodCatepath);
					continue;
				}
				for (int j = 0; j < categorys.length; j++) {
					if (categoryMatch(categorys[j], prodCatepath)) {
						List<Node> temporaryList = resultmap.get(j);
						temporaryList.add(node);
						resultmap.put(j, temporaryList);
					}
				}
			}
			for (Entry<Integer, List<Node>> entry : resultmap.entrySet()) {
				List<String> pidStrings = new ArrayList<String>();
				for(Node nodeV : entry.getValue()){
					pidStrings.add(XMLParser.product_id(nodeV));
				}
				logger.info("配置各分类商品情况："+categorys[entry.getKey()].toString()+pidStrings);
			}
			return validMap(resultmap);
		}
	}

	private boolean validMap(Map<Integer, List<Node>> resultmap) {
		for (Entry<Integer, List<Node>> entry : resultmap.entrySet()) {
			if (!validList(entry.getKey(),entry.getValue())) {
//				logger.info("hehe");
				return false;
			}
		}
		return true;
	}
	//包含该Pid有库存有图片文本相关分大于等于5
	private boolean isvalidProd(Integer integer, Node node) {
		String scoreString = XMLParser.product_scope(node);
		return dataMap.get(categorys[integer]).contains(XMLParser.product_id(node))&&AssertTools.HasImages(scoreString)&&AssertTools.StockStatus(scoreString)&&(AssertTools.TextRelation(scoreString)>=5);
	}
	
	//integer参数
	private boolean validList(Integer integer, List<Node> value) {
		if (value.size()==0) {
			logger.info(" - 该分类无商品");
			return true;
		}else {
			int k = 0;//记录新品数
			for (int i = 0; i < value.size(); i++) {
				if (isvalidProd(integer, value.get(i))) {
					k++;
					continue;
				}else {
					for (int j = i; j < value.size(); j++) {
						if (isvalidProd(integer, value.get(j))) {
							logger.info("相关配置分类"+categorys[integer]+"下前"+k+"个商品为新品");
							logger.info(" - After new products has a new product: "+XMLParser.product_id(value.get(j)));
							return false;
						}
					}
				}
			}
			logger.info("相关配置分类"+categorys[integer]+"下前"+k+"个商品为新品");
		}
		return true;
	}


	private boolean categoryMatch(Object category, String prodCatepath) {
		String b = category.toString().replace(".00", "");
		return prodCatepath.startsWith(b);
	}

	/**
	 * 生成新品分类数据
	 * 抽取5个分类验证
	 * @return
	 */
	public static Map<String, List<String>> newProdCate() {
		
		String aString = StoolData.query4json(baseurl, type);
		Map<String, List<String>> dataMap = JsonParser.newProdData(aString);
		
//		Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
//		String newProdCateString = "01.47.04.01.00.00|01.47.04.04.00.00|01.47.95.00.00.00|01.47.93.04.00.00|01.47.93.02.00.00";
//		String[] newProdCateArray = newProdCateString.split("\\|");
//
//		//抽取5个分类下的PID需要按照配置中分类的顺序加入
//		String newProdString0 = "23665603|23666315|23668263|23666313|23666314|23663852|23668266|23666311|23661219|23666309|23666462|23660177|23663853|23668268|23668267|23658212|23666316|23668265|23666312|23668254|23663851|23666310|23663850|23668264|23663849";
//		String[] newProdArray0 = newProdString0.split("\\|");
//		List<String> newProdList0 = Arrays.asList(newProdArray0);
//
//		String newProdString1 = "23671971|23669127|23668335|23668334|23671973|23669129|23655164|23671974|23669491|23669493|23661085|23656144|23671976|23666935|23669144|23656142|23659120|23669119|23660869|23656140|23650217|23659557|23650215|23661701|23668723|23669492|23671975|23656143|23666936|23669134|23659122|23660870|23666390|23667025|23655165|23669120|23622563|23666391|23666392|23656141|23669128|23655167|23671972|23668336|23659717|23662511|23659121|23660868|23655166";
//		String[] newProdArray1 = newProdString1.split("\\|");
//		List<String> newProdList1 = Arrays.asList(newProdArray1);
//
//		String newProdString2 = "23665900|23665879|23665873|23665896|23665882|23665889|23655168|23665893|23665894|23659695|23665898|23665886|23665899|23665872|23665887|23665888|23665892|23665877|23665885|23665880|23665884|23665883|23665871|23665902|23665895|23665874|23665897|23655169";
//		String[] newProdArray2 = newProdString2.split("\\|");
//		List<String> newProdList2 = Arrays.asList(newProdArray2);
//
//		String newProdString3 = "23660435|23654579|23660175|23654174|23657783|23658940|23661713|23654222|23662109|23653852|23665925|23654223|23665523|23654224|23654228|23650252|23654229|23654227|23654173|23659887|23654230|23654417|23655339|23653465|23654419|23663127|23657724|23655338|23659886|23653466|23654226|23662893|23653851|23660437|23658942|23665924|23665923|23654221|23660436|23655337|23659897|23654225|23666732|23654231|23653464|23654175|23658941|23654232|23650251|23654418|23650253";
//		String[] newProdArray3 = newProdString3.split("\\|");
//		List<String> newProdList3 = Arrays.asList(newProdArray3);
//		
//		String newProdString4 = "23670572|23638834|23666707|23648175|23659898|23662427|23654234|23653474|23654235|23660119|23668855|23663126|23668856|23671083|23654233|23634737|23661297|23653461|23655627|23662879|23653997|23668850|23671712|23671382|23658943|23654177|23639111|23653467|23668852|23661850|23654119|23668396|23653792|23650255|23666211|23665738|23655676|23654236|23667992|23668851|23659854|23639112|23668849|23660158|23668475|23654661|23644987|23669351|23650265|23669350|23667468|23666212|23660595|23634738|23648953|23664259|23650254|23655471|23667696|23669613|23659853|23639115|23671864";
//		String[] newProdArray4 = newProdString4.split("\\|");
//		List<String> newProdList4 = Arrays.asList(newProdArray4);
//
//		dataMap.put(newProdCateArray[0], newProdList0);
//		dataMap.put(newProdCateArray[1], newProdList1);
//		dataMap.put(newProdCateArray[2], newProdList2);
//		dataMap.put(newProdCateArray[3], newProdList3);
//		dataMap.put(newProdCateArray[4], newProdList4);

		return dataMap;
	}
	
	// 调试使用
	public static void main(String[] args) {
		String aString = StoolData.query4json(baseurl, type);
		Map<String, List<String>> dataMap = JsonParser.newProdData(aString);
		
		
		System.out.println(dataMap.get("01.47.04.01.00.00"));
		List<String> aStrings = dataMap.get("01.47.04.01.00.00");
		System.out.println(aStrings.contains("23663849"));
		System.out.println(dataMap.get(dataMap.keySet().toArray()[0]));
		System.out.println(dataMap.toString());

	}
	
	

}
