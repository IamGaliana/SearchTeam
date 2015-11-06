# -*- coding: UTF-8 -*-
__author__ = 'zhaohongqiang'

import xlrd

def row_data(args):
    pass

def read_file(fname):
    """
    @rtype : object
    @param fname:
    """
    bk=xlrd.open_workbook(fname)
    shxrange=range(bk.nsheets)
    try:
        sh=bk.sheet_by_name("cat_ids_one")
    except:
        print "no sheet in %s named cat_ids_one"%fname
    #获取行数
    nrows=sh .nrows
    #print nrows
    #获取列数
    ncols=sh.ncols
    #print ncols
    #print "nrows %d,ncols %d" %(nrows,ncols)
    #获取第一行第一列数据
    cell_value =sh.cell_value(0,0)
    #print str(int(cell_value))
    row_list=[]
    category_ids=[]
    #获取各行数据
    for i in range(0,nrows):
        raw_data=sh.row_values(i)
        #print raw_data[0]
        category_id= int(raw_data[0])
        category_ids.append(category_id)
    return category_ids

#def read_file_str(fname):
#    """
#    @rtype : object
#    @param fname:
#    """
#    bk=xlrd.open_workbook(fname)
#    shxrange=range(bk.nsheets)
#    try:
#        sh=bk.sheet_by_name("cat_ids_one")
#    except:
#        print "no sheet in %s named cat_ids_one"%fname
#    #获取行数
#    nrows=sh .nrows
#    #print nrows
#    #获取列数
#    ncols=sh.ncols
#    #print ncols
#    #print "nrows %d,ncols %d" %(nrows,ncols)
#    #获取第一行第一列数据
#    cell_value =sh.cell_value(0,0)
#    #print str(int(cell_value))
#    row_list=[]
#    category_ids=[]
#    #获取各行数据
#    for i in range(0,nrows):
#        raw_data=sh.row_values(i)
#        #print raw_data[0]
#        category_id= int(raw_data[0])
#        category_ids.append(category_id)
#    return category_ids
#
#fname="E:\\mywork\\Auto_xi\\adsmart_case\\cat_ids.xlsx"
#for i in read_file(fname):
#    print i
