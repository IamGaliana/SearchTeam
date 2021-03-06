package com.dangdang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.dangdang.client.SearchRequest;
import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.util.DBConnUtil;
import com.dangdang.util.Utils;
import com.dangdang.util.XMLParser;
import com.mysql.jdbc.ResultSet;

public class NoUMSeachVerifierScheduler {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(NoUMSeachVerifierScheduler.class);
	/**
	 * @param args
	 */
 	@Test(enabled=false, groups="p1")
	public void noUMSeachVerifier() {
 		long d = System.currentTimeMillis();
		ResultSet result = DBConnUtil.exeQuery("select * from searchkey_by_category_m_1405 limit 100", DBConnUtil.getConnection("jdbc:mysql://10.255.254.129:3306/BigData"));
		int pass = 0, fail = 0;
		String subject = "【搜索后台自动化测试】基础功能回归测试结果";
		StringBuffer content = new StringBuffer();
		content.append("<html><head><meta http-equiv=Content-Type content='text/html; charset=utf-8'></head><body><table border=1 cellspacing=0 cellpadding=0><tr><th>功能模块</th><th>通过query</th><th>失败query</th><th>跳过query</th><th>总计</th><th>耗时</th></tr>");
		
		try {
			while(result.next()){
				String query = result.getString("keyword");
				Map<String,String> map = new HashMap<String,String>();
				map.put("q", query);
				String url = URLBuilder.buildUrl(map);
				String xml = SearchRequester.get(url);
				List<Node> prods = XMLParser.getProductNodes(XMLParser.read(xml));
				if(prods!=null){
					if(prods.size()!=0){
						for(Node prodN: prods){
							String prod_id = XMLParser.product_id(prodN);
							Map<String,String> map_prod = new HashMap<String,String>();
							map_prod.put("product_id", prod_id);
							String url_prod = URLBuilder.buildUrl(map_prod);
							String xml_prod = SearchRequester.get(url_prod);
							List<Node> id_prods = XMLParser.getProductNodes(XMLParser.read(xml_prod));
							if(id_prods!=null){
								if(id_prods.size()!=0){
									logger.info("URL - "+url_prod);
									pass++;
									continue;
								}else{
									fail++;
									logger.error("[P-NORESULT]Search for:"+prod_id +" URL: "+url_prod);
								}
								
							}else{
								fail++;
								logger.error("[P-NONEDERESULT]Search for:"+ prod_id+" URL: "+url_prod);
							}
						}
//						logger.debug("URL - "+url);
						continue;
					}else{
						url = url.replace("10.255.254.71:8390","10.255.254.188:8390");
						String xml_tmp = SearchRequester.get(url);
						List<Node> tem_prods = XMLParser.getProductNodes(XMLParser.read(xml_tmp));
						
						if(tem_prods.size()!=0){
							fail++;
							logger.error("[Q-NORESULT]Search for:"+ query+" URL: "+url);
						}
						
					}
					
				}else{
					url = url.replace("10.255.254.71:8390","10.255.254.188:8390");
					String xml_tmp = SearchRequester.get(url);
					List<Node> tem_prods = XMLParser.getProductNodes(XMLParser.read(xml_tmp));
					if(tem_prods.size()!=0){
						fail++;
						logger.error("[Q-NONEDERESULT]Search for:"+ query+" URL: "+url);
					}
				}
				
			}
			
		} catch (SQLException | MalformedURLException | DocumentException e) {
			// TODO Auto-generated catch block
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
		}
		
		long d2 = System.currentTimeMillis();
		logger.info("总耗时：" + (d2 - d) / 1000.0 + "秒");
		String d3 = Math.ceil((d2 - d) / 60000.0) + "分钟";
		content.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", 
				"NoUM搜索测试", pass, fail, "0", pass+fail, d3));
		content.append("</table></body></html>");
		Utils.sendMail(subject, content.toString(), "HTML");
		
		
	}

}
