# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'
import unittest
import HTMLTestRunner
from function.ad_calc_func import *
import time
import urllib
from urllib import quote
from function.send_email import *


class App_ad_API(unittest.TestCase):

    def setUp(self):
        self.verificationErrors = []
        self.url = "http://a.dangdang.com/cpc_mobile_api.php?pos=%s&extra=%s&page=%s&pagesize=%s" \
                   "&udid=%s&custid=%s&platform=%s&version=%s&req_from=bd"
        #pos
        #extra
        #page
        #pagesize
        #udid
        #custid
        #platform
        #version

    def case_1(self):
        u"""对pos进行测试。pos=200"""
        print u"搜索词=衬衫"
        extra = quote('衬衫')
        result = pc_search_api(extra)
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            self.fail()
        else:
            print u"该广告词在广告系统中有广告"
        parameter = ('200', extra, '', '', '123123', '2324', 'iphone', '4.6.0')
        print u"测试url地址：", self.url % parameter
        print u"测试参数：pos=", parameter[0]
        response = urllib.urlopen(self.url % parameter)
        html = response.read()
        #print html.split('"data":')[1]3
        count = html.split('"count":')[1].split(',')[0]
        if count == '2' or count == '1':
            print u"count =", count, u", 有数据返回。pass"
        else:
            print u"count != 2, 没有数据返回。false"
        self.assertEqual(count in ['1', '2'], True)

    def case_2(self):
        u"""对pos进行测试。pos=abc"""
        extra = quote('衬衫')
        print u"搜索词=衬衫"
        result = pc_search_api(extra)
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            self.fail()
        else:
            print u"该广告词在广告系统中有广告"
        parameter = ('abc', extra, '', '', '123123', '2324', 'iphone', '4.6.0')
        print u"测试url地址：", self.url % parameter
        print u"测试参数：pos=", parameter[0]
        response = urllib.urlopen(self.url % parameter)
        html = response.read()
        result = html.find("data")
        if result == -1:
            print u"返回报错。FALSE"
        else:
            print u"返回未报错。PASS"
        self.assertNotEqual(html.find("data"), -1)

    def case_3(self):
        u"""对pos进行测试。pos=空"""
        extra = quote('衬衫')
        print u"搜索词=衬衫"
        result = pc_search_api(extra)
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            self.fail()
        else:
            print u"该广告词在广告系统中有广告"
        parameter = ('', extra, '', '', '123123', '2324', 'iphone', '4.6.0')
        print u"测试url地址：", self.url % parameter
        print u"测试参数：pos=", parameter[0]
        response = urllib.urlopen(self.url % parameter)
        html = response.read()
        result = html.find("data")
        if result == -1:
            print u"返回报错。FALSE"
        else:
            print u"返回未报错。PASS"
        self.assertNotEqual(html.find("data"), -1)

    def case_4(self):
        u"""对extra进行测试。extra=衬衫"""
        print u"搜索词=衬衫"
        extra = quote('衬衫')
        result = pc_search_api(extra)
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            self.fail()
        else:
            print u"该广告词在广告系统中有广告"
        parameter = ('200', extra, '', '', '123123', '2324', 'iphone', '4.6.0')
        print u"测试url地址：", self.url % parameter
        print u"测试参数：extra=", parameter[1]
        response = urllib.urlopen(self.url % parameter)
        html = response.read()
        #print html.split('"data":')[1]3
        count = html.split('"count":')[1].split(',')[0]
        if count == '2' or count == '1':
            print u"count =", count, u", 有数据返回。pass"
        else:
            print u"count != 2, 没有数据返回。false"
        self.assertEqual(count in ['1', '2'], True)

    def case_5(self):
        u"""对extra进行测试。extra=空"""
        print u"搜索词=空"
        extra = quote('null')
        result = pc_search_api(extra)
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            #self.fail()
        else:
            print u"该广告词在广告系统中有广告"
        parameter = ('200', extra, '', '', '123123', '2324', 'iphone', '4.6.0')
        print u"测试url地址：", self.url % parameter
        print u"测试参数：extra=", parameter[1]
        response = urllib.urlopen(self.url % parameter)
        html = response.read()
        #print html.split('"data":')[1]3
        result = html.find("data")
        if result == -1:
            print u"返回报错。FALSE"
        else:
            print u"返回未报错。PASS"
        self.assertNotEqual(html.find("data"), -1)

    def case_6(self):
        u"""对extra进行测试。extra=不合法的值"""
        print u"搜索词=<script>alert()</script>"
        #extra = quote('null')
        result = pc_search_api('<script>alert()</script>')
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            print u"该广告词在广告系统中有广告"
            parameter = ('200', '<script>alert()</script>', '', '', '123123', '2324', 'iphone', '4.6.0')
            print u"测试url地址：", self.url % parameter
            print u"测试参数：extra=", parameter[1]
            response = urllib.urlopen(self.url % parameter)
            html = response.read()
            #print html.split('"data":')[1]3
            result = html.find("data")
            if result == -1:
                print u"返回报错。FALSE"
            else:
                print u"返回未报错。PASS"
            self.assertNotEqual(result, -1)
        else:
            print u"该广告词在广告系统中有广告"
            parameter = ('200', '<script>alert()</script>', '123123', '2324', 'iphone', '4.6.0')
            print u"测试url地址：", self.url % parameter
            print u"测试参数：pos=", parameter[0]
            response = urllib.urlopen(self.url % parameter)
            html = response.read()
            #print html.split('"data":')[1]3
            count = html.split('"count":')[1].split(',')[0]
            if count == '2' or count == '1':
                print u"count =", count, u", 有数据返回。pass"
            else:
                print u"count != 2, 没有数据返回。false"
            self.assertEqual(count in ['1', '2'], True)

    def case_7(self):
        u"""对extra进行测试。extra=超长值"""
        print u"搜索词=longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglong"
        extra = quote('longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglong')
        result = pc_search_api(extra)
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            print u"该广告词在广告系统中有广告"
            parameter = ('200', extra, '', '', '123123', '2324', 'iphone', '4.6.0')
            print u"测试url地址：", self.url % parameter
            print u"测试参数：extra=", parameter[1]
            response = urllib.urlopen(self.url % parameter)
            html = response.read()
            #print html.split('"data":')[1]3
            result = html.find("data")
            if result == -1:
                print u"返回报错。FALSE"
            else:
                print u"返回未报错。PASS"
            self.assertNotEqual(result, -1)
        else:
            print u"该广告词在广告系统中有广告"
            parameter = ('200', '<script>alert()</script>', '123123', '2324', 'iphone', '4.6.0')
            print u"测试url地址：", self.url % parameter
            print u"测试参数：pos=", parameter[0]
            response = urllib.urlopen(self.url % parameter)
            html = response.read()
            #print html.split('"data":')[1]3
            count = html.split('"count":')[1].split(',')[0]
            if count == '2' or count == '1':
                print u"count =", count, u", 有数据返回。pass"
            else:
                print u"count != 2, 没有数据返回。false"
            self.assertEqual(count in ['1', '2'], True)

    def case_8(self):
        u"""对page进行测试。page=0"""
        print u"搜索词=衬衫"
        extra = quote('衬衫')
        result = pc_search_api(extra)
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            self.fail()
        else:
            print u"该广告词在广告系统中有广告"
        parameter = ('200', extra, '0', '', '123123', '2324', 'iphone', '4.6.0')
        print u"测试url地址：", self.url % parameter
        print u"测试参数：page=", parameter[2]
        response = urllib.urlopen(self.url % parameter)
        html = response.read()
        #print html.split('"data":')[1]3
        count = html.split('"count":')[1].split(',')[0]
        if count == '2' or count == '1':
            print u"count =", count, u", 有数据返回。pass"
        else:
            print u"count != 2, 没有数据返回。false"
        self.assertEqual(count in ['1', '2'], True)

    def case_9(self):
        u"""对page进行测试。page=空"""
        print u"搜索词=衬衫"
        extra = quote('衬衫')
        result = pc_search_api(extra)
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            self.fail()
        else:
            print u"该广告词在广告系统中有广告"
        parameter = ('200', extra, '', '', '123123', '2324', 'iphone', '4.6.0')
        print u"测试url地址：", self.url % parameter
        print u"测试参数：page=", parameter[2]
        response = urllib.urlopen(self.url % parameter)
        html = response.read()
        #print html.split('"data":')[1]3
        count = html.split('"count":')[1].split(',')[0]
        if count == '2' or count == '1':
            print u"count =", count, u", 有数据返回。pass"
        else:
            print u"count != 2, 没有数据返回。false"
        self.assertEqual(count in ['1', '2'], True)

    def case_10(self):
        u"""对page进行测试。page=111111111111111111"""
        print u"搜索词=衬衫"
        extra = quote('衬衫')
        result = pc_search_api(extra)
        if result == -1:
            print u"该搜索词在广告系统中本来就没有广告"
            self.fail()
        else:
            print u"该广告词在广告系统中有广告"
        parameter = ('200', extra, '111111111111111111', '', '123123', '2324', 'iphone', '4.6.0')
        print u"测试url地址：", self.url % parameter
        print u"测试参数：page=", parameter[2]
        response = urllib.urlopen(self.url % parameter)
        html = response.read()
        result = html.find("data")
        if result == -1:
            print u"返回报错。FALSE"
        else:
            print u"返回未报错。PASS"
        self.assertNotEqual(result, -1)

    def tearDown(self):
        self.assertEqual([], self.verificationErrors)

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTest(App_ad_API("case_1"))
    test_suite.addTest(App_ad_API("case_2"))
    test_suite.addTest(App_ad_API("case_3"))
    test_suite.addTest(App_ad_API("case_4"))
    test_suite.addTest(App_ad_API("case_5"))
    test_suite.addTest(App_ad_API("case_6"))
    test_suite.addTest(App_ad_API("case_7"))
    test_suite.addTest(App_ad_API("case_8"))
    test_suite.addTest(App_ad_API("case_9"))
    test_suite.addTest(App_ad_API("case_10"))

    title = time.strftime('%Y-%m-%d_%H.%M.%S_', time.localtime(time.time()))
    str1 = "D:\\API_report\\"+title+"API_Report.html"
    print str1
    fp = file(str1, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'广告系统移动端出广告API',
        description=u'API参数测试'
    )
    runner.run(test_suite)
    subject = runner.title
    files = [str1]

    #send_mail(subject, files=files)
