package Test;

import java.util.ArrayList;
import java.util.List;

public class Basic  {

	public static void main(String[] args) {
		// how to declare a array:
		String[] b = new String[]{"a","b"};		
		
		Magic m = new Magic();
		m.CountLeapYear();
		m.SumOf999();
	}
}


class Magic{
	/*
	 * 计算闰年
	 * 非整数年：能被4整除，但不能被100整除的年是闰年
	 * 整数年：能被400整除的是闰年
	 */
	 void CountLeapYear(){
		List<Integer> leapyear = new ArrayList<Integer>();
		List<Integer> nonLeapyear = new ArrayList<Integer>();
		
		for(int year = 2000 ; year <= 5000; year ++){
			if((year % 100 != 0 && year % 4 == 0) || year % 400 == 0){				
				leapyear.add(year);
			} else {
				nonLeapyear.add(year);
			}
			
		}
		
		System.out.println("以下" + leapyear.size() + "个年份为闰年：" + leapyear.toString());
		System.out.println("以下" + nonLeapyear.size() + "个年份为平年：" + nonLeapyear.toString());
	}

	/*
	 * sum of 1-999
	 */	
	 void SumOf999(){
		int i = 0;
		int sum = 0;
		while(i <= 999){
			sum += i;
			i++;
		}
		System.out.println("sum of 1 to 999 is: " + sum);
	}
}