#coding=utf8
import MySQLdb

def connect_mysql(host,user,passwd,db,port,charset="utf8"):
    try:
        conn = MySQLdb.connect(host,user,passwd,db,port,charset="utf8")
    except MySQLdb.Error,e:
        print "connection mysql error,please check it .",e
    cur = conn.cursor()
    return conn,cur


def close_mysql(conn,cur):
    cur.close()
    conn.close()