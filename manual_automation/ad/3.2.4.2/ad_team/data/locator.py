__author__ = 'xiyucheng'
#coding:utf-8
#邮件短信系统登录#

login_name = ".//*[@id='uName']"
login_password = ".//*[@id='password']"
login_btn = "html/body/div[1]/div/div/form/div[3]/div/input"


#信体版本页
mail_table = "html/body/div[3]/div/div/table"
mail_send_link = ".//font[@color='green']/../.././/a[4]"

#发送邮件alert
mail_send_text_alert = ".//*[@id='msg_to']"
mail_send_ok_btn_alert = ".//*[@id='submitButton']"

#QQ邮箱
mail_loginframe_QQ = "login_frame"
mail_loginname_QQ = ".//*[@id='u']"
mail_password_QQ = ".//*[@id='p']"
mail_loginbtn_QQ = ".//*[@id='login_button']"
mail_inbox_QQ = ".//*[@id='folder_1']"