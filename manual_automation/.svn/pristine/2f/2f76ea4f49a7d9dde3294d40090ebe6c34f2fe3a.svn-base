#coding=utf8
from common_module import mysql
import time


table_list = ["b2c_product","b2c_product_subcat","category_map","coupon_info",
              "product_activity_search","product_info","product_meta","product_price",
              "product_promo_info","product_stock","products_core_search","search_info",
              "search_info_extra"]

source_record = {}
destination_record = {}

start_pid = 1066700124
end_pid = 1066701624

conn2,cur2 = mysql.connect_mysql(host="10.255.254.22",user="writeuser",passwd="ddbackend",db="prodviewdb_recycle",port=3306,charset="utf8")
conn1,cur1 = mysql.connect_mysql(host="10.255.254.22",user="writeuser",passwd="ddbackend",db="prodviewdb",port=3306,charset="utf8")

#insert into prodviewdb_recycle.recycle 10000 record
'''
for i in xrange(2000001):
    if i > 600000:
        cur2.execute("insert into recycle (product_id,shop_id,product_name,category_id,status,creation_date,last_modified_date) values (%d,%d,'xiaolongtest',%d,0,now(),now())" % (i,(i + 10 ),(i * 100)))
    if i > 3000000:
        break
conn2.commit()

'''
'''
pids = []
cur1.execute("select product_id from products_core_search where product_id >=  60209143 limit 100000")
conn1.commit()
pids = cur1.fetchall()
for pid in pids:
    cur2.execute("insert into recycle (product_id,shop_id,product_name,category_id,status,creation_date,last_modified_date) values (%d,%d,'xiaolongtest',%d,0,now(),now())" % (pid[0],(pid[0] + 10 ),(pid[0] + 100)))
conn2.commit()

'''
#before update,select count record from source table and destination table
#source table
for table in table_list:
    cur1.execute("select count(*) from %s where product_id >= %d and product_id <= %d" % (table,start_pid,end_pid))
    conn1.commit()
    source_record[table] = cur1.fetchone()[0]

#destination table   
    cur2.execute("select count(*) from %s where product_id >= %d and product_id <= %d" % (table,start_pid,end_pid))
    conn2.commit()
    destination_record[table] = cur2.fetchone()[0]

print u"**********更新前源表记录**********"
for table in source_record:
    print table,"表记录：", source_record[table]
    
print u"**********更新前目的表记录**********"
for table in destination_record:
    print table,"表记录：", destination_record[table]



print u"**********开始计时**********"
#count the time update all record success costs
start = time.time()
print "start:",time.strftime("%Y-%m-%d %H:%M:%S",time.localtime())
while True:
    cur2.execute("select product_id from recycle where status = 0 and product_id >= %d and product_id <= %d limit 1" % (start_pid,end_pid))
    conn2.commit()
    result  = cur2.fetchall()
    if len(result) == 0:
        break
    #for keep conn1 connection
    cur1.execute("select count(*) from b2c_product where 1 = 2")
end = time.time()
print u"**********计时结束**********"
print "end:",time.strftime("%Y-%m-%d %H:%M:%S",time.localtime())
print "共用时:",((end - start) / 60),"分钟"


print u"**********统计数据记录备份条数**********"
#after update success,compare source table count with destination table count and they shoud be equal
for table in table_list:
    cur2.execute("select count(*) from %s where product_id >= %d and product_id <= %d" % (table,start_pid,end_pid))
    conn2.commit()
    count = cur2.fetchone()[0]
    print table,"源表数据记录为：",source_record[table],"   更新前目的表记录为：",destination_record[table],"    目的表新增数据记录为：",(count - destination_record[table]),"    更新后目的表数据记录为：",count
        
#after update success,look the source table whether the data has't be deleted
temp = []
for table in table_list:
    cur1.execute("select count(*) from %s where product_id >= %d and product_id <= %d" % (table,start_pid,end_pid))
    conn1.commit()
    #if count != 0,check pid whether in recycle,if pid in recycle ,then it's wrong
    if cur1.fetchone()[0] != 0:
        cur1.execute("select product_id from %s where product_id >= %d and product_id <= %d" % (table,start_pid,end_pid))
        conn1.commit()
        pids = cur1.fetchall()
        for pid in pids:
            temp.append(int(pid[0]))
        pids = tuple(temp)
        pid_str = pids.__str__()
        cur2.execute("select product_id from recycle where product_id in " + pid_str) 
        conn2.commit()
        if len(cur2.fetchall()) != 0:
            print table,"表以下pid记录未被删除，请查看." 
            for pid in pids:print pid, 
            print
               
print u"**********运行结束**********"

mysql.close_mysql(conn1,cur1)   
mysql.close_mysql(conn2,cur2)   