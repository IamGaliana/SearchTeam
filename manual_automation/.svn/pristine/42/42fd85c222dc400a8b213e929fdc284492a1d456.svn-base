/*
 * ClassName:	IntelligenceCorrectionScheduler.java
 * Version: 	V1.0
 * Date: 		2015-11-14 10:24
 * copyright
 */

package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncQuery;
import com.dangdang.data.FuncVP;
import com.dangdang.util.Calculator;
import com.dangdang.util.Utils;
import com.dangdang.util.XMLParser;

/**
 * @author gaoyanjun@dangdang.com
 * @version 创建时间：2015-06-30
 * 比较两个不同server下，同一query返回的结果集是否相同
 */
public class IntelligenceCorrectionScheduler {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(IntelligenceCorrectionScheduler.class);
	
	
	@BeforeMethod
	public void setup() {
	}

	@AfterMethod
	public void clearup() {
	}

	@AfterClass
	public void calssclearup() {

	}

	
	@Test(enabled = true, groups = "p2")
	public void IntelligenceCorrection() {
		String subject = "【搜索后台自动化测试】智能纠错";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>耗时</th></tr>");

		String warnSubject = "【搜索后台自动化测试】预警！脚本通过率低";
		StringBuffer warnContent = new StringBuffer();
		warnContent.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>跳过率</th><th>实际通过率</th><th>预期通过率</th></tr>");
		
		try{
			long d = System.currentTimeMillis();
			logger.info("Start at: " + Long.toString(d));
			DBAction dba = new DBAction();
			dba.setFvpCondition("fvp_id = 56");
			dba.setFuncCondition("1=1 and verify_point = 'intelligence_correction' "); 			
			List<FuncQuery> queryList = dba.getFuncQuery();		
			List<FuncVP> fvp = dba.getFVP();
			
			int pass = 0, fail = 0, skip = 0;
			for (FuncQuery fq : queryList){
				String query = fq.getFquery();
				int rt = doQuery(query);
				switch(rt){
					case 0:
						pass +=1;
						break;
					case -1:
						fail +=1;
						break;
					case -2:
						skip +=1;
						break;
					default:
						fail +=1;
						break;
				}
			}			
			
			long d2 = System.currentTimeMillis();
			int d4 = (int) ((d2 - d) / 60000 + 1);
			logger.info("总耗时:" + d4 + "分钟");
			logger.info(String.format(" - [LOG_SUMMARY] - vp: %s, passed: %s, failed: %s, skiped: %s", 
					fvp.get(0).getFvpname(), pass, fail, skip));
			
			//String d3 = String.valueOf(d4) + "分钟";
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					fvp.get(0).getFvpname(), pass, fail, skip,
					pass + fail + skip, d4 + "分钟"));
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
			
			double actualPassrate = Calculator.passrate(pass+skip,pass+fail+skip);
			double expectedPassrate = fvp.get(0).getMinPassrate();
			double skipRate = Calculator.skiprate(skip, pass+fail+skip);
			double maxSkipRate = fvp.get(0).getMaxSkiprate();
			if(actualPassrate < expectedPassrate || (actualPassrate == expectedPassrate && skipRate > maxSkipRate)){
				warnContent.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
						fvp.get(0).getFvpname(), pass, fail, skip, pass+fail+skip, skipRate + "%", actualPassrate + "%" , expectedPassrate + "%"));
				warnContent.append("</table></body></html>");
				Utils.sendWarningMail(warnSubject, warnContent.toString(), "HTML");
			}			
		} catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		
		}
	}
	
	private int doQuery(String query){
		logger.info(String.format(" - [LOG SUMMARY] - query :%s", query));
		// 构建query url
		Map<String, String> map = new HashMap<String, String>();
		map = URLBuilder.converURLPars("",query,null);
		String url = URLBuilder.buildURL(map);
		// 请求url
		String xml = SearchRequester.get(url);
		// 得到xml
		// 是否有想要的节点
		// 没有跳过
		// 有的话比较两个节点值，如果不同，pass；否则fail
		try {
			Document doc = XMLParser.read(xml);
			String originalQuery = XMLParser.getOriginalQuery(doc);
			String fixedQuery = XMLParser.getFixedQuery(doc);
			
			if(originalQuery.isEmpty()){
				logger.info(String.format(" - [LOG SKIP] - query:%s, original query: %s, fixed query: %s",query, originalQuery, fixedQuery));
				return -2;
			}else{
				if(fixedQuery.isEmpty()){
					logger.info(String.format(" - [LOG SKIP] - query:%s, original query: %s, fixed query: %s",query, originalQuery, fixedQuery));
					return -2;
				}else if(originalQuery.equals(fixedQuery)){
					logger.error(String.format(" - [LOG FAILED] - query:%s, original query: %s, fixed query: %s",query, originalQuery, fixedQuery));
					return -1;
				}else if(!originalQuery.equals(fixedQuery)){
					logger.info(String.format(" - [LOG PASS] - query:%s, original query: %s, fixed query: %s",query, originalQuery, fixedQuery));
					return 0;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
}
