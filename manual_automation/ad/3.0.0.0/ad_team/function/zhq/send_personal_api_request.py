# -*- coding: UTF-8 -*-
import json
import urllib2
from function.zhq.write_file import addfile_changeline

__author__ = 'zhaohongqiang'

def app4_api(self, url):
    #extra = quote('..........................')
    #url = "http://10.255.254.62:9090/getadspersonal/9/0/23/0/0/0/3/1407986641294815803640/0/"+brand+"/0/1"
    #app四期API
    url = 'http://a.dangdang.com/cpc_mobile_api.php?pos=200&extra=%E7%94%B7%E8%A3%85&page=0&pagesize=2&udid=11111&custid=50100562&platform=android&version=5.7.0&req_from=bd&perm_id=20141022151845706405256137996282405'
    response = urllib2.Request(url)
    print response
    res_data = urllib2.urlopen(response)
    res = res_data.read()
    print res
    print res[0]
    print '***************'
    #解析str变成字典
    ret_dict = json.loads(res)
    print ret_dict
    ret_dict_data = ret_dict['data']
    #得到一个list=ret_dict_data
    print ret_dict['data']
    print ret_dict_data[0]
    #ad_ids=[]
    for i in range(0, ret_dict_data.__len__()):
    #取字典键为ad_id的值
        print ret_dict_data[i]['ad_id']
        #ad_ids[i]=ret_dict_data[i]['ad_id']
#app4_api(url)

def pc_personal_api_brand(log_add,brand):
    #url
    url = "http://10.255.254.62:9090/getadspersonal/9/0/10/0/0/0/3/1407986641294815803640/0/" + brand + "/0/1"
    response = urllib2.Request(url)
    res_data = urllib2.urlopen(response)
    res = res_data.read()
    #解析str变成字典
    res_dict = json.loads(res)
    #print res_dict
    if res_dict==None:
        str=brand+'该品牌词请求下无广告'
        addfile_changeline(log_add,str)
    else:
        str=brand+'::发送回头客品牌词请求成功'
        addfile_changeline(log_add,str)
        return res_dict

#brand='茵曼'
#pc_personal_api_brand(brand)

def pc_personal_api_all(catid):
    #extra = quote('..........................')
    url = "http://10.255.254.62:9090/getadspersonal/9/0/10/0/0/" + catid + "/3/1407986641294815803640/0/0/0/1"
    #"http://10.255.254.62:9090/getadspersonal/9/0/10/0/0/4008149/3/1407986641294815803640/0/0/0/1"
    response = urllib2.Request(url)
    #print response
    res_data = urllib2.urlopen(response)
    res = res_data.read()
    #print type(res)
    res_li = json.loads(res)
    #print res_li
    if res_li==None:
        print('该分类下无广告')
    else:
        #print("返回请求结果集")
        return res_li

#catid = '4008149'
#pc_personal_api_all(catid)
#for i  in pc_personal_api_all(catid):
#   print i

