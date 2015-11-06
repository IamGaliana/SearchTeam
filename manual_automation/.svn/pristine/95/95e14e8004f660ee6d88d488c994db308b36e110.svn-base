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
tables = excel_table_byindex('category_id_list.xlsx', by_index= 0)
for row in tables:
    category_list.append(int(row["category_id"]))

title = time.strftime('%Y-%m-%d_%H.%M.%S_',time.localtime(time.time()))
test_report = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\category\\"+title+u"danghome_6F_category_ad.html"

#category_list = ['4001394','4002853','4002778']

@ddt
class ad_calc_category(unittest.TestCase):
    def setUp(self):
        self.url = "http://10.3.255.91:8080/getAds/cpc/1/0/50/%s/3232260129/20150408183739945343244477079368468/0/0/0/1/1429083870466543454704/0/0/0/3/1/debug"

    @data(*category_list)
    def test_case(self, category_id):
        url = self.url % (category_id)
        print ""
        print url
        result = run_for_search_category(url)[0]   #获得返回数据
        if len(result) == 0:
            raise u"返回结果为空"
        setlist = set()
        shop_list = []
        for i in result:
            product_id = i['product_id']
            ad_id = i["ad_id"]
            extra_json = json.loads(urllib2.unquote(i['extra_json']))    #将extra_json数据做urldecode处理。
            shop_id = str(extra_json['shop_id'])             #将字符串转换成json格式的字典
            minilist = (shop_id, ad_id, product_id)          #将三个key的值加入到元组。 因为元组可以加到set（集合）里。而字典和list是不能加到set里的。
            setlist.add(minilist)
        for i in setlist:
            shop_list.append(i[0])

        myset = set(shop_list)
        for item in myset:
            print("the %s has found %s" %(item,shop_list.count(item)))
            self.assertLess(shop_list.count(item), 3)

    def tearDown(self):
        pass

if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTests(unittest.makeSuite(ad_calc_category))

    print test_report
    fp = file(test_report.encode('GBK'), 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u'当首六层分类列表页广告策略修改',
        description=u'每个商家的广告不能大于两个'
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    email_contents = "测试结果请看附件, 报告地址：%s " % test_report.encode('utf-8')
    new_send_mail(subject, files=file_list, to_list=['xiyucheng@dangdang.com'], contents= email_contents)


