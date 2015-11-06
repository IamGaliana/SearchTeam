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
import com.dangdang.util.Calculator;
import com.dangdang.util.Utils;
import com.dangdang.verifier.childbookpi.ChildBookPriceIntervalVerifier;

public class CbookScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(CbookScheduler.class);
	@BeforeMethod 
	public void setup() {  		
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	//童书价格带->
	@Test(enabled=true, groups="p2")  
	public void vSearch_cbook() { 
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));			
			DBAction dba = new DBAction();
			dba.setFuncCondition("verify_point = 'cbook_pi'");
			List<FuncQuery> list = dba.getFuncQuery();
			int pass=0,fail=0;
			
			String subject = "【搜索后台自动化测试】基础功能回归测试结果";
			StringBuffer content = new StringBuffer();
			content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th></tr>");
			
			// 增加脚本低通过率预警邮件内容，modified by 高彦君 @ 2015/06/04 
			String warnSubject = "【搜索后台自动化测试】预警！脚本通过率低";
			StringBuffer warnContent = new StringBuffer();
			warnContent.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>跳过率</th><th>实际通过率</th><th>预期通过率</th></tr>");
			
			for(FuncQuery query : list){
				try{
				if(ChildBookPriceIntervalVerifier.doVerify(query)){
					logger.info(" - [PASSED] - "+"Children's book Price Interval for:"+query.getFquery());
					pass++;
				}else{
					logger.error(" - [FAILED] - "+"Children's book Price Interval for:"+query.getFquery());
					fail++;
				}
				}catch(Exception e){
					 ByteArrayOutputStream buf = new ByteArrayOutputStream();
					 e.printStackTrace(new PrintWriter(buf, true));
					 String expMessage = buf.toString();
					 logger.error(" - [LOG_EXCEPTION] - "+expMessage);
					logger.error(" - [FAILED] - "+"Children's book Price Interval for:"+query.getFquery());
					e.printStackTrace();
					fail++;
				}
			}
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"童书价格带策略CbookScheduler", pass, fail, 0, pass+fail+0));
			logger.info("Result  ####    Passed Count:"+pass+" Failed Count:"+fail);
			double pr = Calculator.passrate(pass, pass+fail);
			double fr = 100.00 - pr;	
			logger.info("Result  ####    Passed Rate:"+ pr +"% Failed Rate:"+ fr+"%");
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
			
			// 如果通过率比预期的低，发送邮件
			if(pr < 100){
				warnContent.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
						"童书价格带策略CbookScheduler", pass, fail, 0, pass+fail, "0.00%", pr + "%", "100.00%"));
				warnContent.append("</table></body></html>");
				Utils.sendWarningMail(warnSubject, warnContent.toString(), "HTML");
			}
			
			long d2 = System.currentTimeMillis();
			logger.info(Double.toString((d2-d)/1000.0));
	} 
	
	

}
