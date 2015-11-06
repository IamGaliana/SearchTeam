# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

import urllib2
import json
import re

#                   _ooOoo_
#                  o8888888o
#                  88" . "88
#                  (| -_- |)
#                  O\  =  /O
#               ____/`---'\____
#             .'  \\|     |//  `.
#            /  \\|||  :  |||//  \
#           /  _||||| -:- |||||-  \
#           |   | \\\  -  /// |   |
#           | \_|  ''\---/''  |   |
#           \  .-\__  `-`  ___/-. /
#         ___`. .'  /--.--\  `. . __
#      ."" '<  `.___\_<|>_/___.'  >'"".
#     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
#     \  \ `-.   \_ __\ /__ _/   .-` /  /
#======`-.____`-.___\_____/___.-`____.-'======
#                   `=---='
#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
#             佛祖保佑       永远顺利

ad_calc_product_url = "http://10.255.255.99:7070/getAds/cpc/%s/%s/%s/%s/184537119/0/0/0/107169205/%s/-/0/%s/0/debug2"
product_url = "http://192.168.197.172/v2/find_products.php?by=product_id&keys=%s&expand=&result_format=json"
get_cid_in_son_list_url = "http://192.168.197.172/v2/find_categories.php?for=subpath_is_son&category_id=%s&category_path=&level=&result_format=json&fields=category_id,category_path&subpath=1&is_son=0"
ad_calc_category_url = "http://10.255.254.62:8080/getAds/cpc/1/0/50/%s/3232262672/20141022092715613302051844096892271/0/0/0/1/%s/0/0/0/0/debug"
'''
by xiyucheng
通过单品API获得单品的分类id信息
输入： pid
输出： cid str类型。
'''
def get_product_info(pid):

    url = product_url % pid
    #print "The product info url is", url
    print u"获取单品信息的url是：", url
    content = urllib2.urlopen(url, timeout = 1).read()
    try:
        pid_detail_dict = json.loads(content)
        cid = pid_detail_dict["products"][pid]["core"]
    except Exception, ex:
        pid_detail_dict = {}

    if pid_detail_dict.has_key('error'):
        cid = {}
    return cid

'''
by xiyuheng
获取计算模块请求链接的返回数据
输入： 请求计算模块的url
输出： 字典list
'''
def response_ad_calc_server(url):
    #print u"请求计算模块的请求链接是：http://192.168.197.172/v2/find_categories.php?for=subpath_is_son&category_id=分类id&category_path=&level=&result_format=json&fields=category_id,category_path&subpath=1&is_son=0"

    for i in xrange(0, 5):
        try:
            content = urllib2.urlopen(url, timeout = 1).read()
        except Exception, ex:
            print "ex:", ex, Exception
            import time
            time.sleep(1)
            continue
        break
    try:
        ad_list = json.loads(content)
    except Exception, ex:
        ad_list = []
    return ad_list

'''
by xiyuheng
通过输入的参数，请求计算模块，解析返回的数据，将需要的值存储在字典组成的list中。
输入： 单品id， related_type值， 版本
输出： 字典list
'''
def result_for_product_related_type(pid, related_type, version):
    try:
        cid = get_product_info(pid)['category_id']
    except Exception, ex:
        cid = -1
        print "product_id error"
    url = ad_calc_product_url % ("1", "0", "10", cid, version, pid)
    print u"请求计算模块的链接：", url
    print u"输入的pid所属的cid:", cid

    ad_list = response_ad_calc_server(url)
    try:
        debug = ad_list['debug']['items']
        results = ad_list['results']
    except Exception, ex:
        debug = []
        results = []

    ll=[]
    print u"一共返回了", len(debug), u"个广告"
    if len(debug) != 0:
        for p in range(0, len(debug)):
            #print results[p]
            zidian = {}
            if debug[p]["related_type"]==related_type:
                zidian["product_id"] = debug[p]["product_id"]
                zidian["cat_id"] = debug[p]["cateid"]
                zidian["ad_id"] = debug[p]["ad_id"]
                ll.append(zidian)
    print u"其中有", len(ll), u"个related_type=", related_type, u"的广告返回。"
    return ll

'''
by xiyuheng
通过输入单品id，获取单品id所属的cid，请求计算模块，解析返回的数据，取得submit的信息。submit信息为该cid的扩展cid集合。
输入： 单品id
输出： cid组成的list
'''
def get_related_cid(pid):
    try:
        cid_in = get_product_info(pid)['category_id']
        url = ad_calc_product_url % ("1", "0", "10", cid_in, 3, pid)
        ad_list = response_ad_calc_server(url)
        cid_out_list = ad_list['debug']['submit'].keys()
        #print ad_list['debug']['submit'].keys()
    except Exception, ex:
        cid_out_list = []
    return cid_out_list

'''
by xiyuheng
通过单品API获取分类id的所有儿子类的集合
输入：分类id的集合。list类型
输出：所有分类id的儿子类的集合
'''
def get_cid_out_son_list(cid_in_list):
    lis_name = []
    #print u"获取儿子类信息的url是："
    for i in range(0, len(cid_in_list)):
        url = get_cid_in_son_list_url % cid_in_list[i]
        content = urllib2.urlopen(url, timeout = 10).read()
        try:
            json_content = json.loads(content)
            cid_son_list = json_content["categorys"]
        except Exception, ex:
            cid_son_list = {}

        #lis_name = "list_"+str(i)

        for i in range(0, len(cid_son_list)):
            lis_name.append(cid_son_list[i]['category_id'])
    return lis_name

'''
by xiyuheng
通过输入的参数，请求计算模块，解析返回的数据，将需要的值存储在字典组成的list中。
输入： 分类id， related_type值， 版本
输出： 字典list
'''
def result_for_category_related_type(cid, related_type, version):
    url = ad_calc_category_url % cid, version
    ad_list = response_ad_calc_server(url)
    result = ad_list['data']
    ll = []
    for p in range(0, len(result)):
            #print results[p]
            zidian = {}
            if result[p]["related_type"]==related_type:
                zidian["product_id"] = result[p]["product_id"]
                zidian["cat_id"] = result[p]["cateid"]
                zidian["ad_id"] = result[p]["ad_id"]
                ll.append(zidian)
    return ll

#cidli = ['4008157']
#print get_cid_out_son_list(cidli)


'''
by xiyuheng
从debuginfo中提取bid和ctr值，返回组合字符串
输入：json格式的list
输出：bid+ctr组成的字符串
'''
def bid_ctr_return_for_search(debug_info):
    items = debug_info.strip().split(" ")
    #print items
    for item in items:
        tokens = item.split(":")
        if tokens[0]=="bid":
            bid = tokens[1]
        elif tokens[0] == "ctr":
            ctr =tokens[1]
    return bid+ctr

def bid_akscore_return_for_search(debug_info):
    items = debug_info.strip().split(" ")
    #print items
    for item in items:
        tokens = item.split(":")
        if tokens[0]=="bid":
            bid = tokens[1]
        elif tokens[0] == "t_akscore":
            t_akscore =tokens[1]
    return int(bid) * int(t_akscore)

def match_bid_ctr_return_for_search(debug_info):
    items = debug_info.strip().split(" ")
    #print items
    bid_pattern = re.compile(r"bid:(\d*)")
    ctr_pattern = re.compile(r"ctr:(\d*)")
    match_pattern = re.compile(r"match:(\d*)")
    bid_find = ''.join(bid_pattern.findall(''.join(items)))
    ctr_find = ''.join(ctr_pattern.findall(''.join(items)))
    match_find = ''.join(match_pattern.findall(''.join(items)))
    return match_find + bid_find + ctr_find
'''
by xiyuheng
处理存储ad_id，debug_info, bid_dtr的
字典组成的list, 返回存储ad_id的setlist
输入：字典组成的list
输出：set格式数据组成的list
'''
def get_ad_id_set_list_by_bidctr(lists):
    last_bid_ctr = ''
    bid_ctr = ''
    set_list = []

    for i in range(0, len(lists)):
        bid_ctr = lists[i]['bid_ctr']
        if last_bid_ctr != bid_ctr:
            x = set()
            x.add(str(lists[i]['ad_id']))
            last_bid_ctr = bid_ctr
            set_list.append(x)
        elif last_bid_ctr == bid_ctr:
            set_list[len(set_list)-1].add(str(lists[i]['ad_id']))
    return set_list

def matchbidctrget_ad_id_set_list(lists):
    last_match_bid_ctr = ''
    bid_ctr = ''
    set_list = []

    for i in range(0, len(lists)):
        match_bid_ctr = lists[i]['match_bid_ctr']
        #print bid_ctr
        if last_match_bid_ctr != match_bid_ctr:
            x = set()
            x.add(str(lists[i]['ad_id']))
            last_match_bid_ctr = match_bid_ctr
            set_list.append(x)
        elif last_match_bid_ctr == match_bid_ctr:
            set_list[len(set_list)-1].add(str(lists[i]['ad_id']))
    return set_list

'''
##########################################
#  by xiyuheng                           #
#  读取搜索计算模块，返回存有ad_id，debug_info
#  bid_dtr的字典组成的list，和ad_id的set
#  list 和本次请求的DebugQueryPath信息     #
##########################################
'''
def run_for_search_category(url):
    try:
        response = response_ad_calc_server(url)
        result = response['data']
    except Exception, ex:
        result = []
    try:
        DebugQueryPath = result[0]['DebugQueryPath']
    except Exception, ex:
        DebugQueryPath = ''
    list = []
    for i in range(0, len(result)):
        zidian = {}
        extra_json = result[i]['extra_json']
        product_id = result[i]["product_id"]
        zidian["product_id"] = product_id
        debug_info = result[i]['DebugAdInfo']
        search_type = str(result[i]['DebugAdInfo'].strip().split(" ")[0].split(":")[1])
        score = str(result[i]['DebugAdInfo'].strip().split(" ")[5])
        t_akscore = result[i]['DebugAdInfo'].strip().split(" ")[10].split(":")[1]
        zidian["ad_id"] = result[i]['ad_id']
        zidian["debug_info"] = debug_info
        bid_ctr = bid_ctr_return_for_search(debug_info)
        zidian["bid_ctr"] = bid_ctr
        match_bid_ctr = match_bid_ctr_return_for_search(debug_info)
        zidian["match_bid_ctr"] = match_bid_ctr
        zidian["score"] = score
        zidian["search_type"] = search_type
        zidian['extra_json'] = extra_json
        zidian["t_akscore"] = t_akscore
        zidian["bid*akscore"] = bid_akscore_return_for_search(debug_info)
        list.append(zidian)
    return list, DebugQueryPath

##################################################################################################
'''
##########################################
#  by xiyuheng                           #
#  处理存储ad_id，debug_info, score
#  字典组成的list, 返回存储ad_id的setlist  #
##########################################
'''
def get_ad_id_set_list_by_score(lists):
    last_score = ''
    score = ''
    set_list = []
    for i in range(1, len(lists)):
        if lists[0]['ad_id'] == lists[i]['ad_id']:
            lists[i:len(lists)] = []
            break
    for i in range(0, len(lists)):
        score = lists[i]['score']
        if last_score != score:
            x = set()
            x.add(str(lists[i]['ad_id']))
            last_score = score
            set_list.append(x)
        elif last_score == score:
            set_list[len(set_list)-1].add(str(lists[i]['ad_id']))
    return set_list

'''
##########################################
#  by xiyuheng                           #
#  处理存储ad_id，debug_info, bid*ak_score
#  字典组成的list, 返回存储ad_id的setlist  #
##########################################
'''
def get_ad_id_set_list_by_bidakscore(lists):
    last_bidakscore = ''
    bidakscore = ''
    set_list = []
    for i in range(1, len(lists)):
        if lists[0]['ad_id'] == lists[i]['ad_id']:
            lists[i:len(lists)] = []
            break
    for i in range(0, len(lists)):
        bidakscore = lists[i]['bid*akscore']
        if last_bidakscore != bidakscore:
            x = set()
            x.add(str(lists[i]['ad_id']))
            last_bidakscore = bidakscore
            set_list.append(x)
        elif last_bidakscore == bidakscore:
            set_list[len(set_list)-1].add(str(lists[i]['ad_id']))
    return set_list

'''
##########################################
#  by xiyuheng                           #
#  按条件对数据字典组成的list做数据过滤      #
##########################################
'''
def data_filter(lists, column, value):
    results = filter(lambda each: value == each[column], lists)
    return results

'''
##########################################
#  by xiyuheng                           #
#  读取单品计算模块，返回广告数据字典        #
##########################################
'''
def run_for_product_personal(url):
    try:
        result = response_ad_calc_server(url)
        #result = response['results']
    except Exception, ex:
        result = []

    return result

'''
##########################################
#  by xiyuheng                           #
#  读取query静态文件。 返回字典格式数据      #
##########################################
'''
def key_dict(file_path):
    kv_dict = {}
    fi_handler = open(file_path, 'r')
    for line in fi_handler:
        fields = line.strip().split('\t')
        k = fields[0]
        v = '\t'.join(fields[1:])
        kv_dict[k] = v
    fi_handler.close()
    return kv_dict
#黄金展位ab版测试用
def request_goldab(url):
    ad_str = ''
    for i in xrange(0, 1):
        try:
            ad_str = urllib2.urlopen(url, timeout=1).read()
            ad_str = json.loads(ad_str, encoding='utf8')

        except Exception, ex:
            print ex
            continue
    print "*************"
    print ad_str, type(ad_str)
    return ad_str

def goldab_filte_keyword(query_list, kv_dict):
    new_query_list = []
    for i in query_list:
        i = i.encode('utf8')
        try:
            value = kv_dict[i]
        except Exception:
            value = ""
        if value.startswith("58"):
            new_query_list.append(i.replace('/', '\/'))
    return new_query_list

def goldab_get_values(ad_data):
            count = 0
            searchtype_find = []
            m_oriscore_find = []
            ad_id = []
            ad_id_all = []
            gold_search_type_all = []
            gold_search_type = []
            for i in ad_data:
                ad_id_all.append(i['ad_id'])
                gold_search_type_all.append(i['gold_search_type'])

            for i in range(len(ad_data)):
                count += 1
                if count <= 4:
                    searchtype_pattern = re.compile(r"searchtype:(-?\d)")
                    searchtype_find.append(''.join(searchtype_pattern.findall(ad_data[i]['DebugAdInfo'])))
                    #print "searchtype_find:", searchtype_find
                    m_oriscore_pattern = re.compile(r"m_oriscore:(\d*)")
                    m_oriscore_find.append(''.join(m_oriscore_pattern.findall(ad_data[i]['DebugAdInfo'])))
                    #print "m_oriscore_find:", m_oriscore_find
                    ad_id.append(ad_data[i]['ad_id'])
                    #print ad_id
                    gold_search_type.append(ad_data[i]['gold_search_type'])
                    #print "gold_search_type:", gold_search_type
            return searchtype_find, m_oriscore_find, ad_id, gold_search_type, ad_id_all, gold_search_type_all

#URL_OLD = "http://10.255.254.62:8080/getAds/cpc/0/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
##URL_NEW = "http://10.255.254.62:8080/getAds/cpc/0/0/100/%s/184537119/0/0/0/107169205/%s/-/0/0/0/debug"
##
#version = '1'
#old_url = URL_OLD % (urllib2.quote("sql"), version)
##new_url = URL_NEW % (urllib2.quote("衬衫"), version)
##
#old_result = run_for_search(old_url)[0]
#debug_query = run_for_search(old_url)[1]
##guolvlist = data_filter(old_result, 'search_type', '1')
#
#list = get_ad_id_set_list_by_bidctr(old_result)
#print list

