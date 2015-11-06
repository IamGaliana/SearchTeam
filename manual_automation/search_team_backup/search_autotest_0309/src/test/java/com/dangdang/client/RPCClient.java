package com.dangdang.client;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.LoggerFactory;

import com.dangdang.Test001;
import com.dangdang.util.thrift.QueryCate;
import com.dangdang.util.thrift.QueryCateService;

public class RPCClient {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(RPCClient.class);
	
	/**
	 * 调用开发接口，得到预测分类与各分类的权重值
	 * @param keyWord   搜索关键字
	 * @return
	 */
	public static SortedMap<String,Double> getCategoryPriority(String keyWord){

		 return getCategoryPriority(keyWord,false);
	}
	
	public static SortedMap<String,Double> getCategoryPriority(String keyWord,boolean isNormalizing){
		SortedMap<String,Double> result = new TreeMap<String,Double>();
		 TTransport transport;
		 //王碧瑶开发机
		 transport = new TSocket("10.255.254.71",9197);
//		 transport = new TSocket("192.168.85.155",9197);
//		 logger.info("192.168.85.155:9197");
		 try {
			transport.open();
			 TProtocol protocol = new TBinaryProtocol(transport); 
			 QueryCateService.Client client = new QueryCateService.Client(protocol);
			 List<QueryCate> list =	 client.getQueryCate(keyWord, isNormalizing, "UTF8");
			 for(QueryCate cate: list){
				 result.put(cate.getCate_path(), Double.valueOf(cate.getWeight()));
			 }
			 transport.close();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 return result;
	}
	public static void main(String[] args) {
		try{
		String keyWord = "乔丹男鞋";	
		String keyWords = "哈伦裤 女";
		System.out.println(getCategoryPriority(keyWord,true));
		System.out.println(getCategoryPriority(keyWords,false));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	
}
