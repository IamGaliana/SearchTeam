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
test_report = u"E:\\manual_automation\\ad_team\\test_cases\\ad_system\\bug_357134\\"+title+u"app_ads_related_rules.html"

def is_in_c2c(cat_path, c2c_set):
    if cat_path in c2c_set:
        return True
    if cat_path[0:10]+"00" in c2c_set:
        return True
    if cat_path[0:8]+"0000" in c2c_set:
        return True
    if cat_path[0:6]+"000000" in c2c_set:
        return True
    if cat_path[0:4]+"00000000" in c2c_set:
        return True
    if cat_path[0:2]+"0000000000" in c2c_set:
        return True
    return False

@ddt
class Ad_Search_Strategy(unittest.TestCase):
    def setUp(self):
        #self.url = "http://10.3.255.91:9080/getAds/cpc/0/0/20/%s/3232262697/20150624143722317328328211769204851/;/23262293,23219914,23345760,21000723,1092857535,1121025235,1121022935,1121021635,1121023435,1017555608/0/2/1435542709927575220528/0/0/0/0/debug"
        self.url = "http://10.3.255.91:9080/getAds/cpc/200/0/20/%s/184537119/0/0/0/107169205/3/-/0/0/0/debug"
    @data(*query_list)
    def test_case(self, query):
        url = self.url % (urllib2.quote(query))
        print url
        result = run_for_search_category_alldebug(url)   #获得返回数据

        return_data = result[0]
        #print return_data
        if len(return_data) == 0:
            raise unittest.SkipTest("no ads")
        return_DebugQueryPath = result[1]
        return_DebugC2CPath = result[2]
        return_DebugSynPath = result[3]
        return_DebugSynC2CPath = result[4]

        querylist = return_DebugQueryPath.split()
        c2clist = return_DebugC2CPath.split()

        synlist = []
        synlist_1 = return_DebugSynPath.split(';')
        for i in synlist_1:
            synlist_2 = i.split(':')
            for j in synlist_2:
                synlist_3 = j.split()
                synlist = synlist + synlist_3

        sync2clist = []
        sync2clist_1 = return_DebugSynC2CPath.split(';')
        for i in sync2clist_1:
            sync2clist_2 = i.split(':')
            for j in sync2clist_2:
                sync2clist_3 = j.split()
                sync2clist = sync2clist + sync2clist_3

        c2c_set = set(querylist + c2clist + synlist + sync2clist)

        #print list

        for i in return_data:
            ad_id = i["ad_id"]
            cat_path = i["cat_path"]
            precise = i["search_type"]
            app_sim_flag = i["app_sim_flag"]

            if is_in_c2c(cat_path, c2c_set) and precise == '0':
                if app_sim_flag != '0':
                    print "广告%s高低相关规则错误" % ad_id
                self.assertEqual(app_sim_flag, "0")

            elif is_in_c2c(cat_path, c2c_set) and precise == '1':
                if app_sim_flag != '1':
                    print "广告%s高低相关规则错误" % ad_id
                self.assertEqual(app_sim_flag, "1")

            elif is_in_c2c(cat_path, c2c_set) and precise == '0':
                if app_sim_flag != '1':
                    print "广告%s高低相关规则错误" % ad_id
                self.assertEqual(app_sim_flag, "1")

            elif is_in_c2c(cat_path, c2c_set) and precise == '1':
                if app_sim_flag != '1':
                    print "广告%s高低相关规则错误" % ad_id
                self.assertEqual(app_sim_flag, "1")


        # print "return_DebugQueryPath: " + return_DebugQueryPath
        # print "return_DebugC2CPath: " + return_DebugC2CPath
        # print "return_DebugSynPath" + return_DebugSynPath
        # print "return_DebugSynC2CPath" + return_DebugSynC2CPath
        # print c2c_set

    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(Ad_Search_Strategy))

    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'App广告高低相关规则',
        description=u'''
        规则如下：
        1) cat_path in c2c且precise等于0时，app_sim_flag为0\n
        2）cat_path in c2c且precise等于1时，app_sim_flag为1
        3）cat_path not in c2c且precise等于0时，app_sim_flag为1
        4) cat_path not in c2c且precise等于1时，app_sim_flag为1
        '''
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    # email_contents = "测试结果请看附件, 报告地址：%s " % test_report.encode('utf-8')
    # new_send_mail(subject, files=file_list, to_list=['gaoxuewei@dangdang.com'], contents= email_contents)


