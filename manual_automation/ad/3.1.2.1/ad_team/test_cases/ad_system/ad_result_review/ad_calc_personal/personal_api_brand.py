# -*- coding: UTF-8 -*-
import xlrd
from function.zhq.send_personal_api_request import pc_personal_api_brand
from function.zhq import personal_get_brand
from function.zhq.write_file import addfile_changeline

__author__ = 'zhaohongqiang'

#['6'] = "百货品牌-历史"

def personal_api_brand(log_add,brand):
    #发送api品牌词请求
    ad_brand = pc_personal_api_brand(log_add,brand)
    #获取cat_extend=6的product_list
    product_id_li = []
    for i in range(0, ad_brand.__len__()):
        #获取product_id=int(ad_brand[i]['product_id']
        #print type(brand_cat_extend)
        #print type(ad_brand[i]['cat_extend'].encode('utf-8'))
        #print ad_brand[i]['cat_extend']
        if ad_brand[i]['cat_extend'].encode('utf-8')=='6':
            product_id = int(ad_brand[i]['product_id'])
            #write log:print product_id
            product_str=str(product_id)
            #print ad_brand[i]['cat_extend']
            addfile_changeline(log_add,product_str)
            log_str=str(ad_brand[i]['ad_id'])+'是百货历史广告'
            addfile_changeline(log_add,log_str)
            product_id_li.append(product_id)
        else:
            #write log 不是百货历史广告。
            log_str=str(ad_brand[i]['ad_id'])+'不是百货历史广告'
            addfile_changeline(log_add,log_str)
            #break
    return product_id_li

def equal_brand(log_add,brand):
    #print('start')
    product_id_li=personal_api_brand(log_add,brand)
    #print('end')
    for i in product_id_li:
        #print i
        #print type(i)
        try:
            personal_get_brand(str(i))
            if brand== personal_get_brand(str(i)):
                continue
            else:
                return False
                break
        except Exception:
            #write_log:print('data error:: this brand name is None')
            #print('该product_id在product_Core中的brand为空值')
            brand_none_log_str=str(i)+'  :this brand name is None'
            addfile_changeline(log_add,brand_none_log_str)
            return False
            break
#brand = '特百惠'
#log_add='E:\\svn\\ad_team\\test_result\\brand_log.txt'
#equal_brand(log_add,brand)

def is_correct_brand_case(bug_file_add,log_add,brand):
    if equal_brand(log_add,brand) == False:
        #write brand error
        bug_str = brand+'::error::请求brand与返回广告brand 不一致'
        addfile_changeline(bug_file_add, bug_str)
    else:
        #write brand pass
        bug_str = brand+'::pass::请求brand与返回广告brand一致'
        addfile_changeline(bug_file_add, bug_str)

if __name__ == "__main__":
    #文件地址：
    fname = "E:\\svn\\ad_team\\cases\\personal_brand.xlsx"
    log_add='E:\\svn\\ad_team\\test_result\\brand_log.txt'
    bug_file_add = 'E:\\svn\\ad_team\\test_result\\brand_bug.txt'
    bk = xlrd.open_workbook(fname)
    shxrange = range(bk.nsheets)
    try:
        sh = bk.sheet_by_name("brands_one")
    except:
        print "no sheet in %s named cat_ids_one" % fname
        #获取行数
    nrows = sh.nrows
    #print nrows
    #获取列数
    ncols = sh.ncols
    #print ncols
    #获取第一行第一列数据
    cell_value = sh.cell_value(0, 0)
    #print str(int(cell_value))
    row_list = []
    #获取各行数据
    for i in range(0, nrows):
        raw_data = sh.row_values(i)
        #print raw_data[0]
        #print type(raw_data[0])
        #将unicode转换成utf-8
        brand =raw_data[0].encode('utf-8')
        #print brand
        is_correct_brand_case(bug_file_add,log_add,brand)









