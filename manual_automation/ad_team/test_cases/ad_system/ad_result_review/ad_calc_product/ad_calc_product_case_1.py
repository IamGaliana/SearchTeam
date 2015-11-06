# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

'''
#精确匹配。 输入pid_in, cid_in 取精确结果集U。 U中所有广告品cid_out = cid_in
'''
import unittest
import HTMLTestRunner
from function.send_email import *
from function.ad_calc_func import *
from function.excel_func import *
import time


pid_list = []
tables = excel_table_byindex('product_id.csv')
for row in tables:
    pid_list.append(row["product_id"])

#print pid_list

#pid_list = ["1382271802", "1370872608", "60556559","2312312312312312315675672323", "1158336824"]
func_name_list = ["testcase_%s" % str(i) for i in range(0, len(pid_list))]


class ad_calc_product(unittest.TestCase):
    def setUp(self):
        pass

    def tearDown(self):
        pass


def make_func(func_name, pid):
    def test_case(self):

        try:
            cid_in = get_product_info(pid)['category_id']
        except Exception, ex:
            cid_in = -1
            raise(u"单品id错误。没有该单品id的信息返回")

        cid_out = result_for_product_related_type(pid, 0, 3)

        if len(cid_out) == 0:
            raise(u"该单品下没有广告返回")

        else:
            print u"返回的广告的cid数据list长度是", len(cid_out)
            for i in range(0, len(cid_out)):
                print "product_id_in=", pid, "cid_in=", cid_in, "cid_out=", cid_out[i]["cat_id"], "ad_id=", cid_out[i]["ad_id"]
                self.assertEqual(cid_in, str(cid_out[i]["cat_id"]))
            return True

    setattr(ad_calc_product, "%s_%s" % (func_name, pid), test_case)

for i, fn in enumerate(func_name_list):
    make_func(fn, pid_list[i])


if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    #for i in range(len(pid_list)):
    #    test_suite.addTest(ad_calc_product("testcase_%s_%s" % (str(i), pid_list[i])))
    test_suite.addTests(unittest.makeSuite(ad_calc_product))
    title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
    test_report = "D:\\TDD_Report\\"+title+"ad_calc_product_testcase_1.html"
    print test_report
    fp = file(test_report, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'单品页计算模块--召回功能测试',
        description=u'单品页计算模块——召回精确匹配测试, 主要测试返回的广告中， 相关性强的广告数据的cid需要与输入的单品的cid相同。'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    #time.sleep(10)
    fp.close()
    send_mail(subject, files=file_list)