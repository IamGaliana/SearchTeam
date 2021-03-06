package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author liuzhipengjs@dangdang.com
 * @version 创建时间：2015年1月19日 下午5:58:49 类说明 电子书推广
 */
public class EBookScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(EBookScheduler.class);

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
			logger.info(Long.toString(d));
			TestLauncher tl = new TestLauncher();
			tl.start("id < 727", "fvp_id = 30");
			long d2 = System.currentTimeMillis();
			logger.info(Double.toString((d2 - d) / 1000.0));
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
