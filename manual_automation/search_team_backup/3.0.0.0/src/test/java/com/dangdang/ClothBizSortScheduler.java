package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.data.FuncQuery;
import com.dangdang.verifier.sort.clothBzs.ClothBusinessSortVerifier;
import com.dangdang.verifier.sort.clothBzs.goodsInWindowsVerifier;

public class ClothBizSortScheduler {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ClothBizSortScheduler.class);
	
//	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ClothBizSortScheduler.class);
//	{
//		PropertyConfigurator.configure("conf/clothbiz_log4j.properties");
//	}

	public static int pass = 0;
	public static int fail = 0;
	public static long d = 0;

	@BeforeClass
	public void setup() {
		// int pass=0,fail=0;
		d = System.currentTimeMillis();
	}

	@AfterClass
	public void clearup() {
		logger.info("Result  ####    Passed Count:" + pass + " Failed Count:" + fail);
		double pr = Double.valueOf(pass) / (pass + fail) * 100;
		double fr = Double.valueOf(fail) / (fail + pass) * 100;
		logger.info("Result  ####    Passed Rate:" + Math.round(pr) + "% Failed Rate:" + Math.round(fr) + "%");

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString(Math.ceil(d2 - d) / 1000.0));
	}

	// 服装事业部
	@Test(enabled = true, groups="p2")
	public void ClothBizSort_01() {
		try {

			logger.info(Long.toString(d));
			DBAction dba = new DBAction();
			dba.setFuncCondition("verify_point = 'ClothBiz' and id < 178225");
			List<FuncQuery> queryList = dba.getFuncQuery();

			for (FuncQuery fquery : queryList) {
				ClothBusinessSortVerifier aaa = new ClothBusinessSortVerifier();
				try {
					if (aaa.verify(fquery)) {
						logger.info(" - [LOG_PASSED] - " + "Cloth bussiness sort for:" + fquery.getFquery());
						pass++;
					} else {
						logger.info(" - [LOG_FAILED] - " + "Cloth bussiness sort for:" + fquery.getFquery());
						fail++;
						// break;
					}
				} catch (Exception e) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					e.printStackTrace(new PrintStream(baos));
					String exception = baos.toString();
					logger.error(" - [LOG_EXCEPTION] - " + exception);
					fail++;
				}
			}

		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {

		}
	}

	// 橱窗宝贝加分、无法长期有效
	@Test(enabled = false)
	public void goodsInWindows() {
		try {

			logger.info(Long.toString(d));
			DBAction dba = new DBAction();
			dba.setFuncCondition("verify_point = 'goods_windows' and id mod 4=1");
			List<FuncQuery> queryList = dba.getFuncQuery();

			for (FuncQuery fquery : queryList) {
				goodsInWindowsVerifier aaa = new goodsInWindowsVerifier();
				try {
					if (aaa.verify(fquery)) {
						logger.info(" - [LOG_PASSED] - " + "Cloth bussiness sort for:" + fquery.getFquery());
						pass++;
					} else {
						logger.info(" - [LOG_FAILED] - " + "Cloth bussiness sort for:" + fquery.getFquery());
						fail++;
					}
				} catch (Exception e) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					e.printStackTrace(new PrintStream(baos));
					String exception = baos.toString();
					logger.error(" - [LOG_EXCEPTION] - " + exception);
					fail++;
				}
			}

		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {

		}
	}

	@Test(enabled = false)
	public void ClothBizSort_02() {
		try {

			logger.info(Long.toString(d));
			DBAction dba = new DBAction();
			dba.setFuncCondition("verify_point = 'ClothBiz' and id mod 4=2");
			List<FuncQuery> queryList = dba.getFuncQuery();

			for (FuncQuery fquery : queryList) {
				ClothBusinessSortVerifier aaa = new ClothBusinessSortVerifier();
				try {
					if (aaa.verify(fquery)) {
						logger.info(" - [LOG_PASSED] - " + "Cloth bussiness sort for:" + fquery.getFquery());
						pass++;
					} else {
						logger.info(" - [LOG_FAILED] - " + "Cloth bussiness sort for:" + fquery.getFquery());
						fail++;
					}
				} catch (Exception e) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					e.printStackTrace(new PrintStream(baos));
					String exception = baos.toString();
					logger.error(" - [LOG_EXCEPTION] - " + exception);
					fail++;
				}
			}

		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {

		}
	}

	@Test(enabled = false)
	public void ClothBizSort_03() {
		try {

			logger.info(Long.toString(d));
			DBAction dba = new DBAction();
			dba.setFuncCondition("verify_point = 'ClothBiz' and id mod 4 =3");
			List<FuncQuery> queryList = dba.getFuncQuery();

			for (FuncQuery fquery : queryList) {
				ClothBusinessSortVerifier aaa = new ClothBusinessSortVerifier();
				try {
					if (aaa.verify(fquery)) {
						logger.info(" - [LOG_PASSED] - " + "Cloth bussiness sort for:" + fquery.getFquery());
						pass++;
					} else {
						logger.info(" - [LOG_FAILED] - " + "Cloth bussiness sort for:" + fquery.getFquery());
						fail++;
					}
				} catch (Exception e) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					e.printStackTrace(new PrintStream(baos));
					String exception = baos.toString();
					logger.error(" - [LOG_EXCEPTION] - " + exception);
					fail++;
				}
			}

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
