package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.Utils;
import com.dangdang.verifier.duplicate.DuplicateVerifier;

public class DupliScheduler {

	private int pass = 0;
	private int fail = 0;
	private int ps = 60;
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DupliScheduler.class);

	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void clearup() {
		logger.info("Result  ####    Passed Count:" + pass + " Failed Count:" + fail);
		double pr = Double.valueOf(pass) / (pass + fail) * 100;
		double fr = Double.valueOf(fail) / (fail + pass) * 100;
		logger.info("Result  ####    Passed Rate:" + Math.round(pr) + "% Failed Rate:" + Math.round(fr) + "%");
		String subject = "【搜索后台自动化测试】基础功能回归测试结果";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th></tr>");
		content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", "Duplicate Result Test", pass, fail, 0, pass + fail
				+ 0));
		content.append("</table></body></html>");
		Utils.sendMail(subject, content.toString(), "HTML");

	}

	@Test(enabled = true, groups="p1")
	public void dSearch_1() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		// dba.setFuncCondition(" id mod 10 = 0  and  id <10000");
		dba.setFuncCondition(" id <100");
		List<FuncQuery> list = dba.getFuncQuery();

		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());

				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.info(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}

	// 重复结果导致丢品->
	@Test(enabled = false, groups="p1")
	public void dSearch_2() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		dba.setFuncCondition(" id mod 10 = 1 and  id <10000");
		// dba.setFuncCondition(" id <727");
		List<FuncQuery> list = dba.getFuncQuery();
		// int pass=0,fail=0;
		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());
				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.debug(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}

	@Test(enabled = false, groups="p1")
	public void dSearch_3() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		dba.setFuncCondition(" id mod 10 = 2  and  id <10000");
		List<FuncQuery> list = dba.getFuncQuery();

		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());

				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.debug(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}

	@Test(enabled = false, groups="p1")
	public void dSearch_4() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		dba.setFuncCondition(" id mod 10 = 3  and  id <10000");
		List<FuncQuery> list = dba.getFuncQuery();

		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());

				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.debug(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}

	@Test(enabled = false, groups="p1")
	public void dSearch_5() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		dba.setFuncCondition(" id mod 10 = 4  and  id <10000");
		List<FuncQuery> list = dba.getFuncQuery();

		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());

				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.debug(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}

	@Test(enabled = false, groups="p1")
	public void dSearch_6() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		dba.setFuncCondition(" id mod 10 = 5  and  id <10000");
		List<FuncQuery> list = dba.getFuncQuery();

		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());

				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.debug(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}

	@Test(enabled = false, groups="p1")
	public void dSearch_7() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		dba.setFuncCondition(" id mod 10 = 6  and  id <10000");
		List<FuncQuery> list = dba.getFuncQuery();

		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());

				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.info(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}

	@Test(enabled = false, groups="p1")
	public void dSearch_8() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		dba.setFuncCondition(" id mod 10 = 7  and  id <10000");
		List<FuncQuery> list = dba.getFuncQuery();

		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());

				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.debug(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}

	@Test(enabled = false, groups="p1")
	public void dSearch_9() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		dba.setFuncCondition(" id mod 10 = 8  and  id <10000");
		List<FuncQuery> list = dba.getFuncQuery();

		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());

				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.debug(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}

	@Test(enabled = false, groups="p1")
	public void dSearch_10() {
		long d = System.currentTimeMillis();
		logger.info(Long.toString(d));
		DBAction dba = new DBAction();
		dba.setFuncCondition(" id mod 10 = 9  and  id <10000");
		List<FuncQuery> list = dba.getFuncQuery();

		for (FuncQuery query : list) {
			try {
				ProdIterator.setPageSize(ps);
				ProdIterator iterator = URLBuilder.getDefaultIterator(query.getFquery());

				if (DuplicateVerifier.doVerifier(iterator)) {
					logger.debug(" - [PASSED] - " + "Duplicate result test for:" + query.getFquery());
					pass++;
				} else {
					logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
					fail++;
				}
			} catch (Exception e) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				String exception = baos.toString();
				logger.error(" - [LOG_EXCEPTION] - " + exception);
				logger.error(" - [FAILED] - " + "Duplicate result test for:" + query.getFquery());
				e.printStackTrace();
				fail++;
			}
		}

		long d2 = System.currentTimeMillis();
		logger.info(Double.toString((d2 - d) / 1000.0));
	}
}
