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