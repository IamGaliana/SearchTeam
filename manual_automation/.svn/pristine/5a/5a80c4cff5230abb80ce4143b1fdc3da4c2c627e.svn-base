/*
 * ClassName:	DifferentServerCmpScheduler.java
 * Version: 	V1.0
 * Date: 		2015-07-08 18:15
 * copyright
 */

package com.dangdang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Node;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.dangdang.util.FileUtil;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

/**
 * @author gaoyanjun@dangdang.com
 * @version 创建时间：2015-08-19
 * 小搜索走缓存机制
 * 因为只有一套环境，只能用部署前和部署后的不同环境，分别跑脚本，把结果集写入文本文件，再比较文本文件
 * 属于一次性脚本
 */
public class MiniSearchCacheScheduler {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MiniSearchCacheScheduler.class);
	List<String> RequestURLs = new ArrayList<String>();
	
	{		
		RequestURLs.add("http://10.3.255.227:8390/?shop_id=13207&_url_token=26&st=mall");
		RequestURLs.add("http://10.3.255.227:9615/?shop_id=13207&_url_token=26&st=mall");		
		RequestURLs.add("http://10.3.255.227:8390/?st=mall&um=search_ranking&shop_id=4609&inner_cat=0020021&ps=24&pg=1&_url_token=2");
		RequestURLs.add("http://10.3.255.227:9615/?st=mall&um=search_ranking&shop_id=4609&inner_cat=0020021&ps=24&pg=1&_url_token=2");		
		RequestURLs.add("http://10.3.255.227:8390/?st=mall&um=search_ranking&shop_id=15263&ps=10&fs=first_input_date&_url_token=2");
		RequestURLs.add("http://10.3.255.227:9615/?st=mall&um=search_ranking&shop_id=15263&ps=10&fs=first_input_date&_url_token=2");		
		RequestURLs.add("http://10.3.255.227:8390/?um=search_ranking&st=mall&gp=cat_paths,brand_id,attrib,promotion_type&shop_id=11338&pg=2&q=%B5%D8%CD%BC&fs=sale_week&fa=0&lowp=&highp=&promo=&type=&ps=24&_url_token=4391051277507022478");
		RequestURLs.add("http://10.3.255.227:9615/?um=search_ranking&st=mall&gp=cat_paths,brand_id,attrib,promotion_type&shop_id=11338&pg=2&q=%B5%D8%CD%BC&fs=sale_week&fa=0&lowp=&highp=&promo=&type=&ps=24&_url_token=4391051277507022478");		
		RequestURLs.add("http://10.3.255.227:8390/?um=list_ranking&st=mall&gp=cat_paths,brand_id,attrib,promotion_type&shop_id=4285&p=&-attrib=24073:3-24012:3&fs=sale_week&fa=0&lowp=&highp=&promo=&inner_cat=0070001&type=&ps=36");
		RequestURLs.add("http://10.3.255.227:9615/?um=list_ranking&st=mall&gp=cat_paths,brand_id,attrib,promotion_type&shop_id=4285&p=&-attrib=24073:3-24012:3&fs=sale_week&fa=0&lowp=&highp=&promo=&inner_cat=0070001&type=&ps=36");
		
		RequestURLs.add("http://10.3.255.227:8390/?shop_id=5048&um=list_ranking&st=mall");
		RequestURLs.add("http://10.3.255.227:9615/?shop_id=5048&um=list_ranking&st=mall");		
		RequestURLs.add("http://10.3.255.227:8390/?shop_id=0&um=search_ranking&st=pub&gp=cat_paths&-is_promotion=1&-is_publication=1&_cat_paths=58&sm=promo_pack&logid=1439863201.977432&_url_token=17907897404250837312");
		RequestURLs.add("http://10.3.255.227:9615/?shop_id=0&um=search_ranking&st=pub&gp=cat_paths&-is_promotion=1&-is_publication=1&_cat_paths=58&sm=promo_pack&logid=1439863201.977432&_url_token=17907897404250837312");		
		RequestURLs.add("http://10.3.255.227:8390/?cat_paths=01.05&pg=4&ps=40&fs=publish_date&fa=0&-custom_discount=0~30&-debate_price=10~15&gp=cat_paths&um=list_ranking&st=pub&-discount=0~39&_url_token=12740733061298605582");
		RequestURLs.add("http://10.3.255.227:9615/?cat_paths=01.05&pg=4&ps=40&fs=publish_date&fa=0&-custom_discount=0~30&-debate_price=10~15&gp=cat_paths&um=list_ranking&st=pub&-discount=0~39&_url_token=12740733061298605582");		
		RequestURLs.add("http://10.3.255.227:8390/?um=search_ranking&st=full&ps=200&_url_token=37&product_id=1295269705,1106695706");
		RequestURLs.add("http://10.3.255.227:9615/?um=search_ranking&st=full&ps=200&_url_token=37&product_id=1295269705,1106695706");		
//		RequestURLs.add("http://10.3.255.227:8390/?q=linux&um=search_ranking&_url_token=21&st=full");
//		RequestURLs.add("http://10.3.255.227:9615/?q=linux&um=search_ranking&_url_token=21&st=full");
//		RequestURLs.add("http://10.3.255.227:8390/?st=full&um=list_ranking&ps=20&-promotion_type=1~&-is_dd_sell=0&cat_paths=58.59&_url_token=665810015725621256");
//		RequestURLs.add("http://10.3.255.227:9615/?st=full&um=list_ranking&ps=20&-promotion_type=1~&-is_dd_sell=0&cat_paths=58.59&_url_token=665810015725621256");		
//		RequestURLs.add("http://10.3.255.227:8390/?product_id=1311648630&_url_token=17065584913147750150");
//		RequestURLs.add("http://10.3.255.227:9615/?product_id=1311648630&_url_token=17065584913147750150");
//		RequestURLs.add("http://10.3.255.227:8390/?product_id=1295269705,1106695706&_url_token=17065584913147750150");
//		RequestURLs.add("http://10.3.255.227:8390/?product_id=1295269705,1106695706&_url_token=17065584913147750150");
		
	}
	
	@BeforeMethod
	public void setup() {
	}

	@AfterMethod
	public void clearup() {
	}

	@AfterClass
	public void calssclearup() {

	}

	
	//@Test(enabled = true, groups = "p2")
	public void Run(boolean flag) {
		try{		
			List<String> pids = new ArrayList<String>();			 
			for(String url : RequestURLs){	
				logger.info("Start:" + url);
				pids.clear();
				// 取到结果集中的pid，写入list
				ProdIterator initialIterator1 = new ProdIterator(1200, url, true);				
				while(initialIterator1.hasNext()){
					Node node = initialIterator1.next(url,true);
					String pid = XMLParser.product_id(node);
					pids.add(pid);
				}
				
				// 这个地方文件的命名只是用来做参考
				// 需要对比的有：部署后开缓存机制&不开缓存机制；部署后第一次写入缓存&后续命中缓存；部署前&部署后；
				// 以上每组，2个结果集都应该一样
				String filepath1 = "E:\\manual_automation\\txtFiles\\" + RequestURLs.indexOf(url) + "_newwithcache.txt";
				String filepath2 = "E:\\manual_automation\\txtFiles\\" + RequestURLs.indexOf(url) + "_newnocache.txt";
				
				// flag代表第几次执行程序， true表示第一次，false表示第二次，这两次分别代表新、旧版本
				// 把list中的pid写入文件
				if(flag){
					FileUtil.writeToTxt(pids.toString(), filepath1);
				}else
					FileUtil.writeToTxt(pids.toString(), filepath2);
				
				//只有两次都跑完了，才会有前缀、后缀以_1/_2区分的文件名，然后比较一下其中的内容
				File file1 = new File(filepath1);
				File file2 = new File(filepath2);
				
				if(file1.exists() && file2.exists())
					FileUtil.compareFiles(filepath1, filepath2);
					//compareFile(filepath1,filepath2);
				logger.info("Done:" + url);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	private static void compareFile(String oldFile, String newFile) throws IOException{
//		logger.info("compare files:" + oldFile + " and " + newFile);
//		FileReader freader1 = new FileReader(oldFile);
//		@SuppressWarnings("resource")
//		BufferedReader breader1 = new BufferedReader(freader1);
//		String s1=null;
//		
//		int i=0;
//		boolean isSame = true;
//		
//		FileReader freader2 = new FileReader(newFile);
//		@SuppressWarnings("resource")
//		BufferedReader breader2 = new BufferedReader(freader2); 
//		String s2=null;
//		
//		while((s1 = breader1.readLine())!=null){
//			while((s2 = breader2.readLine())!=null){
//				if(s1.equals(s2) != true){ 
//					logger.info("第"+i+"行:"+s1+"和"+s2+"内容不一样");
//					//System.out.println("第"+i+"行:"+s1+"和"+s2+"内容不一样");
//					isSame = false;
//					break;
//				}
//			}
//		}
//	}

	public static void main(String[] args){		
		MiniSearchCacheScheduler s = new MiniSearchCacheScheduler();
		//新版本，把下边一行注释掉跑第一次
		//s.Run(true);
		//旧版本，把上边一行注释掉跑第二次
		s.Run(false);
	}

}


