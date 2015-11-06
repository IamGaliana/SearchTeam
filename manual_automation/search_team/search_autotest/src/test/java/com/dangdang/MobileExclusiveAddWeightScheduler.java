package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MobileExclusiveAddWeightScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MobileExclusiveAddWeightScheduler.class);
	@BeforeMethod 
	public void setup() {  	
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	/**
	 * 对无线端手机专享价的商品加商业因素分
	 */
	@Test(enabled=true)  
	public void MobileExclusiveAddWeightTest() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("verify_point = 'mbexclusive_addweight' ", "fvp_id=44", true);
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