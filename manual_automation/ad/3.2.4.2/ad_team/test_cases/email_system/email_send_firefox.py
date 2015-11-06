__author__ = 'xiyucheng'
#coding:utf-8
import unittest
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
import HTMLTestRunner
from function.email_func import *

class Email_send(unittest.TestCase):
    def setUp(self):
        self.driver = webdriver.Firefox()
        self.driver.implicitly_wait(30)
        self.verificationErrors = []
        self.accept_next_alert = True
        self.yourname="xiyucheng"
        self.password = "ca$hc0wC"
        self.event_id = "8021, 8022, 8023"

    def send_mail(self):
        u"这是测试用例1"
        driver = self.driver
        login(driver, self.yourname, self.password)
        send(driver, self.event_id)
        driver.close()
    def screenshot_for_qq(self):
        u"这是登录QQ邮箱截图测试用例"




    def tearDown(self):
        self.driver.quit()
        self.assertEqual([], self.verificationErrors)



if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTest(Email_send("send_mail"))
    #test_suite.addTest(Email_send("test_case_2"))
    #test_suite.addTest(Email_send("test_case_3"))
    filename = 'D:\\TDD_Report\\11.html'
    fp = file(filename, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'这是标题',
        description=u'这是描述'
    )
    runner.run(test_suite)
