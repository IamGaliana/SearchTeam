#!/usr/bin/env python
#-*- coding: UTF-8 -*-

__author__ = 'tangna'
import unittest
import HTMLTestRunner
import json
import urllib2
from ddt import ddt, data
import sys
#from function.send_email import *
import codecs
from function.excel_func import *

keywords_list = []
tables = excel_table_byindex('keyword.xlsx')
for row in tables:
    keywords_list.append(row["keyword"].encode("utf-8"))
#print keywords_list
'''
keywords_list = []
f = open("D:\\keyword.txt")
for line in f.readlines():
    if line[:3] == codecs.BOM_UTF8:
        line = line[3:]
    line = line.strip('\n')
    #line = line.decode("utf-8")
    keywords_list.append(line)
f.close()
'''
@ddt
class SearchCal(unittest.TestCase):

    def setUp(self):
        #self.keyword = random.choice([u"外套",u"裙子",u"洗衣液",u"床单"])
        #self.keyword = urllib2.quote(self.keyword.encode("utf8"))
        pass

    #取得广告返回的结果
    def get_search_result(self, keyword):
        keyword = urllib2.quote(keyword)

        self.url = "http://10.255.254.62:8080/getAds/cpc/0/0/50/%s/3232262672/20141022092715613302051844096892271/0/0/0/1/0/0/0/0/debug"
        self.url = self.url %keyword
        print self.url

        content = urllib2.urlopen(self.url, timeout = 3).read()
        result_dic = json.loads(content)
        return result_dic
        #print results

    #处理异常，将含有扩展匹配的结果过滤，如果全部过滤或返回结果为空，抛出异常
    def error_handle(self, result_dic):
        try:
            results = result_dic['data']
        except Exception:
            results = []
        # results = [{u'precise': u'0', u'extra': u'\u5916\u5957', u'ad_id': 3847842, u'DebugAdInfo': u'  searchtype:1 bid:8800000  '}, \
        #               {u'precise': u'0', u'ad_id': 3842482, u'extra': u'\u5916\u5957', u'DebugAdInfo': u'  searchtype:0  '},\
        #               {u'precise': u'0', u'ad_id': 1111111, u'extra': u'\u5916\u5957', u'DebugAdInfo': u'  searchtype:0  '}]
        results = filter(lambda each: "searchtype:0" in each['DebugAdInfo'], results)
        #print results
        try:
            if len(results) == 0:
                raise
        except Exception:
            print "****** 没有精确匹配的结果! *****. "
        else:
            return results

    #从关键词列表中依次取得关键词，与输入的关键词进行比较是否相等
    @data(*keywords_list)
    def test_search_cal(self, keyword):

        result_dic = self.get_search_result(keyword)
        results = self.error_handle(result_dic)

        print "****输入的关键词是: " ,keyword
        for each in results:
            
            each['extra'] = (each['extra']).encode('utf-8')
            print "**结果中的关键词和其广告id是： ", each['extra'], each['ad_id']
            self.assertEqual(each['extra'], keyword)

            '''
            if "searchtype:0" in each['DebugAdInfo']:
                #print each['extra']
                each['extra'] = (each['extra']).encode('utf-8')
                print "**结果中的关键词和其广告id是： ", each['extra'], each['ad_id']
                self.assertEqual(each['extra'], keyword)
            else:
                pass
            '''

    def tearDown(self):
        pass

if __name__ == "__main__":
    #unittest.main()
    test_suite = unittest.TestSuite()
    test_suite.addTest(unittest.makeSuite(SearchCal))
    test_report = "D:\\result.html"
    fp = file(test_report, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title='Report_title',
        description='Report_description'
    )
    runner.run(test_suite)
    fp.close()
    # subject = runner.title
    # file_list = [test_report]
    # send_mailhtml(subject, files=file_list)
