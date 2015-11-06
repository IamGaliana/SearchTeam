__author__ = 'xiyucheng'
# -*- coding: utf-8 -*-
import  xdrlib ,sys
import xlrd
def open_excel(file):
    try:
        data = xlrd.open_workbook(file)
        return data
    except Exception, e:
        print str(e)
#根据索引获取Excel表格中的数据   参数:file：Excel文件路径     colnameindex：表头列名所在行的所以  ，by_index：表的索引
def excel_table_byindex(file,colnameindex=0,by_index=0):
    data = open_excel(file)
    table = data.sheets()[by_index]
    nrows = table.nrows #行数
    ncols = table.ncols #列数
    colnames = table.row_values(colnameindex) #某一行数据
    list =[]
    for rownum in range(1, nrows):

         row = table.row_values(rownum)
         if row:
             app = {}
             for i in range(len(colnames)):
                app[colnames[i]] = row[i]
             list.append(app)
    return list

#根据名称获取Excel表格中的数据   参数:file：Excel文件路径     colnameindex：表头列名所在行的所以  ，by_name：Sheet1名称
def excel_table_byname(file,sheet_name,colnameindex=0):
    data = open_excel(file)
    table = data.sheet_by_name(sheet_name)
    nrows = table.nrows #行数
    colnames =  table.row_values(colnameindex) #某一行数据
    list =[]
    for rownum in range(1,nrows):
         row = table.row_values(rownum)
         if row:
             app = {}
             for i in range(len(colnames)):
                app[colnames[i]] = row[i]
             list.append(app)
    return list

#黄金展位ab版测试用
def excel_table_byindex2(excel_file, col_index=0, sheet_index=0):
    col_values = []
    data = open_excel(excel_file)
    table = data.sheet_by_index(sheet_index)
    table.col_values(col_index)
    return table.col_values(col_index)

