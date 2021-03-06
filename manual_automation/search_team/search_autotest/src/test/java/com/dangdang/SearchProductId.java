package com.dangdang;

/**
 * Author:dongxiaobing
 * 验证点：搜索product_id，格式为http://10.255.254.71:8390/?product_id=0
 * 验证xml的内容是否正确。例如是否有Product、Totalcnt是否等于1
 * 
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.Calculator;
import com.dangdang.util.Utils;
import com.dangdang.util.XMLParser;

public class SearchProductId {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SearchProductId.class);
	@BeforeMethod 
	public void setup() {  	
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	/*
	 * 搜索产品id
	 */
	//product_id在视图中存在
	@Test(enabled=true)  
	public void search_product_id_test1() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			//"dxb"只是为了调用一个被重载的方法，无特殊意义
			start("id>1 and id<1000");
			//start("id=3");
			long d2 = System.currentTimeMillis();
			logger.info("总耗时："+(d2-d)/1000.0+"秒");
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		}
	}
	/**
	 * add by dongxiaobing
	 * overload start()
	 * @throws Exception 
	 * 
	 */
	public static void start(String pids) throws Exception{
		long d1 = System.currentTimeMillis() ;
		DBAction dba = new DBAction();
		dba.setFuncCondition(pids);
		List<FuncQuery> querys = dba.getPorductId();
		String subject = "【搜索后台自动化测试】基础功能回归测试结果";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>耗时</th></tr>");
		
		// 增加脚本低通过率预警邮件内容，modified by 高彦君 @ 205/06/04 
		String warnSubject = "【搜索后台自动化测试】预警！脚本通过率低";
		StringBuffer warnContent = new StringBuffer();
		warnContent.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>跳过率</th><th>实际通过率</th><th>预期通过率</th></tr>");
		boolean doSendWarnMail = false;
		
		//根据数据库中是否取到的fvps选择验证功能点，fvps取不到则取FuncQuery中的fvps
		int passed=0, failed=0, skiped=0;
		for(FuncQuery query : querys){
			int rt = doQuery(query.getProduct_id());
				switch(rt){
					case 0:
						passed += 1;
						break;
					case -1:
						failed += 1;
						break;
					case -2:
						skiped += 1;
						break;
					default:
						failed += 1;							
				}

		}		
		logger.info(String.format(" - [LOG_SUMMARY] -passed: %s, failed: %s, skiped: %s", passed, failed, skiped));
		long d2 = System.currentTimeMillis();
		String d3 = Math.ceil((d2-d1)/60000.0)+"分钟";
		content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"search_product_id:"+SearchProductId.class.getName(), passed, failed, skiped, passed+failed+skiped, d3));	
		Utils.sendMail(subject, content.toString(), "HTML");
		
		
		double actualPassrate = Calculator.passrate(passed+skiped,passed+failed+skiped);
		double expectedPassrate = 100.00;
		double skipRate = Calculator.skiprate(skiped, passed+failed+skiped);
		// 如果通过率比预期的低，发送邮件
		if(actualPassrate < expectedPassrate || (actualPassrate == expectedPassrate && skipRate > 15.00)){
			warnContent.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"search_product_id:"+SearchProductId.class.getName(), passed, failed, skiped, passed+failed+skiped, skipRate + "%", actualPassrate + "%", expectedPassrate + "%"));
			doSendWarnMail = true;
		}			
		// 增加脚本低通过率预警邮件内容，modified by 高彦君 @ 205/06/04
		if (doSendWarnMail) {
			warnContent.append("</table></body></html>");
			Utils.sendWarningMail(warnSubject, warnContent.toString(), "HTML");
		}
		
		
		
	}			
	public static int doQuery(String pid) throws Exception{
		Map<String,String> urlMap = new HashMap<String,String>();
		System.out.println(pid);
		urlMap.put("product_id", pid);
		
		
		String url = URLBuilder.buildURL(urlMap);
		System.out.println(url);
		
		String xml = SearchRequester.get(url);
		Document doc = XMLParser.read(xml);
		List l1=XMLParser.getProductNodes(doc);
		String totalCnt=XMLParser.getTotalcnt(doc);
		List<String> pids=XMLParser.getPidList(doc);
		if("1".equals(totalCnt)&& l1.size()==1 && pid.equals(pids.get(0))){
			return 0;
		}else{
			return -1;
		}
	}
}