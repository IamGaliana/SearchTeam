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
from fake_front import get_catid, get_brand, get_behavior, random_permid, random_permid_pro


img_url = "http://img3%s.ddimg.cn/%s/%s/%s-1_l.jpg"
pid_url = "http://product.dangdang.com/%s.html"
product_url = "http://10.4.32.240:8270/product/?pid=%s"
#pos_id, page_no, page_size, perm_id, cids, version, cust_id, brand, page_type, cur_cid
ad_url = "http://10.255.254.62:8080/getadspersonal/%s/%s/%s/0/%s/%s/%s/0/%s/%s/%s/%s"
#ad_url = "http://192.168.196.187:8081/getadspersonal/%s/%s/%s/0/%s/%s/%s/0/%s/%s/%s/%s"


eng_ch_dict = {}
eng_ch_dict['1'] = "类别"
eng_ch_dict['3'] = "品牌"
eng_ch_dict['5'] = "类别扩展"
eng_ch_dict['6'] = "托底分类"

eng_ch_dict['7'] = "图书类别"
eng_ch_dict['8'] = "图转图百"
eng_ch_dict['9'] = "百货类别"
eng_ch_dict['10'] = "百货品牌"
eng_ch_dict['11'] = "百货扩展"
eng_ch_dict['12'] = "性别托底"
eng_ch_dict['13'] = "百搭托底"

def zero(x):

    if not x:
        return '0'
    else:
        return x

def url_encode(x):

    return urllib2.quote(x.encode("utf8")).replace('/', "%2F")



def fetch_pid_detail(pid):

    url = product_url % pid 
    content = urllib2.urlopen(url, timeout = 10).read()
    try:
        pid_detail_dict = json.loads(content)
    except Exception, ex:
        pid_detail_dict = {}

    if pid_detail_dict.has_key('error'):
        pid_detail_dict = {}

    return pid_detail_dict


def fetch_ad_data(pos_id, page_no, page_size, perm_id, cids, version, cust_id, brand, page_type, cur_cid):

    url = ad_url % (pos_id, page_no, page_size, perm_id, cids, version, cust_id, brand, page_type, cur_cid)
    print url

    for i in xrange(0, 1):
        try:
            content = urllib2.urlopen(url, timeout = 3).read()
        except Exception, ex:
            print ex
            continue

    try:
        ad_list = json.loads(content)
    except Exception, ex:
        ad_list = []

    return ad_list


def gen_ad_ret(ad_list):

    ad_ret = []
    for each in ad_list:

        pid_info = {}
        pid = each['product_id']

        pid_info['pid'] = pid
        pid_info['img'] = img_url % (pid % 10, pid % 99, pid % 37, pid)
        pid_info['url'] = pid_url % (pid)
        pid_info['bid'] = float(each['cost']) / 100000
        pid_info['strategy'] = eng_ch_dict.get(each['cat_extend'], 'NA')

        pid_detail_dict = fetch_pid_detail(pid)
        pid_info['name'] = pid_detail_dict['product_name'] if 'product_name' in pid_detail_dict else "NA"
        pid_info['category_path'] = pid_detail_dict['category_path'] if 'category_path' in pid_detail_dict else "NA"
        pid_info['category_name'] = pid_detail_dict['category_name'] if 'category_path' in pid_detail_dict else "NA"

        ad_ret.append(pid_info)

    return ad_ret


class MainHandler(tornado.web.RequestHandler):

    def get(self):

        xpos_id = self.get_argument("pos_id", '9')
        xpage_no = self.get_argument("page_no", '0')
        xpage_size = self.get_argument("page_size", '50')
        xperm_id = self.get_argument("perm_id", '')
        xcids = self.get_argument("cids", '')
        xversion = self.get_argument("version", '2')
        xcust_id = self.get_argument("cust_id", '')
        xbrand = url_encode(self.get_argument("brand", ''))
        xpage_type = self.get_argument("page_type", '1')
        xcur_cid = self.get_argument("cur_cid", '')
        xhistory = self.get_argument("history", '1')

        # APP没有3版本
        if xpos_id == "209" and xversion == "3":
            xversion = "1"

        # PC的1版本没有图书页
        if xpos_id == "9" and xversion == "1" and xpage_type == "2":
            xpage_type = "1"

        # APP没有其它页
        if xpos_id == "209" and xpage_type == "0":
            xpage_type = "1"

        # 随机perm_id
        if (not xperm_id) and (not xcust_id):
            xperm_id = random_permid_pro()

        if xhistory == "1":
            xcids = ""
            if xpos_id == '9':                                          # pc端
                tmp_brand_list = get_brand(xperm_id)
                if tmp_brand_list:
                    xbrand = url_encode(','.join(tmp_brand_list))

                if xversion == '1':                                     # 1版
                    tmp_cid_list = get_catid(xcust_id, xperm_id, 5)
                else:                                                   # 2&3版
                    tmp_cid_list = get_catid(xcust_id, xperm_id, 0)

                if tmp_cid_list:
                    xcids = ','.join(tmp_cid_list)

            else:                                                       # APP端
                xbrand = ""
                if xversion == 'xxx':                                   # 最早的1版，已经没了
                    tmp_cid_list = get_behavior(xcust_id, xperm_id, 100)
                else:                                                   # 1&2版
                    tmp_cid_list = get_catid(xcust_id, "", 0)

                if tmp_cid_list:
                    xcids = ','.join(tmp_cid_list)

        ad_list = fetch_ad_data(xpos_id, xpage_no, xpage_size, zero(xperm_id), zero(xcids), xversion, zero(xcust_id), zero(xbrand), xpage_type, zero(xcur_cid))
        ad_ret = gen_ad_ret(ad_list)

        self.render("show_personal_ad.html",
                    pos_id = xpos_id,
                    page_no = xpage_no,
                    page_size = xpage_size,
                    perm_id = xperm_id,
                    cids = xcids,
                    version = xversion,
                    cust_id = xcust_id,
                    brand = urllib2.unquote(xbrand),
                    page_type = xpage_type,
                    cur_cid = xcur_cid,
                    history = xhistory,
                    total_count = len(ad_ret),
                    ret = ad_ret)


def main():

    application = tornado.web.Application([(r"/", MainHandler),])
    application.listen(8919)
    tornado.ioloop.IOLoop.instance().start()


if __name__ == "__main__":

    main()
