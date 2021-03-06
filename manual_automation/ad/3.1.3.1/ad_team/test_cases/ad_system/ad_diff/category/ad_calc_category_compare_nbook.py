# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

import unittest
from ddt import ddt, data
import HTMLTestRunner
from function.send_email import *
from function.ad_calc_func import *
from function.excel_func import *
import time

category_list = []
tables = excel_table_byindex('category_for_compare.xlsx', by_index= 0)
for row in tables:
    category_list.append(int(row["category_id"]))

title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
test_report = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\category\\"+title+u"ad_calc_category_compare_nbook.html"
test_log = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\category\\"+title+u"ad_calc_category_compare_nbook.txt"
f = file(test_log.encode('GBK'), 'a')
version = '1'
print "version:", version

@ddt
class ad_calc_category(unittest.TestCase):
    def setUp(self):
        self.URL_NEW = "http://10.3.255.91:9080/getAds/cpc/1/0/50/%s/3232262672/20141022092715613302051844096892271/0/0/0/1/%s/0/0/0/0/debug"
        self.URL_OLD = "http://10.3.255.207:9080/getAds/cpc/1/0/50/%s/3232262672/20141022092715613302051844096892271/0/0/0/1/%s/0/0/0/0/debug"

    @data(*category_list)
    def test_case(self, category_id):
        old_url = self.URL_OLD % (category_id, version)
        new_url = self.URL_NEW % (category_id, version)
        print " "
        print u'老环境请求地址：', old_url.decode('utf-8')
        print u'老环境请求地址：', new_url.decode('utf-8')
        print u'测试的计算模块版本为：', version
        print u'测试的category_id为：', category_id
        old_result = run_for_search_category(old_url)    #字典，旧版本的所有数据，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type
        new_result = run_for_search_category(new_url)    #字典，新版本的所有数据，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type

        old_ad_info_list = old_result[0]    #字典，旧版本的所有数据的字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type
        new_ad_info_list = new_result[0]    #字典，新版本的所有数据字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type
        print u'老产品返回广告长度:', len(old_ad_info_list)
        print u'新产品返回广告长度:', len(new_ad_info_list)

        if len(old_ad_info_list) == 0 and len(new_ad_info_list) == 0:
            raise u"没有广告返回"
        elif len(old_ad_info_list) != len(new_ad_info_list):
            self.fail(u"广告返回长度不一致")

        old_list = get_ad_id_set_list_by_score(old_ad_info_list)   #老版本数据的ad_id存储的set
        new_list = get_ad_id_set_list_by_score(new_ad_info_list)   #新版本数据的ad_id存储的set

        f.write('老环境请求地址：%s\n' % str(old_url))
        f.write('新环境请求地址：%s\n'% str(new_url))
        f.write('测试的计算模块版本为：%s\n' % version)
        f.write("老版本返回广告长度:%s\n" % len(old_ad_info_list))
        f.write("新版本返回广告长度:%s\n" % len(new_ad_info_list))
        for i in range(0, len(new_list)):
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
    test_suite.addTests(unittest.makeSuite(ad_calc_category))

    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'列表页计算模块---百货, version = %s' % version,
        description=u'所有的category_id请求出的数据，对比新老产品的同一版本的结果是否一致。本测试用例仅适用于所有广告都是按照score排序进行比对的， 仅适用于1,3,4 版本'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    f.close()
    email_contents = "列表页计算模块版本比对百货分类页测试完毕\n本次测试版本为：%s\n测试报告地址：%s\n测试日志地址：%s\n" % (version.encode('utf-8'), test_report.encode('utf-8'), test_log.encode('utf-8'))
    #new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com,wangdongdong@dangdang.com,dingyu@dangdang.com,chengmingbo@dangdang.com'], contents= email_contents)
    new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com'], contents= email_contents)


