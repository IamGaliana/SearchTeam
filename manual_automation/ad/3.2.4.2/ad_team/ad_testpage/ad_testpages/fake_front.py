#!/usr/bin/env python
#encoding:utf8

import redis
import json
import urllib
import urllib2

r_product = redis.Redis(host = "192.168.197.143", port = 6379, db = 0)
r_prefer = redis.Redis(host = "192.168.197.143", port = 6379, db = 2)
r_interest = redis.Redis(host = "10.4.32.240", port = 8502, db = 0)
behavior_url = "http://rtuuserapi.idc4:8709/?cust_id=%s&perm_id=%s&fields=behavior"


def random_permid(loop_cnt = 10):

    perm_id = ""
    for i in range(0, loop_cnt):
        ret = r_prefer.randomkey()
        if ret.startswith("p_"):
            perm_id = ret[2:]
            break

    return perm_id


def random_key(loop_cnt = 10):

    for i in range(0, loop_cnt):
        ret = r_interest.randomkey()
        if not ret or (not ret.isdigit()):
            continue

        if len(ret) == 35:
            return ("", ret)
        else:
            return (ret, "")


def pid_2_catid(pid_list):

    if not pid_list:
        return []

    ret = r_product.mget(["c%s" % x for x in pid_list])
    if ret:
        return [ x for x in ret if x != None ]

    return []


def get_catid(cust_id, perm_id):

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
