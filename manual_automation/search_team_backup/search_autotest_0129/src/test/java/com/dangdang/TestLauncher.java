package com.dangdang;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.dangdang.client.DBAction;
import com.dangdang.client.URLBuilder;
import com.dangdang.data.FuncCatePath;
import com.dangdang.data.FuncQuery;
import com.dangdang.data.FuncVP;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.Utils;


public class TestLauncher {

	private static Logger logger = Logger.getLogger(TestLauncher.class);
	
	public void start(String fc, String fvpc){
		long d1 = System.currentTimeMillis() ;
		DBAction dba = new DBAction();
		dba.setFuncCondition(fc);
		dba.setFvpCondition(fvpc);
		List<FuncQuery> querys = dba.getFuncQuery();
		List<FuncVP> fvps = dba.getFVP();
		String subject = "【搜索后台自动化测试】基础功能回归测试结果";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>耗时</th></tr>");
		//根据数据库中是否取到的fvps选择验证功能点，fvps取不到则取FuncQuery中的fvps
		if(fvps.size()>0){						
			for(FuncVP fvp : fvps){
				int passed=0, failed=0, skiped=0;
				for(FuncQuery query : querys){
					int rt = doQuery(fvp.getFvp(), query.getFquery(), query.getMedium());
					switch(rt){
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
					}
				}		
				logger.fatal(String.format(" - [LOG_SUMMARY] - vp: %s, passed: %s, failed: %s, skiped: %s", fvp.getFvp(), passed, failed, skiped));
				long d2 = System.currentTimeMillis();
				String d3 = Math.ceil((d2-d1)/60000.0)+"分钟";
				content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
						fvp.getFvp(), passed, failed, skiped, passed+failed+skiped, d3));
			}				
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
		}else{		
			int passed=0, failed=0, skiped=0;
			for(FuncQuery query: querys){				
				int rt = doQuery(query.getVerify_point(), query.getFquery(), query.getMedium());
				switch(rt){
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
				}
			}
			logger.fatal(String.format(" - [LOG_SUMMARY] - passed: %s, failed: %s, skiped: %s", passed, failed, skiped));
			String funcvp = "";
			if(querys.size()>0){
				String fvp = querys.get(0).getVerify_point();
				if (fvp.contains(",")){
					funcvp = "filtergroup";
				}else{
					funcvp = fvp;
				}					
			}
			long d2 = System.currentTimeMillis();
			String d3 = Math.ceil((d2-d1)/60000.0)+"分钟";
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					funcvp, passed, failed, skiped, passed+failed+skiped, d3));
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
		}			
		return;
	}
	
	/*
	 * list
	 */
	public void l_start(String fc, String fvpc){
		long d1 = System.currentTimeMillis() ;
		DBAction dba = new DBAction();
		dba.setFuncCondition(fc);
		dba.setFvpCondition(fvpc);
		List<FuncCatePath> categorys = dba.getFuncCatePath();
		List<FuncVP> fvps = dba.getFVP();
		String subject = "【搜索后台自动化测试】基础功能回归测试结果";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过category</th><th>失败category</th><th>跳过category</th><th>总计</th><th>耗时</th></tr>");
		//根据数据库中是否取到的fvps选择验证功能点，fvps取不到则取FuncQuery中的fvps
		if(fvps.size()>0){						
			for(FuncVP fvp : fvps){
				int passed=0, failed=0, skiped=0;
				for(FuncCatePath category : categorys){
					int rt = l_doQuery(fvp.getFvp(), category.getCat_path(), category.getFcate_name());
					switch(rt){
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
					}
				}		
				logger.fatal(String.format(" - [LOG_SUMMARY] - vp: %s, passed: %s, failed: %s, skiped: %s", fvp.getFvp(), passed, failed, skiped));
				long d2 = System.currentTimeMillis();
				String d3 = Math.ceil((d2-d1)/60000.0)+"分钟";
				content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
						fvp.getFvp(), passed, failed, skiped, passed+failed+skiped, d3));
			}				
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
		}else{		
			int passed=0, failed=0, skiped=0;
			for(FuncCatePath category: categorys){				
				int rt = l_doQuery(category.getVerify_point(), category.getCat_path(), category.getFcate_name());
				switch(rt){
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
				}
			}
			logger.fatal(String.format(" - [LOG_SUMMARY] - passed: %s, failed: %s, skiped: %s", passed, failed, skiped));
			String funcvp = "";
			if(categorys.size()>0){
				String fvp = categorys.get(0).getVerify_point();
				if (fvp.contains(",")){
					funcvp = "filtergroup";
				}else{
					funcvp = fvp;
				}					
			}
			long d2 = System.currentTimeMillis();
			String d3 = Math.ceil((d2-d1)/60000.0)+"分钟";
			content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
					funcvp, passed, failed, skiped, passed+failed+skiped, d3));
			content.append("</table></body></html>");
			Utils.sendMail(subject, content.toString(), "HTML");
		}			
		return;
	}
	
	public int doQuery(String vps, String query, String medium){
		boolean multiFilter = false;//默认关,多重过滤case开关，如果是true，需要修改用例中fvp条件为空以致fvp有多个
		
		if (query==null || vps==null){
			logger.error(String.format(" - [LOG_FAILED] - query or vps not available query: %s, vps: %s", query, vps));
			return -1;
		}
		logger.fatal(String.format(" - query: %s, function: %s", query, vps));
		Map<String, String> infop = URLBuilder.getPreSearchInfo(query);  //获取默认的查询内容	
		infop.put("medium", medium);
		infop.put("query", query);
		logger.debug(" - preSearchInfo: " + infop.toString());
		
//		if (!medium.equals(infop.get("template"))){
//			logger.error(String.format(" - [LOG_FAILED] - query %s, Template not according", query));
//			return -1;
//		}
		if (Integer.valueOf(infop.get("totalCount"))<1){
			logger.error(String.format(" - [LOG_SKIP] - query %s, No results", query));
			return -2;
		}
		if (vps.contains("priceInterval") && infop.get("priceInterval").equals("")){
			logger.error(String.format(" - [LOG_SKIP] - query %s, No priceInterval", query));
			return -2;
		}
		if (vps.contains("brand") && infop.get("brand").equals("")){
			logger.error(String.format(" - [LOG_SKIP] - query %s, No brand", query));
			return -2;
		}
		if (vps.contains("texture") && infop.get("template").equals("0")){
			logger.error(String.format(" - [LOG_SKIP] - query %s, book No texture", query));
			return -2;
		}
		if (vps.contains("ebook") && !infop.get("webTemplete").equals("0")){
			logger.info(String.format(" - [LOG_SKIP] - query %s, webTemplete is not PUB_TEMPLATE", query));
			return -2;
		}
		
		Map<String, String> urlp = URLBuilder.converURLPars(vps, query, infop);
		logger.debug(" - SearchInfo: " + urlp.toString());
		ProdIterator iterator = new ProdIterator(urlp);	
		if(iterator.getTotalCount()<1&&multiFilter){
			iterator = null;
			iterator =  URLBuilder.getDefaultIterator(query);
			//多重过滤筛选验证
			if(URLBuilder.doMFVerify(vps, iterator, infop)){
				logger.fatal(" - [LOG_SUCCESS] - verify function 【"+ vps+"】 for query: "+query);
				return 0;
			}else{
				logger.error(" - [LOG_FAILED] - verify function 【"+ vps+"】 for query: "+query);
				return -1;
			}	
		}else{
			//单个过滤筛选验证
			if(URLBuilder.doVerify(vps, iterator, infop)){
				logger.fatal(" - [LOG_SUCCESS] - verify function 【"+ vps+"】 for query: "+query);
				return 0;
			}else{
				logger.error(" - [LOG_FAILED] - verify function 【"+ vps+"】 for query: "+query);
				return -1;
			}
		}
	}
	
	/*
	 * list
	 */
	public int l_doQuery(String vps, String catepath, String catename){
		boolean multiFilter = false;//默认关,多重过滤case开关，如果是true，需要修改用例中fvp条件为空以致fvp有多个
		
		if (catepath==null || vps==null){
			logger.error(String.format(" - [LOG_FAILED] - category or vps not available category: %s, vps: %s", catename+catepath, vps));
			return -1;
		}
		logger.fatal(String.format(" - [LOG_SUMMARY] - categoryname: %s, function: %s", catename+catepath, vps));
		Map<String, String> infop = URLBuilder.l_getPreSearchInfo(catepath);  //获取默认的查询内容	
		infop.put("catename", catename);
		infop.put("cat_paths", catepath);
		logger.fatal(" - [LOG_SUMMARY] - preSearchInfo: " + infop.toString());
		
//		if (!medium.equals(infop.get("template"))){
//			logger.error(String.format(" - [LOG_FAILED] - query %s, Template not according", query));
//			return -1;
//		}
		if (Integer.valueOf(infop.get("totalCount"))<1){
			logger.error(String.format(" - [LOG_SKIP] - category %s, No results", catename+catepath));
			return -2;
		}
		if (vps.contains("priceInterval") && infop.get("priceInterval").equals("")){
			logger.error(String.format(" - [LOG_SKIP] - category %s, No priceInterval", catename+catepath));
			return -2;
		}
		if (vps.contains("brand") && infop.get("brand").equals("")){
			logger.error(String.format(" - [LOG_SKIP] - category %s, No brand", catename+catepath));
			return -2;
		}
		if (vps.contains("texture") && infop.get("template").equals("0")){
			logger.error(String.format(" - [LOG_SKIP] - category %s, book No texture", catename+catepath));
			return -2;
		}
		
		Map<String, String> urlp = URLBuilder.l_converURLPars(vps, catepath, infop);
		logger.debug(" - SearchInfo: " + urlp.toString());
		ProdIterator iterator = new ProdIterator(urlp);	
		if(iterator.getTotalCount()<1&&multiFilter){
			iterator = null;
			iterator =  URLBuilder.l_getDefaultIterator(catepath);
			//多重过滤筛选验证
			if(URLBuilder.doMFVerify(vps, iterator, infop)){
				logger.fatal(" - [LOG_SUCCESS] - verify function 【"+ vps+"】 for category: "+catename+catepath);
				return 0;
			}else{
				logger.error(" - [LOG_FAILED] - verify function 【"+ vps+"】 for category: "+catename+catepath);
				return -1;
			}	
		}else{
			//单个过滤筛选验证
			if(URLBuilder.doVerify(vps, iterator, infop)){
				logger.fatal(" - [LOG_SUCCESS] - verify function 【"+ vps+"】 for category: "+catename+catepath);
				return 0;
			}else{
				logger.error(" - [LOG_FAILED] - verify function 【"+ vps+"】 for category: "+catename+catepath);
				return -1;
			}
		}
	}
	


}
