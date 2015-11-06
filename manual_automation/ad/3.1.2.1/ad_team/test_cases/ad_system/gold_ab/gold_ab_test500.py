#!/usr/bin/env python
#coding:utf8
__author__ = 'tangna'
import unittest
from ddt import ddt, data
from function.send_email import *
import HTMLTestRunner
import urllib2
from function.ad_calc_func import *
from function.excel_func import *
import HTMLTestRunner
#from test_data import *
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

#从关键词的excel文件中取非图的关键词放入query_list
query_list = []
query_list = excel_table_byindex2(excel_file='keyword_for_compare.xlsx', sheet_index=1)[1:]
print "len(query_list):", len(query_list)

#通过query_cate_utf8文件将所有分类以58的词筛选出来
fi = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\query_cate_utf8"
kv_dict = key_dict(fi)
new_query_list = goldab_filte_keyword(query_list, kv_dict)
print "new query list: ", len(new_query_list)

@ddt
class GoldAB500(unittest.TestCase):
    def setUp(self):
        #self.url = 'http://10.255.254.62:8080/getAds/cpc/%s/0/10/%s/184537119/0/0/0/107169205/2/-/0/0/0/0/-/debug'
        self.url = 'http://10.255.254.62:8080/getAds/cpc/%s/0/10/%s/184537119/0/0/0/107169205/2/-/0/0/0/0/-/debug'
        #self.url = 'http://a.dangdang.com/cpc_new.php?position=%s&page=0&extra=%s&source=other&pagesize=16&brand=&ref=&pid=0&title=0&topsize=0&rdm=1430187091753547399573&style=ssnew&precise=0&gold_size=4'
    #从得到的赶回结果中将前4个广告的ad_id, searchtype, m_oriscore,gold_search_type, 放入对应列表中，并将所有的广告ad_id和gold_search_type放入对应列表中

    @data(*new_query_list)
    def test_gold_ab(self, query):
        posid = 500
        self.url = self.url % (posid, urllib2.quote(query.encode('utf-8')))
        ad_result = request_goldab(self.url)
        print "url:", self.url
        print "query:", query
        if not ad_result:
            raise "没有广告返回"
        else:
            ad_data = ad_result['data']
            searchtype_find, m_oriscore_find, ad_id, gold_search_type, ad_id_all, gold_search_type_all = goldab_get_values(ad_data)

            #测试前三个广告searchtype = -1
            ad_searchtype = dict(zip(ad_id[0:3], searchtype_find[0:3]))
            for i in searchtype_find[0:3]:
                self.assertEqual(i, '-1', '前三个广告的searchtype不都为-1\n%s' %ad_searchtype)

            #测试前四个广告中,m_oriscore>=70的个数 >=3
            count_m_o = 0
            ad_id_m_schore = dict(zip(ad_id, m_oriscore_find))
            for i in m_oriscore_find:
                if int(i) < 70:
                    count_m_o += 1
            if count_m_o > 1:
                self.fail('m_oriscore>=70的个数不应小于3个\n%s' %ad_id_m_schore)

            #测试前四个广告的gold_search_type = 1
            ad_gold_search_type = dict(zip(ad_id, gold_search_type))
            for i in gold_search_type:
                self.assertEqual(i, '1', '前四个广告的gold_search_type不都为1\n%s' %ad_gold_search_type)

            #测试第五个之后的广告(包括第五个)gold_search_type=0
            ad_gold_search_type5 = dict(zip(ad_id_all[4:], gold_search_type_all[4:]))
            for i in ad_data[4:]:
                self.assertEqual(i['gold_search_type'], '0', '第5个及之后的广告的gold_search_type不都为0\n%s' %ad_gold_search_type5)


if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTest(unittest.makeSuite(GoldAB500))
    test_report = "D:\\goldab500.html"
    fp = file(test_report, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title= u'黄金展位AB版计算模块测试',
        description= u'''
        测试点如下：
        1)前三个广告searchtype = -1,
        2)前四个广告中,m_oriscore>=70的个数 >=3,
        3)前四个广告的gold_search_type = 1,
        4)第五个之后的广告(包括第五个)gold_search_type=0
        '''
    )
    runner.run(test_suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    #email_contents = "黄金展位AB版计算模块测试，该测试为百货搜索词测试\n版本为2\n测试报告地址：%s\n测试日志地址：%s\n" % (test_report.encode('utf-8'), test_log.encode('utf-8'))
    #new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com,wangdongdong@dangdang.com,dingyu@dangdang.com,chengmingbo@dangdang.com'], contents= email_contents)
    #new_send_mail(subject, files=[], to_list=['xiyucheng@dangdang.com,tangna@dangdang.com'], contents= email_contents)