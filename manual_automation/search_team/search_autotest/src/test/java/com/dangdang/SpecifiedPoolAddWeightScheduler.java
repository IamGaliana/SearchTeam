package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class SpecifiedPoolAddWeightScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SpecifiedPoolAddWeightScheduler.class);
	@BeforeMethod 
	public void setup() {  		
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	//搜索页指定商品池&品牌&店铺排序加权
	@Test(enabled=true)  
	public void SpecifiedPoolAddWeight()  { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("verify_point = 'specified_pool'", "fvp_id = 35", true);
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
}
