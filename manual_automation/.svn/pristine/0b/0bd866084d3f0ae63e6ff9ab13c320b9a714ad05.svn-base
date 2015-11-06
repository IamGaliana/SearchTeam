package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author liuzhipengjs@dangdang.com
 * @version 创建时间：2015年1月19日 下午5:58:49 类说明 电子书推广
 */
public class EBookScheduler {
	private static Logger logger = Logger.getLogger(EBookScheduler.class);
	{
		PropertyConfigurator.configure("conf/ebook_log4j.properties");
	}

	@BeforeMethod
	public void setup() {
	}

	@AfterMethod
	public void clearup() {
	}

	// 电子书推广
	@Test(enabled = true, groups = "p2")
	public void ebook_generalize() {
		try {
			long d = System.currentTimeMillis();
			logger.fatal(d);
			TestLauncher tl = new TestLauncher();
			tl.start("id < 727", "fvp_id = 30");
			long d2 = System.currentTimeMillis();
			logger.fatal((d2 - d) / 1000.0);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		}
	}

}
