# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'
import unittest
from ddt import ddt, data
import HTMLTestRunner
from function.send_email import *
from function.ad_calc_func import *
from function.excel_func import *
import time

query_list = []
tables = excel_table_byindex('keyword_for_compare.xlsx', by_index = 0)  #读取第一个sheet的数据
for row in tables:
    query_list.append(row["keyword"].encode("utf-8"))

#query_list = ['linux','sql']
title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
test_report = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\search\\"+title+u"ad_calc_search_book_compare_APP.html"
test_log = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\search\\"+title+u"ad_calc_search_book_compare_APP.txt"
f = file(test_log.encode('GBK'), 'a')

kv_dict = {}
fi = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\query_cate_utf8"
fi_handler = open(fi, 'r')
for line in fi_handler:
    fields = line.strip().split('\t')
    k = fields[0]
    v = '\t'.join(fields[1:])
    kv_dict[k] = v
fi_handler.close()

version = '3'

@ddt
class ad_calc_search(unittest.TestCase):
    def setUp(self):
        self.URL_OLD = "http://10.255.242.162:8080/getAds/cpc/200/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
        self.URL_NEW = "http://10.255.242.213:8080/getAds/cpc/200/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
        pass
    @data(*query_list)
    def test_case(self, query):
        print u'测试的query为：', query.decode('utf-8')
        try:
            value = kv_dict[query]
        except Exception:
            value = ""
        print u"该搜索词的DebugQueryPath为", value
        if value.startswith("01")!= True:
            raise u"该图书qurey预测为非图书"

        old_url = self.URL_OLD % (urllib2.quote(query), version)
        new_url = self.URL_NEW % (urllib2.quote(query), version)

        old_result = run_for_search_category(old_url)    #字典，旧版本的所有数据的字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type， 第二个返回结果为请求预测类型
        new_result = run_for_search_category(new_url)    #字典，新版本的所有数据字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type， 第二个返回结果为请求预测类型
        old_ad_info_list = old_result[0]    #字典，旧版本的所有数据的字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type
        new_ad_info_list = new_result[0]    #字典，新版本的所有数据字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type

        old_list = get_ad_id_set_list_by_bidctr(old_ad_info_list)   #老版本数据的ad_id存储的set
        new_list = get_ad_id_set_list_by_bidctr(new_ad_info_list)   #新版本数据的ad_id存储的set

        print " "
        print u'老环境请求地址：', old_url.decode('utf-8')
        print u'新环境请求地址：', new_url.decode('utf-8')
        f.write('老环境请求地址：%s\n' % str(old_url))
        f.write('新环境请求地址：%s\n' % str(new_url))
        f.write('测试的计算模块版本为：%s\n' % version)
        f.write('测试的query为：%s\n' % query)

        #DebugQueryPath_old = old_result[1]
        #DebugQueryPath_new = new_result[1]
        #f.write("老版本的DebugQueryPath为: %s\n" % str(DebugQueryPath_old))
        #f.write("新版本的DebugQueryPath为: %s\n" % str(DebugQueryPath_new))

        if len(old_ad_info_list) == 0 and len(new_ad_info_list) == 0:
            raise u"没有广告返回"
        elif len(old_ad_info_list) != len(new_ad_info_list):
            print u'老产品返回广告长度:', len(old_ad_info_list)
            print u'新产品返回广告长度:', len(new_ad_info_list)
            self.fail(u"广告返回长度不一致")

        f.write("老版本返回广告长度:%s\n" % len(old_ad_info_list))
        f.write("新版本返回广告长度:%s\n" % len(new_ad_info_list))
        for i in range(0, len(new_list)-1):
            f.write("第%s个广告set\n" % str(i))
            f.write("old_list: %s\n" % old_list[i])
            f.write("new_list: %s\n" % new_list[i])
            self.assertEqual(old_list[i], new_list[i])
            f.write("------------------------------\n")

        f.write("=============================================================================\n")

    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(ad_calc_search))
    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'搜索页计算模块--图书query新老产品版本结果比对_APP',
        description=u'针对所有预测为图书的query，对比新老产品的同一版本的结果是否一致，针对图书的query。本测试用例仅适用于所有广告都是按照bid+str排序进行比对的'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    f.close()
    email_contents = "APP搜索页计算模块版本比对测试完毕，该测试为图书搜索词测试\n本次测试版本为：%s\n测试报告地址：%s\n测试日志地址：%s\n" % (version.encode('utf-8'), test_report.encode('utf-8'), test_log.encode('utf-8'))
    new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com'], contents= email_contents)
