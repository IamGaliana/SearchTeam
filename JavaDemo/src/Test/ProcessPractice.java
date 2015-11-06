package Test;

import java.io.IOException;

public class ProcessPractice {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("c:\\windows\\notepad.exe");
		
		System.out.println("max memory: " + rt.maxMemory() + "bytes");
		System.out.println("total memory: " + rt.totalMemory() + "bytes");
		System.out.println("left memory: " + rt.freeMemory() + "bytes");

	}

}
