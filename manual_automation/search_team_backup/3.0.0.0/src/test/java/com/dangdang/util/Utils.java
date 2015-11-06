package com.dangdang.util;

public class Utils {
	
	public static void sendMail(String subject, String content, String format){
	     MailSenderInfo mailInfo = new MailSenderInfo();      
	     mailInfo.setMailServerHost("sl2k8mail-01.dangdang.com");      
	     mailInfo.setMailServerPort("587");      
	     mailInfo.setValidate(true);      
	     mailInfo.setUserName("test_report@dangdang.com");      
	     mailInfo.setPassword("Sl-1234");//您的邮箱密码      
	     mailInfo.setFromAddress("test_report@dangdang.com");      
	     mailInfo.setToAddress("liuzhipengjs@dangdang.com");
//	     mailInfo.setCcAddress("zhangruichao@dangdang.com");
//	     mailInfo.setCcAddress("maoqian@dangdang.com; zhangqiannan@dangdang.com");
//	     mailInfo.setBccAddress("zhangxiaojingjs@dangdang.com");
	     mailInfo.setSubject(subject);      
	     mailInfo.setContent(content); 
	     SendMail sms = new SendMail();     
	     if (format.equals("TEXT")){
	    	 sms.sendTextMail(mailInfo);//发送文体格式     
	     }else if (format.equals("HTML")){
	    	 sms.sendHtmlMail(mailInfo);//发送html格式
	     }	     
	}
}
