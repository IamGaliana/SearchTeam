#coding=utf8
#function:about ProductCoreImageVersionSync_job test_case
''' environment deploy merchine:10.3.255.228:/d1/JOB/ProductCoreImageVersionSync   '''

import MySQLdb,chardet,time,unittest,sys

sys.path.append(r"D:\Users\Administrator\workspace\UnitTest\test_case\test_job")

from common_module import *


class Test_ProductCoreImageVersionSync_Job(unittest.TestCase):
    
    conn1 = cur1 = conn2 = cur2 = None
    
    def setUp(self):
        
        global conn1,cur1,conn2,cur2    
        conn1,cur1 = mysql.connect_mysql(host="10.255.254.22",user="writeuser",passwd="ddbackend",db="prodviewdb",port=3306)
        conn2,cur2 = mysql.connect_mysql(host="10.255.255.22",user="writeuser",passwd="ddbackend",db="products_image_version",port=3306)     
        
    def test_one_thread_per_get_one_record(self):
        
        u''' 测试单线程一次拉取一条数据场景  '''       
        cur1.execute("select product_id,image_version from products_core_search where product_id = 410015990")
        before_image_version = cur1.fetchall()[0][1]
        print "before_image_version = ",before_image_version
        cur2.execute("update image_version0 set image_version=5,last_modified_date=now() where image_id = 410015990")
        conn2.commit()
        cur2.execute("select last_modified_date from image_version0 where image_id = 410015990")
        source_data_last_modified_date = cur2.fetchall()[0][0]
        #print "source_data_last_modified_date =",source_data_last_modified_date
        cur1.execute("select process_last_date from process_status where process_id = 700100017 and product_tail = 0 ")
        process_data_process_last_date =  cur1.fetchall()[0][0]   
        #print "process_data_process_last_date =",process_data_process_last_date
        if source_data_last_modified_date >= process_data_process_last_date:
            #time.sleep(120)
            
            #验证product_core_search表中image_version字段同源表的image_version是否相同
            cur1.execute("select product_id,image_version from products_core_search where product_id = 410015990")
            after_image_version = cur1.fetchall()[0][1]
            self.assertEqual(after_image_version, 1)
            
            #验证process_status表中process_last_date字段同源表的last_modified_date是否相同
            cur1.execute("select process_last_date from process_status where process_id = 700100017 and product_tail = 0 ")
            process_data_process_last_date =  cur1.fetchall()[0][0]
            #self.assertEqual(process_data_process_last_date, source_data_last_modified_date)
        else:
            self.assertLessEqual(source_data_last_modified_date,process_data_process_last_date)
        
        #数据还原
        cur1.execute("update products_core_search set image_version=1 where product_id = 410015990")
        conn1.commit()
        
    def test_one_thread_per_get_many_record(self):
        u''' 测试单线程一次拉取多条(10条)数据场景  '''       
        #cur1.execute("select product_id,image_version from products_core_search WHERE product_id in (1149793111,1117826721,1007287321,1149793211)")
        #cur1.execute("select product_id,image_version from products_core_search where product_id = 410015990")
        #before_image_version = cur1.fetchall()[0][1]
        #print "before_image_version = ",before_image_version
        cur2.execute("update image_version0 set image_version=5,last_modified_date=now() where image_id in \
        (410017060,410018330,410000680,410017580,410017580,410005900,410005790,410005260,410017420)")
        conn2.commit()   #变更数据，需要commit，否则在数据库中不生效
        cur2.execute("select last_modified_date from image_version0 where image_id = 410017580")
        source_data_last_modified_date = cur2.fetchall()[0][0]
        #print "source_data_last_modified_date =",source_data_last_modified_date
        cur1.execute("select process_last_date from process_status where process_id = 700100017 and product_tail = 0 ")
        process_data_process_last_date =  cur1.fetchall()[0][0]   
        #print "process_data_process_last_date =",process_data_process_last_date
        if source_data_last_modified_date >= process_data_process_last_date:
            #time.sleep(120)
            
            #验证product_core_search表中image_version字段同源表的image_version是否相同
            cur1.execute("select product_id,image_version from products_core_search where product_id in \
            (410017060,410018330,410000680,410017580,410017580,410005900,410005790,410005260,410017420)")
            result = cur1.fetchall()
            for i in range(len(result)):
                after_image_version = result[i][1]
                self.assertEqual(after_image_version, 1)
            
            #验证process_status表中process_last_date字段同源表的last_modified_date是否相同
            cur1.execute("select process_last_date from process_status where process_id = 700100017 and product_tail = 0 ")
            process_data_process_last_date =  cur1.fetchall()[0][0]
            #self.assertEqual(process_data_process_last_date, source_data_last_modified_date)
        else:
            self.assertLessEqual(source_data_last_modified_date,process_data_process_last_date)
        #数据还原   
        cur1.execute("update products_core_search set image_version=1 WHERE product_id in \
        (410017060,410018330,410000680,410017580,410017580,410005900,410005790,410005260,410017420)")
        conn1.commit()
    
    def test_many_thread_per_get_many_record(self):
        u''' 测试多线程一次拉取多条数据场景  '''
        for i in range(10):           
            cur2.execute("update image_version%s set image_version=5,last_modified_date=now() where id > 300 and id < 310" % i)
            conn2.commit()
            cur2.execute("select image_id from image_version%s"" where id > 300 and id < 310" % i)
            result = product_ids = cur2.fetchall()
            cur2.execute("select last_modified_date from image_version%s where id = 301" % i)
            source_data_last_modified_date = cur2.fetchall()[0][0]
            cur1.execute("select process_last_date from process_status where process_id = 700100017 and product_tail = %s " % i)
            process_data_process_last_date =  cur1.fetchall()[0][0]   
            if source_data_last_modified_date >= process_data_process_last_date:
                #time.sleep(120)
                
                #验证product_core_search表中image_version字段同源表的image_version是否相同
                for pid in result:
                    cur1.execute("select product_id,image_version from products_core_search where product_id =%s " % pid)
                    after_image_version = cur1.fetchall()[0][1]
                    self.assertEqual(after_image_version, 1)
                
                #验证process_status表中process_last_date字段同源表的last_modified_date是否相同
                cur1.execute("select process_last_date from process_status where process_id = 700100017 and product_tail = %s " % i)
                process_data_process_last_date =  cur1.fetchall()[0][0]
                #self.assertEqual(process_data_process_last_date, source_data_last_modified_date)
            else:
                self.assertLessEqual(source_data_last_modified_date,process_data_process_last_date)
            
            #数据还原
            for pid in result:
                cur1.execute("update products_core_search set image_version=1 WHERE product_id = %s" % pid)
            
            conn1.commit()
                   
            
    def tearDown(self):
        global conn1,cur1,conn2,cur2
        mysql.close_mysql(conn1,cur1)
        mysql.close_mysql(conn2,cur2)
 

                   
if __name__ == "__main__": 
    
    '''
    suite = unittest.TestSuite()
    suite.addTest(Test_ProductCoreImageVersionSync_Job("test_one_thread_per_get_one_record"))
    suite.addTest(Test_ProductCoreImageVersionSync_Job("test_one_thread_per_get_many_record"))
    suite.addTest(Test_ProductCoreImageVersionSync_Job("test_many_thread_per_get_many_record"))
    '''
    suite =unittest.makeSuite(Test_ProductCoreImageVersionSync_Job,"test")
    runner = unittest.TextTestRunner()
    runner.run(suite)
       