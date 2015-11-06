package com.dangdang;

import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncCatePath;
import com.dangdang.data.FuncQuery;
import com.dangdang.data.FuncVP;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.Utils;
import com.dangdang.util.XMLParser;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2015年3月3日 上午10:57:10  
 * 类说明  
 */
public class SearchCustomScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryAngQueryScheduler.class);
	
	private static int scheme = 2 ;//采用方案2

	@BeforeMethod
	public void setup() {
	}

	@AfterMethod
	public void clearup() {
	}

	@Test(enabled = true, groups="p2")
	public void search_custom() {
		
		long d = System.currentTimeMillis();
		DBAction dba = new DBAction();
		dba.setFuncCondition("verify_point = 'search_custom'");
		dba.setFvpCondition("fvp_id = 32");
		List<FuncQuery> querys = dba.getFuncQuery();
		List<FuncVP> fvps = dba.getFVP();
		String subject = "【搜索后台自动化测试】基础功能回归测试结果";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过category</th><th>失败category</th><th>跳过category</th><th>总计</th><th>耗时</th></tr>");
		for (FuncVP fvp : fvps) {
			int passed = 0, failed = 0, skiped = 0;
			for (FuncQuery query : querys) {
				int rt = doQuery(query, fvp);
				switch (rt) {
				case 0:
					passed += 1;
					break;
				case -1:
					failed += 1;
					break;
				case -2:
					skiped += 1;
					break;

				default:
					failed += 1;
					break;
				}
			}
			logger.info(String.format(" - [LOG_SUMMARY] - vp: %s, passed: %s, failed: %s, skiped: %s", fvp.getFvpname(), passed, failed, skiped));
			long d2 = System.currentTimeMillis();
			int d4 = (int) ((d2 - d) / 60000 + 1);
			logger.info("总耗时:" + d4 + "分钟");
			String d3 = String.valueOf(d4) + "分钟";
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", fvp.getFvpname(), passed, failed, skiped,
					passed + failed + skiped, d3));
		}
		content.append("</table></body></html>");
		Utils.sendMail(subject, content.toString(), "HTML");

	}

	private int doQuery(FuncQuery query, FuncVP fvp) {
		
		logger.info(String.format(" - [LOG_SUMMARY] - OldQuery: %s, NewQuery: %s", query.getFquery(), query.getDesc()));
		Map<String, String> infop = URLBuilder.l_getPreSearchInfo(query.getCat_path());  //获取默认的查询内容	
		infop.put("OldQuery", query.getFquery());
		infop.put("NewQuery", query.getDesc());
		logger.info(" - [LOG_SUMMARY] - preSearchInfo: " + infop.toString());
		
		Map<String, String> urlp = URLBuilder.converURLPars(fvp.getFvp(), query.getFquery(), infop);
		ProdIterator oldquery_iterator = new ProdIterator(urlp);
		
		Map<String, String> n_urlp = URLBuilder.l_converURLPars(fvp.getFvp(), query.getDesc(), infop);
		ProdIterator newquery_iterator = new ProdIterator(n_urlp);	
		
		
		if (newquery_iterator.getTotalCount()<1||oldquery_iterator.getTotalCount()<1) {
			logger.info(String.format(" - [LOG_SKIP] - no result:OldQuery: %s, NewQuery: %s", query.getFquery(), query.getDesc()));
			return -2;
		}
		
		if(doVerify(newquery_iterator, oldquery_iterator, fvp, infop)){
			logger.info(" - [LOG_SUCCESS] - verify function 【"+fvp.getFvpname()+"】 for old_query: "+query.getFquery()+"new_query:"+query.getDesc());
			return 0;
		}else{
			logger.error(" - [LOG_FAILED] - verify function 【"+fvp.getFvpname()+"】 for old_query: "+query.getFquery()+"new_query:"+query.getDesc());
			return -1;
		}
		
	}

	private boolean doVerify(ProdIterator l_iterator, ProdIterator s_iterator, FuncVP fvp, Map<String, String> infop) {
		
		if (scheme == 2) {
			for (int i = 0; i < 1200; i++) {
				if (l_iterator.hasNext()&s_iterator.hasNext()) {
					
					String l_product_id = XMLParser.product_id(l_iterator.next());
					String s_product_id = XMLParser.product_id(s_iterator.next());
					if (!l_product_id.equals(s_product_id)) {
						logger.error(" - [LOG_FAILED] - verify function 【"+fvp.getFvpname()+"】 for l_product_id: "+l_product_id+"s_product_id:"+s_product_id);
						return false;
					}
				}else if (!l_iterator.hasNext()&!s_iterator.hasNext()) {
					break;
				}else {
					logger.error(" - [LOG_FAILED] - verify function 【"+fvp.getFvpname()+"】 : totalCount is not fit");
					break;
				}
			}
		//方案3	
		}else if (scheme == 3) {
		} 
		return true;
	}
	

}
