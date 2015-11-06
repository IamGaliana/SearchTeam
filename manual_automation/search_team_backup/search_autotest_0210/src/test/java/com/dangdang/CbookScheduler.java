package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.Utils;
import com.dangdang.verifier.childbookpi.ChildBookPriceIntervalVerifier;

public class CbookScheduler {
	private static Logger logger = Logger.getLogger(VSearchScheduler.class);
	{
		PropertyConfigurator.configure("conf/cSearch_log4j.properties");
	}
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
			logger.fatal(d);			
			DBAction dba = new DBAction();
			dba.setFuncCondition("verify_point = 'cbook_pi'");
			List<FuncQuery> list = dba.getFuncQuery();
			int pass=0,fail=0;
			String subject = "【搜索后台自动化测试】基础功能回归测试结果";
			StringBuffer content = new StringBuffer();
			content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th></tr>");
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
					"Child Book Price Interval", pass, fail, 0, pass+fail+0));
			logger.fatal("Result  ####    Passed Count:"+pass+" Failed Count:"+fail);
			double pr = Double.valueOf(pass)/(pass+fail)*100;
			double fr = Double.valueOf(fail)/(fail+pass)*100;
			logger.fatal("Result  ####    Passed Rate:"+Math.round(pr)+"% Failed Rate:"+Math.round(fr)+"%");
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
			long d2 = System.currentTimeMillis();
			logger.fatal((d2-d)/1000.0);
	} 
	
	

}
