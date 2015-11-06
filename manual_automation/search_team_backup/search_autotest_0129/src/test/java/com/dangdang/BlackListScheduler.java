package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.data.BlackListQuery;
import com.dangdang.util.Utils;
import com.dangdang.verifier.blacklist.ListRankingBlackListVerifier;
import com.dangdang.verifier.blacklist.SearchRankingBlackListVerifier;
import com.dangdang.verifier.blacklist.SearchThroughVerifier;
import com.dangdang.verifier.blacklist.ShopThroughVerifier;

/**
 * @author liuzhipengjs@dangdang.com
 * @version 创建时间：2014年12月20日 下午1:30:44 类说明:搜索黑名单，列表黑名单，搜索直达，店铺直达等运营工具策略
 */
public class BlackListScheduler {
	private static Logger logger = Logger.getLogger(BlackListScheduler.class);
	{
		PropertyConfigurator.configure("conf/blacklist_log4j.properties");
	}
	//店铺直达
//	private static ShopThroughVerifier shopThroughVerifier = new ShopThroughVerifier();
	//分类直达
//	private static SearchThroughVerifier searchThroughVerifier = new SearchThroughVerifier();
	
	@BeforeMethod
	public void setup() {
	}

	@AfterMethod
	public void clearup() {
	}
	
	@AfterClass
	public void calssclearup() {
		
	}

	// List黑名单过滤
	@Test(enabled = true, groups="p2")
	public void ListRankingBlackList() {
		String subject = "【搜索后台自动化测试】基础功能回归测试结果";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>耗时</th></tr>");
		try {
			long d = System.currentTimeMillis();
			logger.fatal(d);
			DBAction dba = new DBAction();
			dba.setVsq_condition(" 1=1 and um = 'list_ranking'");
			List<BlackListQuery> listRankingQueryList = dba.getBlackListQuery();
			ListRankingBlackListVerifier listRankingVerifier = new ListRankingBlackListVerifier();
			int pass = 0, fail = 0;
			for (BlackListQuery category : listRankingQueryList) {
				try {
					if (listRankingVerifier.doVerify(category)) {
						logger.fatal(" - [PASSED] - " + "list_ranking blacklist filer for:" + category.getQuery());
						pass++;
					} else {
						logger.fatal(" - [FAILED] - " + "list_ranking blacklist filer for:" + category.getQuery());
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
			logger.fatal("Result  ####    Passed Count:" + pass + " Failed Count:" + fail);
			double pr = Double.valueOf(pass) / (pass + fail) * 100;
			double fr = Double.valueOf(fail) / (fail + pass) * 100;
			logger.fatal("Result  ####    Passed Rate:" + Math.round(pr) + "% Failed Rate:" + Math.round(fr) + "%");

			long d2 = System.currentTimeMillis();
			String d3 = (d2 - d) / 1000.0 + "秒";
			logger.fatal("总耗时：" + (d2 - d) / 1000.0 + "秒");
			
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"List黑名单", pass, fail, "0", pass+fail, d3));
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		}
	}

	// Search黑名单过滤
	@Test(enabled = true, groups="p2")
	public void SearchRankingBlackList() {
		try {
			String subject = "【搜索后台自动化测试】基础功能回归测试结果";
			StringBuffer content = new StringBuffer();
			content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>耗时</th></tr>");
			long d = System.currentTimeMillis();
			logger.fatal(d);
			DBAction dba = new DBAction();
			dba.setVsq_condition(" 1=1 and um = 'search_ranking'");
			List<BlackListQuery> searchRankingQueryList = dba.getBlackListQuery();
			SearchRankingBlackListVerifier searchRankingVerifier = new SearchRankingBlackListVerifier();
			int pass = 0, fail = 0;
			for (BlackListQuery query : searchRankingQueryList) {
				try {
					if (searchRankingVerifier.doVerify(query)) {
						logger.fatal(" - [PASSED] - " + "search_ranking blacklist filer for:" + query.getQuery());
						pass++;
					} else {
						logger.fatal(" - [FAILED] - " + "search_ranking blacklist filer for:" + query.getQuery());
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
			logger.fatal("Result  ####    Passed Count:" + pass + " Failed Count:" + fail);
			double pr = Double.valueOf(pass) / (pass + fail) * 100;
			double fr = Double.valueOf(fail) / (fail + pass) * 100;
			logger.fatal("Result  ####    Passed Rate:" + Math.round(pr) + "% Failed Rate:" + Math.round(fr) + "%");

			long d2 = System.currentTimeMillis();
			logger.fatal("总耗时：" + (d2 - d) / 1000.0 + "秒");
			String d3 = (d2 - d) / 1000.0 + "秒";
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"Search黑名单", pass, fail, "0", pass+fail, d3));
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		}
	}

	// 店铺直达
	@Test(enabled = true, groups="p2")
	public void shopthrough() {
		try {
			String subject = "【搜索后台自动化测试】基础功能回归测试结果";
			StringBuffer content = new StringBuffer();
			content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>耗时</th></tr>");
			long d = System.currentTimeMillis();
			logger.fatal(d);
			DBAction dba = new DBAction();
			dba.setVsq_condition(" 1=1 and um = 'shopthrough'");
			List<BlackListQuery> shopThroughQueryList = dba.getBlackListQuery();
			ShopThroughVerifier shopThroughVerifier = new ShopThroughVerifier();
			int pass = 0, fail = 0;
			for (BlackListQuery query : shopThroughQueryList) {
				try {
					if (shopThroughVerifier.doVerify(query)) {
						logger.fatal(" - [PASSED] - " + "shopthrough for:" + query.getQuery());
						pass++;
					} else {
						logger.fatal(" - [FAILED] - " + "shopthrough for:" + query.getQuery());
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
			logger.fatal("Result  ####    Passed Count:" + pass + " Failed Count:" + fail);
			double pr = Double.valueOf(pass) / (pass + fail) * 100;
			double fr = Double.valueOf(fail) / (fail + pass) * 100;
			logger.fatal("Result  ####    Passed Rate:" + Math.round(pr) + "% Failed Rate:" + Math.round(fr) + "%");

			long d2 = System.currentTimeMillis();
			int d4 = (int) ((d2 - d) / 60000 + 1);
			logger.fatal("总耗时:" + d4 + "分钟");
			String d3 = (d2 - d) / 1000.0 + "秒";
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"店铺直达", pass, fail, "0", pass+fail, d3));
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		}
	}

	// 搜索直达
	@Test(enabled = true, groups="p2")
	public void searchthrough() {
		try {
			String subject = "【搜索后台自动化测试】基础功能回归测试结果";
			StringBuffer content = new StringBuffer();
			content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>耗时</th></tr>");
			long d = System.currentTimeMillis();
			logger.fatal(d);
			DBAction dba = new DBAction();
			dba.setVsq_condition(" 1=1 and um = 'searchthrough'");
			List<BlackListQuery> searchThroughQueryList = dba.getBlackListQuery();
			SearchThroughVerifier searchThroughVerifier = new SearchThroughVerifier();
			int pass = 0, fail = 0;
			for (BlackListQuery query : searchThroughQueryList) {
				try {
					if (searchThroughVerifier.doVerify(query)) {
						logger.fatal(" - [PASSED] - " + "searchthrough for:" + query.getQuery());
						pass++;
					} else {
						logger.fatal(" - [FAILED] - " + "searchthrough for:" + query.getQuery());
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
			logger.fatal("Result  ####    Passed Count:" + pass + " Failed Count:" + fail);
			double pr = Double.valueOf(pass) / (pass + fail) * 100;
			double fr = Double.valueOf(fail) / (fail + pass) * 100;
			logger.fatal("Result  ####    Passed Rate:" + Math.round(pr) + "% Failed Rate:" + Math.round(fr) + "%");

			long d2 = System.currentTimeMillis();
			int d4 = (int) ((d2 - d) / 60000 + 1);
			logger.fatal("总耗时:" + d4 + "分钟");
			String d3 = (d2 - d) / 1000.0 + "秒";
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					"搜索直达", pass, fail, "0", pass+fail, d3));
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
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
