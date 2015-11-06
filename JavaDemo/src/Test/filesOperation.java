package Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFWriter;


/**
 * 给孙浩写的工具
 * @author gaoyanjun
 *
 */
public class filesOperation {
	
	public static List<String> m_files = new ArrayList<String>();
	public static List<String> l_file = new ArrayList<String>();
	public enum  SAVEDFIELD{
		ks_yyjb,
		js_zkz,
		ks_xml,
		ks_bpxx,
		ks_bpxxmc
	}
	
	
	public static void main(String[] args){
		try {
			m_files = GetFileMap("D:/test");
			System.out.println("files in map:");
			for(String file : m_files){
				System.out.println("file:" + file);
				//ReadDBF(file);
				
			}
			ReadDBF(m_files.get(0));
			writeDBF(m_files.get(0));
		} catch (IOException e) {
			System.out.println("IOException at main method: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Ok!");
	}
	
	
	
	/*
	 *
	 */
	public static List<String> GetFileMap(String path) throws FileNotFoundException, IOException{		
		try{			
			File file = new File(path);
			File[] fileList = file.listFiles();
			for(int i=0;i<fileList.length; i ++){			
				if(fileList[i].isFile()){
					// e.g:
					// file path is:D:\test\cet6-2\cet6-2.xlsx
					// file name is:cet6-2.xlsx
					l_file.add(fileList[i].getAbsolutePath());
				}else if(fileList[i].isDirectory()){
					GetFileMap(fileList[i].getPath());
				}
			}
		}
		catch(FileNotFoundException e){
			System.out.println("GetFileMap() exception:" + e.getMessage());
		}
		return l_file;
	}

	public static void ReadDBF(String path){
		InputStream fis = null;
		OutputStream fos = null;
        try 
        {  
            //璇诲彇鏂囦欢鐨勮緭鍏ユ祦 
            fis = new FileInputStream(path);
            //鏍规嵁杈撳叆娴佸垵濮嬪寲涓�涓狣BFReader瀹炰緥锛岀敤鏉ヨ鍙朌BF鏂囦欢淇℃伅
            DBFReader reader = new DBFReader(fis);  
            // 鍒濆鍖栦竴涓狣BFWriter瀹炰緥锛岀敤鏉ュ啓鍏BF鏂囦欢
            DBFWriter writer = new DBFWriter(); 
            
            //璋冪敤DBFReader瀵瑰疄渚嬫柟娉曞緱鍒皃ath鏂囦欢涓瓧娈电殑涓暟 
            int fieldsCount = reader.getFieldCount();   
            int rowsCount = reader.getRecordCount();
            //System.out.println("瀛楁鏁�:"+fieldsCount);
            
            //瀹氫箟DBF鏂囦欢瀛楁   			  
		    DBFField[] writeFields = new DBFField[5]; 
		    int index = 0;
            //閬嶅巻鍘熷鏂囦欢涓墍鏈夊瓧娈靛悕   
            for( int i=0; i < fieldsCount; i++)    
            {   
            	//瀛楁鍚�
            	String name = reader.getField(i).getName();          
            	//鎵惧埌闇�瑕佷繚鐣欑殑瀛楁鍚嶏紝寰楀埌瀛楁绫诲瀷鍜岄暱搴︼紝瀹氫箟鏂扮殑瀛楁
            	for(SAVEDFIELD savedfield: SAVEDFIELD.values()){
            		if(name.equals(savedfield.toString())){
	            		 byte dataType = reader.getField(i).getDataType();
	            		 int fieldLength = reader.getField(i).getFieldLength();
	            		 writeFields[index] = new DBFField();    
	            		 writeFields[index].setName(name);   
	            		 writeFields[index].setDataType(dataType);   
	            		 writeFields[index].setFieldLength(fieldLength);
	            		 index ++;
	            		 break;
            	  }
            	}
            }
            //鍐欏叆鏂囦欢娴�
            writer.setFields(writeFields);   
            
            //Object[] rowData = new Object[5]; 
            //for(int m = 0; m < rowsCount ; m++){
        	   //rowData[0] = 
            //}
           
            Object[] rowValues;   
            //涓�鏉℃潯鍙栧嚭path鏂囦欢涓褰�   
            while((rowValues = reader.nextRecord()) != null) 
            {   
              for( int i=0; i<rowValues.length; i++) 
              {   
            	 
                System.out.println(rowValues[i]); 
              }   
            }   
            
          }catch(Exception e){   
        	  e.printStackTrace();  
          }finally{
        	  try{
        		  fis.close();
        		 }catch(Exception e){
        			  
        		 }  
        	  } 
        }

	public static boolean RemoveFiled(String filedName){
		boolean success = false;
		return success;
	}
	
	public static void writeDBF(String path)
	 {
		OutputStream fos = null;
		  try  
		  {   
		      //瀹氫箟DBF鏂囦欢瀛楁   			  
		      DBFField[] fields = new DBFField[5]; 
		      //鍒嗗埆瀹氫箟鍚勪釜瀛楁淇℃伅锛宻etFieldName鍜宻etName浣滅敤鐩稿悓锛� 
		      //鍙槸setFieldName宸茬粡涓嶅缓璁娇鐢�   
		      fields[0] = new DBFField();    
		      fields[0].setName("ks_yyjb");   
		      fields[0].setDataType(DBFField.FIELD_TYPE_C);   
		      fields[0].setFieldLength(10);   
		 
		      fields[1] = new DBFField();   
		      fields[1].setName("js_zkz");   
		      fields[1].setDataType(DBFField.FIELD_TYPE_C);   
		      fields[1].setFieldLength(20);   
		 
		      fields[2] = new DBFField();    
		      fields[2].setName("ks_xml");  
		      fields[2].setDataType(DBFField.FIELD_TYPE_N);   
		      fields[2].setFieldLength(12);   
		      fields[2].setDecimalCount(2);   
		      
		      fields[3] = new DBFField();    
		      fields[3].setName("ks_bpxx");   
		      fields[3].setDataType(DBFField.FIELD_TYPE_C);   
		      fields[3].setFieldLength(20);   
		 
		      fields[4] = new DBFField();   
		      fields[4].setName("ks_bpxxmc");  
		      fields[4].setDataType(DBFField.FIELD_TYPE_N);   
		      fields[4].setFieldLength(12);   
		      fields[4].setDecimalCount(2);
		 
		      //DBFWriter writer = new DBFWriter(new File(path));   
		 
		      //瀹氫箟DBFWriter瀹炰緥鐢ㄦ潵鍐橠BF鏂囦欢   
		      DBFWriter writer = new DBFWriter(); 
		      //鎶婂瓧娈典俊鎭啓鍏BFWriter瀹炰緥锛屽嵆瀹氫箟琛ㄧ粨鏋�  
		      writer.setFields(fields);   
		      
		      
		      //涓�鏉℃潯鐨勫啓鍏ヨ褰�   
		      Object[] rowData = new Object[5]; 
		      rowData[0] = "1000";   
		      rowData[1] = "John";   
		      rowData[2] = new Double(5000.00);
		      rowData[3] = "1000";   
		      rowData[4] = new Double(5000.00);
		      writer.addRecord(rowData);   
		 
		      rowData = new Object[5];  
		      rowData[0] = "1001";  
		      rowData[1] = "Lalit"; 
		      rowData[2] = new Double(3400.00);  
		      rowData[3] = "1001";  
		      rowData[4] = new Double(5000.00);
		      writer.addRecord(rowData);   
		 
		      
		      //瀹氫箟杈撳嚭娴侊紝骞跺叧鑱旂殑涓�涓枃浠�   
		      fos = new FileOutputStream(path);
		      //鍐欏叆鏁版嵁   
		      writer.write(fos);   
		 
		      //writer.write();  
		  }catch(Exception e)   
		  {   
		      e.printStackTrace();   
		  }   
		  finally  
		  {   
		      try{   
		      fos.close();
		      }catch(Exception e){}
		  }
	 }
}
