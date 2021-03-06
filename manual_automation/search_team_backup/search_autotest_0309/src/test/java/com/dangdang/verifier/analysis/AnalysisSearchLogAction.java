package com.dangdang.verifier.analysis;

import java.io.File;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Connection;

import com.dangdang.util.Config;
import com.dangdang.util.DBConnUtil;

/**
 * 扫描分析搜索日志。将有效信息过滤出来保存在数据库表中
 * @author zhangyamin
 *
 */
public class AnalysisSearchLogAction {

	/**
	 * @param args
	 */

	public static int length = 1024*1024;
	public static int offset = 0;
	private static Config config = new Config();
	private static String SearchLogPath =config.get_SearchLogFile();
	private static long size=0;
//	 static  int  length  =  0x8FFFFFF; 
	
	public static boolean doAction(){
	
		try {
			File f = new File(SearchLogPath);
			RandomAccessFile raFile = new RandomAccessFile(f,"rw");
			final FileChannel fc = raFile.getChannel();
			size= fc.size();
			
			//记录当前文档的剩余长度
			while(size>=0){
				//定义最大并行线程数
				while(Thread.activeCount()<=4){
					//创建新线程
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try{
								Connection conn = DBConnUtil.getConnection();
								//得到真实的文档剩余长度
								long realSize = size>length?length:size;
								//如果文档都已遍历完成，直接返回不再继续创建线程
								if(realSize<0){
									return;
								}
								//分配文档段落
								MappedByteBuffer out=fc.map(FileChannel.MapMode.READ_ONLY,offset*length,realSize);
								size = size-length;
			 					offset++;
			 					//转成字符串
								String content = byteBufferToString(out);  
//	                            System.out.println(content == null);
								//转成行
	                            LineNumberReader read = new  LineNumberReader(new StringReader(content));
	                            //按行取数据
	                            String line = read.readLine();
	                            //遍历所有行
	                            
	                            //数量记录器
	                            int iCount = 0;
	                            while (null != line)  
	                            {  
	                                //System.out.println(Thread.currentThread().getId()+" "+line);
	                            	//分割行数据，找到真正想要的数据
	                				String[] subArray = line.split("\t");
	                				//mysql的 query模板
	                				String queryF = "INSERT INTO Search_log_sample (`Query`,`查询次数`,`pid`,`位置`,`展示次数`,`点击次数`) VALUES ('%s','%s','%s','%s','%s','%s');";
	                				//点击次数不为零                                    点击位置是有效数据                                    query词中不包含问好                           query词长度大于3个字符
	                				if(!subArray[5].equals("0") && !subArray[3].equals("1000000") &&!subArray[0].contains("?") &&!(subArray[5].length()<=3)){
	                					
	                				String act_query = String.format(queryF, subArray[0],subArray[1],subArray[2],subArray[3],subArray[4],subArray[5]);
	                            	//这行插入
	                				DBConnUtil.exeUpdate(act_query, conn);
//	                            	System.out.println(act_query);
	                				}
	                                line = read.readLine();  
	                                
	                                iCount++;
	                                System.out.println(iCount);
	                            }
	                           
						}catch(Exception e){
							//因为是常照字符长度来截取文档片段。难免会发生截取的末尾没有将完整的数据保留下来。这个时候可能会进入这里。这里不做任何处理即可。也就是说数据不会被插入到数据库中。
							//直接跳到下一个线程。可以看到这个线程负责的数据可能进相当于丢失。不过丢失量相比总体来说，不大。
						}
						}
					});
					t.start();
					
					
				}
					
			}
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	  public static String byteBufferToString(ByteBuffer buffer)   
	    {  
	        CharBuffer charBuffer = null;  
	        try   
	        {  
	        	Charset inCharset = Charset.forName("GBK");
	            charBuffer = inCharset.decode(buffer);  
	            buffer.flip();  
	            return charBuffer.toString();  
	        }   
	        catch (Exception e)   
	        {  
	            e.printStackTrace();  
	            return null;  
	        }  
	    }  
	public static void main(String[] args) {
		doAction();
	}
}
