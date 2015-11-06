#!/usr/bin/env python
#encoding:utf8

import tornado.ioloop
import tornado.web
import json
import urllib2

img_url = "http://img3%s.ddimg.cn/%s/%s/%s-1_l.jpg"
pid_url = "http://product.dangdang.com/%s.html"
#product_url = "http://10.4.32.240:8270/product/?pid=%s"
product_url = "http://192.168.197.172/v2/find_products.php?by=product_id&keys=%s&expand=&result_format=json"
ad_url = "http://10.255.255.99:7070/getAds/cpc/%s/%s/%s/%s/184537119/0/0/0/107169205/%s/-/0/%s/0/debug2"

def zero(x):

    if not x:
        return '0'
    else:
        return x

def url_encode(x):

    return urllib2.quote(x.encode("utf8")).replace('/', "%2F")

#def get_cids(xproduct_id):
#    return xcat_id

def fetch_pid_detail(pid):

    url = product_url % pid
    print url
    content = urllib2.urlopen(url, timeout = 10).read()
    try:
        info = json.loads(content)
        pid_detail_dict = info["products"][pid]["core"]
    except Exception, ex:
        pid_detail_dict = {}

    if pid_detail_dict.has_key('error'):
        pid_detail_dict = {}

    return pid_detail_dict


def fetch_ad_data(pos_id, page_no, page_size, cat_id, version, product_id):

    url = ad_url % (pos_id, page_no, page_size, cat_id, version, product_id)
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
    try:
        debug = ad_list['debug']['items']
        results = ad_list['results']
    except Exception, ex:
        debug = []
        results = []

    ad_ret = []
    for each in results:

        pid_info = {}
        pid = each['product_id']
        pid_info['ad_id'] = each['ad_id']
        pid_info['pid'] = pid
        pid_info['url'] = pid_url % (pid)
        pid_info['img'] = img_url % (pid % 10, pid % 99, pid % 37, pid)
        pid_info['pos'] = each['pos']
        pid_info['cat_id'] = each['cat_id']
        pid_info['prom_type'] = each['prom_type']
        pid_info['ad_name'] = each['ad_name']


        for i in range(0, debug.__len__()):
            if debug[i]['product_id'] == each['product_id']:
                pid_info['ctr'] = debug[i]['ctr']
                pid_info['related_type'] = debug[i]['related_type']
                pid_info['sim'] = debug[i]['sim']
                pid_info['score'] = debug[i]['score']

        #pid_info['strategy'] = eng_ch_dict.get(each['cat_extend'], 'NA')
        pid_detail_dict = fetch_pid_detail(str(pid))
        pid_info['name'] = pid_detail_dict['product_name'] if 'product_name' in pid_detail_dict else "NA"
        pid_info['category_path'] = pid_detail_dict['category_path'] if 'category_path' in pid_detail_dict else "NA"
        pid_info['category_name'] = pid_detail_dict['category_name'] if 'category_path' in pid_detail_dict else "NA"

        ad_ret.append(pid_info)

    return ad_ret


class MainHandler(tornado.web.RequestHandler):

    def get(self):
        xpos_id = self.get_argument("pos_id", '1')
        xpage_no = self.get_argument("page_no", '0')
        xpage_size = self.get_argument("page_size", '4')
        xversion = self.get_argument("version", '2')
        xproduct_id = self.get_argument("product_id", '1031914720')

        pid_detail_dict = fetch_pid_detail(xproduct_id)
        cat_id = pid_detail_dict['category_id']
        print "pos_id:", xpos_id, "page:", xpage_no, "page_size:", xpage_size, "cat_id:", cat_id, "version:", xversion, "product_id:", xproduct_id
        ad_list = fetch_ad_data(xpos_id, xpage_no, xpage_size, cat_id, xversion, zero(xproduct_id),)
        ad_ret = gen_ad_ret(ad_list)

        self.render("show_product_ad.html",
                    pos_id = xpos_id,
                    page_no = xpage_no,
                    page_size = xpage_size,
                    cat_id = cat_id,
                    version = xversion,
                    product_id = xproduct_id,
                    total_count = len(ad_ret),
                    ret = ad_ret)

def main():

    application = tornado.web.Application([(r"/", MainHandler),])
    application.listen(8911)
    tornado.ioloop.IOLoop.instance().start()

if __name__ == "__main__":

    main()
