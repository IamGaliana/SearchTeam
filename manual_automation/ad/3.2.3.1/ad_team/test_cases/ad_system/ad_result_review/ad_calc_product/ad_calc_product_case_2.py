# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

'''
#扩展匹配。 输入pid_in, 取出pid_in所属的cid_in的所有匹配cid_in集合U（cid_in）。 取匹配结果集中所有广告品的cid_out集合U（cid_out）。U（cid_in）包含U（cid_out）
'''
import unittest
import HTMLTestRunner
from function.send_email import *
from function.ad_calc_func import *
from function.excel_func import *
import time
from ddt import ddt, data


pid_list = []
tables = excel_table_byindex('product_id.csv')
for row in tables:
    pid_list.append(row["product_id"])


@ddt
class ad_calc_product(unittest.TestCase):
    def setUp(self):
        pass
    @data(*pid_list)
    def test_case(self, pid):

        try:
            cid_in = get_product_info(pid)['category_id']

        except Exception, ex:
            raise(u"单品id错误。没有该单品id的信息返回")

        cid_in_list = get_related_cid(pid)
        print u"输入的cid的扩展cid集合：", cid_in_list
        cid_in_son_list = get_cid_out_son_list(cid_in_list)
        print u"输入的cid的扩展cid的所有儿子类集合（包含自身）=", cid_in_son_list
        cid_out_list = result_for_product_related_type(pid, 1, 3)

        if len(cid_out_list) == 0:
            raise(u"该单品下没有广告返回")

        else:
            print u"返回的广告的cid数据list长度是", len(cid_out_list)
            for i in range(0, len(cid_out_list)):
                print "product_id_in=", pid, "cid_in=", cid_in, "cid_out=", cid_out_list[i]["cat_id"], "ad_id=", cid_out_list[i]["ad_id"]
                self.assertIn(str(cid_out_list[i]["cat_id"]), cid_in_son_list)
            return True

    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(ad_calc_product))
    title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
    test_report = "D:\\TDD_Report\\"+title+"ad_calc_product_testcase_2.html"
    print test_report
    fp = file(test_report, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'单品页计算模块--召回功能测试',
        description=u'扩展匹配。 输入pid_in, 取出pid_in所属的cid_in的所有匹配cid_in集合U（cid_in）。'
                    u'取匹配结果集中所有广告品的cid_out集合U（cid_out）。U（cid_in）包含U（cid_out）'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    #time.sleep(10)
    fp.close()
    #send_mail(subject, files=file_list)