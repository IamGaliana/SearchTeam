# -*- coding: UTF-8 -*-
import MySQLdb

__author__ = 'zhaohongqiang'

def personal_get_brand(product_id):
    con=MySQLdb.connect('10.255.255.22','writeuser','ddbackend','ProductDB',charset='utf8')
    cursor=con.cursor()
    sql="select brand from Products_Core where product_id="+str(product_id)
    cursor.execute(sql)
    #str row=''
    row=cursor.fetchone()
    #如果为空返回空字符串，如果有值就返回该值。
    brand = ''
    if row:
        #print row[0]
        #print type(row[0])
        brand=row[0].encode('utf-8')
        #print brand
    cursor.close()
    con.close()
    return brand

##调试：特百惠
#s='8000'
#personal_get_brand(s)