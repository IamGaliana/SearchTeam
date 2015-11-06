package com.dangdang.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.dangdang.data.SourceItem;

public class FileUtil {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	private File file;
	private File failedfile;
	private File successfile;
	private File untestedfile;
	private File termFixedFile;
	private File fullFixedFile;
	/**
	 * 文件处理工具类；用来打开文件输入/输出流；初始化搜索项文件reader
	 */
	public FileUtil() {
		try {
			Config config = new Config();
			String filePath = config.get_itemFile();
			this.failedfile = new File(config.get_failedFile());
			if (!this.failedfile.exists()) {
				this.failedfile.createNewFile();
			}
			this.successfile = new File(config.get_successFile());
			if (!this.successfile.exists()) {

				this.successfile.createNewFile();
			}

			this.untestedfile = new File(config.get_untestFile());
			if (!this.untestedfile.exists()) {
				this.untestedfile.createNewFile();
			}

			this.termFixedFile = new File(config.get_termFixFile());
			if (!this.termFixedFile.exists()) {
				this.termFixedFile.createNewFile();
			}

			this.fullFixedFile = new File(config.get_fullFixFile());
			if (!this.fullFixedFile.exists()) {
				this.fullFixedFile.createNewFile();
			}
			this.file = new File(filePath);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
		}
	}

	public List<SourceItem> initSourceItem() {
		try {
			List<SourceItem> list = new ArrayList<SourceItem>();
			FileInputStream in = new FileInputStream(file);
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = bf.readLine()) != null) {
				logger.debug(" - [FileUtil] - 读取内容：" + line);
				String[] terms = line.split("\t");
				SourceItem item = new SourceItem(terms[0], terms[1], terms[2]);
				list.add(item);
			}
			bf.close();
			in.close();
			return list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			e.printStackTrace();
			return null;
		}
	}

	public BufferedWriter failedOutput() {
		try {
			FileOutputStream out = new FileOutputStream(failedfile, true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					out));
			return writer;
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return null;
		}
	}

	public BufferedWriter successOutput() {
		try {
			FileOutputStream out = new FileOutputStream(successfile, true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					out));
			return writer;
		} catch (Exception e) {
			return null;
		}
	}

	public BufferedWriter untestOutput() {
		try {
			FileOutputStream out = new FileOutputStream(untestedfile, true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					out));
			return writer;
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return null;
		}
	}

	public BufferedWriter termFixOnlyOutput() {
		try {
			FileOutputStream out = new FileOutputStream(termFixedFile, true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					out));
			return writer;
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return null;
		}
	}

	public BufferedWriter fullfixedOutput() {
		try {
			FileOutputStream out = new FileOutputStream(fullFixedFile, true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					out));
			return writer;
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			logger.error(" - [LOG_EXCEPTION] - "+exception);
			return null;
		}
	}

	public static boolean exists(String strPath){		
		File file = new File(strPath);
		return (file.exists() || file.isDirectory());
	}
	
	public static boolean isFile(String strPath){		
		File file = new File(strPath);
		return file.isFile();
	}
	
	public static boolean isFolder(String strPath){		
		File file = new File(strPath);
		return file.isDirectory();
	}
	
	public static boolean hasFiles(String strPath){		
		File file = new File(strPath);
		if(file.listFiles().length > 0)
			return true;
		else 
			return false;
	}
	
	public static File[] getSubFiles(String strPath){		
		File file = new File(strPath);
		File[] files = file.listFiles();		
		return files;		
	}
	
	public static void createNewFile(String strFilePath){
		File file=new File(strFilePath);    
		if(!file.exists()){    
		    try{
		    	file.createNewFile();    
		    }catch (IOException e) {   
		    	logger.error("create new file failed");
		        e.printStackTrace();    
		    }    
		}    
	}
	
	public static void createNewFolder(String strPath){
		File file =new File(strPath);    
		//如果文件夹不存在则创建    
		if(!file .exists()  && !file .isDirectory())
			file .mkdir();
		else
			logger.error("create new file failed");
	}
	
	/**
	 * 读取指定网络位置的文件，并写入到output.txt中，返回该文件
	 * @param strUrl
	 * @return
	 * @throws IOException
	 */
	public static String getRemoteFile(String strUrl) throws IOException { 
		String fileName = "output.txt";
		URL url = new URL(strUrl); 
		HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
		DataInputStream input = new DataInputStream(conn.getInputStream()); 
		DataOutputStream output = new DataOutputStream(new FileOutputStream(fileName)); 
		byte[] buffer = new byte[1024 * 8]; 
		int count = 0; 
		while ((count = input.read(buffer)) > 0) { 
		output.write(buffer, 0, count); 
		} 
		output.close(); 
		input.close(); 
		return fileName; 
	}
	
	/**
	 * 读取filePath给定的文件，把第一列数据写入lists列表返回
	 * @param filePath
	 * @return
	 */
	 public static List<String> readTxtFile(String filePath){
		 List<String> lists = new ArrayList<String>();
	     try {	    	
	    	 String encoding="GBK";
             File file=new File(filePath);
             if(file.isFile() && file.exists()){ //判断文件是否存在
            	 InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
	             BufferedReader bufferedReader = new BufferedReader(read);
	             String lineTxt = null;
	             while((lineTxt = bufferedReader.readLine()) != null){
	            	 lists.add(lineTxt.split("\\t")[0]);
	             }
                 read.close();
	         }else{
	        	 System.out.println("找不到指定的文件");
	        	 }
             } catch (Exception e) {
            	 System.out.println("读取文件内容出错");
            	 e.printStackTrace();
             }
	     return lists;
	 }

	 /**
	  * 把指定内容content写入文件file
	  * @param content		要写入的内容
	  * @param fileName		指定文件路径
	  */
	 public static void writeToTxt(String content, String fileName){	
		  File file = new File(fileName);
          FileWriter fw = null;
          BufferedWriter writer = null;
          try {
        	  if((file.exists() && file.isFile()) || file.createNewFile()){
	        	  fw = new FileWriter(file);
	        	  writer = new BufferedWriter(fw);	
	        	  writer.write("");
	        	  writer.write(content);
	        	  writer.newLine();//换行
	        	  writer.flush();
        	  }
          } catch (FileNotFoundException e) {
        	  e.printStackTrace();
          }catch (IOException e) {
           		e.printStackTrace();
          }finally{
        	  try {
        		  writer.close();
        		  fw.close();
        	  } catch (IOException e) {
        		  e.printStackTrace();
        	  }
        }
	  }
 	
	 /**
	  * 创建文件夹 
	  * @param strFilePath
	  * @return
	  */
     public boolean mkdirFolder(String strFilePath) {
    	 boolean bFlag = false;  
         try {  
             File file = new File(strFilePath.toString());  
             if (!file.exists()) {  
                 bFlag = file.mkdir();  
             }  
         } catch (Exception e) {  
             logger.error("新建目录操作出错" + e.getLocalizedMessage());  
             e.printStackTrace();  
         }  
         return bFlag;  
     }  

     /** 
      * 删除文件 
      *  
      * @param strFilePath 
      * @return 
      */  
     public static boolean removeFile(String strFilePath) {  
         boolean result = false;  
         if (strFilePath == null || "".equals(strFilePath)) {  
             return result;  
         }  
         File file = new File(strFilePath);  
         if (file.isFile() && file.exists()) {  
             result = file.delete();  
             if (result == Boolean.TRUE) {  
                 logger.debug("[REMOE_FILE:" + strFilePath + "删除成功!]");  
             } else {  
                 logger.debug("[REMOE_FILE:" + strFilePath + "删除失败]");  
             }  
         }  
         return result;  
     }  
   
     /** 
      * 删除文件夹(包括文件夹中的文件内容，文件夹) 
      *  
      * @param strFolderPath 
      * @return 
      */  
     public static boolean removeFolder(String strFolderPath) {  
         boolean bFlag = false;  
         try {  
             if (strFolderPath == null || "".equals(strFolderPath)) {  
                 return bFlag;  
             }  
             File file = new File(strFolderPath.toString());  
             bFlag = file.delete();  
             if (bFlag == Boolean.TRUE) {  
                 logger.debug("[REMOE_FOLDER:" + file.getPath() + "删除成功!]");  
             } else {  
                 logger.debug("[REMOE_FOLDER:" + file.getPath() + "删除失败]");  
             }  
         } catch (Exception e) {  
             logger.error("FLOADER_PATH:" + strFolderPath + "删除文件夹失败!");  
             e.printStackTrace();  
         }  
         return bFlag;  
     }  
   
     /** 
      * 移除所有文件 
      *  
      * @param strPath 
      */  
     public static void removeAllFile(String strPath) {  
         File file = new File(strPath);  
         if (!file.exists()) {  
             return;  
         }  
         if (!file.isDirectory()) {  
             return;  
         }  
         String[] fileList = file.list();  
         File tempFile = null;  
         for (int i = 0; i < fileList.length; i++) {  
             if (strPath.endsWith(File.separator)) {  
                 tempFile = new File(strPath + fileList[i]);  
             } else {  
                 tempFile = new File(strPath + File.separator + fileList[i]);  
             }  
             if (tempFile.isFile()) {  
                 tempFile.delete();  
             }  
             if (tempFile.isDirectory()) {  
                 removeAllFile(strPath + "/" + fileList[i]);// 下删除文件夹里面的文件  
                 removeFolder(strPath + "/" + fileList[i]);// 删除文件夹  
             }  
         }  
     }  
   
     public static void copyFile(String oldPath, String newPath) {  
         try {  
             int bytesum = 0;  
             int byteread = 0;  
             File oldfile = new File(oldPath);  
             if (oldfile.exists()) { // 文件存在时  
                 InputStream inStream = new FileInputStream(oldPath); // 读入原文件  
                 FileOutputStream fs = new FileOutputStream(newPath);  
                 byte[] buffer = new byte[1444];  
                 while ((byteread = inStream.read(buffer)) != -1) {  
                     bytesum += byteread; // 字节数 文件大小  
                     System.out.println(bytesum);  
                     fs.write(buffer, 0, byteread);  
                 }  
                 inStream.close();  
                 logger.debug("[COPY_FILE:" + oldfile.getPath() + "复制文件成功!]");  
             }  
         } catch (Exception e) {  
             System.out.println("复制单个文件操作出错 ");  
             e.printStackTrace();  
         }  
     }  
   
     public static void copyFolder(String oldPath, String newPath) {  
         try {  
             (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹  
             File a = new File(oldPath);  
             String[] file = a.list();  
             File temp = null;  
             for (int i = 0; i < file.length; i++) {  
                 if (oldPath.endsWith(File.separator)) {  
                     temp = new File(oldPath + file[i]);  
                 } else {  
                     temp = new File(oldPath + File.separator + file[i]);  
                 }  
                 if (temp.isFile()) {  
                     FileInputStream input = new FileInputStream(temp);  
                     FileOutputStream output = new FileOutputStream(newPath  
                             + "/ " + (temp.getName()).toString());  
                     byte[] b = new byte[1024 * 5];  
                     int len;  
                     while ((len = input.read(b)) != -1) {  
                         output.write(b, 0, len);  
                     }  
                     output.flush();  
                     output.close();  
                     input.close();  
                     logger.debug("[COPY_FILE:" + temp.getPath() + "复制文件成功!]");  
                 }  
                 if (temp.isDirectory()) {// 如果是子文件夹  
                     copyFolder(oldPath + "/ " + file[i], newPath + "/ "  
                             + file[i]);  
                 }  
             }  
         } catch (Exception e) {  
             System.out.println("复制整个文件夹内容操作出错 ");  
             e.printStackTrace();  
         }  
     }  
   
     public static void moveFile(String oldPath, String newPath) {  
         copyFile(oldPath, newPath);  
         //removeFile(oldPath);  
     }  
   
     public static void moveFolder(String oldPath, String newPath) {  
         copyFolder(oldPath, newPath);  
         //removeFolder(oldPath);  
     }  
    
     /**
      * 过滤已某种后缀结尾的文件 
      * @param strPath	路径
      * @param suffix	文件后缀
      */
     public static void refreshFileList(String strPath, String suffix) {  
    	 Set<String> sets = new HashSet<String>();  
         File dir = new File(strPath);  
         File[] files = dir.listFiles();  
         if (files == null) {  
             return;  
         }  
         for (int i = 0; i < files.length; i++) {  
             if (files[i].isDirectory()) {  
                 refreshFileList(files[i].getAbsolutePath(), suffix);  
             } else {  
                 String strFilePath = files[i].getAbsolutePath().toLowerCase();  
                 String strName = files[i].getName();  
                 if (strName.endsWith(suffix)) {  
                     boolean bFlag = sets.add(strName);  
                     if (bFlag == Boolean.FALSE) {  
                         // 删除重复文件  
                        //removeFile(strFilePath);  
                     }  
                 }  
             }  
         }  
     }  

     public static void compareFiles(String strPath1, String strPath2) throws FileNotFoundException{
 		FileInputStream fs1 = null;
 		FileInputStream fs2 = null;
// 		BufferedReader reader = null;
// 		String sFile;
 		
 		fs1 = new FileInputStream(strPath1);
 		fs2 = new FileInputStream(strPath2);
 		
 		/*
 		 * 读取文件中的内容，打到log里
 		 */
// 		try{
// 			reader = new BufferedReader(new FileReader(strPath1));
// 			logger.info("File1: ");
// 			while((sFile = reader.readLine()) != null){
// 				logger.info(sFile);
// 			}
// 			
// 			reader = new BufferedReader(new FileReader(strPath2));
// 			logger.info("File2: ");
// 			while((sFile = reader.readLine()) != null){
// 				logger.info(sFile);
// 			}
// 		}catch(IOException e){
// 			logger.info(e);
// 		}finally{
// 			try{
// 				if(reader != null)
// 					reader.close();
// 			}catch(Exception e){
// 				logger.info(e);
// 			}
// 		}
 		
 		/*
 		 * 比较内容
 		 */
 		try{
 			if(fs1.available() != fs2.available()){
 				logger.error("[" + strPath1 + "] does not equal to [" + strPath2+ "]");  
 			}else{
 				boolean tag = true;
 				while(fs1.read() != -1 && fs2.read() != -1){
 					if(fs1.read() != fs2.read()){
 						tag = false;
 						break;
 					}
 				}
 				
 				if(tag == true)
 					logger.info("[" + strPath1 + "] equals to [" + strPath2 + "]");  
 				else
 					logger.error("[" + strPath1 + "] does not equal to [" + strPath2 + "]"); 
 			}
 		}catch(IOException e){
 			 e.printStackTrace(); 
 		}
 		
 		try{
 			if(fs1 != null)
 				fs1.close();
 			if(fs2 != null)
 				fs2.close();  
         }catch (IOException e){  
             e.printStackTrace();  
         } 
 	}
}
