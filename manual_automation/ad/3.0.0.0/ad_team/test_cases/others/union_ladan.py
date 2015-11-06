# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'
import unittest
import HTMLTestRunner
from function.order_db import *
from function.linux_order import *
from function.send_email import *
import time


class Union_TDD(unittest.TestCase):

    def setUp(self):
        self.verificationErrors = []
        self.host = '10.255.255.96'
        self.port = 22
        self.user = 'root'
        self.passwd = '10Test96'

    def case_1(self):
        u"这是测试用例1,order_id=22031983111,测试已经拉过的单不会重复在拉。"
        order_id = 22031983111
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2013-09-09 01:29:00' -e '2013-09-09 01:31:00'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        self.assertEqual(result_2, 1)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2013-09-09 01:29:00' -e '2013-09-09 01:31:00'")
        num = count(22031983111)
        self.assertEqual(num, 1)
        print u"还原测试环境："
        return_environment(order_id)

    def case_2(self):
        u"这是测试用例2,order_id=5073258221,测试刷单，拉单不成功。"
        order_id = 5073258221
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2013-09-09 00:19:00' -e '2013-09-09 00:21:00'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 0)
        print u"还原测试环境："
        return_environment(order_id)

    def case_3(self):
        u"这是测试用例3,order_id=22031983091,测试无联盟信息单，拉单不成功。"
        order_id = 22031983091
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2013-09-09 01:19:00' -e '2013-09-09 01:21:00'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 0)
        print u"还原测试环境："
        return_environment(order_id)

    def case_4(self):
        u"这是测试用例4,order_id=5073258281,测试order_items数据库无明细信息，拉单不成功。"
        order_id = 5073258281
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2013-0909 02:09:00' -e '2013-0909 02:11:00'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 0)
        print u"还原测试环境："
        return_environment(order_id)

    def case_5(self):
        u"这是测试用例5,order_id=5073258321,测试正常的淘宝代购订单，拉单成功。"
        order_id = 5073258321
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2013-09-09 02:19:00' -e '2013-09-09 02:21:00'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 1)
        print u"还原测试环境："
        return_environment(order_id)

    def case_6(self):
        u"这是测试用例6,order_id=22090151052,测试为校园代理联盟订单，拉单成功。"
        order_id = 22090151052
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2014-06-05 18:49:37' -e '2014-06-05 18:49:39'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 1)
        print u"还原测试环境："
        return_environment(order_id)

    def case_7(self):
        u"这是测试用例7,order_id=22090156192,测试为校园代理联盟订单，收货地址不属于对应学校地址，拉单不成功。"
        order_id = 22090156192
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2014-06-05 19:00:51' -e '2014-06-05 19:00:53'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 0)
        print u"还原测试环境："
        return_environment(order_id)

    def case_8(self):
        u"这是测试用例8,order_id=22090159213,测试不是当当网联盟订单。普通账号在未点击广告的情况下下单，拉单不成功。"
        order_id = 22090159213
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2014-06-05 19:09:34' -e '2014-06-05 19:09:36'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 0)
        print u"还原测试环境："
        return_environment(order_id)

    def case_9(self):
        u"这是测试用例9,order_id=22090172433,测试为普通的无线联盟订单，拉单成功。"
        order_id = 22090172433
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2014-06-05 20:11:44' -e '2014-06-05 20:11:46'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 1)
        print u"还原测试环境："
        return_environment(order_id)

    def case_10(self):
        u"这是测试用例10,order_id=22090173093,测试为普通当当网账号未点击联盟广告的状态下下单，拉单不成功。"
        order_id = 22090173093
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2014-06-05 20:15:05' -e '2014-06-05 20:15:07'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 0)
        print u"还原测试环境："
        return_environment(order_id)

    def case_11(self):
        u"这是测试用例11,order_id=22090164773,测试为普通联盟订单，拉单成功。"
        order_id = 22090164773
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2014-06-05 19:35:31' -e '2014-06-05 19:35:33'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 1)
        print u"还原测试环境："
        return_environment(order_id)

    def case_12(self):
        u"这是测试用例11,order_id=22090166913,测试为联盟作弊单。无点击单，拉单不成功。"
        order_id = 22090166913
        print u"初始化测试环境："
        result_1 = return_environment(order_id)
        self.assertEqual(result_1, 0)
        ssh(self.host, self.port, self.user, self.passwd, cmd="cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/;php run_job.php -j 'Order::pullOrderOrders' -s '2014-06-05 19:45:28' -e '2014-06-05 19:45:30'")
        print u"查找数据库："
        result_2 = search_mango(order_id)
        print "result=", result_2
        self.assertEqual(result_2, 0)
        print u"还原测试环境："
        return_environment(order_id)



    def tearDown(self):
        self.assertEqual([], self.verificationErrors)

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTest(Union_TDD("case_1"))
    test_suite.addTest(Union_TDD("case_2"))
    test_suite.addTest(Union_TDD("case_3"))
    test_suite.addTest(Union_TDD("case_4"))
    test_suite.addTest(Union_TDD("case_5"))
    test_suite.addTest(Union_TDD("case_6"))
    test_suite.addTest(Union_TDD("case_7"))
    test_suite.addTest(Union_TDD("case_8"))
    test_suite.addTest(Union_TDD("case_9"))
    test_suite.addTest(Union_TDD("case_10"))
    test_suite.addTest(Union_TDD("case_11"))
    test_suite.addTest(Union_TDD("case_12"))
    title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
    str1 = "D:\\TDD_Report\\"+title+"TDD_Report.html"
    print str1
    fp = file(str1, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'联盟TDD项目测试报告--拉单作业',
        description=u'联盟TDD项目测试报告——拉单作业'
    )
    runner.run(test_suite)
    subject = runner.title
    files = [str1]
    send_mail(subject, files=files)