package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NoStockDropWeightScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(NoStockDropWeightScheduler.class);
	@BeforeMethod 
	public void setup() {  	
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	/**
	 * 无库存降权策略
	 */
	@Test(enabled=true)  
	public void NoStockDropWeightTest() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("1 =1 and verify_point='NoStockDropWeight'", "fvp_id=48");
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
