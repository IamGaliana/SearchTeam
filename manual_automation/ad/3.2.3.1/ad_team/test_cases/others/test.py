# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

from function.email_func import *
from selenium import webdriver
from function.excel_func import *
from function.linux_order import *
import urllib2
import urllib

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

#kv_dict = {"大野狼诊所":"01.00.00.00.00.00", "简爱世界图书出版":"01.00.00.00.00.00", "硫磺回收":"01.00.00.00.00.00"}

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
#str = "大野狼诊所"
#value = ""
#try:
#    value = kv_dict[str]
#except Exception:
#    value = ""
#
#print value.startswith(" ")
#
#if value.startswith("")== True:
#    print "not true"
#else:
#    print "true"
#
#print value
#==================================================
#host = '10.255.242.72'
#port = 22
#user = 'root'
#passwd = '10test72a'
#cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log"
#print cmd
#x_log = ssh(host, port, user, passwd, cmd)
#zidian = {}
##str = "31/Mar/2015:01:10:55 +0800 GET /logurl.htm?tp=1&seq=0&pos=9&ad_id=2274462&extra=%B3%C4%C9%C0&extra_org=16833&ip=3232262659&permanent_id=20150325154118059361843851804982950&rdm=1427707871785403643514&style=search__float_new_2&showdiv=cpc_float&ab=1&sflag=5 HTTP/1.1 http://search.dangdang.com/?key=%B3%C4%C9%C0"
##str = "03/Apr/2015:04:15:18 +0800	GET /logurl.htm?tp=2&seq=0,1,2,3,&pos=9,9,9,9,&ad_id=3857792,2910882,3856762,3868392,&extra=88888&extra_org=4003844,4003844,4003844,4003844,&ip=3232262697&permanent_id=20150324135333287766468342271876382&rdm=1427942366882682696476&style=dangHome&showdiv=cpc_0&sflag=8,8,8,8,&ab=response.write(9412458*9542755) HTTP/1.1	http://10.255.242.72:9090/logurl.htm?tp=2&seq=0,1,2,3,&pos=9,9,9,9,&ad_id=3857792,2910882,3856762,3868392,&extra=88888&extra_org=4003844,4003844,4003844,4003844,&ip=3232262697&permanent_id=20150324135333287766468342271876382&rdm=1427942366882682696476&style=dangHome&showdiv=cpc_0&sflag=8,8,8,8,&ab=response.write(9412458*9542755)"
#str_1 = x_log[1].split(" ")
#print str_1.__len__()
#print str_1
#if len(str_1) == 4:
#    print "1123123123123"
#    zidian["time"]=str_1[0]
#    zidian["url"]=str_1[3]
#    bb = str_1[2].split("&")
#elif len(str_1) == '5':
#    print "dsfdf "
#    zidian["time"]=str_1[0]
#    zidian["url"]=str_1[5]
#    bb = str_1[3].split("&")
##print bb
#for i in bb:
#    x = i.split("=")
#    zidian[x[0]]=x[1]
#print zidian

#============================
#list = ['1','2','3,4','5','6','7,8,9']
#str = ("").join(list)
#print str
#===================================
#from function.ad_calc_func import *
#url = "http://10.3.255.91:8080/getAds/cpc/1/0/50/4000000/3232260129/20150408183739945343244477079368468/0/0/0/1/1429083870466543454704/0/0/0/3/1/debug"
#result = run_for_search_category(url)[0]
#setlist = set()
#shop_list = []
#for i in result:
#    product_id = i['product_id']
#    ad_id = i["ad_id"]
#    extra_json = json.loads(urllib2.unquote(i['extra_json']))
#    shop_id = str(extra_json['shop_id'])
#    minilist = (shop_id, ad_id, product_id)
#    setlist.add(minilist)
#
#for i in setlist:
#    shop_list.append(i[0])
#
##print list
#print shop_list
#myset = set(shop_list)
#for item in myset:
#    print("the %s has found %s" %(item, shop_list.count(item)))


#print urllib2.unquote("%7B%22mp%22%3A0%2C%22shop%5Fid%22%3A8617%2C%22shop%5Fname%22%3A%22%22%2C%22review%5Fcount%22%3A47%2C%22average%5Fscore%22%3A5%2C%22crazy%5Flike%5Fcount%22%3A43%7D")

#===========================
#from function.ad_calc_func import *
#old_result = run_for_search_category("http://10.3.255.91:8080/getAds/cpc/0/0/100/%E7%B1%B3%E5%A5%87%E4%B9%A6%E5%8C%85/184537119/0/0/0/107169205/4/-/0/0/0/debug")[0]
#results = filter(lambda each: '0' == each["search_type"], old_result)
#print results



#df = [{"a":"1", "b":"2"}, {"a":"2", "b":"2"}, {"a":"3", "b":"2"}]
#xf = filter(lambda each: "3" in each['a'], df)
#print xf

#============================
#list = ["1","2","3","4"]
#dict = {"1":"a", "2":"b", "3":"c", "4":"d"}
#new_list = []
#for i in list:
#    if dict[i].startswith("a") == True:
#        new_list.append(i)
#
#print new_list
#============================
#value="01.00.00.00.00.00"
#
#
#if value.startswith("01") and not value.endswith(".00.00.00.00.00"):
#    print "111"
#
#
#if value.startswith("58") == True or value.endswith(".00.00.00.00.00") or value == "":
#    print "2222"
#
#from function.ad_calc_func import *
#url = "http://10.3.255.91:8080/getAds/cpc/200/0/100/%E4%BA%8C%E9%98%B6%E9%AD%94%E6%96%B9/184537119/0/0/0/107169205/1/-/0/0/0/debug"
#print response_ad_calc_server(url)
#import json
#str = ""
#ss = json.loads(str)
#print ss

#===========================
from function.ad_calc_func import *
url = "http://10.3.255.91:7070/getAds/cpc/201/0/4/4004279/168037785/20150610170750870625412404687296476/0/0/0/1/1433927454896869709792/0/1063105734/0/0/h5%09h5Product/debug2"

result = response_ad_calc_server(url)
#print result
submit = result["debug"]["submit"]
print "submit:", submit
for i in submit:
    print i
#print url.split("/")[6]