# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

import unittest
from ddt import ddt, data
import HTMLTestRunner
from function.send_email import *
import urllib2
import json
from function.ad_calc_func import *
from function.excel_func import *
import time

category_list = []
category_list1 = []
category_list2 = []
tables1 = excel_table_byindex('category_for_compare.xlsx', by_index= 0)
for row in tables1:
    category_list1.append(int(row["category_id"]))
tables2 = excel_table_byindex('category_for_compare.xlsx', by_index= 1)
for row in tables2:
    category_list2.append(int(row["category_id"]))

category_list=category_list1+category_list2
print "excel: ", len(category_list)
title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
test_report = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\category\\"+title+u"ad_calc_category_compare_book.html"
test_log = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\category\\"+title+u"ad_calc_category_compare_book.txt"
f = file(test_log.encode('GBK'), 'a')
version = '4'
print "version:", version

@ddt
class ad_calc_search(unittest.TestCase):
    def setUp(self):
        self.URL_NEW = "http://10.3.255.91:9080/getAds/cpc/6/0/50/%s/3232262672/20141022092715613302051844096892271/0/0/0/1/%s/0/0/0/0"
        self.URL_OLD = "http://10.3.255.207:9080/getAds/cpc/6/0/50/%s/3232262672/20141022092715613302051844096892271/0/0/0/1/%s/0/0/0/0"

        pass

    @data(*category_list)
    def test_case(self, category_id):
        print u'测试的category为：', category_id

        old_url = self.URL_OLD % (category_id, version)
        new_url = self.URL_NEW % (category_id, version)

        print " "
        print u'老环境请求地址：', old_url.decode('utf-8')
        print u'新环境请求地址：', new_url.decode('utf-8')


        old_result = response_ad_calc_server(old_url)
        new_result = response_ad_calc_server(new_url)
        print len(old_result)
        if len(old_result) ==0:
            raise unittest.SkipTest("--")
        else:
            self.assertEqual(old_result, new_result)

    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(ad_calc_search))
    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'分类临时比对脚本, verser = %s' % version,
        description=u'直接比对返回串'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    f.close()
    email_contents = "临时比对脚本，本次测试版本为：%s\n测试报告地址：%s\n测试日志地址：%s\n" % (version.encode('utf-8'), test_report.encode('utf-8'), test_log.encode('utf-8'))
    #new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com,wangdongdong@dangdang.com,dingyu@dangdang.com,chengmingbo@dangdang.com'], contents= email_contents)
    new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com'], contents= email_contents)
