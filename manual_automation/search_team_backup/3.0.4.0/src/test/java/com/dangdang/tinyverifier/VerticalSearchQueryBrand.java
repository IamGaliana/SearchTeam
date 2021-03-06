package com.dangdang.tinyverifier;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.dangdang.client.DBAction;
import com.dangdang.data.VerticalSearchQuery;
import com.dangdang.util.ProdIterator;
import com.dangdang.util.XMLParser;

public class VerticalSearchQueryBrand {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBAction action = new DBAction();
		action.setVsq_condition("id between 741 and 796");
		List<VerticalSearchQuery> qlist = action.getVerticalSearchQuery();
		Map<String,List<String>> q2brand = new HashMap<String,List<String>>();
		try{
			//遍历所有百货query
			for(VerticalSearchQuery q: qlist){
				String query = q.getQuery();
				List<String> brands = getBrand(query);
				//联系query 与品牌列表
				q2brand.put(query, brands);
				}
				//Perm_id备选文件
				File source = new File("C:\\Users\\zhangyamin.DANGDANG\\Downloads\\b2c_brand.txt");
				//暂存文件
				File target = new File("C:\\Users\\zhangyamin.DANGDANG\\Downloads\\reco.txt");
				target.createNewFile();
//				FileInputStream input = new FileInputStream(source);
				FileReader reader = new FileReader(source);				
				BufferedReader breader = new BufferedReader(reader);
				FileWriter write = new FileWriter(target);
				BufferedWriter bwrite = new BufferedWriter(write);
				String line="";
//				int index=0;
			
				StringBuffer sbf = new StringBuffer();
				
				for(int j=0;j<qlist.size();j++){
					String qtmp = qlist.get(j).getQuery();
					String cate_id = qlist.get(j).getCate_id();
					String query_type = "BH";//qlist.get(j).getQuery_type();
					String shop_products = qlist.get(j).getShop_products();
					String hot_products = qlist.get(j).getHot_products();
					List<String> brankds  = q2brand.get(qtmp);
					Date d = new Date();
					Random ran = new Random(d.getTime()); 
//					int iindex = 0;
					if((line=breader.readLine())!=null){
						String [] elements = line.split("\t");
						elements[3]="";
						
						try{
						elements[3] = brankds.get(0)+":"+Math.round(ran.nextFloat()*100)/100.00;
						}catch(Exception e){
//							e.printStackTrace();
						}
						if(!brankds.get(0).equals("")){
						bwrite.write(String.format("%s\t%s\t%s\t%s\n", elements[0],elements[1],elements[2],elements[3]));
						bwrite.flush();
						System.out.println(String.format("Insert into vertical_search_query (`query`,`cate_id`,`query_type`,`shop_products`,`hot_products`,`perm_id`) values ('%s','%s','%s','%s','%s','%s'); ",
								qtmp,cate_id,query_type,shop_products,hot_products,elements[0]));
						sbf.append(String.format("INSERT INTO perm_reco (`perm_id`,`brand_reco`) VALUES ('%s','%s');\n",elements[0],elements[3]));
						}
					}
					
					if((line=breader.readLine())!=null){
						String [] elements = line.split("\t");
						elements[3]="";
//						List<String> brankds  = q2brand.get(qtmp);
						try {
							elements[3] = brankds.get(0)+":"+Math.round(ran.nextFloat()*100)/100.00+"\t"+brankds.get(1)+":"+Math.round(ran.nextFloat()*100)/100.00;
						} catch (Exception e) {
							
						}
						if(!brankds.get(0).equals("")){
						bwrite.write(String.format("%s\t%s\t%s\t%s\n", elements[0],elements[1],elements[2],elements[3]));
						bwrite.flush();
						System.out.println(String.format("Insert into vertical_search_query (`query`,`cate_id`,`query_type`,`shop_products`,`hot_products`,`perm_id`) values ('%s','%s','%s','%s','%s','%s'); ",
								qtmp,cate_id,query_type,shop_products,hot_products,elements[0]));
						sbf.append(String.format("INSERT INTO perm_reco (`perm_id`,`brand_reco`) VALUES ('%s','%s');\n",elements[0],elements[3]));
						}
					}
					if((line=breader.readLine())!=null){
						String [] elements = line.split("\t");
						elements[3]="5129:0.80";
						bwrite.write(String.format("%s\t%s\t%s\t%s\n", elements[0],elements[1],elements[2],elements[3]));
						bwrite.flush();
						System.out.println(String.format("Insert into vertical_search_query (`query`,`cate_id`,`query_type`,`shop_products`,`hot_products`,`perm_id`) values ('%s','%s','%s','%s','%s','%s'); ",
								qtmp,cate_id,query_type,shop_products,hot_products,elements[0]));
						sbf.append(String.format("INSERT INTO perm_reco (`perm_id`,`brand_reco`) VALUES ('%s','%s');\n",elements[0],elements[3]));
					}
				}
				System.out.println(sbf.toString());
				bwrite.close();
				breader.close();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	
	//得到每个query对应结果前200个品涵盖的品牌
	public static List<String> getBrand(String query){
		Map<String,Integer> brandcount = new HashMap<String,Integer>();
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("q",URLEncoder.encode(query, "GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("st", "full");
		map.put("um", "search_ranking");
		map.put("_new_tpl", "1");//search_ranking必加
		map.put("vert", "1");
		List<String> brandList = new ArrayList<String>();
		ProdIterator iterator = new ProdIterator(map,200);
		while(iterator.hasNext()){
			String brandid = XMLParser.product_brand(iterator.next());
			
			int prod_index = iterator.getpageIndex()*iterator.getPageSize()+iterator.getPoint();
			if(!brandcount.containsKey(brandid) && !brandid.equals("0")&&prod_index>=57){
//				brandList.add(brandid);
				brandcount.put(brandid, 1);
			}else if(brandcount.containsKey(brandid)&& !brandid.equals("0")&& prod_index>=57){
				brandcount.put(brandid, brandcount.get(brandid)+1);
			}else{
				continue;
			}
		}
		for(Map.Entry<String, Integer> entry: brandcount.entrySet()){
			if(entry.getValue()>=4){
				brandList.add(entry.getKey());
			}
		}
		
		if(brandList.size()==0){
			brandList.add("5129");
		}
		
		return brandList;
	}
	
}
