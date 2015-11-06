# -*- coding: UTF-8 -*-
__author__ = 'gaoxuewei'

import unittest
from ddt import ddt, data
import HTMLTestRunner
from function.send_email import *
from function.ad_calc_func import *
from function.excel_func import *
import time
import sys
import json
import urllib2

reload(sys)
sys.setdefaultencoding('utf-8')

query_list = []
tables = excel_table_byindex('golden_filter_query1.xlsx')
for row in tables:
    query_list.append(str(row["query"]).encode("utf-8"))
    #query_list.append(str(row["query"]).urlencode("utf-8"))

title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
#test_report = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\searchstrategy\\"+title+u"search_strategy.html"
test_report = u"E:\\manual_automation\\ad_team\\test_cases\\ad_system\\bug_361710\\"+title+u"golden_filter_strategy.html"

@ddt
class Ad_Search_Strategy(unittest.TestCase):
    def setUp(self):
        self.url = "http://10.3.255.91:9080/getAds/cpc/500/0/4/%s/184537119/0/0/0/107169205/2/-/0/0/0/debug"
        #self.url = "http://10.3.255.207:9080/getAds/cpc/500/0/4/%s/184537119/0/0/0/107169205/2/-/0/0/0/debug"
        self.producturl = "http://192.168.197.172/v2/find_products.php?by=product_id&keys=%s&expand=&result_format=json"

    @data(*query_list)
    def test_case(self, query):
        url = self.url % ("%2F".join(urllib2.quote(query).split("/")))
        print url
        result = run_for_search_category(url)   #获得返回数据

        return_data = result[0]
        if len(return_data) == 0:
            raise unittest.SkipTest("no ads")

        for i in return_data:
            product_id = i["product_id"]
            producturl = self.producturl % (product_id)
            print "producturl: ", producturl

            try:
                content = urllib2.urlopen(producturl, timeout=10).read()
                info = json.loads(content, encoding='utf8')
                shop_id = info["products"][str(product_id)]["core"]["shop_id"]

            except Exception, ex:
                print ex

            self.assertEqual(shop_id, "0")


    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(Ad_Search_Strategy))

    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'query中包含 当当自营 时，不在PC端黄金展位出非自营的广告',
        description=u'query中包含 当当自营 时，不在PC端黄金展位出非自营的广告'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    # email_contents = "测试结果请看附件, 报告地址：%s " % test_report.encode('utf-8')
    # new_send_mail(subject, files=file_list, to_list=['gaoxuewei@dangdang.com'], contents= email_contents)


