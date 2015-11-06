#!/usr/bin/env python
#encoding:utf8


import json
import urllib2
import MySQLdb
import paramiko
import socket
import struct
import time
from config import *
#from fake_front import get_catid, get_brand, get_behavior, random_permid, random_permid_pro


img_url = "http://img3%s.ddimg.cn/%s/%s/%s-1_l.jpg"
pid_url = "http://product.dangdang.com/%s.html"
#product_url = "http://10.4.32.240:8270/product/?pid=%s"
product_url = "http://192.168.197.172/v2/find_products.php?by=product_id&keys=%s&expand=&result_format=json"
search_ad_url = "http://10.255.254.62:8080/getAds/cpc/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/debug"
category_ad_url = "http://10.255.254.62:8080/getAds/cpc/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/-/debug"
#product_ad_url = "http://10.255.255.99:7070/getAds/cpc/%s/%s/%s/%s/184537119/0/0/0/107169205/%s/-/0/%s/0/debug2"
product_ad_url = "http://10.3.255.226:7070/getAds/cpc/%s/%s/%s/%s/184537119/0/0/0/107169205/%s/-/0/%s/0/debug2"
#以下地址回头客测试页面使用
#product_url = "http://10.4.32.240:8270/product/?pid=%s"
personal_product_url = "http://192.168.196.47:8270/product/?pids=%s&fields=product_id,product_name,category_path,category_name&ref=test"
cpath_url = "http://192.168.196.47:8270/category/?category_paths=%s&fields=full_path_name&ref=test"
cid_url = "http://192.168.196.47:8270/category/?category_ids=%s&fields=category_path,full_path_name&ref=test"
#pos_id, page_no, page_size, perm_id, cids, version, cust_id, brand, page_type, cur_cid
ad_url = "http://10.255.254.62:9090/getadspersonal/%s/%s/%s/0/%s/%s/%s/0/%s/%s/%s/%s/debug"
#ad_url = "http://10.255.253.14:8081/getadspersonal/%s/%s/%s/0/%s/%s/%s/0/%s/%s/%s/%s/debug"


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
#各计算模块测试页面方法
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

#def fetch_ad_data(type, pos_id, page_no, page_size, extra, ip, perm_id, history_key, product_history, cust_id, exp_ver, through_id, brand, cpc_pid,cpc_title, is_index):
def fetch_ad_data(**kwargs):
    if kwargs['type'] == 'search':
        extra = urllib2.quote(kwargs['extra'].encode("utf8"))
        url = search_ad_url % (kwargs['pos_id'], kwargs['page_no'], kwargs['page_size'], extra,
                               kwargs['ip'], kwargs['perm_id'], kwargs['history_key'], kwargs['product_history'],
                               kwargs['cust_id'], kwargs['exp_ver'], kwargs['through_id'], kwargs['brand'], kwargs['cpc_pid'],kwargs['cpc_title'])
    elif kwargs['type'] == 'cate':
        url = category_ad_url % (kwargs['pos_id'], kwargs['page_no'], kwargs['page_size'], kwargs['extra'],
                               kwargs['ip'], kwargs['perm_id'], kwargs['history_key'], kwargs['product_history'],
                               kwargs['cust_id'], kwargs['exp_ver'], kwargs['through_id'], kwargs['brand'], kwargs['cpc_pid'],kwargs['cpc_title'],kwargs['is_index'])
    elif kwargs['type'] == 'product':
        url = product_ad_url % (kwargs['pos_id'], kwargs['page_no'], kwargs['page_size'], kwargs['cat_id'], kwargs['version'], kwargs['product_id'])

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
    return url, ad_list

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
        pid_info['category_name'] = pid_detail_dict['category_name'] if 'category_path' in pid_detail_dict else "NA"

        pid_info['ad_name'] = each['ad_name']
        pid_info['debug_info'] = each['DebugAdInfo']
        #print each['DebugAdInfo']
        pid_info['ad_id'] = each['ad_id']
        pid_info['pos'] = each['pos']

        ad_ret.append(pid_info)
    return ad_ret

def gen_product_ad_ret(ad_list):
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

#回头客测试页面方法

eng_ch_dict = {}
eng_ch_dict['1'] = "图书类别-实时"
eng_ch_dict['2'] = "图书类别-历史"
eng_ch_dict['3'] = "百货类别-实时"
eng_ch_dict['4'] = "百货类别-历史"
eng_ch_dict['5'] = "百货品牌-实时"
eng_ch_dict['6'] = "百货品牌-历史"
eng_ch_dict['7'] = "图书扩展"
eng_ch_dict['8'] = "百货扩展"
eng_ch_dict['9'] = "性别托底"
eng_ch_dict['10'] = "默认托底"

def personal_fetch_pid_detail(pid_list):

    pid_str = ','.join(pid_list)
    url = personal_product_url % pid_str

    for i in xrange(0, 3):
        try:
            content = urllib2.urlopen(url, timeout = 3).read()
            pid_detail_dict = json.loads(content)
            break
        except Exception, ex:
            pid_detail_dict = {}

    if "items" not in pid_detail_dict:
        return {}

    return pid_detail_dict['items']


def personal_fetch_category_info(t_url, cat_list):

    cat_str = ','.join(cat_list)
    url = t_url % cat_str

    for i in xrange(0, 3):
        try:
            content = urllib2.urlopen(url, timeout = 3).read()
            category_info_dict = json.loads(content)
            break
        except Exception, ex:
            category_info_dict = {}

    if not category_info_dict.has_key('items'):
        return {}

    return category_info_dict['items']


def personal_fetch_ad_data(pos_id, page_no, page_size, perm_id, cids, version, cust_id, brand, page_type, cur_cid):

    url = ad_url % (pos_id, page_no, page_size, perm_id, cids, version, cust_id, brand, page_type, cur_cid)
    print url

    for i in xrange(0, 3):
        try:
            content = urllib2.urlopen(url, timeout = 3).read()
            break
        except Exception, ex:
            content = "{}"

    try:
        ret_dict = json.loads(content)
    except Exception, ex:
        ret_dict = {}

    return ret_dict


def personal_gen_ad_ret(ad_list):

    pid_list = [str(x['product_id']) for x in ad_list]
    pid_detail_dict = personal_fetch_pid_detail(pid_list)

    cpath_list = [x['category_path'] for x in pid_detail_dict.values()]
    category_info_dict = personal_fetch_category_info(cpath_url, cpath_list)
    #print category_info_dict

    ad_ret = []
    for each in ad_list:

        pid_info = {}
        pid = each['product_id']

        pid_info['pid'] = pid
        pid_info['img'] = img_url % (pid % 10, pid % 99, pid % 37, pid)
        pid_info['url'] = pid_url % (pid)
        pid_info['cost'] = float(each['cost']) / 100000
        pid_info['bid'] = float(each['bid']) / 100000
        pid_info['sim'] = float(each['sim']) / 100
        pid_info['score'] = "%.3f" % (each['score'] * 10)
        pid_info['cid'] = each['cat_id']

        pid_info['related_cid'] = each['related_cid']
        pid_info['cid_weight'] = float(each['cid_weight']) / 100
        pid_info['cid_ratio'] = "%.2f" % each['cid_ratio']

        pid_info['brand'] = each['brand']
        pid_info['brand_weight'] = float(each['brand_weight']) / 100
        pid_info['brand_ratio'] = "%.2f" % each['brand_ratio']

        pid_info['gender_ratio'] = "%.2f" % each['gender_ratio']
        pid_info['strategy'] = eng_ch_dict.get(each['cat_extend'], 'NA')

        pid_detail = pid_detail_dict[str(pid)] if str(pid) in pid_detail_dict else {}
        pid_info['name'] = pid_detail['product_name'] if 'product_name' in pid_detail else "NA"
        pid_info['category_path'] = pid_detail['category_path'] if 'category_path' in pid_detail else "NA"
        pid_info['category_name'] = category_info_dict[pid_info['category_path']]['full_path_name'] \
            if pid_info['category_path'] in category_info_dict else "NA"

        pid_info['phrase_str'] = each['phrase_str']
        pid_info['time_weight'] = each['time_weight']

        ad_ret.append(pid_info)

    return ad_ret

def gen_debug_ret(debug_dict):

    ret_dict = {'cid_weight':[], 'brand_weight':[]}
    if not debug_dict:
        return ret_dict

    cid_list = [str(x[0]) for x in debug_dict['cid_weight']]
    category_info_dict = personal_fetch_category_info(cid_url, cid_list)

    interest_cid_dict = {}
    for each in debug_dict['interest_cid']:
        interest_cid_dict[each[0]] = each[1]

    cid_weight_list = []
    for each in debug_dict['cid_weight']:
        cid = str(each[0])
        cpath = category_info_dict[cid]['category_path'] if cid in category_info_dict else "NA"
        cpath_name = category_info_dict[cid]['full_path_name'] if cid in category_info_dict else "NA"
        weight = each[1] / 100.0
        if each[2] == 0:
            tag = "C2C"
        else:
            tag = "%s/%s/%s" % (each[2] / 100, each[2] % 100 / 10, each[2] % 10)
            if int(cid) in interest_cid_dict:
                tag = "%s(%s)" % (tag, interest_cid_dict[int(cid)] / 100.0)
        cid_weight_list.append([cid, cpath, cpath_name, weight, tag])

    brand_weight_list = []
    for each in debug_dict['brand_weight']:
        brand = each[0]
        weight = each[1] / 100.0
        tag = "%s/%s/%s" % (each[2] / 100, each[2] % 100 / 10, each[2] % 10)
        brand_weight_list.append([brand, weight, tag])

    ret_dict['cid_weight'] = sorted(cid_weight_list, key = lambda x:x[3], reverse = True)
    ret_dict['brand_weight'] = sorted(brand_weight_list, key = lambda x:x[1], reverse = True)

    return ret_dict

#log测试页面方法
def ssh(host, port, user, password, cmd):

    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(host,port,user,password)
    stdin,stdout,stderr = ssh.exec_command(cmd)
    str1 = stdout.readlines()
    return str1

def getdb(id,ip):

    try:
         conn = MySQLdb.connect(host=dbhost, user=dbuser, passwd=dbpasswd, db=db, charset=dbcharset)
    except:
        print("连接数据库错误")

    cur = conn.cursor()

    clicktime = time.time()
    if (id == "" and ip ==""):
        sql = "select * from ad_log_cpc where click_time between %s and %s" % (clicktime - 300, clicktime)
    elif (id == "" and ip !=""):
        sql = "select * from ad_log_cpc where ip=%s and click_time between %s and %s" % (ip, clicktime - 300, clicktime)
    elif (id != "" and ip ==""):
        sql = "select * from ad_log_cpc where ad_info_id=%s and click_time between %s and %s" % (id, clicktime - 300, clicktime)
    else:
        sql = "select * from ad_log_cpc where ad_info_id=%s and ip=%s and click_time between %s and %s" % (id, ip, clicktime - 300, clicktime)
    cur.execute(sql)
    ret = cur.fetchall()
    cur.close()
    conn.commit()
    conn.close()
    return ret

def convert_time(timeStamp):
    timeArray = time.localtime(timeStamp)
    mytime = time.strftime("%Y-%m-%d %H:%M:%S",timeArray)
    return mytime


def log_gen_ad_ret(ad_list):

    ad_ret = []
    for each in ad_list:
        click_info = {}
        click_info['ad_info_id'] = each[1]
        clicktime = convert_time(each[2])
        click_info['click_time'] = clicktime
        ip = socket.inet_ntoa(struct.pack('I',socket.htonl(int(each[3]))))
        click_info['pip'] = ip
        if (each[4] == ""):
            click_info['keyword'] = "无"
        else:
            click_info['keyword'] = each[4]
        click_info['cost'] = int(each[6])/10000
        if (each[7] == 0):
            click_info['status'] = "正常"
        elif (each[7] == 1):
            click_info['status'] = "扣费超过3次"
        elif (each[7] == -1):
            click_info['status'] = "失败"
        click_info['pos_id'] = each[9]
        ad_ret.append(click_info)
    return ad_ret
