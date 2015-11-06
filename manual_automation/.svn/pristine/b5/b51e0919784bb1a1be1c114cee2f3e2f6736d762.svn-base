/*
 * ClassName:	DifferentServerCmpScheduler.java
 * Version: 	V1.0
 * Date: 		2015-07-08 18:15
 * copyright
 */

package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.data.BlackListQuery;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.Calculator;
import com.dangdang.util.JsonParser;
import com.dangdang.util.StoolData;
import com.dangdang.util.Utils;
import com.dangdang.verifier.blacklist.BrandThroughVerifier;
import com.dangdang.verifier.blacklist.CategoryDirectionVerifier;
import com.dangdang.verifier.blacklist.ListRankingBlackListVerifier;
import com.dangdang.verifier.blacklist.SearchRankingBlackListVerifier;
import com.dangdang.verifier.blacklist.SearchThroughVerifier;
import com.dangdang.verifier.blacklist.ShopThroughVerifier;
import com.dangdang.verifier.compare.DifferentServerCmpVerifier;

/**
 * @author gaoyanjun@dangdang.com
 * @version 创建时间：2015-06-30
 * 比较两个不同server下，同一query返回的结果集是否相同
 */
public class DifferentServerCmpScheduler {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DifferentServerCmpScheduler.class);
	
	
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
	public void CompareResult() {
		String subject = "【搜索后台自动化测试】两server之间结果集对比";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>耗时</th></tr>");
		try{
			long d = System.currentTimeMillis();
			logger.info("Start at: " + Long.toString(d));
			DBAction dba = new DBAction();
			dba.setFuncCondition("1=1 and id <=182157 "); 
			
			List<FuncQuery> queryList = dba.getFuncQuery();
			DifferentServerCmpVerifier verifier = new DifferentServerCmpVerifier();
			
			int pass = 0, fail = 0;
			for (FuncQuery fq : queryList){
				try{
					if(verifier.Verifier(fq.getFquery())){
						logger.info(" - [PASSED] - Different Server Compare Verifier for: " + fq.getFquery());
						pass ++;
					} else {
						logger.info(" - [FAILED] - Different Server Compare Verifier for: " + fq.getFquery());
						fail ++;
					}
					
				}catch(Exception e){
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					e.printStackTrace(new PrintStream(baos));
					String exception = baos.toString();
					logger.error(" - [LOG_EXCEPTION] - " + exception);
					fail ++;
				}
			}
			
			long d2 = System.currentTimeMillis();
			String diff = (d2 - d) / 1000.0 + "seconds";
			logger.info("总耗时：" + diff);
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", "List黑名单", pass, fail, "0",
					pass + fail, diff));
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
		} catch(Exception e){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		
		}
	}
	
}
