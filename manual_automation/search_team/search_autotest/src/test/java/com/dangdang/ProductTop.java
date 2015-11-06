package com.dangdang;

/**
 * Author:dongxiaobing
 * 验证点：根据query，把运营工具中设置的单品置顶的产品id在搜索结果集中置顶
 * * 共运营工具拉取单品置顶的规则：
 * 1.单品置顶是由搜索后台直接访问运营工具来拉取单品置顶的数据，不需要经过数据中心
 * 2.拉取的脚本为inc_operator.py  /d1/search/search_v3/inc_operator
 * 3.拉取的命令为http://10.255.254.72:8898/?realindex=1&ret_type=json&_creation_date=2015-07-30+13%3A00%3A39&um=search_keyword&op_type=query&type=key_pid
 * 4.会比较_creation_date=2015-07-30+13%3A00%3A39与运营工具上单品置顶的创建时间，例如_creation_date=2015-07-30+13%3A00%3A39，拉取的频率是一分钟，那么可以把运营工具单品置顶
 * 的创建时间改为2015-07-30+13%3A00%3A39之后的时间就可以了，等到达这个创建时间，就会拉取配置
 * 5.如果是图书类型的，并且不是当当自营的产品，单品置顶无效
 * 6.如果是非图书类型的，没有当当自营的限制
 * 7.注意查看inc_operator.conf文件中的engine_domain，指的是对哪个搜索后台有效
 * 
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ProductTop {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ProductTop.class);
	@BeforeMethod 
	public void setup() {  	
	}  

	@AfterMethod 
	public void clearup() { 
	}   
	
	/*
	 * 单品置顶
	 */
	@Test(enabled=true)  
	public void product_top_test1() { 
		try{
			long d = System.currentTimeMillis() ;
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			//"dxb"只是为了调用一个被重载的方法，无特殊意义
			tl.start("verify_point='product_top'", "fvp_id=43","dxb");
			//tl.start("id='182255'", "fvp_id=43","dxb");
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