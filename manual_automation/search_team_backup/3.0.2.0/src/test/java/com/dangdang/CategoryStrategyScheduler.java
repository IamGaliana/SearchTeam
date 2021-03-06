package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2014年10月30日 下午5:07:39  
 * 类说明  分类页自营图书靠前
 */
public class CategoryStrategyScheduler {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryStrategyScheduler.class);
	@BeforeMethod 
	public void setup() {  	
	}  

	@AfterMethod 
	public void clearup() { 
	} 
	
	//临时指定分类策略
	@Test(enabled=true, groups="p2")  
	public void book_strategy() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));	
			TestLauncher tl = new TestLauncher();
			tl.l_start("verify_point = 'book_strategy'", "fvp_id=24");
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
	
	//临时指定分类策略,童书
	@Test(enabled=true, groups="p2")  
	public void children_strategy() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.l_start("verify_point = 'children_strategy'", "fvp_id=25");
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
	
	//其他分类按照Score排序
	@Test(enabled=true, groups="p2")  
	public void other_score() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.l_start("verify_point = 'other_score'", "fvp_id=27");
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
	
	
	
	//分类页重复品校验
	@Test(enabled=true, groups="p2")  
	public void list_duplicate() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));	
			TestLauncher tl = new TestLauncher();
			tl.l_start("verify_point in ('children_strategy','book_strategy')", "fvp_id=26");
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
