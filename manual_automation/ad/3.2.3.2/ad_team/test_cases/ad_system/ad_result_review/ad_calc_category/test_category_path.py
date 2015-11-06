#!/usr/bin/env python
#-*- coding: UTF-8 -*-

__author__ = 'tangna'
import unittest
import HTMLTestRunner
import json
import urllib2
from ddt import ddt, data
import sys
#from function.send_email import *
import codecs
from function.excel_func import *

#sys.path.append('../../function')

#从excel中读取cid
cat_id_list = []
tables = excel_table_byindex('cat_ids.xlsx')
for row in tables:
    #cat_id.append(row["cat_id"].encode("utf-8"))
    cat_id_list.append(row["cat_id"])
#print cat_id_list

#定义测试类
@ddt
class CategoryId(unittest.TestCase):

    #接受cid,用cid查询出category_path
    def get_cpath_request(self, cat_id):
        get_cid_path_url = "http://192.168.197.172/v2/find_categories.php?for=subpath_is_son&category_id=%s&category_path=&level=&result_format=json&fields=category_id,category_path&subpath=1&is_son=0"
        url = get_cid_path_url % cat_id
        #print url
        content = urllib2.urlopen(url, timeout = 10).read()
        try:
            category_detail_dict = json.loads(content)
            cpath = category_detail_dict["categorys"][0]["category_path"]
            #print cpath
        except Exception, ex:
            category_detail_dict = []

        if category_detail_dict.has_key('error'):
            cpath = []
        return cpath

    #查询结果，当结果为空时抛出异常
    def get_ad_ret(self, cid):
        ad_url = "http://10.255.254.62:8080/getAds/cpc/1/0/20/%s/3232262672/20141022092715613302051844096892271/0/0/0/3/0/0/0/0/0/-/debug"
        url = ad_url % cid
        print url
        content = urllib2.urlopen(url, timeout=10).read()
        result_dic = json.loads(content)
        result_cpath_list = []
        try:
            result = result_dic["data"]
        except Exception, ex:
            result = []
        try:
            if len(result) == 0:
                raise
        except Exception:
            print "****** No Result! *****. "
        else:
            return result

    #比较category_path的前两位是否相等
    @data(*cat_id_list)
    def test_catoegory_id(self, cat_id):
        cpath_request = self.get_cpath_request(cat_id)
        cpath_request_second = cpath_request[0:5]

        result = self.get_ad_ret(cat_id)

        for each in result:

            result_cid = str(each["cat_id"])
            result_cpath = self.get_cpath_request(result_cid)
            #print result_cpath
            self.assertEqual(cpath_request_second, result_cpath[0:5])
            print "cpath_request: ", cpath_request_second
            print "cpath_return: ", result_cpath[0:5] + "  " + "adid: ", each["ad_id"]


if __name__ == "__main__":
    test_suite = unittest.TestSuite()
    test_suite.addTest(unittest.makeSuite(CategoryId))
    test_report = "D:\\result.html"
    fp = file(test_report, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title='Report_title',
        description='Report_description'
    )
    runner.run(test_suite)
    fp.close()
