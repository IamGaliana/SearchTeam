#coding = utf-8
__author__ = 'xiyucheng'
import MySQLdb
import pymssql
import sys
import pymongo

reload(sys)
sys.setdefaultencoding('utf-8')


def search_order_mysql(sql):
    try:
        conn = MySQLdb.connect(host='10.255.254.185', user='writeuser', passwd='ddbackend', db='Order0', charset='utf8')
        cursor = conn.cursor()
        cursor.execute(sql)
        xx = cursor.fetchall()
        for r in xx:
            print r[1]
        #print "this is result:", xx
        cursor.close()
        conn.close()
        return xx
    except MySQLdb.Error, e:
        print "Mysql Error %d: %s" % (e.args[0], e.args[1])


def search_order_sql(sql):
    try:
        conn=pymssql.connect(host='10.255.254.185', user='writeuser', password='ddbackend', database="Order0")
        cursor = conn.cursor()
        cursor.execute(sql)
        row = cursor.fetchall()
        #print xx[0]
        #for r in xx:
        #    print r[0]
        print row[0][0],row[0][1],row[0][2]
        #while row:
        #    print row[i], i
        #    row = cursor.fetchone()
        #    i == i+1
        cursor.close()
        conn.close()
        return row
    except pymssql.Error, e:
        print "Mysql Error %d: %s" % (e.args[0], e.args[1])


def search_mango(order_id):

    host = '10.255.208.44'
    port = 27018
    try:
        coon = pymongo.Connection(host, port)
        db = coon.Association

        str1 = db.orders.find_one({'order_id':order_id})
        if str1 is None:
            print "There is no data that order_id =",order_id,"in mongo DB"
            return 0
        elif str1 is not None:
            print "find the data that order_id =",order_id,"in mongo DB"
            return 1
    except:
        print "pymongo Error"


def return_environment(order_id):
    host = '10.255.208.44'
    port = 27018
    try:
        coon = pymongo.Connection(host, port)
        db = coon.Association
        db.orders.remove({'order_id':order_id})
        #db.orders.remove()
        str1 = db.orders.find_one({'order_id':order_id})
        if str1 is None:
            print "The environment has been restored. remove successful"
            return 0
        elif str1 is not None:
            print "The environment restore failed. remove failed"
            return 1
    except:
        print "pymongo Error"

def count(order_id):
    host = '10.255.208.44'
    port = 27018
    try:
        coon = pymongo.Connection(host, port)
        db = coon.Association
        num = db.orders.find({'order_id':order_id}).count()
        return num

    except:
        print "pymongo Error"