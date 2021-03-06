package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2015年4月1日 上午11:46:21  
 * 类说明  教育新品加权策略
 */
public class NewProdWeightScheduler {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(NewProdWeightScheduler.class);
	
	@BeforeMethod 
	public void setup() {  	
	}  

	@AfterMethod 
	public void clearup() { 
	} 
	
	@Test(enabled=true, groups="p1")  
	public void newProdWeight() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();			
			tl.start("id > 181711 AND id < 182117", "fvp_id = 33");
			long d2 = System.currentTimeMillis();
			logger.info(Double.toString((d2 - d) / 1000.0));
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
