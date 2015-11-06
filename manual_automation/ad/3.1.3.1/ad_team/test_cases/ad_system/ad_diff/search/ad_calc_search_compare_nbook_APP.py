# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

import unittest
from ddt import ddt, data
import HTMLTestRunner
from function.send_email import *
from function.ad_calc_func import *
from function.excel_func import *
import time
from search_conf import *

query_list_nbook = []
tables = excel_table_byindex(KEYWORD_FILE, by_index = 1)   #读取第二个sheet的数据
for row in tables:
    query_list_nbook.append(row["keyword"].encode("utf-8"))

query_list_null = []
tables = excel_table_byindex(KEYWORD_FILE, by_index = 2)  ##读取第三个sheet的数据
for row in tables:
    query_list_null.append(row["keyword"].encode("utf-8"))

query_list = query_list_nbook+query_list_null
print "excel: ", len(query_list)
#query_list = ['衬衫','短裤']
title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
#test_report = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\search\\"+title+u"ad_calc_search_nbook_compare_APP.html"
#test_log = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\search\\"+title+u"ad_calc_search_nbook_compare_APP.txt"
test_report = REPORT_PATH + title + u"ad_calc_search_nbook_compare_APP.html"
test_log = LOG_PATH + title + u"ad_calc_search_nbook_compare_APP.txt"
f = file(test_log.encode('GBK'), 'a')

#fi = QUERYCATE_FILE
#kv_dict = key_dict(fi)

#new_query_list = []
#for i in query_list:
#    try:
#        value = kv_dict[i]
#    except Exception:
#        value = ""
#    if value.startswith("58") == True or value.endswith(".00.00.00.00.00") or value == "":
#        new_query_list.append(i)
#print "new query list: ", len(new_query_list)
version = '4'
print "version:", version
@ddt
class ad_calc_search(unittest.TestCase):
    def setUp(self):
        self.URL_NEW = "http://%s/getAds/cpc/200/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
        self.URL_OLD = "http://%s/getAds/cpc/200/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
        pass
    @data(*query_list)
    def test_case(self, query):
        print u'测试的query为：', query.decode('utf-8')

        old_url = self.URL_OLD % (OLDURL_IP, urllib2.quote(query), version)
        new_url = self.URL_NEW % (NEWURL_IP, urllib2.quote(query), version)

        print " "
        print u'老环境请求地址：', old_url.decode('utf-8')
        print u'新环境请求地址：', new_url.decode('utf-8')

        old_result_x = run_for_search_category(old_url)    #字典，旧版本的所有数据，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type
        new_result_x = run_for_search_category(new_url)    #字典，新版本的所有数据，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type

        old_result = old_result_x[0]
        new_result = new_result_x[0]

        if len(old_result) == 0 and len(new_result) == 0:
            raise unittest.SkipTest("no ads")
        debugquerypath = old_result_x[1]
        print u"预测query为：", debugquerypath
        if not debugquerypath.startswith("58") and debugquerypath!="":
            raise unittest.SkipTest("no general merchandise")
        if len(old_result) != len(new_result):
            print u'老产品返回广告长度:', len(old_result)
            print u'新产品返回广告长度:', len(new_result)
            self.fail(u"广告返回长度不一致")
        print u"老版本返回广告长度", len(old_result)
        print u"新版本返回广告长度", len(new_result)

        old_search_type_list = []   #存有旧版本所有广告的search_type的信息list。
        new_search_type_list = []   #存有新版本所有广告的search_type的信息list。
        for i in range(0, len(old_result)):
            old_search_type_list.append(old_result[i]['search_type'])
        for i in range(0, len(new_result)):
            new_search_type_list.append(old_result[i]['search_type'])

        old_result_search_type_0 = data_filter(old_result, "search_type", "0")  #老版本过滤了search_type=1的数据。 也就是精确匹配的数据
        new_result_search_type_0 = data_filter(new_result, "search_type", "0")  #新版本过滤了search_type=1的数据。 也就是精确匹配的数据

        old_result_search_type_1 = data_filter(old_result, "search_type", "1")  #老版本过滤了search_type=0的数据。 也就是扩展匹配的数据
        new_result_search_type_1 = data_filter(new_result, "search_type", "1")  #新版本过滤了search_type=0的数据。 也就是扩展匹配的数据

        old_list_jingque = get_ad_id_set_list_by_bidctr(old_result_search_type_0)   #老版本精确匹配的数据的ad_id存储的set
        new_list_jingque = get_ad_id_set_list_by_bidctr(new_result_search_type_0)   #新版本精确匹配的数据的ad_id存储的set
        old_list_kuozhan = get_ad_id_set_list_by_score(old_result_search_type_1)    #老版本扩展匹配的数据ad_id存储的set
        new_list_kuozhan = get_ad_id_set_list_by_score(new_result_search_type_1)    #新版本扩展匹配的数据的ad_id存储的set

        f.write('老环境请求地址：%s\n' % str(old_url))
        f.write('新环境请求地址：%s\n' % str(new_url))
        f.write('测试的计算模块版本为：%s\n' % version)
        f.write('测试的query为：%s\n' % query)
        DebugQueryPath_old = run_for_search_category(old_url)[1]
        DebugQueryPath_new = run_for_search_category(new_url)[1]
        f.write("老版本的DebugQueryPath为: %s\n" % str(DebugQueryPath_old))
        f.write("新版本的DebugQueryPath为: %s\n" % str(DebugQueryPath_new))

        self.assertEqual(old_search_type_list, new_search_type_list, msg = u"精确匹配和扩展匹配的排序不同。")  #由于非图书预测要分为精确和非精确分别对比。 所以该验证点是为了验证整块的精确匹配和非精确匹配的数据块排序不同。
        for i in range(0, len(new_list_jingque)-1):
            f.write("第%s个精确匹配广告set\n" % str(i))
            f.write("old_list:%s\n" % old_list_jingque[i])
            f.write("new_list:%s\n" % new_list_jingque[i])
            self.assertEqual(old_list_jingque[i], new_list_jingque[i], msg=u"精确匹配错误")
            f.write("------------------------------------------\n")
        for i in range(0, len(new_list_kuozhan)-1):
            f.write("第%s个扩展匹配广告set\n" % str(i))
            f.write("old_list:%s\n" % old_list_kuozhan[i])
            f.write("new_list:%s\n" % new_list_kuozhan[i])
            self.assertEqual(old_list_kuozhan[i], new_list_kuozhan[i], msg=u"扩展匹配错误")
            f.write("-----------------------------------------\n")

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
        title=u'搜索页计算模块---百货---APP, version - %s' % version,
        description=u'针对所有预测为百货的query，对比新老产品的同一版本的结果是否一致，'
                    u'其中，search_type=0的精确匹配按照bid+str排序进行比对的，search_type=1的扩展匹配按照score排序进行对比的。'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    f.close()
    email_contents = "搜索页计算模块版本比对测试完毕，该测试为百货搜索词测试\n本次测试版本为：%s\n测试报告地址：%s\n测试日志地址：%s\n" % (version.encode('utf-8'), test_report.encode('utf-8'), test_log.encode('utf-8'))
    #new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com,wangdongdong@dangdang.com,dingyu@dangdang.com,chengmingbo@dangdang.com'], contents= email_contents)
    new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com'], contents= email_contents)