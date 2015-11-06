#!/usr/bin/env python
#encoding:utf8

import tornado.ioloop
import tornado.web
import datetime
import random
import time
import math
import sys
import json
import urllib
import urllib2
import re
#from fake_front import get_catid, get_brand, get_behavior, random_permid, random_permid_pro


img_url = "http://img3%s.ddimg.cn/%s/%s/%s-1_l.jpg"
pid_url = "http://product.dangdang.com/%s.html"
#product_url = "http://10.4.32.240:8270/product/?pid=%s"
product_url = "http://192.168.197.172/v2/find_products.php?by=product_id&keys=%s&expand=&result_format=json"
ad_url = "http://10.255.254.62:8080/getAds/cpc/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/debug"
'''
#pos_id, page_no, page_size, exa,
#ip, perm_id,
#history_key,productid_history,
#customer_id,exp_ver,
#throughid,brand,cpc_pid,cpc_title
/0/0/16/%CD%E2%CC%D7
/3232262672/20141022092715613302051844096892271
/%3B/1481021005%2C1180106112%2C1399765502%2C1433158705%2C1161337412%2C1161355612%2C1074868606%2C1453394301%2C1236782206%2C1383240605
/0/1
/1414482767814542429961/0/0/0
'''
def zero(x):

    if not x:
        return '0'
    else:
        return x

def url_encode(x):
    return urllib2.quote(x.encode("utf8")).replace('/', "%2F")

def trans_code(x):
    x = x.encode("utf8")
    return str.split(x)
'''
def fetch_pid_detail(pid):

    url = product_url % pid
    #print url
    content = urllib2.urlopen(url, timeout = 10).read()
    try:
        info = json.loads(content)
        pid_detail_dict = info["products"][pid]["core"]

    except Exception, ex:
        pid_detail_dict = {}

    if pid_detail_dict.has_key('error'):
        pid_detail_dict = {}
    return pid_detail_dict
'''
def fetch_pid_detail(pid):

    url = product_url % pid
    #print url
    content = urllib2.urlopen(url, timeout = 10).read()
    #print content
    try:
        info = json.loads(content)
        pid_detail_dict = info["products"][pid]["core"]
        #print pid_detail_dict
    except Exception, ex:
        pid_detail_dict = {}

    if pid_detail_dict.has_key('error'):
        pid_detail_dict = {}

    return pid_detail_dict

def fetch_category_detail(pid):

    url = product_url % pid
    #print url
    content = urllib2.urlopen(url, timeout = 10).read()
    #print content
    try:
        info = json.loads(content)
        category_detail_dict = info["products"][pid]["category"]
        #print pid_detail_dict
    except Exception, ex:
        category_detail_dict = {}

    if category_detail_dict.has_key('error'):
        category_detail_dict = {}

    return category_detail_dict
    #print category_detail_dict

def fetch_ad_data(pos_id, page_no, page_size, extra, ip, perm_id, history_key, product_history, cust_id, exp_ver, through_id, brand, cpc_pid,cpc_title):
    extra = urllib2.quote(extra.encode("utf8"))
    url = ad_url % (pos_id, page_no, page_size, extra, ip, perm_id, history_key, product_history, cust_id, exp_ver, through_id, brand, cpc_pid,cpc_title)
    #print url

    for i in xrange(0, 1):
        try:
            content = urllib2.urlopen(url, timeout = 3).read()
        except Exception, ex:
            print ex
            continue

    try:
        ad_list = json.loads(content)
        #print ad_list
    except Exception, ex:
        ad_list = []

    return ad_list


def gen_ad_ret(ad_list):
    try:
        results = ad_list['data']
    except Exception, ex:
        results = []
    ad_ret = []
    for each in results:

        pid_info = {}
        pid = each['product_id']

        pid_info['pid'] = pid
        pid_info['img'] = img_url % (pid % 10, pid % 99, pid % 37, pid)
        pid_info['url'] = pid_url % (pid)
        pid_info['bid'] = float(each['cost']) / 100000
        #pid_info['strategy'] = eng_ch_dict.get(each['cat_extend'], 'NA')

        pid = str(pid)
        pid_detail_dict = fetch_pid_detail(pid)
        category_detail_dict = fetch_category_detail(pid)

        pid_info['name'] = pid_detail_dict['product_name'] if 'product_name' in pid_detail_dict else "NA"
        pid_info['cat_id'] = each['cat_id'] if 'cat_id' in each else "NA"
        pid_info['category_path'] = category_detail_dict['category_path'] if 'category_path' in category_detail_dict else "NA"
        pid_info['path_name'] = category_detail_dict['path_name'] if 'path_name' in category_detail_dict else "NA"

        pid_info['ad_name'] = each['ad_name']
        pid_info['debug_info'] = each['DebugAdInfo']
        #print each['DebugAdInfo']
        pid_info['ad_id'] = each['ad_id']
        pid_info['pos'] = each['pos']

        ad_ret.append(pid_info)

    return ad_ret


class MainHandler(tornado.web.RequestHandler):

    def get(self):

        xpos_id = self.get_argument("pos_id", '0')
        xpage_no = self.get_argument("page_no", '0')
        xpage_size = self.get_argument("page_size", '20')
        xextra = self.get_argument("extra", u'外套')
        xip = self.get_argument("ip", '3232262672')
        xperm_id = self.get_argument("perm_id", '20141022092715613302051844096892271')
        xhistory_key = self.get_argument("history_key",'')
        xproduct_history = self.get_argument("product_history", '')
        xcust_id = self.get_argument("cust_id", '')
        xexp_ver = self.get_argument("exp_ver",'1')
        xthrough_id = self.get_argument("through_id",'')
        xbrand = self.get_argument("brand", '')
        xcpc_pid = self.get_argument("cpc_pid", '')
        xcpc_title = self.get_argument("cpc_title", '')

        ad_list = fetch_ad_data(xpos_id, xpage_no, xpage_size, xextra, xip, xperm_id, zero(xhistory_key), zero(xproduct_history), \
                                zero(xcust_id), xexp_ver, zero(xthrough_id), zero(xbrand), zero(xcpc_pid), zero(xcpc_title))
        #print ad_list
        ad_ret = gen_ad_ret(ad_list)

        self.render("show_search_ad.html",
                    pos_id = xpos_id,
                    page_no = xpage_no,
                    page_size = xpage_size,
                    extra = xextra,
                    ip = xip,
                    perm_id = xperm_id,
                    history_key = xhistory_key,
                    product_history = xproduct_history,
                    cust_id = xcust_id,
                    exp_ver = xexp_ver,
                    through_id = xthrough_id,
                    brand = urllib2.unquote(xbrand),
                    cpc_pid = xcpc_pid,
                    cpc_title = xcpc_title,
                    total_count = len(ad_ret),
                    #page_type = "0",
                    ret = ad_ret)


def main():

    application = tornado.web.Application([(r"/", MainHandler),])
    application.listen(8912)
    tornado.ioloop.IOLoop.instance().start()

if __name__ == "__main__":

    main()

