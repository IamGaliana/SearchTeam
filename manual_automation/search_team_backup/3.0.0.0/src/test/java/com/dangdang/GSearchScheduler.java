package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
public class GSearchScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(GSearchScheduler.class);
	
	@BeforeMethod 
	public void setup() {
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	
	//图书->高级搜索基础查询
	@Test(enabled=true, groups="p2")
	public void gSearch_book_search() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_book_search"));
		try{
			long d = System.currentTimeMillis();
			logger.info(Long.toString(d));		
			TestLauncher tl = new TestLauncher();
			tl.start("id between 727 and 736", "fvp_id in (0)");
//			tl.start("id between 727 and 766", "fvp_id in (0)");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_book_search:"+(d2-d)/1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		}
	} 
	
	//图书->高级搜索列表查询
	@Test(enabled=true, groups="p2")
	public void gSearch_book_list() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_book_list"));
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));		
			TestLauncher tl = new TestLauncher();
			tl.start("id between 767 and 776", "fvp_id in (0)");
//			tl.start("id between 767 and 806", "fvp_id in (0)");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_book_list:"+(d2-d)/1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		}
	}
	
	//图书->高级搜索综合查询
	@Test(enabled=true, groups="p2") 
	public void gSearch_book_merge() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_book_merge"));
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));			
			TestLauncher tl = new TestLauncher();
			tl.start("id between 807 and 817", "fvp_id in (0)");
//			tl.start("id between 807 and 827", "fvp_id in (0)");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_book_merge:"+(d2-d)/1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		}
	}
	
	//电子书->高级搜索基础查询
	@Test(enabled=true, groups="p2") 
	public void gSearch_ebook_search() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_ebook_search"));
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));	
			TestLauncher tl = new TestLauncher();
			tl.start("id between 828 and 837", "fvp_id in (0)");
//			tl.start("id between 828 and 867", "fvp_id in (0)");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_ebook_search:"+(d2-d)/1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		}
	} 
	
	//电子书->高级搜索列表查询
	@Test(enabled=true, groups="p2")
	public void gSearch_ebook_list() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_ebook_list"));
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));	
			TestLauncher tl = new TestLauncher();
			tl.start("id between 868 and 877", "fvp_id in (0)");
//			tl.start("id between 868 and 907", "fvp_id in (0)");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_ebook_list:"+(d2-d)/1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		} 
	}
	
	//电子书->高级搜索综合查询
	@Test(enabled=true, groups="p2")  
	public void gSearch_ebook_merge() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_ebook_merge"));
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id between 908 and 917", "fvp_id in (0)");
//			tl.start("id between 908 and 926", "fvp_id in (0)");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_ebook_merge:"+(d2-d)/1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		}
	}
	
	//数字->高级搜索数字商品查询
	@Test(enabled=true, groups="p2")
	public void gSearch_digital() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_digital"));
		try{
			long d = System.currentTimeMillis();
			logger.info(Long.toString(d));	
			TestLauncher tl = new TestLauncher();
			tl.start("id between 1229 and 1258", "1=2");
//			tl.start("id between 1229 and 1328", "1=2");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_digital:"+(d2-d)/1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		}
	}
	
	//音乐->高级搜索音乐查询
	@Test(enabled=true, groups="p2")  
	public void gSearch_music() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_music"));
		try{
			long d = System.currentTimeMillis();
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id between 1329 and 1356", "1=2");
//			tl.start("id between 1329 and 1426", "1=2");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_music:"+(d2-d)/1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		}
	}
	
	//影视->高级搜索影视查询
	@Test(enabled=true, groups="p2")  
	public void gSearch_video() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_video"));
		try{
			long d = System.currentTimeMillis();
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id between 1427 and 1456", "1=2");
//			tl.start("id between 1427 and 1526", "1=2");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_video:"+(d2-d)/1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		} finally {	
		}
	}
	
	//百货->高级搜索百货查询
	@Test(enabled=true, groups="p2")  
	public void gSearch_b2c() {   		
//		PropertyConfigurator.configure(String.format("conf/%s_log4j.properties", "gSearch_b2c"));
		try{
			long d = System.currentTimeMillis();
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id between 1530 and 1560", "1=2");
//			tl.start("id between 1530 and 1760", "1=2");
			long d2 = System.currentTimeMillis();
			logger.info("eslipse for gSearch_b2c:"+(d2-d)/1000.0);
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
