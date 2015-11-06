# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

import unittest
from ddt import ddt, data
import HTMLTestRunner
from function.send_email import *
from function.ad_calc_func import *
from function.excel_func import *
import time

url_ilst = []
tables = excel_table_byindex('product_url_for_compare.xlsx')
for row in tables:
    url_ilst.append(row["url"])
title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
test_report = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\product\\"+title+u"ad_calc_product_compare.html"
test_log = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\product\\"+title+u"ad_calc_product_compare.txt"
f = file(test_log.encode('GBK'), 'a')

@ddt
class ad_calc_product(unittest.TestCase):
    def setUp(self):
        self.URL_OLD = "http://10.3.255.91:7070%s"
        self.URL_NEW = "http://10.3.255.207:7070%s"

    @data(*url_ilst)
    def test_case(self, url):

        if url.split("/")[12] == "1":
            raise unittest.SkipTest("version=1")

        if url.split("/")[3].startswith("20"):
            raise unittest.SkipTest("app")


        old_url = self.URL_OLD % (url)
        new_url = self.URL_NEW % (url)
        old_result = run_for_product_personal(old_url)    #字典，旧版本的所有数据的字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type， 第二个返回结果为请求预测类型
        new_result = run_for_product_personal(new_url)    #字典，新版本的所有数据字典list，包含ad_id，DebugAdInfo，debug_info，bid_ctr，score，search_type， 第二个返回结果为请求预测类型

        print " "
        print u'老环境请求地址：', old_url.decode('utf-8')
        print u'老环境请求地址：', new_url.decode('utf-8')
        f.write('老环境请求地址：%s\n' % str(old_url))
        f.write('新环境请求地址：%s\n'% str(new_url))
        if len(old_result) == 0 and len(new_result) == 0:
            raise unittest.SkipTest("no return")
        elif len(old_result) != len(new_result):
            print u'老产品返回广告长度:', len(old_result)
            print u'新产品返回广告长度:', len(new_result)
            self.fail(u"广告返回长度不一致")
        f.write("老版本返回广告长度: %s \n" % len(old_result))
        f.write("新版本返回广告长度: %s \n" % len(new_result))
        for i in range(0, len(old_result)):
            f.write("第%s个广告\n" % i)
            f.write("old_list: %s \n" % old_result[i])
            f.write("new_list: %s \n"% new_result[i])
            self.assertEqual(old_result[i], new_result[i])
            f.write('------------------------------------\n')
        f.write('################################################################\n')



    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(ad_calc_product))
    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'单品页计算模块--新老产品版本结果比对',
        description=u'从线上请求中筛选出请求的链接，拼装新老版本的计算模块地址。对比新老产品的同一版本的结果是否一致。'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    f.close()
    email_contents = "单品页计算模块版本比对测试完毕\n测试报告地址：%s\n测试日志地址：%s\n" % (test_report.encode('utf-8'), test_log.encode('utf-8'))
    new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com'], contents= email_contents)


