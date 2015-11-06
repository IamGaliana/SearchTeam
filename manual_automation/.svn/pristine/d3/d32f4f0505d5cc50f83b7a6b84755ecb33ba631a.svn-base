package com.dangdang;
/**
 * @author dongxiaobing
 * 日志来源：http://10.5.24.148:9030/joblog/search_v3/log/
 * 测试：20万个query的平均响应时间
 * 线程数据：6
 * 第一次测试平均响应时间为：平均响应时间：64
 */


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.testng.annotations.Test;
import org.xml.sax.SAXParseException;

import com.dangdang.client.SearchRequester;
import com.dangdang.util.XMLParser;
import com.mysql.jdbc.log.Log;

public class MultiThreadGetQueryCost {
	private int lineNums;
	static Cost costInstance = new Cost();
	
	//保存指定query的文件
	private final  static String queryFileName=System.getProperty("user.dir") + "\\queryCostLog.dxb";
	
	//生成qury文件，只生成一次，
	@Test(enabled=false)
	public void parseLogFile() {
		// 取哪一天的日志
		String timeStamp = "2015-09-24";
		// 取包含如下内容的日志
		// 一共有多少行query数据
		int count = 0;
		String strSpilt = "query =";
		InputStreamReader fr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(queryFileName);
			fr = new InputStreamReader(new FileInputStream(
					"C:\\Users\\dongxiaobing\\Downloads\\log-20150924_1"));
			br = new BufferedReader(fr);
			StringBuffer sb = null;
			while (br.readLine() != null) {
				String line = br.readLine();

				if (line.contains(timeStamp) && line.contains(strSpilt)
						&& !line.contains("GET")) {

					sb = new StringBuffer();
					String query = line.substring(line.indexOf("query") + 8);
					if (query.contains(" ")) {
						query = query.replace(" ", "");
					}
					if (query.contains("http:/www.dangdang.com/")) {
						//System.out.println("dangdang");
						continue;
					}
					if (query.contains("http:/category.dangdang.com/")) {
						//System.out.println("cat");
						continue;
					}
					if (query.contains("BODY")) {
						System.out.println("BODY");
						continue;
					}

					if (!query.trim().contains("N")
							&& !query.trim().contains("\\")) {
						sb.append("http://10.5.25.161:8390/?").append(
								query.trim());
						//System.out.println("query:" + sb.toString());
						fw.write(sb.toString() + "\r\n");
						++count;
						if(count==200000){
							break;
						}
						//System.out.println("count:" + count);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Test(enabled = true)
	public void test() {
		try {
			// 线程数
			int ThreadCount = 6;
			// 获取文件多少行
			// lineNums=getLineNums();
			lineNums = 200000;
			// 计算出每个线程应该下载多少行
			int size = lineNums / ThreadCount;
			// 统计开始时间
			Long startTime = System.currentTimeMillis();
			Thread t[] = new Thread[ThreadCount];
			for (int i = 0; i < ThreadCount; i++) {
				// 计算线程下载的开始位置和结束位置
				/*
				 * 第一个线程：1,size 第二个线程：size+1,2*size 第三个线程：2*size+1,lineNums
				 */
				int startIndex = i * size + 1;
				int endIndex = (i + 1) * size;
				if (i == ThreadCount - 1) {
					endIndex = lineNums;
				}
				t[i] = new getCost(startIndex, endIndex, i, costInstance);
				System.out.println("startIndex:" + startIndex + " endIndex:"
						+ endIndex);
				t[i].start();

			}
			for (int i = 0; i < ThreadCount; i++) {
				t[i].join();
			}
			Long endTime = System.currentTimeMillis();
			System.out.println("测试一共发费：" + (endTime - startTime) / 1000+"秒");
			System.out.println("共发送了：" + costInstance.getLineNums() + "次query");
			System.out.println("平均响应时间：" + costInstance.getCost()/ costInstance.getLineNums());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获取文件一共有多少行
	public int getLineNums() {
		int lineNums = 0;
		InputStreamReader fr = null;
		BufferedReader br = null;
		// FileWriter fw = new FileWriter("log.dxb");
		try {
			fr = new InputStreamReader(new FileInputStream(queryFileName));
			br = new BufferedReader(fr);
			StringBuffer sb = null;
			while (br.readLine() != null) {
				lineNums++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return lineNums;
	}
}

class getCost extends Thread {
	int startIndex;
	int endIndex;
	int threadId;
	Cost costInstance;

	public getCost(int startIndex, int endIndex, int threadId, Cost costInstance) {
		super();
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.threadId = threadId;
		this.costInstance = costInstance;
	}

	@Override
	public void run() {
		// 获取query，发送请求
		FileReader file = null;
		LineNumberReader reader = null;
		try {
			file = new FileReader(System.getProperty("user.dir") + "\\queryCostLog.dxb");
			reader = new LineNumberReader(file);
			// 跳转到当前线程读取的起始行
			reader.setLineNumber(startIndex);
			while (reader.getLineNumber() <= endIndex) {
				System.out.println("-------------------线程" + threadId + "读取"
						+ reader.getLineNumber() + "行");
				String line = reader.readLine();
				// 跳转到下一行
				reader.setLineNumber(++startIndex);
				System.out.println("请求url:" + line);
				String xml = SearchRequester.get(line.trim());

				if (xml.contains("&")) {
					xml = xml.replace("&", "&amp;");
				}

				Document doc = XMLParser.read(xml);

				// 获取cost节点
				String cost = XMLParser.getSingleNode(doc, "//cost");
				System.out.println(Thread.currentThread().getName() + " "
						+ "增加cost.....");

				costInstance.addCost(Integer.parseInt(cost.trim()));

			}
			System.out.println("-------------------线程" + threadId
					+ "下载完毕-------------------");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}

class Cost {
	private int lineNums;
	private long costSum;

	public synchronized void addCost(int cost) {
		this.lineNums++;
		this.costSum += cost;
		System.out.println("lineNums:" + lineNums);
		System.out.println("costSum:" + costSum);
	}

	public synchronized long getCost() {
		return this.costSum;
	}

	public synchronized int getLineNums() {
		return this.lineNums;
	}

}
