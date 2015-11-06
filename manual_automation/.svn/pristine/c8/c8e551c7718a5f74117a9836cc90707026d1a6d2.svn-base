# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

from function.email_func import *
from selenium import webdriver
from function.excel_func import *

#ff = webdriver.Firefox()
#
#mail_qq(ff)

#url = "https://mail.qq.com/cgi-bin/loginpage"
#ff.get(url)
#time.sleep(5)
#ff.switch_to_frame(mail_loginframe_QQ)
#ff.find_element_by_xpath(mail_loginname_QQ).send_keys("sdfasdf")





#str = "  searchtype:0 bid:8800000 ctr:1 sim:1000 score: 0.0359577052 select:0 match:5 path:586405260000 "
#items = str.strip().split(" ")
#print items
#for item in items:
#        tokens = item.split(":")
#        if tokens[0]=="bid":
#            bid = tokens[1]
#        elif tokens[0] in set(["sim", "newsim"]):
#            sim =tokens[1]
#print bid, sim

#list = []
#list.append(set('1'))
#list.append(set('2'))
#list[0].add('5')
#print list

#ss = str.strip().split(" ")[5]
#print ss

#list = [{"adid":"1", "adxxxx":"xxfsdadf"},{"adid":"2", "adxxxx":"xfdxfadf"},{"adid":"1", "adxxxx":"xxfsdadf"},{"adid":"1", "adxxxx":"xxfsdfadf"}]
#
#
#
#results = filter(lambda each: "2" in each["adid"], list)
#
#print results

#import sys
#reload(sys)
#sys.setdefaultencoding( "utf-8" )
###############################################################################################
#import urllib2
#query_list = []
#tables = excel_table_byindex('D:\\Work\\ad_team\\cases\\ad_system\\ad_calc_search\\keyword.xlsx', by_index=1)
#for row in tables:
#    print row["keyword"]
#    query_list.append(row["keyword"].encode("utf-8"))
#
#print query_list
#
#URL_OLD = "http://10.255.254.62:8080/getAds/cpc/0/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
#
#old_url = URL_OLD % (urllib2.quote(query_list[0]), "1")
#
#print old_url
#############################################
#str = "abcd"
#
#test_report = "D:\\ad_calc_personal_compare.txt"
#f = file(test_report, 'a')
#f.write('test %s\n' % str.decode('utf-8'))
#f.close()
###################################################

kv_dict = {"大野狼诊所":"01.00.00.00.00.00", "简爱世界图书出版":"01.00.00.00.00.00", "硫磺回收":"01.00.00.00.00.00"}

#fi = u"\\\\ddfile\\技术部\\测试部\\广告系统\\自动化测试报告\\query_cate_utf8"
#fi = u"D:\\query_cate_utf8"
#fi_handler = open(fi, 'r')
#
#for line in fi_handler:
#
#    fields = line.strip().split('\t')
#    k = fields[0]
#    v = '\t'.join(fields[1:])
#    kv_dict[k] = v
##
#fi_handler.close()
#print kv_dict.__len__()

#大野狼诊所	01.00.00.00.00.00
str = "大野狼诊所"
value = ""
try:
    value = kv_dict[str]
except Exception:
    value = ""

print value.startswith(" ")
#
#if value.startswith("")== True:
#    print "not true"
#else:
#    print "true"
#
#print value