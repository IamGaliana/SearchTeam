package com.dangdang;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.execchain.RequestAbortedException;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class SegmentTest {

	private final static org.slf4j.Logger logger = LoggerFactory
			.getLogger(SegmentTest.class);

	private static final CloseableHttpClient httpClient;
	public static final String CHARSET = "GBK";
	static {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(60000)
				.setSocketTimeout(15000).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config)
				.build();
	}
	
	private static final String urlOld = "http://192.168.85.133:12345/segment?key=";
//	private static final String urlNew = "http://192.168.85.133:12345/segment_new?key=";
	private static final String urlNew = "http://192.168.85.133:12345/segment?key=";
	private static final int MaxSegment = 2;
	private static final int FullSegment = 4;
	
	
	@Test(enabled = true)
	public void testSegment_prod_new(){
		String url=SegmentTest.class.getResource("/product_name_new.new").getPath(); 
		File file = new File(url);
        BufferedReader reader = null;
        int Max = 0;
        int Full = 0; 
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
//            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
            //product_name_new文件内容太大，只取前10000个
            for (int i = 0; i < 10000; i++) {
            	tempString = reader.readLine();
            	System.out.println("query:"+tempString);
//                String oldMax = segment(urlOld, tempString, MaxSegment);
                String newMax = segment(urlNew, tempString, MaxSegment);
                String[] maxary = newMax.split(" ");
//                System.out.println("newMax:"+newMax);
                if (!containquery(maxary,tempString)) {
					logger.info("MaxSegment   不包含初始完整词: "+tempString);
					Max++;
				}
                
//                String oldFull = segment(urlOld, tempString, FullSegment);
                String newFull = segment(urlNew, tempString, FullSegment);
                String[] fullary = newFull.split(" ");
                if (!containquery(fullary,tempString)) {
					logger.info("FullSegment  不包含初始完整词: "+tempString);
					Full++;
				}
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        logger.info("product_name_new.new共有"+Max+"个词最大切分不包含原始词");
        logger.info("product_name_new.new共有"+Full+"个词全切分不包含原始词");
	}
	
	
	@Test(enabled = true)
	public void testSegment_author_new(){
		String url=SegmentTest.class.getResource("/author_new_new.new").getPath(); 
		File file = new File(url);
        BufferedReader reader = null;
        int Max = 0;
        int Full = 0; 
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
//            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
            //product_name_new文件内容太大，只取前10000个
            for (int i = 0; i < 10000; i++) {
            	tempString = reader.readLine();
            	System.out.println("query:"+tempString);
//                String oldMax = segment(urlOld, tempString, MaxSegment);
                String newMax = segment(urlNew, tempString, MaxSegment);
                String[] maxary = newMax.split(" ");
//                System.out.println("newMax:"+newMax);
                if (!containquery(maxary,tempString)) {
					logger.info("MaxSegment   不包含初始完整词: "+tempString);
					Max++;
				}
                
//                String oldFull = segment(urlOld, tempString, FullSegment);
                String newFull = segment(urlNew, tempString, FullSegment);
                String[] fullary = newFull.split(" ");
                if (!containquery(fullary,tempString)) {
					logger.info("FullSegment  不包含初始完整词: "+tempString);
					Full++;
				}
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        logger.info("author_new_new.new共有"+Max+"个词最大切分不包含原始词");
        logger.info("author_new_new.new共有"+Full+"个词全切分不包含原始词");
	}
	
	
	@Test(enabled = true)
	public void testSegment_brand_new(){
		String url=SegmentTest.class.getResource("/brand_new.new").getPath(); 
		File file = new File(url);
        BufferedReader reader = null;
        int Max = 0;
        int Full = 0; 
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
            //product_name_new文件内容太大，只取前10000个
//            for (int i = 0; i < 10000; i++) {
//            	tempString = reader.readLine();
            	System.out.println("query:"+tempString);
//                String oldMax = segment(urlOld, tempString, MaxSegment);
                String newMax = segment(urlNew, tempString, MaxSegment);
                String[] maxary = newMax.split(" ");
//                System.out.println("newMax:"+newMax);
                if (!containquery(maxary,tempString)) {
					logger.info("MaxSegment   不包含初始完整词: "+tempString);
					Max++;
				}
                
//                String oldFull = segment(urlOld, tempString, FullSegment);
                String newFull = segment(urlNew, tempString, FullSegment);
                String[] fullary = newFull.split(" ");
                if (!containquery(fullary,tempString)) {
					logger.info("FullSegment  不包含初始完整词: "+tempString);
					Full++;
				}
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        logger.info("brand_new.new共有"+Max+"个词最大切分不包含原始词");
        logger.info("brand_new.new共有"+Full+"个词全切分不包含原始词");
	}
	
	@Test(enabled = true)
	public void testSegment_top2000_new(){
		String url=SegmentTest.class.getResource("/top2000query.txt").getPath(); 
		File file = new File(url);
        BufferedReader reader = null;
        int Max = 0;
        int Full = 0; 
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
            //product_name_new文件内容太大，只取前10000个
//            for (int i = 0; i < 10000; i++) {
//            	tempString = reader.readLine();
            	System.out.println("query:"+tempString);
//                String oldMax = segment(urlOld, tempString, MaxSegment);
                String newMax = segment(urlNew, tempString, MaxSegment);
                String[] maxary = newMax.split(" ");
//                System.out.println("newMax:"+newMax);
                if (!containquery(maxary,tempString)) {
					logger.info("MaxSegment   不包含初始完整词: "+tempString);
					Max++;
				}
                
//                String oldFull = segment(urlOld, tempString, FullSegment);
                String newFull = segment(urlNew, tempString, FullSegment);
                String[] fullary = newFull.split(" ");
                if (!containquery(fullary,tempString)) {
					logger.info("FullSegment  不包含初始完整词: "+tempString);
					Full++;
				}
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        logger.info("top2000query共有"+Max+"个词最大切分不包含原始词");
        logger.info("top2000query共有"+Full+"个词全切分不包含原始词");
	}
	
	
	
	/*
	 * 以下是废弃调试代码
	 */
	
	public static void main(String[] args) {
		String oldString = doGet(urlOld, "纸尿裤拍拍乳", CHARSET);
		System.out.println(oldString);
		
//		System.out.println(segment(urlOld, "纸尿裤拍拍乳", MaxSegment));
		//正则表达式
//		Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
//		Pattern pattern = Pattern.compile("(<p>MaxSegment:</p>)(.+)(<p>FullSegment:</p>)");
//		Pattern pattern = Pattern.compile("(MaxSegment)(.+?)(FullSegment)");
//		Pattern pattern = Pattern.compile("MaxSegment");
//		Matcher matcher = pattern.matcher(oldString);
//		String string = matcher.replaceAll("");
//		System.out.println(string);
		
//		String s = "dsadsadas<peter>dsadasdas<lionel>\"www.163.com\"<kenny><>";
//		Pattern pattern = Pattern.compile("(<[^>]*>)");
//		Matcher matcher = pattern.matcher(s);
//		List<String> result=new ArrayList<String>();
//		while(matcher.find()){
//			System.out.println(matcher.group());
//			System.out.println(matcher.group());
//			System.out.println(matcher.group());
//			result.add(matcher.group());
//		}
//		for(String s1:result){
//			System.out.println(s1);
//		}
//		
	}
	
	@Test(enabled = false)
	public void testSegment_prod(){
		String url=SegmentTest.class.getResource("/product_name_new.new").getPath(); 
		File file = new File(url);
        BufferedReader reader = null;
        int Max = 0;
        int Full = 0; 
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
//            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
            //product_name_new文件内容太大，只取前10000个
            for (int i = 0; i < 10000; i++) {
            	tempString = reader.readLine();
            	System.out.println(tempString);
                String oldMax = segment(urlOld, tempString, MaxSegment);
                String newMax = segment(urlNew, tempString, MaxSegment);
                if (!oldMax.equals(newMax)) {
					logger.info("MaxSegment不一致: "+tempString);
					Max++;
				}
                
                String oldFull = segment(urlOld, tempString, FullSegment);
                String newFull = segment(urlNew, tempString, FullSegment);
                if (!oldFull.equals(newFull)) {
					logger.info("FullSegment不一致:"+tempString);
					Full++;
				}
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        logger.info("product_name_new.new共有"+Max+"个词不一致");
        logger.info("product_name_new.new共有"+Full+"个词不一致");
	}
	
	
	
	
	



	@Test(enabled = false)
	public void testSegment_brand(){
		String url=SegmentTest.class.getResource("/brand_new.new").getPath(); 
		File file = new File(url);
        BufferedReader reader = null;
        int Max = 0;
        int Full = 0; 
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
            	System.out.println(tempString);
                String oldMax = segment(urlOld, tempString, MaxSegment);
                String newMax = segment(urlNew, tempString, MaxSegment);
                if (!oldMax.equals(newMax)) {
					logger.info("MaxSegment不一致: "+tempString);
					Max++;
				}
                
                String oldFull = segment(urlOld, tempString, FullSegment);
                String newFull = segment(urlNew, tempString, FullSegment);
                if (!oldFull.equals(newFull)) {
					logger.info("FullSegment不一致:"+tempString);
					Full++;
				}
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        logger.info("brand_new.new共有"+Max+"个词MaxSegment不一致");
        logger.info("brand_new.new共有"+Full+"个词FullSegment不一致");
	}
	
	@Test(enabled = false)
	public void testSegment_auth(){
		String url=SegmentTest.class.getResource("/author_new_new.new").getPath(); 
		File file = new File(url);
        BufferedReader reader = null;
        int Max = 0;
        int Full = 0; 
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
//            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
            //author_new_new文件内容太大，只取前10000个
            for (int i = 0; i < 10000; i++) {
            	tempString = reader.readLine();
            	System.out.println(tempString);
                String oldMax = segment(urlOld, tempString, MaxSegment);
                String newMax = segment(urlNew, tempString, MaxSegment);
                if (!oldMax.equals(newMax)) {
					logger.info("MaxSegment不一致: "+tempString);
					Max++;
				}
                
                String oldFull = segment(urlOld, tempString, FullSegment);
                String newFull = segment(urlNew, tempString, FullSegment);
                if (!oldFull.equals(newFull)) {
					logger.info("FullSegment不一致:"+tempString);
					Full++;
				}
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        logger.info("author_new_new.new共有"+Max+"个词MaxSegment不一致");
        logger.info("author_new_new.new共有"+Full+"个词FullSegment不一致");
	}
	
	@Test(enabled = false)
	public void testSegment_top2000(){
		String url=SegmentTest.class.getResource("/top2000query.txt").getPath(); 
		File file = new File(url);
        BufferedReader reader = null;
        int Max = 0;
        int Full = 0; 
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
            	System.out.println(tempString);
                String oldMax = segment(urlOld, tempString, MaxSegment);
                String newMax = segment(urlNew, tempString, MaxSegment);
                if (!oldMax.equals(newMax)) {
					logger.info("MaxSegment不一致: "+tempString);
					Max++;
				}
                
                String oldFull = segment(urlOld, tempString, FullSegment);
                String newFull = segment(urlNew, tempString, FullSegment);
                if (!oldFull.equals(newFull)) {
					logger.info("FullSegment不一致:"+tempString);
					Full++;
				}
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        logger.info("brand_new.new共有"+Max+"个词MaxSegment不一致");
        logger.info("brand_new.new共有"+Full+"个词FullSegment不一致");
	}
	
	
	
	
	//获取html
	public static String doGet(String url,String query,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        if(StringUtils.isBlank(query)){
            return null;
        }
        try {
            url += query;
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
        	if(e instanceof RequestAbortedException || e instanceof SocketTimeoutException ||e instanceof SocketException){
        		try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		return doGet(url,query,charset);
        	}
        	
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
            e.printStackTrace();
        }	
        return null;
    }
	
	
	 /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    
	public static String segment(String url,String query ,int local){
		
		String resultHtml = doGet(url, query, CHARSET);
//		System.out.println(resultHtml);
		String value = "";
		Document doc = Jsoup.parse(resultHtml);
//		Element content = doc.getElementById("content");
		Elements values = doc.getElementsByTag("p");
		value = values.get(local).toString();
//		System.out.println(value);
		Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(value);
		String valueString = matcher.replaceAll("");
//		System.out.println(valueString);
		String a = valueString.replace("&nbsp; ", "");
//		System.out.println(a);
		return a;
		
	}
	
	
	private boolean containquery(String[] fullary, String tempString) {
		// TODO Auto-generated method stub
		for (int i = 0; i < fullary.length; i++) {
			if (fullary[i].equals(tempString)) {
				return true;
			}
		}
		return false;
	}
	
}
