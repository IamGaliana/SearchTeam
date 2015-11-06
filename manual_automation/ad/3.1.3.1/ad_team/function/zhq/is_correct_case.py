# -*- coding: UTF-8 -*-
from function.zhq.write_file import addfile_changeline

__author__ = 'zhaohongqiang'

def is_correct_usecase(catid,exact_extend_list):
    catid_compare_list=[]
    #print exact_extend_list
    bug_address='E:\\svn\\ad_team\\test_result\\exact_cat_id_bug.txt'
    log_address='E:\\svn\\ad_team\\test_result\\exact_cat_id_log.txt'
    for i in range(0,exact_extend_list.__len__()):
        #打印每一条记录
        #print exact_extend_list[i]
        dic_compare=[]
        #print exact_extend_list[i]['cat_id']
        #print type(exact_extend_list[i]['cat_id'])
        #print "*******************"

        if catid== str(int(exact_extend_list[i]['extra'])):
            #print('pass:写log，cat_id,product_id,')
            #log_str='ad_id='+str(int(exact_extend_list[i]['ad_id']))+',product_id='+str(int(exact_extend_list[i]['product_id']))+',cat_id:'+str(int(exact_extend_list[i]['cat_id']))+'cat_id='+str(int(exact_extend_list[i]['cat_id']))+',cat_extend:'+str(int(exact_extend_list[i]['cat_extend']))
            #addfile_changeline(log_address,log_str)
            dic_compare.append(1)
        else:
            #print('error:写log')
            log_str='error::ad_id='+str(int(exact_extend_list[i]['ad_id']))+',extra='+str(int(exact_extend_list[i]['extra']))+',product_id='+str(int(exact_extend_list[i]['product_id']))+',cat_id:'+str(int(exact_extend_list[i]['cat_id']))+',cat_extend:'+str(int(exact_extend_list[i]['cat_extend']))
            addfile_changeline(log_address,log_str)
            dic_compare.append(0)
    if 0 in dic_compare:
        #print(写错误报告)
        bug_str=catid+':该分类请求返回错误'
        addfile_changeline(bug_address,bug_str)
    else:
        #print(write error report)
        bug_str=catid+':该分类请求返回数据正确'
        addfile_changeline(bug_address,bug_str)

#catid='4008149'
#exact_extend_list=exact_extend_list(catid)
#is_correct_usecase(catid,exact_extend_list(catid))

