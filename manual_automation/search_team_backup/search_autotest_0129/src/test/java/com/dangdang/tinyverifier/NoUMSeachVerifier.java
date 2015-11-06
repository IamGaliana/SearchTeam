package com.dangdang.tinyverifier;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.testng.annotations.Test;

import com.dangdang.client.SearchRequester;
import com.dangdang.client.URLBuilder;
import com.dangdang.util.DBConnUtil;
import com.dangdang.util.XMLParser;
import com.mysql.jdbc.ResultSet;

public class NoUMSeachVerifier {
 public static Logger logger = Logger.getLogger(NoUMSeachVerifier.class);
	/**
	 * @param args
	 */
 	@Test
	public void noUMSeachVerifier() {
		// TODO Auto-generated method stub
		PropertyConfigurator.configure("conf/log4j.properties");
		ResultSet result = DBConnUtil.exeQuery("select * from searchkey_by_category_m_1405", DBConnUtil.getConnection("jdbc:mysql://10.255.254.129:3306/BigData"));
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
									logger.fatal("URL - "+url_prod);
									continue;
								}else{
									logger.error("[P-NORESULT]Search for:"+prod_id +" URL: "+url_prod);
								}
								
							}else{
								logger.error("[P-NONEDERESULT]Search for:"+ prod_id+" URL: "+url_prod);
							}
						}
						logger.fatal("URL - "+url);
						continue;
					}else{
						url = url.replace("10.255.254.29:52114","192.168.196.174:8390");
						String xml_tmp = SearchRequester.get(url);
						List<Node> tem_prods = XMLParser.getProductNodes(XMLParser.read(xml_tmp));
						
						if(tem_prods.size()!=0){
							logger.error("[Q-NORESULT]Search for:"+ query+" URL: "+url);
						}
						
					}
					
				}else{
					url = url.replace("10.255.254.29:52114","192.168.196.186:8390");
					String xml_tmp = SearchRequester.get(url);
					List<Node> tem_prods = XMLParser.getProductNodes(XMLParser.read(xml_tmp));
					if(tem_prods.size()!=0){
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
		
	}

}
