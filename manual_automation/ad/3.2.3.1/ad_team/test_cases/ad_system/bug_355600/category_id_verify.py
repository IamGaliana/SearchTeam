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


@ddt
class category_id_verify(unittest.TestCase):
    def setUp(self):
        self.URL = "http://10.3.255.91:7070%s/debug2"

    @data(*url_ilst)
    def test_case(self, url):

        if url.split("/")[6] != "4004279":
            raise unittest.SkipTest("no phone")

        url = self.URL % (url)
        print " "
        print u'老环境请求地址：', url.decode('utf-8')
        result = response_ad_calc_server(url)

        if len(result) == 0:
            raise unittest.SkipTest("no return")

        submit = result["debug"]["submit"]
        print "submit:", submit
        for i in submit:
            self.assertNotEqual(i, "4005712")

    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(category_id_verify))
    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'手机分类4004279召回广告的扩展分类不能有坚果分类 4005712',
        description=u'手机分类4004279召回广告的扩展分类不能有坚果分类 4005712。'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    email_contents = "测试报告地址：%s\n" % (test_report.encode('utf-8'))
    new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com'], contents= email_contents)


