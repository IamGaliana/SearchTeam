# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'
from selenium import webdriver
from data.locator import *
#from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
import time


  ##========================================
  ##功能：登录邮件短信系统
  ##作者：奚煜成
  ##参数：name 用户名 password 密码
  ##日期：2014-03-21
  ##========================================
def login(driver, name, password):
    #driver=webdriver.Firefox()
    url = "http://10.255.253.159:8000/User/Login.aspx"
    driver.get(url)
    WebDriverWait(driver, 30).until(lambda rrr: rrr.find_element_by_xpath(login_name).is_displayed())
    driver.find_element_by_xpath(login_name).send_keys(name)
    driver.find_element_by_xpath(login_password).send_keys(password)
    time.sleep(2)
    driver.find_element_by_xpath(login_btn).submit()

  ##========================================
  ##功能：指定信体发送到指定邮箱。
  ##作者：奚煜成
  ##参数：mail_id 信体id组
  ##日期：2014-03-21
  ##========================================
def send(driver, mail_id):
    #driver=webdriver.Firefox()
    ev =mail_id.split(",")
    for i in range(0, len(ev)):
        driver.get("http://10.255.253.159:8000/EmailTpl/List.aspx?event_id="+ev[i])
        emaila=["xiyucheng@dangdang.com", "zhaohongqiang@dangdang.com"]
        WebDriverWait(driver, 30).until(lambda rrr: rrr.find_element_by_xpath(mail_send_link).is_displayed())
        for e in range(0, len(emaila)):
            WebDriverWait(driver, 30).until(lambda rrr: rrr.find_element_by_xpath(mail_send_link).is_displayed())
            time.sleep(2)
            driver.find_element_by_xpath(mail_send_link).click()
            WebDriverWait(driver, 30).until(lambda rrr: rrr.find_element_by_xpath(mail_send_text_alert).is_displayed())
            driver.find_element_by_xpath(mail_send_text_alert).send_keys(emaila[e])
            driver.find_element_by_xpath(mail_send_ok_btn_alert).click()

  ##========================================
  ##功能：QQ邮箱登录。
  ##作者：奚煜成
  ##参数：
  ##日期：2014-03-21
  ##========================================
def mail_qq(driver):
    #driver= webdriver.Firefox()
    url = "https://mail.qq.com/cgi-bin/loginpage"
    driver.get(url)
    driver.switch_to_frame(mail_loginframe_QQ)
    driver.find_element_by_xpath(mail_loginname_QQ).send_keys("ddmailtest")
    driver.find_element_by_xpath(mail_password_QQ).send_keys("testmail@dd")
    driver.find_element_by_xpath(mail_loginbtn_QQ).click()




