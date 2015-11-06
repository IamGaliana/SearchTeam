# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'

import operator

#yyy = [{"a":"1","b":"2","c":"3"},{"a":"4","b":"5","c":"6"},{"a":"8","b":"9","c":"10"}]
#
#
#print yyy.sort(yyy,key= lambda x:x['c'])

#x = [{'name':'Homer', 'age':39}, {'name':'Bart', 'age':10}]
##sorted_x = sorted(x, key=lambda x : x['age'], reverse=True)
##print sorted_x
#
#for i in range(0, len(x)):
#    print x[i]
#    zidian = {}
#    if x[i]['age']==39:
#        print x[i]['name']
#
#c_in = ['1','2','3','4','5','6']
#c_out = ['7', '8']
#
#c = c_in+c_out
#print c
#
#c_in.remove('1')
#print c_in
#
#
#str = "abcdefg"
#if "ab" in str:
#    print "xxx"



#!/usr/bin/env python
# -*- coding: UTF-8 -*-



get_cid_in_son_list_url = "http://192.168.197.172/v2/find_categories.php?for=subpath_is_son&category_id=s%&category_path=&level=&result_format=json&fields=category_id,category_path&subpath=1&is_son=0"
print get_cid_in_son_list_url % 234


