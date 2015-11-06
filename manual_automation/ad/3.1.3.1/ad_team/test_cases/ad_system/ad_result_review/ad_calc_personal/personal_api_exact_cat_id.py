# -*- coding: UTF-8 -*-
from function.zhq.is_correct_case import is_correct_usecase
from function.zhq.write_file import addfile_changeline
from function.zhq import read_file, exact_extend_list

__author__ = 'zhaohongqiang'

def personal_api_exact_cat_id(fname):
    log_address='E:\\svn\\ad_team\\test_result\\exact_cat_id_log.txt'
    #获取得category_list
    category_list=read_file(fname)
    #循环取category_list
    for i in category_list:
        #print category_list[i]
        catid = str(int(i))
        #写请求log
        str1 = 'request category_id:'+ catid
        addfile_changeline(log_address,str1)
        #判断case是否通过
        is_correct_usecase(catid,exact_extend_list(catid))

fname="E:\\svn\\ad_team\\cases\\personal_exact_cat_ids.xlsx"
personal_api_exact_cat_id(fname)
