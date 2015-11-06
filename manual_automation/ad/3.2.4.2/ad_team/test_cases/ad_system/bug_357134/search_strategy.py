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

reload(sys)
sys.setdefaultencoding('utf-8')

query_list = []
tables = excel_table_byindex('query_list.xlsx')
for row in tables:
    query_list.append(str(row["query"]).encode("utf-8"))

title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
#test_report = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\searchstrategy\\"+title+u"search_strategy.html"
test_report = u"E:\\manual_automation\\ad_team\\test_cases\\ad_system\\bug_357134\\"+title+u"search_strategy.html"

@ddt
class Ad_Search_Strategy(unittest.TestCase):
    def setUp(self):
        #self.url = "http://10.3.255.91:9080/getAds/cpc/0/0/20/%s/3232262697/20150624143722317328328211769204851/;/23262293,23219914,23345760,21000723,1092857535,1121025235,1121022935,1121021635,1121023435,1017555608/0/2/1435542709927575220528/0/0/0/0/debug"
        self.url = "http://10.3.255.91:9080/getAds/cpc/200/0/20/%s/184537119/0/0/0/107169205/3/-/0/0/0/debug"
    @data(*query_list)
    def test_case(self, query):
        url = self.url % (urllib2.quote(query))
        print url
        result = run_for_search_category(url)   #获得返回数据

        return_data = result[0]
        if len(return_data) == 0:
            raise unittest.SkipTest("no ads")
        return_debug = result[1]
        queryPath = return_debug[0:4]
        queryPath_1 = return_debug[0:2]

        if queryPath == '0141':           #童书
            for i in return_data:
                ad_id = i["ad_id"]
                cat_path = i["cat_path"]
            #    print cat_path
                cat_path_1 = i["cat_path"][0:4]
                if cat_path_1 != '0141':
                    print "广告%s的cat_path为%s,非童书广告" % (ad_id, cat_path)
                self.assertEqual(cat_path_1, "0141")


        elif queryPath_1 == '01':         #图书
            for i in return_data:
                ad_id = i["ad_id"]
                cat_path = i["cat_path"]
            #    print cat_path
                cat_path_1 = i["cat_path"][0:2]
                if cat_path_1 != '01':
                    print "广告%s的cat_path为%s,非图书广告" % (ad_id, cat_path)
                self.assertEqual(cat_path_1, "01")


    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(Ad_Search_Strategy))

    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'童书搜索只能出童书广告 图书搜索只能出图书广告',
        description=u'童书搜索只能出童书广告 图书搜索只能出图书广告'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    # email_contents = "测试结果请看附件, 报告地址：%s " % test_report.encode('utf-8')
    # new_send_mail(subject, files=file_list, to_list=['gaoxuewei@dangdang.com'], contents= email_contents)


