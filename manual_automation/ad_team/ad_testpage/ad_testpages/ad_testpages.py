#!/usr/bin/env python
#encoding:utf8
import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
import os
from ftornado import *
import urllib2
from tornado.options import define, options
from config import *
define("port", default=8001, help="run on the given port", type=int)
from fake_front import get_catid, get_brand,  random_permid, random_key

class Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/", MainHandler),
            (r"/search/", SearchHandler),
            (r"/category/", CategoryHandler),
            (r"/product/", ProductHandler),
            (r"/personal/", PersonalHandler),
            (r"/clicklog/", ClickLogHandler),
            (r"/showlog/", ShowLogHandler),
        ]
        settings = dict(
            template_path=os.path.join(os.path.dirname(__file__), "templates"),
            static_path=os.path.join(os.path.dirname(__file__), "static"),
            debug=True,
        )
        tornado.web.Application.__init__(self, handlers, **settings)


class MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.render(
            "index.html",
            page_title="Ad",
            header_text="广告测试页面",
        )


class SearchHandler(tornado.web.RequestHandler):
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
        request_url, ad_list = fetch_ad_data(type='search', pos_id=xpos_id, page_no=xpage_no, page_size=xpage_size, extra=xextra,
                                             ip=xip, perm_id=xperm_id, history_key=zero(xhistory_key), product_history=zero(xproduct_history),
                                             cust_id=zero(xcust_id), exp_ver=xexp_ver, through_id=zero(xthrough_id), brand=zero(xbrand),
                                             cpc_pid=zero(xcpc_pid), cpc_title=zero(xcpc_title))
        ad_ret = gen_ad_ret(ad_list)
        self.render("show_search_ad.html", pos_id=xpos_id, page_no=xpage_no, page_size=xpage_size, extra=xextra, ip=xip,
                    perm_id=xperm_id, history_key=xhistory_key, product_history=xproduct_history, cust_id=xcust_id, exp_ver=xexp_ver,
                    through_id=xthrough_id, brand=urllib2.unquote(xbrand), cpc_pid=xcpc_pid, cpc_title=xcpc_title, total_count=len(ad_ret),
                    ret=ad_ret, url=request_url)


class CategoryHandler(tornado.web.RequestHandler):
    def get(self):
        xpos_id = self.get_argument("pos_id", '1')
        xpage_no = self.get_argument("page_no", '0')
        xpage_size = self.get_argument("page_size", '20')
        xextra = self.get_argument("extra", '4002378')
        xip = self.get_argument("ip", '3232262672')
        xperm_id = self.get_argument("perm_id", '20141022092715613302051844096892271')
        xhistory_key = self.get_argument("history_key", '')
        xproduct_history = self.get_argument("product_history", '')
        xcust_id = self.get_argument("cust_id", '')
        xexp_ver = self.get_argument("exp_ver", '1')
        xthrough_id = self.get_argument("through_id", '')
        xbrand = self.get_argument("brand", '')
        xcpc_pid = self.get_argument("cpc_pid", '')
        xcpc_title = self.get_argument("cpc_title", '')
        xis_index = self.get_argument("is_index", '1')

        request_url, ad_list = fetch_ad_data(type='cate', pos_id=xpos_id, page_no=xpage_no, page_size=xpage_size, extra=xextra,
                                             ip=xip, perm_id=xperm_id, history_key=zero(xhistory_key), product_history=zero(xproduct_history),
                                             cust_id=zero(xcust_id), exp_ver=xexp_ver, through_id=zero(xthrough_id), brand=zero(xbrand),
                                             cpc_pid=zero(xcpc_pid), cpc_title=zero(xcpc_title), is_index=xis_index)
        ad_ret = gen_ad_ret(ad_list)
        self.render("show_category_ad.html",
                    pos_id=xpos_id, page_no=xpage_no, page_size=xpage_size, extra=xextra, ip=xip, perm_id=xperm_id,
                    history_key=xhistory_key, product_history=xproduct_history, cust_id=xcust_id, exp_ver=xexp_ver,
                    through_id=xthrough_id, brand=urllib2.unquote(xbrand), cpc_pid=xcpc_pid, cpc_title=xcpc_title,
                    total_count=len(ad_ret), is_index=xis_index, url=request_url, ret=ad_ret,)


class ProductHandler(tornado.web.RequestHandler):
    def get(self):
        xpos_id = self.get_argument("pos_id", '1')
        xpage_no = self.get_argument("page_no", '0')
        xpage_size = self.get_argument("page_size", '4')
        xversion = self.get_argument("version", '2')
        xproduct_id = self.get_argument("product_id", '1031914720')

        pid_detail_dict = fetch_pid_detail(xproduct_id)
        cat_id = pid_detail_dict['category_id']
        request_url, ad_list = fetch_ad_data(type='product', pos_id=xpos_id, page_no=xpage_no, page_size=xpage_size, cat_id=cat_id, version=xversion, product_id=xproduct_id,)
        ad_ret = gen_product_ad_ret(ad_list)

        self.render("show_product_ad.html",
                    pos_id=xpos_id, page_no=xpage_no, page_size=xpage_size, cat_id=cat_id, version=xversion,
                    product_id=xproduct_id, total_count=len(ad_ret), ret=ad_ret, url=request_url)

class PersonalHandler(tornado.web.RequestHandler):
    def get(self):

        xpos_id = self.get_argument("pos_id", '9')
        xpage_no = self.get_argument("page_no", '0')
        xpage_size = self.get_argument("page_size", '50')
        xperm_id = self.get_argument("perm_id", '')
        #xcids = self.get_argument("cids", '')
        xcids = ''
        xversion = self.get_argument("version", '1')
        xcust_id = self.get_argument("cust_id", '')
        #xbrand = url_encode(self.get_argument("brand", ''))
        xbrand = ''
        xpage_type = self.get_argument("page_type", '1')
        xcur_cid = self.get_argument("cur_cid", '')
        #xhistory = self.get_argument("history", '1')
        xhistory = '1'

        # APP没有其它页
        if xpos_id == "209" and xpage_type == "0":
            xpage_type = "1"

        # 历史数据已经没用了
        xcids = ""
        xbrand = ""

        # 随机perm_id
        if (not xperm_id) and (not xcust_id):
            xcust_id, xperm_id = random_key()

        ret_dict = personal_fetch_ad_data(xpos_id, xpage_no, xpage_size, zero(xperm_id), zero(xcids), xversion, zero(xcust_id), zero(xbrand), xpage_type, zero(xcur_cid))
        ad_list = ret_dict.get("data", [])
        ad_ret = personal_gen_ad_ret(ad_list)

        debug_dict = ret_dict.get("debug", {})
        debug_ret = gen_debug_ret(debug_dict)

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
                    ret = ad_ret,
                    debug = debug_ret)


class ClickLogHandler(tornado.web.RequestHandler):
     def get(self):
        x_btn = self.get_argument("btn", "1")
        x_adid = self.get_argument("ad_id", '')
        x_perid = self.get_argument("per_id", '')
        x_pip = self.get_argument("p_ip", '')
        list = []
        if (x_pip!=""):
            try:
                ipint = socket.ntohl(struct.unpack("i",socket.inet_aton(x_pip))[0])
            except:
                ipint = 111
        else:
            ipint = ""

        if x_btn == '1':
            cmd = "rm -rf /data/adsmart/log/*"
            ssh(host, port, user, passwd, cmd)
            self.render("click_info.html",
                        ad_id = "",
                        per_id = "",
                        p_ip = "",
                        show_log = "",
                        ret = [])

        if x_btn == '2':
            time.sleep(10)
            result = getdb(x_adid,ipint)
            ad_ret = gen_ad_ret(result)
            tm = time.strftime("%Y%m%d",time.localtime(time.time()))
            if (x_adid!="" and x_perid!="" and ipint!=""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s|grep %s|grep %s" % (tm, x_adid, x_perid, ipint)
            elif (x_adid=="" and x_perid!="" and ipint!=""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s|grep %s" % (tm, x_perid, ipint)
            elif (x_adid!="" and x_perid=="" and ipint!=""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s|grep %s" % (tm, x_adid, ipint)
            elif (x_adid!="" and x_perid!="" and ipint==""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s|grep %s" % (tm, x_adid, x_perid)
            elif (x_adid=="" and x_perid=="" and ipint!=""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s" % (tm, ipint)
            elif (x_adid=="" and x_perid!="" and ipint==""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s" % (tm, x_perid)
            elif (x_adid!="" and x_perid=="" and ipint==""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s" % (tm, x_adid)
            else:
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click" % (tm)

            x_log = ssh(host, port, user, passwd, cmd)
            for i in range(0, x_log.__len__()):
                zidian = {}
                str_1 = x_log[i].split("	")

                mytime = convert_time(float(str_1[0]))
                zidian["lclick_time"] = mytime
                ipint = socket.inet_ntoa(struct.pack('I',socket.htonl(int(str_1[1]))))
                zidian["lip"] = ipint
                zidian["lpermanent_id"] = str_1[3]
                zidian["ladid"] = str_1[4]
                zidian["lcost"] = int(str_1[5])/10000
                print zidian["lcost"]
                zidian["lthroughid"] = str_1[14]
                zidian["lurl"] = str_1[16]
                list.append(zidian)

            self.render("click_info.html",
                         ad_id = x_adid,
                         per_id = x_perid,
                         p_ip = x_pip,
                         show_log = list,
                         ret = ad_ret)


class ShowLogHandler(tornado.web.RequestHandler):
    def get(self):
        x_btn = self.get_argument("btn", "1")
        x_perid = self.get_argument("per_id", '')
        x_posid = self.get_argument("pos_id", '')
        list = []
        ad_id_list = []

        if x_btn == '1':
            cmd = "echo > /data/adsmart/joblog/lazy_logs/ad_log"
            ssh(host, port, user, passwd, cmd)
            self.render("show_log_ad.html",
                        per_id = "",
                        pos_id = "",
                        show_log = "",
                        ad_id_list = [])

        if x_btn == '2':
            if x_perid != "" and x_posid !="":
                cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log | grep permanent_id=%s | grep pos=%s" % (x_perid, x_posid)
            elif x_perid == "" and x_posid != "":
                cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log | grep pos=%s" % x_posid
            elif x_posid == "" and x_perid != "":
                cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log | grep permanent_id=%s" % x_perid
                # cmd = "cat /d1/adsmart/joblog/lazy_logs/ad_log | grep permanent_id=%s" % x_perid
            elif x_posid == "" and x_perid == "":
                cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log"
            print cmd
            x_log = ssh(host, port, user, passwd, cmd)

            if cmd == "cat /data/adsmart/joblog/lazy_logs/ad_log":
                g = 1
            else:
                g = 0

            for i in range(g, x_log.__len__()):
                zidian = {}
                str_1 = x_log[i].split(" ")
                if len(str_1) == 4:
                    zidian["time"]=str_1[0]
                    zidian["url"]=str_1[3]
                    b = str_1[2].split("&")
                if len(str_1) == 5:
                    zidian["time"]=str_1[0]
                    zidian["url"]=str_1[5]
                    b = str_1[3].split("&")
                for i in b:
                    x = i.split("=")
                    zidian[x[0]]=x[1]

                zidian['ip'] = socket.inet_ntoa(struct.pack('I',socket.htonl(int(zidian['ip']))))

                try:
                    ids = zidian['ad_id'].split(",")

                    for j in ids:
                        if j == "":
                            break
                        # y = j[2:]
                        ad_id_list.append(j)
                except:
                    # y = (zidian['ad_id'])[2:]
                    ad_id_list.append(zidian['ad_id'])

                list.append(zidian)

            for i in range(0, len(ad_id_list)):
                str1 = (u" |第%s名:" % int(i+1)) + str(ad_id_list[i])
                ad_id_list[i] = str1

            self.render("show_log_ad.html",
                        per_id = x_perid,
                        pos_id = x_posid,
                        show_log = list,
                        ad_id_list = ad_id_list)


if __name__ == "__main__":
    tornado.options.parse_command_line()
    http_server = tornado.httpserver.HTTPServer(Application())
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.instance().start()