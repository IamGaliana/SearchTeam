package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SortSearchScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SortSearchScheduler.class);
	@BeforeMethod 
	public void setup() {
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	//排序
	@Test(enabled=true, groups="p1")  
	public void sort01() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));	
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id in (1,2,3,4,5)");
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
	
	//排序
		@Test(enabled=false, groups="p1")  
		public void sort02() { 
			try{
				long d = System.currentTimeMillis() ;
				logger.info(Long.toString(d));	
				TestLauncher tl = new TestLauncher();
				tl.start("id < 100", "fvp_id in (1,2)");
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
	
	//排序
	@Test(enabled=false, groups="p1")  
	public void sort03() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));	
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id = 3");
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
	
	//排序
		@Test(enabled=false, groups="p1")  
		public void sort04() { 
			try{
				long d = System.currentTimeMillis() ;
				logger.info(Long.toString(d));	
				TestLauncher tl = new TestLauncher();
				tl.start("id < 100", "fvp_id = 4");
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
	
	//排序
	@Test(enabled=false, groups="p1")  
	public void sort05() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));	
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id = 5");
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