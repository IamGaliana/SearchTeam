package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.Utils;
import com.dangdang.verifier.blacklist.NoPictureProductVerifier;
import com.dangdang.verifier.blacklist.TopProductBlackListVerifier;

public class TopAndNoPictureProductScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(TopAndNoPictureProductScheduler.class);
	@BeforeMethod 
	public void setup() {  		
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	//单品置顶黑名单->
	@Test(enabled=false, groups="p2")  
	public void topProductBlackList() { 
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));			
			DBAction dba = new DBAction();
			dba.setFuncCondition("verify_point = 'top_product_blacklist'");
			List<FuncQuery> list = dba.getFuncQuery();
			int pass=0,fail=0;
			String subject = "【搜索后台自动化测试】基础功能回归测试结果";
			StringBuffer content = new StringBuffer();
			content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th></tr>");
			for(FuncQuery query : list){
				try{
				if(TopProductBlackListVerifier.doVerify(query)){
					logger.info(" - [PASSED] - "+"top product blacklist for:"+query.getFquery());
					pass++;
				}else{
					logger.info(" - [FAILED] - "+"top product blacklist for:"+query.getFquery());
					fail++;
				}
				}catch(Exception e){
					 ByteArrayOutputStream buf = new ByteArrayOutputStream();
					 e.printStackTrace(new PrintWriter(buf, true));
					 String expMessage = buf.toString();
					 logger.error(" - [LOG_EXCEPTION] - "+expMessage);
					logger.error(" - [FAILED] - "+"top product blacklist for:"+query.getFquery());
					e.printStackTrace();
					fail++;
				}
			}
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"top product blacklist", pass, fail, 0, pass+fail+0));
			logger.info("Result  ####    Passed Count:"+pass+" Failed Count:"+fail);
			double pr = Double.valueOf(pass)/(pass+fail)*100;
			double fr = Double.valueOf(fail)/(fail+pass)*100;
			logger.info("Result  ####    Passed Rate:"+Math.round(pr)+"% Failed Rate:"+Math.round(fr)+"%");
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
			long d2 = System.currentTimeMillis();
			logger.info(Double.toString((d2 - d) / 1000.0));
	} 
	
	//无图片商品策略->
	@Test(enabled=true, groups="p2")  
	public void noPictureProduct() { 
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));			
			DBAction dba = new DBAction();
			dba.setFuncCondition("id < 100");
			List<FuncQuery> list = dba.getFuncQuery();
			int pass=0,fail=0;
			String subject = "【搜索后台自动化测试】基础功能回归测试结果";
			StringBuffer content = new StringBuffer();
			content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th></tr>");
			for(FuncQuery query : list){
				try{
				if(NoPictureProductVerifier.doVerify(query)){
					logger.info(" - [PASSED] - "+"no picture product for:"+query.getFquery());
					pass++;
				}else{
					logger.info(" - [FAILED] - "+"no picture product for:"+query.getFquery());
					fail++;
				}
				}catch(Exception e){
					 ByteArrayOutputStream buf = new ByteArrayOutputStream();
					 e.printStackTrace(new PrintWriter(buf, true));
					 String expMessage = buf.toString();
					 logger.error(" - [LOG_EXCEPTION] - "+expMessage);
					logger.error(" - [FAILED] - "+"no picture product for:"+query.getFquery());
					e.printStackTrace();
					fail++;
				}
			}
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"top product blacklist", pass, fail, 0, pass+fail+0));
			logger.info("Result  ####    Passed Count:"+pass+" Failed Count:"+fail);
			double pr = Double.valueOf(pass)/(pass+fail)*100;
			double fr = Double.valueOf(fail)/(fail+pass)*100;
			logger.info("Result  ####    Passed Rate:"+Math.round(pr)+"% Failed Rate:"+Math.round(fr)+"%");
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
			long d2 = System.currentTimeMillis();
			logger.info(Double.toString((d2 - d) / 1000.0));
	}
}
