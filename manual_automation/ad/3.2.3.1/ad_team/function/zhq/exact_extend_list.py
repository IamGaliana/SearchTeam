# -*- coding: UTF-8 -*-
from function.zhq.send_personal_api_request import pc_personal_api_all

__author__ = 'zhaohongqiang'

cat_extend_history = [2, 4]
def exact_extend_list(catid):
    exact_extend_li = []
    #获取返回数据，赋值给list.
    res_li = pc_personal_api_all(catid)
    #print res_li
    #log地址
    log_address='E:\\svn\\ad_team\\test_result\\exact_cat_id_log.txt'
    #print res_cat_extend
    for i in range(0, res_li.__len__()):
        #print res_li[i]
        if int(res_li[i]['cat_extend']) in cat_extend_history:
            exact_extend_li.append(res_li[i])
        else:
            print('无精确匹配数据')
    return exact_extend_li

#catid = '4008149'
#exact_extend_list(catid)
#for i in exact_extend_list(catid):
#   print i

