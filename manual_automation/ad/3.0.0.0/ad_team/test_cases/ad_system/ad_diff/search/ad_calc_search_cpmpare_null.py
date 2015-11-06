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
tables = excel_table_byindex('keyword_for_compare.xlsx', by_index = 2)  #
for row in tables:
    query_list.append(row["keyword"].encode("utf-8"))

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
        self.URL_OLD = "http://10.255.242.162:8080/getAds/cpc/0/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
        self.URL_NEW = "http://10.255.242.213:8080/getAds/cpc/0/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
        pass
    @data(*query_list)
    def test_case(self, query):
        print u'测试的query为：', query.decode('utf-8')
        try:
            value = str(kv_dict[query])
        except Exception:
            value = ""
        print repr(value)
        print u"该搜索词的DebugQueryPath为", value
        #if value != "01.00.00.00.00.00" or value !="":
        if value !="" and not value.endswith(".00.00.00.00.00"):
            raise u"该qurey不是未预测分类的query"

        old_url = self.URL_OLD % (urllib2.quote(query), version)
        new_url = self.URL_NEW % (urllib2.quote(query), version)

        old_result = run_for_search_category(old_url)    #字典，旧版本的所有数据的字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type， 第二个返回结果为请求预测类型
        new_result = run_for_search_category(new_url)    #字典，新版本的所有数据字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type， 第二个返回结果为请求预测类型
        old_ad_info_list = old_result[0]    #字典，旧版本的所有数据的字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type
        new_ad_info_list = new_result[0]    #字典，新版本的所有数据字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type

        old_list = get_ad_id_set_list_by_score(old_ad_info_list)   #老版本数据的ad_id存储的set
        new_list = get_ad_id_set_list_by_score(new_ad_info_list)   #新版本数据的ad_id存储的set
        print " "
        print u'老环境请求地址：', old_url.decode('utf-8')
        print u'老环境请求地址：', new_url.decode('utf-8')
        print u'测试的计算模块版本为：', version
        print "======================"

        #DebugQueryPath_old = old_result[1]
        #DebugQueryPath_new = new_result[1]
        #print u"老版本的DebugQueryPath为:", DebugQueryPath_old
        #print u"新版本的DebugQueryPath为:", DebugQueryPath_new

        if len(old_ad_info_list) == 0 and len(new_ad_info_list) == 0:
                raise u"没有广告返回"
        elif len(old_ad_info_list) != len(new_ad_info_list):
            print u'老产品返回广告长度:', len(old_ad_info_list)
            print u'新产品返回广告长度:', len(new_ad_info_list)
            self.fail(u"广告返回长度不一致")

        print u"老版本返回广告长度", len(old_ad_info_list)
        print u"新版本返回广告长度", len(new_ad_info_list)
        for i in range(0, len(new_list)-1):
            print i
            print "old_list:", old_list[i]
            print "new_list", new_list[i]
            self.assertEqual(old_list[i], new_list[i])
            print "========================"

    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(ad_calc_search))
    title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
    test_report = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\search\\"+title+u"ad_calc_search_null_compare.html"
    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'搜索页计算模块--未预测成功query新老产品版本结果比对',
        description=u'针对所有预测不出是图书还是百货的query，对比新老产品的同一版本的结果是否一致。本测试用例仅适用于所有广告都是按照score排序进行比对的'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    email_contents = "搜索页计算模块版本比对测试完毕，该测试为未预测搜索词测试\n本次测试版本为：%s\n测试报告地址：%s\n" % (version.encode('utf-8'), test_report.encode('utf-8'))
    new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com'], contents= email_contents)