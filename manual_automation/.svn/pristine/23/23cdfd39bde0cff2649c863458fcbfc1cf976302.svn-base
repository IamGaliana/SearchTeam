package com.dangdang;
/**
 * Author:dongxiaobing
 * 验证点：
 * 先得到（query+需要被置顶的分类）的产品列表1
 * 在得到query的产品列表2
 * 比较产品列表1的前7个pid在产品列表2的前7个当中

 * 
 * 共运营工具拉取分类反馈的规则：
 * 1.分类反馈是由搜索后台直接访问运营工具来拉取分类反馈的数据，不需要经过数据中心
 * 2.拉取的脚本为inc_operator.py  /d1/search/search_v3/inc_operator
 * 3.拉取的命令为 http://10.255.254.72:8898/?realindex=1&ret_type=json&_creation_date=2015-07-30+13%3A00%3A39&um=search_keyword&op_type=query&type=key_cat
 * 4.会比较_creation_date=2015-07-30+13%3A00%3A39与运营工具上分类反馈的创建时间，例如_creation_date=2015-07-30+13%3A00%3A39，拉取的频率是一分钟，那么可以把运营工具分类反馈
 * 的创建时间改为2015-07-30+13%3A00%3A39之后的时间就可以了，等到达这个创建时间，就会拉取配置
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CategoryFeedback {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryFeedback.class);
	@BeforeMethod 
	public void setup() {  	
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	/*
	 * 分类反馈
	 */
	@Test(enabled=true)  
	public void category_feedback_test1() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			//"dxb"只是为了调用一个被重载的方法，无特殊意义
			tl.start("verify_point='category_feedback'", "fvp_id=42","dxb");
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