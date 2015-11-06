# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'
from selenium import webdriver
import sys

username = "xiyucheng"
password = ""
email_to = "xiyucheng@dangdang.com,zhangruichao@dangdang.com,zhaojianjun@dangdang.com,qutengjiao@dangdang.com,zhangjingjs@dangdang.com"
login_addr = "http://bugzilla.dangdang.com/"
addr = "http://bugzilla.dangdang.com/enter_bug.cgi?product=A.100%20PDLC%C0%EF%B3%CC%B1%AE"

ff = webdriver.Firefox()
ff.get(login_addr)
ff.find_element_by_xpath(".//*[@id='login']/a").click()
ff.find_element_by_xpath(".//*[@id='username']").send_keys(username)
ff.find_element_by_xpath(".//*[@id='password']").send_keys(password)
ff.find_element_by_xpath(".//input[@type='submit']").submit()

def new_bug(argv):
    ff.get(addr)
    try:
        project_name = argv[1].decode("utf-8")
    except:
        project_name = argv[1].decode("gbk")
    print project_name

    option = ".//select[@name='component']/option[@value='100.15 部署上线']"
    ff.find_element_by_xpath(option).click()
    title = ".//input[@name='short_desc']"
    ff.find_element_by_xpath(title).send_keys(u"部署上线："+project_name)
    if argv.__len__()==3:
        view_bug = " "

        ff.find_element_by_xpath(".//textarea[@name='comment']").send_keys(project_name+ u" 已经测试完毕。申请上线。请看附件checklist。测试验收bug：http://bugzilla.dangdang.com/show_bug.cgi?id="+argv[2])
    if argv.__len__()==4:
        ff.find_element_by_xpath(".//textarea[@name='comment']").send_keys(project_name + u" 已经测试完毕。申请上线。请看附件checklist。测试bug：http://bugzilla.dangdang.com/show_bug.cgi?id="+argv[2]+u"验收bug：http://bugzilla.dangdang.com/show_bug.cgi?id="+argv[3])
    ff.find_element_by_xpath(".//input[@name='cc']").send_keys(email_to)

new_bug(sys.argv)