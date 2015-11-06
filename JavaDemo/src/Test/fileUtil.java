package Test;

import java.io.*;

public class fileUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		if(args.length != 2){
//			System.out.println("The command line should be: java fileUtil test1.txt test2.txt");
//			System.exit(0);
//		}
		try{
			compareFiles("D:\\0.Personal\\JavaSeriesDemo\\src\\Test\\test1.txt", "D:\\0.Personal\\JavaSeriesDemo\\src\\Test\\test2.txt");
		}catch(FileNotFoundException e){
			System.out.println("file not found");
		}
		
	}
	
	public static void compareFiles(String strPath1, String strPath2) throws FileNotFoundException{
		FileInputStream File1 = null;
		FileInputStream File2 = null;
		BufferedReader reader = null;
		String sFile;
		
		File1 = new FileInputStream(strPath1);
		File2 = new FileInputStream(strPath2);
		
		try{
			reader = new BufferedReader(new FileReader(strPath1));
			System.out.println("File1: ");
			while((sFile = reader.readLine()) != null){
				System.out.println(sFile);
			}
			
			reader = new BufferedReader(new FileReader(strPath2));
			System.out.println("File2: ");
			while((sFile = reader.readLine()) != null){
				System.out.println(sFile);
			}
		}catch(IOException e){
			System.out.println(e);
		}finally{
			try{
				if(reader != null)
					reader.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		try{
			if(File1.available() != File2.available()){
				 System.out.println(strPath1 + " is not equal to " + strPath2);  
			}else{
				boolean tag = true;
				while(File1.read() != -1 && File2.read() != -1){
					if(File1.read() != File2.read()){
						tag = false;
						break;
					}
				}
				
				if(tag == true)
					System.out.println("[" + strPath1 + "] equals to [" + strPath2 + "]");  
				else
					System.out.println("[" + strPath1 + "] does not equal to [" + strPath2 + "]"); 
			}
		}catch(IOException e){
			  System.out.println(e);  
		}
		
		try{
			if(File1 != null)
				File1.close();
			if(File2 != null)
				File2.close();  
        }catch (IOException e){  
            System.out.println(e);  
        } 
	}
}
