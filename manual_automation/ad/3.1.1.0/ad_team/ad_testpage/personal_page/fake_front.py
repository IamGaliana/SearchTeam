#!/usr/bin/env python
#encoding:utf8

import redis
import json
import urllib
import urllib2

r_product = redis.Redis(host = "192.168.197.143", port = 6379, db = 0)
r_prefer = redis.Redis(host = "192.168.197.143", port = 6379, db = 2)
behavior_url = "http://rtuuserapi.idc4:8709/?cust_id=%s&perm_id=%s&fields=behavior"


def random_permid(loop_cnt = 10):

    perm_id = ""
    for i in range(0, loop_cnt):
        ret = r_prefer.randomkey()
        if ret.startswith("p_"):
            perm_id = ret[2:]
            break

    return perm_id


def random_permid_pro(loop_cnt = 10):

    perm_id = ""
    for i in range(0, loop_cnt):
         ret = r_prefer.randomkey()
         if ret.startswith("p_"):
             perm_id = ret[2:]
             if(get_behavior("", perm_id, 1)):
                 break

    return perm_id


def pid_2_catid(pid_list):

    if not pid_list:
        return []

    ret = r_product.mget(["c%s" % x for x in pid_list])
    if ret:
        return [ x for x in ret if x != None ]

    return []


def get_behavior(cust_id, perm_id, max_cnt):

    catid_list = []
    catid_set = set()
    pid_list = []

    url = behavior_url % (cust_id, perm_id)
    content = urllib2.urlopen(url, timeout = 3).read()
    behavior_dict = json.loads(content)
    if behavior_dict.has_key('status') and behavior_dict['status'] == -1: 
        return catid_list

    cnt = 0
    for session in behavior_dict["behavior"]:
        for i in range(1, len(session)):
            if cnt >= max_cnt:
                break

            if session[i][0] != "p":
                continue

            pid = session[i][3]
            pid_list.append(pid)
            cnt += 1

    tmp_list = pid_2_catid(pid_list)
    for catid in tmp_list:
        if catid in catid_set:
            continue

        catid_list.append(catid)
        catid_set.add(catid)

    return catid_list


def get_catid(cust_id, perm_id, behavior_cnt = 5):

    tmp_list = []
    catid_list = []
    catid_set = set()

    if cust_id:
        ret = r_prefer.get("c_%s" % cust_id)
        if ret:
            tmp_list.extend(json.loads(ret))

    if perm_id:
        ret = r_prefer.get("p_%s" % perm_id)
        if ret:
            tmp_dict = json.loads(ret)
            for each in tmp_dict["cat_id"]:
                tmp_list.append(each[0])

    if cust_id or perm_id:
         tmp_list.extend(get_behavior(cust_id, perm_id, behavior_cnt))

    for catid in tmp_list:
        if catid in catid_set:
            continue

        catid_list.append(catid)
        catid_set.add(catid)

    return catid_list[:10]


def get_brand(perm_id):

    brand_list = []
    if perm_id:
        ret = r_prefer.get("p_%s" % perm_id)
        if ret:
            tmp_dict = json.loads(ret)
            for each in tmp_dict["brand"]:
                brand_list.append(each[0])

    return brand_list


if __name__ == "__main__":

     print get_catid("3994893", "20140723110234168409127786977201557")
     print get_brand("20140723110234168409127786977201557")
