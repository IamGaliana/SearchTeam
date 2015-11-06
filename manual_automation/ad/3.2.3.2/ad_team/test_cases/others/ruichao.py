# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

import urllib
from urllib import quote
import string
import sys
from elementtree import ElementTree
from elementtree.ElementTree import Element

def pc_search_api(extra, pid):
    url = "http://10.255.254.188:8390/?q=%s&pg=1&ps=200&ip=192.168.95.162&pid=%s&domain=search.dangdang.com&_url_token=5&vert=1&cate_type=0&_new_tpl=1&session_id=cc37a8302fee7b5db6e8d30c2d6b59f5&st=full&um=search_ranking&gp=cat_paths,label_id"
    parameter = (extra, pid)
    print url % parameter
    response = urllib.urlopen(url % parameter).read()
    r = response.decode("gbk","ignore").encode("utf-8").replace('GBK', 'UTF-8')
    root = ElementTree.fromstring(r)
    product = root.findall(".//Product")
    #print product
    ll = []
    for p in product:
        zidian = {}
        for subElement in p.getchildren():
            if subElement.tag == "Score" or subElement.tag == "product_id" or subElement.tag == "brand_id":
                zidian[subElement.tag] = subElement.text
        #print zidian
        ll.append(zidian)
    return ll

def run(argv):

    s = argv[1]
    extra = quote(s.decode("utf8").encode("gb2312"))
    pid = argv[2]
    score = argv[3]

    xx= pc_search_api(extra, pid)
    xx[0:57]= []
    #print xx
    for i in range(0,xx.__len__()):
      xx[i]['Score'] = string.atol(xx[i]['Score'])*string.atof(score)*10000
    #print xx
    sorted_x = sorted(xx, key=lambda x : x['Score'], reverse=True)
    #print sorted_x
    for i in range(0,4):
        print sorted_x[i]

#run(sys.argv)
#list["乐吉儿","20020101004721171170094134297437825",1]
run(["","乐吉儿","20020101004721171170094134297437825",1])

