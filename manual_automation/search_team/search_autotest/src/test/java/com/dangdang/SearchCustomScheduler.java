package com.dangdang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncQuery;
import com.dangdang.data.FuncVP;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.Utils;
import com.dangdang.util.XMLParser;
import com.dangdang.util.Calculator;

/**   
 * @author liuzhipengjs@dangdang.com  
 * @version 创建时间：2015年3月3日 上午10:57:10  
 * 类说明  定制化搜索
 * 
 */
public class SearchCustomScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SearchCustomScheduler.class);
	
	private static int scheme = 2 ;//采用方案2

	@BeforeMethod
	public void setup() {
	}

	@AfterMethod
	public void clearup() {
	}
	/**   
	 * 定制化搜索，主要验证分类汇总，和结果集排序
	 */
	@Test(enabled = true, groups="p2")
	public void search_custom() {
		
		long d = System.currentTimeMillis();
		DBAction dba = new DBAction();
		dba.setFuncCondition("test_data = 'search_custom' and id not in ('2349','2350','182126','182127','182128')"); // 全球购、海外购、跨境购、当当自营、自营，这几个词是在所有品中做筛选，脚本执行时间超长，且可能导致searcher挂掉
		dba.setFvpCondition("fvp_id = 32");
		List<FuncQuery> querys = dba.getFuncQuery();
		List<FuncVP> fvps = dba.getFVP();
		
		String subject = "【搜索后台自动化测试】基础功能回归测试结果";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过category</th><th>失败category</th><th>跳过category</th><th>总计</th><th>耗时</th></tr>");
		
		String warnSubject = "【搜索后台自动化测试】预警！脚本通过率低";
		StringBuffer warnContent = new StringBuffer();
		warnContent.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>跳过率</th><th>实际通过率</th><th>预期通过率</th></tr>");
		boolean doSendWarnMail = false;
		
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
			logger.debug(String.format(" - [LOG_SUMMARY] - vp: %s, passed: %s, failed: %s, skiped: %s", fvp.getFvpname(), passed, failed, skiped));
			long d2 = System.currentTimeMillis();
			int d4 = (int) ((d2 - d) / 60000 + 1);
			logger.info("总耗时:" + d4 + "分钟");
			String d3 = String.valueOf(d4) + "分钟";
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", fvp.getFvpname(), passed, failed, skiped,
					passed + failed + skiped, d3));
			
			double actualPassrate = Calculator.passrate(passed+skiped,passed+failed+skiped);
			double expectedPassrate = fvp.getMinPassrate();
			double skipRate = Calculator.skiprate(skiped, passed+failed+skiped);
			double maxSkipRate = fvp.getMaxSkiprate();
			if(actualPassrate < expectedPassrate || (actualPassrate == expectedPassrate && skipRate > maxSkipRate)){
				warnContent.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
						fvp.getFvpname(), passed, failed, skiped, passed+failed+skiped, skipRate + "%", actualPassrate + "%" , expectedPassrate + "%"));
				doSendWarnMail = true;
			}	
		}
		content.append("</table></body></html>");
		Utils.sendMail(subject, content.toString(), "HTML");
		
		if (doSendWarnMail){
			warnContent.append("</table></body></html>");
			Utils.sendWarningMail(warnSubject, warnContent.toString(), "HTML");
		}

	}

	private int doQuery(FuncQuery query, FuncVP fvp) {		
		logger.info(String.format(" - [LOG_SUMMARY] - OldQuery: %s", query.getFquery()));
		
		// 区分平台
		List<String> platform  = new ArrayList<String>();
		platform.add(0, "1"); // 无线端
		platform.add(1, "2"); // H5端
		platform.add(2, "4"); // PC端
		
		Map<String, String> preSearch  = new HashMap<String, String>(); // 预搜索map
		Map<String, String> o_urlp = new HashMap<String, String>();     // 带了原query词的map
		Map<String, String> n_urlp = new HashMap<String, String>();		// 替换了新query词的map
		ProdIterator oldquery_iterator = null;							// 原词结果集
		ProdIterator newquery_iterator = null;							// 新词结果集
		String newQuery = "";											// 新词
		boolean isPass = true;
		boolean isSkip = false;
		
		try{
			for(String eachPlatForm: platform){
				// 在预搜索参数中增加平台参数，用于触发不同的配置项效果
				Map<String, String> pf = new HashMap<String, String>();
				pf.put("platform", eachPlatForm);
				preSearch = URLBuilder.getPreSearchInfo(query.getFquery(),pf);
				
				preSearch.put("OldQuery", query.getFquery());
				preSearch.put("NewQuery", query.getDesc());
				preSearch.put("platform", eachPlatForm);
				logger.debug(" - [LOG_SUMMARY] - preSearchInfo: " + preSearch.toString());
				
				//在请求中增加平台
				
				o_urlp = URLBuilder.converURLPars(fvp.getFvp(), query.getFquery(), preSearch);
				oldquery_iterator = new ProdIterator(o_urlp);
			
				
				if(oldquery_iterator != null){
					// 根据配置文件search_ranking/module_data/filter_query_term.txt，对不同的平台配置进行验证，如果配置文件调整，此处代码也需要调整
					if (eachPlatForm == "4"){									// pc_platform，只验证全球购
						if (query.getDesc().contains("oversea")) {				// 如果包含"oversea"，把它去掉, 拼接成新的请求参数,得到新的搜索结果	
							newQuery = query.getDesc().replace("oversea", "");
							n_urlp = URLBuilder.converURLPars(query.getVerify_point(), newQuery, preSearch);
						} else {
							if(query.getVerify_point().equals("search_custom")){ // 针对用书、正版，还是要用处理过后的query
								newQuery = query.getDesc();
							}
							else{
								newQuery = query.getFquery();					 // 非海外购、非用书、非正版的query，用原词
							}
							n_urlp = URLBuilder.converURLPars("search_custom", newQuery, preSearch);
						}
						newquery_iterator = new ProdIterator(n_urlp);
					}else if (eachPlatForm == "2"){												// H5_platform，没有配置项，策略不生效
						if(query.getVerify_point().equals("search_custom")){
							newQuery = query.getDesc();
						}
						else{
							newQuery = query.getFquery();
						}
						n_urlp = URLBuilder.converURLPars("search_custom", newQuery, preSearch);
						newquery_iterator = new ProdIterator(n_urlp);
					}else if (eachPlatForm == "1"){													// 无线_platform，只验证当当自营
						if (query.getDesc().contains("ziying")) {				// 如果包含"ziying"，把它去掉, 拼接成新的请求参数,得到新的搜索结果	
							newQuery = query.getDesc().replace("ziying", "");
							n_urlp = URLBuilder.converURLPars(query.getVerify_point(), newQuery, preSearch);
						} else {
							if(query.getVerify_point().equals("search_custom")){
								newQuery = query.getDesc();
							}
							else{
								newQuery = query.getFquery();
							}
							n_urlp = URLBuilder.converURLPars("search_custom", newQuery, preSearch);
						}
						newquery_iterator = new ProdIterator(n_urlp);				
					}else{
						n_urlp = o_urlp;
						newquery_iterator = new ProdIterator(n_urlp);				
					}
					
					if (newquery_iterator.getTotalCount() != oldquery_iterator.getTotalCount()) {
						logger.error(" - [LOG_FAILED] - verify function 【"+fvp.getFvpname()+"】 for old_query: "+query.getFquery()+", new_query:"+newQuery+", platform:"+eachPlatForm + ", totalcount is different for two querys");
						isPass = false;
						continue;
					}
					
					// 结果集总数不能小于1
					if (newquery_iterator.getTotalCount()<1||oldquery_iterator.getTotalCount()<1) {
						logger.info(String.format(" - [LOG_SKIP] - no result:OldQuery: %s, NewQuery: %s, PlatForm：%s", query.getFquery(), newQuery, eachPlatForm));
						isSkip = true;
						continue;
					}
					
					// 验证前后结果集是否相同
					if(doVerify(newquery_iterator, oldquery_iterator, fvp, preSearch)){
						logger.info(" - [LOG_SUCCESS] - verify function 【"+fvp.getFvpname()+"】 for old_query: "+query.getFquery()+", new_query:"+newQuery+", platform:"+eachPlatForm);
						continue;
					}else{
						logger.error(" - [LOG_FAILED] - verify function 【"+fvp.getFvpname()+"】 for old_query: "+query.getFquery()+", new_query:"+newQuery+", platform:"+eachPlatForm);
						isPass = false;
						continue;
					}
				}
			}
		}catch(Exception ex){
			logger.error(" - [LOG_FAILED] - verify function 【"+fvp.getFvpname()+"】 for query: "+query.getFquery()+", exception:" + ex.getMessage());
			isPass = false;
		}
		
		if (isPass == true && isSkip == false)
			return 0;
		else if (isPass == false)
			return -1;
		
		if (isSkip == true)
			return -2;
		
		return 0;
	}

	/*
	 * 说明： 验证oldquery和newquery返回结果应该是一样的
	 */
	private boolean doVerify(ProdIterator n_iterator, ProdIterator o_iterator, FuncVP fvp, Map<String, String> infop) {
		
		if (scheme == 2) {
			//验证结果以及排序一致性
			for (int i = 0; i < 1200; i++) {
				if (n_iterator.hasNext()&o_iterator.hasNext()) {
					
					String n_product_id = XMLParser.product_id(n_iterator.next());
					String o_product_id = XMLParser.product_id(o_iterator.next());
					if (!n_product_id.equals(o_product_id)) {
						logger.error(" - [LOG_FAILED] - verify function 【"+fvp.getFvpname()+"】 for n_product_id: "+n_product_id+"o_product_id:"+o_product_id);
						return false;
					}
				}else if (!n_iterator.hasNext()&!o_iterator.hasNext()) {
					break;
				}else {
					logger.error(" - [LOG_FAILED] - verify function 【"+fvp.getFvpname()+"】 : totalCount is not fit");
					break;
				}
			}
			
			//验证分类汇总属性的一致
			List<String> n_cat_pathList = n_iterator.getCat_path();
			List<String> o_cat_pathList = o_iterator.getCat_path();
			for (int j = 0; j < n_cat_pathList.size(); j++) {
				if (!n_cat_pathList.get(j).equals(o_cat_pathList.get(j))) {
					logger.error(" - [LOG_FAILED] - verify function 【"+fvp.getFvpname()+"】 for n_cat_path: "+n_cat_pathList.get(j).toString()+"o_cat_path:"+o_cat_pathList.get(j).toString());
					return false;
				}
			}
		//方案3	
		}else if (scheme == 3) {
		} 
		return true;
	}
	

}
