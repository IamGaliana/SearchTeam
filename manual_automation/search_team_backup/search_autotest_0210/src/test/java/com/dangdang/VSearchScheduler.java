package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.data.VerticalSearchQuery;
import com.dangdang.util.Utils;
import com.dangdang.verifier.iVerifier.IVerticalSearchVerifer;
import com.dangdang.verifier.verticalSearch.B2cVsVerifier;
import com.dangdang.verifier.verticalSearch.ClothVSVerifier;
import com.dangdang.verifier.verticalSearch.PubVSVerifier;

public class VSearchScheduler {
	private static Logger logger = Logger.getLogger(VSearchScheduler.class);
	{
		PropertyConfigurator.configure("conf/vSearch_log4j.properties");
	}
	@BeforeMethod 
	public void setup() {  		
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	//垂直搜索->
	@Test(enabled=true, groups="p2")  
	public void vSearch_XX() { 
		
			String subject = "【搜索后台自动化测试】基础功能回归测试结果";
			StringBuffer content = new StringBuffer();
			content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th></tr>");
			long d = System.currentTimeMillis() ;
			logger.fatal(d);			
			DBAction dba = new DBAction();
			dba.setVsq_condition(" t.query_type = 'PUB2' group by t.query");
			List<VerticalSearchQuery> vsQueryPUB = dba.getVerticalSearchQuery();
			dba.setVsq_condition(" t.query_type = 'FZ2' group by t.query");
			List<VerticalSearchQuery> vsQueryFZ = dba.getVerticalSearchQuery();
			dba.setVsq_condition(" t.query_type = 'BH2' ");
			List<VerticalSearchQuery> vsQueryBH = dba.getVerticalSearchQuery();
			IVerticalSearchVerifer veriferPUB = new PubVSVerifier();
			IVerticalSearchVerifer veriferFZ = new ClothVSVerifier();
			IVerticalSearchVerifer veriferBH = new B2cVsVerifier();
			int pass=0,fail=0;
			for(VerticalSearchQuery query:vsQueryPUB){
				try{
				if(veriferPUB.verifier(query)){
					logger.info(" - [PASSED] - "+"Vertical Search for:"+query.getQuery().toString());
					pass++;
				}else{
					logger.error(" - [FAILED] - "+"Vertical Search for:"+query.getQuery());
					fail++;
				}
				}catch(Exception e){
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();  
					e.printStackTrace(new PrintStream(baos));  
					String exception = baos.toString();  
					logger.error(" - [LOG_EXCEPTION] - "+exception);
					logger.error(" - [FAILED] - "+"Vertical Search for:"+query.getQuery());
					e.printStackTrace();
					fail++;
				}
			}
			 
			for(VerticalSearchQuery query:vsQueryFZ){
				try{
				if(veriferFZ.verifier(query)){
					logger.info(" - [PASSED] - "+"Vertical Search for:"+query.getQuery());
					pass++;
				}else{
					logger.error(" - [FAILED] - "+"Vertical Search for:"+query.getQuery());
					fail++;
				}
			}catch(Exception e){
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				e.printStackTrace(new PrintStream(baos));  
				String exception = baos.toString();  
				logger.error(" - [LOG_EXCEPTION] - "+exception);
				logger.error(" - [FAILED] - "+"Vertical Search for:"+query.getQuery());
				e.printStackTrace();
				fail++;
			}
			}
			
			for(VerticalSearchQuery query:vsQueryBH){
				try{
				if(veriferBH.verifier(query)){
					logger.info(" - [PASSED] - "+"Vertical Search for:"+query.getQuery());
					pass++;
				}else{
					logger.error(" - [FAILED] - "+"Vertical Search for:"+query.getQuery());
					fail++;
				}
			}catch(Exception e){
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				e.printStackTrace(new PrintStream(baos));  
				String exception = baos.toString();  
				logger.error(" - [LOG_EXCEPTION] - "+exception);
				logger.error(" - [FAILED] - "+"Vertical Search for:"+query.getQuery());
				e.printStackTrace();
				fail++;
			}
			}
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"Vertical Search", pass, fail, 0, pass+fail+0));
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
