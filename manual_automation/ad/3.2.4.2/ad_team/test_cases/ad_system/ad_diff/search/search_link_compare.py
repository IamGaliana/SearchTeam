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
from search_conf import *

query_list = []
query_list0 = []
query_list1 = []
query_list2 = []
query_list3 = []
tables0 = excel_table_byindex(KEYWORD_FILE, by_index = 0)  #读取第一个sheet的数据
for row in tables0:
    query_list0.append(row["keyword"].encode("utf-8"))
tables1 = excel_table_byindex(KEYWORD_FILE, by_index = 1)  #读取第一个sheet的数据
for row in tables1:
    query_list1.append(row["keyword"].encode("utf-8"))
tables2 = excel_table_byindex(KEYWORD_FILE, by_index = 2)  #读取第一个sheet的数据
for row in tables2:
    query_list2.append(row["keyword"].encode("utf-8"))
tables3 = excel_table_byindex(KEYWORD_FILE, by_index = 3)  #读取第一个sheet的数据
for row in tables3:
    query_list3.append(row["keyword"].encode("utf-8"))

query_list = query_list1+query_list2+query_list3+query_list0
print "excel: ", len(query_list)
title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
test_report = REPORT_PATH + title + u"ad_calc_search_book_compare.html"
test_log = LOG_PATH + title + u"ad_calc_search_book_compare.txt"

f = file(test_log.encode('GBK'), 'a')

version = '4'
print "version:", version

@ddt
class ad_calc_search(unittest.TestCase):
    def setUp(self):
        #self.URL_NEW = "http://10.3.255.91:8080/getAds/cpc/0/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
        #self.URL_OLD = "http://10.3.255.91:8080/getAds/cpc/0/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
        self.URL_NEW = "http://%s/getAds/cpc/0/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/"
        self.URL_OLD = "http://%s/getAds/cpc/0/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/"
        pass

    @data(*query_list)
    def test_case(self, query):
        print u'测试的query为：', query.decode('utf-8')

        old_url = self.URL_OLD % (OLDURL_IP, urllib2.quote(query), version)
        new_url = self.URL_NEW % (NEWURL_IP, urllib2.quote(query), version)

        print " "
        print u'老环境请求地址：', old_url.decode('utf-8')
        print u'新环境请求地址：', new_url.decode('utf-8')

        #old_result = run_for_search_category(old_url)[0]
        #new_result = run_for_search_category(new_url)[0]

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
        title=u'搜索临时比对脚本, verser = %s' % version,
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
