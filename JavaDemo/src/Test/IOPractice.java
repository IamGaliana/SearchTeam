package Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IOPractice {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Reader read = new Reader();
//		read.ReadFile();
		read.ReadData();
		
//		Writer writer = new Writer();
//		writer.WriteFile();
		
		
	}
}

class Reader {
	public void ReadFile() throws IOException{
		BufferedReader br = null;
		try{
			br=	new BufferedReader(new FileReader("file.txt"));
			String line = br.readLine();
			
			while(line != null){
				System.out.println(line);
				line = br.readLine();
			}
			br.close();
		}catch(IOException e){
			System.out.println("IO Problem!");
		} finally{
			br.close();
		}
	}

	/**
	 * 输出字符的ASCII码
	 * @throws IOException
	 */
	public void ReadData() throws IOException{
		int a = 0;
		System.out.println("pls give a char:");
		a= System.in.read();
		System.out.println("ASCII:" + a);
	}
}

class Writer{
	public void WriteFile() throws IOException{
		BufferedWriter bw = null;
		try{
			String content = "thanks for your fish.";
			
			File file = new File("new.txt");
			
			if (!file.exists()){
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsolutePath());
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch(IOException e){
			System.out.println("IO Problem");
		} finally{
			bw.close();
		}
		
	}

}
