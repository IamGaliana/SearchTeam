#!/usr/bin/env python
#coding:utf8
__author__ = 'tangna'
import unittest
from ddt import ddt, data
from function.send_email import *
import HTMLTestRunner
import urllib2
from function.ad_calc_func import *
from function.excel_func import *
import HTMLTestRunner
from test import *
import sys
import urllib
import chardet
reload(sys)
sys.setdefaultencoding('utf-8')

#从关键词的excel文件中取非图的关键词放入query_list
query_list = []
query_list = excel_table_byindex2(excel_file='keyword_for_compare.xlsx', sheet_index=1)[1:]
print "len(query_list):", len(query_list)

#通过query_cate_utf8文件将所有分类以58的词筛选出来
fi = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\query_cate_utf8"
kv_dict = key_dict(fi)
new_query_list = goldab_filte_keyword(query_list, kv_dict)
print "new query list: ", len(new_query_list)

@ddt
class GoldAB0(unittest.TestCase):
    def setUp(self):
        self.url0 = "http://10.255.254.62:8080/getAds/cpc/0/0/4/%s/184537119/0/0/0/107169205/2/-/0/0/0/debug"
        self.url500 = "http://10.255.254.62:8080/getAds/cpc/500/0/4/%s/184537119/0/0/0/107169205/2/-/0/0/0/0/-/debug"

    @data(*new_query_list)
    def test_gold_ab(self, query):
        url0 = self.url0 % urllib2.quote(query.encode('utf-8'))
        print "url0", url0
        ad_result0 = request_goldab(url0)

        url500 = self.url500 % urllib2.quote(query.encode('utf-8'))
        print "url500", url500
        ad_result500 = request_goldab(url500)

        print "query:", query
        if not ad_result0:
            raise "没有广告返回"
        else:
            ad_data0 = ad_result0['data']
            match_result0 = []
            for i in xrange(len(ad_data0)):
                searchtype_pattern = re.compile(r"searchtype:(-?\d)")
                searchtype_find = (''.join(searchtype_pattern.findall(ad_data0[i]['DebugAdInfo']))).encode('utf8')

                m_oriscore_pattern = re.compile(r"m_oriscore:(\d*)")
                m_oriscore_find = int(''.join(m_oriscore_pattern.findall(ad_data0[i]['DebugAdInfo'])))

                if searchtype_find == '-1' and m_oriscore_find >= 70:
                    match_result0.append(ad_data0[i]['ad_id'])
            print "用posid0请求的前4个结果中，满足条件：searchtype=-1 且 m_oriscore>=70的广告是：", match_result0

            if len(match_result0) < 3:
                self.assertEqual(len(ad_result500), 0, '请求posid为0返回符合条件结果小于3时，您的500的结果不为空')
                for i in xrange(4):
                    self.assertEqual(ad_data0[i]['gold_search_type'], '0', '请求posid为0返回符合条件结果小于3时，您的0的结果中gold_search_type不都为0')
            else:
                self.assertGreaterEqual(len(ad_result500['data']), 4, '请求posid为0返回符合条件结果大于等于3时，您的500的结果中广告的个数不等于4')
                for i in xrange(4):
                    self.assertEqual(ad_data0[i]['gold_search_type'], '1', '请求posid为0返回符合条件结果大于等于3时，您的0的结果中gold_search_type不都为1')

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTest(unittest.makeSuite(GoldAB0))
    test_report = u"D:\\goldab0.html"
    fp = file(test_report, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title= u'黄金展位AB版计算模块测试',
        description= u'''
        测试点如下：
        1) 请求posid=0，pagesize=4，返回结果中满足条件：searchtype=-1 且 m_oriscore>=70的广告个数小于3时，500的结果应为空
        2）请求posid为0返回符合条件结果小于3时，0的结果中gold_search_type应都为0
        3）请求posid为0返回符合条件结果大于等于3时，500的结果中广告的个数应等于4
        4) 请求posid为0返回符合条件结果大于等于3时，0的结果中gold_search_type应都为1
        '''
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()