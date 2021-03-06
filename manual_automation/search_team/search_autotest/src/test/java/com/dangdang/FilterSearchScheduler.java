package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FilterSearchScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(FilterSearchScheduler.class);
	@BeforeMethod 
	public void setup() {  	
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	//过滤->all
	@Test(enabled=true, groups="p1")  
	public void filter_all() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();			
			//tl.start("id < 638", "fvp_id in (6,8,9,10,11,12,13,14,15,18,22,23,36,37,38)");
			tl.start("id =4", "fvp_id in (23)");
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
	
	
	//过滤->
	@Test(enabled=false, groups="p1")  
	public void filter_1() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id = 6");
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
	@Test(enabled=false, groups="p1")  
	public void filter_2() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id =8");
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
	@Test(enabled=false, groups="p1")  
	public void filter_3() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id =9");
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
	@Test(enabled=false, groups="p1")  
	public void filter_4() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id=10");
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
	@Test(enabled=false, groups="p1")  
	public void filter_5() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id =11");
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
	@Test(enabled=false, groups="p1")  
	public void filter_6() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id=12");
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
	@Test(enabled=false, groups="p1")  
	public void filter_7() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id =13");//品牌过滤
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
	@Test(enabled=false, groups="p1")  
	public void filter_8() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id=14");//材质过滤
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
	
	
	@Test(enabled=false, groups="p1")  
	public void filter_9() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id=15");//分类过滤
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
	@Test(enabled=false, groups="p1")  
	public void filter_10() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id=18");
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
	//PC手机专享价
	@Test(enabled=false, groups="p1")  
	public void filter_11() { 
		try{
			long d = System.currentTimeMillis();
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id=22");
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
	//无线
	@Test(enabled=false, groups="p1")  
	public void filter_12() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 100", "fvp_id=23");
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
	
	
	/*
	 * 统计品牌直达,效果测试使用
	 */
	@Test(enabled=false)  
	public void search_ranking() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 200000", "fvp_id=31");
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
