#coding=utf8

from common_module import mysql
import unittest,time

class TestProductInfoSync(unittest.TestCase):
    
    def setUp(self):
        self.conn1,self.cur1 = mysql.connect_mysql(host="10.255.254.22",user="writeuser",passwd="ddbackend",db="prodviewdb",port=3306)
        self.conn2,self.cur2 = mysql.connect_mysql(host="10.255.255.22",user="writeuser",passwd="ddbackend",db="prodinterSeachdb",port=3306) 
          
    def test_sync_data(self):
        u''' 验证同步数据功能，synch_status 为 0 时同步，为 1 则不同步   '''
        print 100 * '*'
        print "start testing the function test_sync_data()"
        data1 = u' 测试abstract字段， 中间 两侧都有空格  '
        data2 = u' 测试content字段， 中间 两侧都有空格  '
        self.cur2.execute("update product_info_queue set abstract = '%s',content = '%s',synch_status = 1 where queue_id = 1" % (data1,data2))
        self.conn2.commit()
        time.sleep(20)
        self.cur2.execute("select product_id,synch_status,abstract,content from product_info_queue where queue_id =1 ")
        self.conn2.commit()
        result = self.cur2.fetchone()
        pid = result[0]
        synch_status = result[1]
        abstract = result[2]
        content = result[3]
        print "synch_status =",synch_status
        try:
            if self.assertEqual(synch_status,1) is None:
                self.cur1.execute("update product_info set abstract = '',content = '' where product_id = %s" % pid) 
                self.conn1.commit()
                self.cur1.execute("select abstract,content from product_info where product_id = %s " % pid )
                self.conn1.commit()
                result = self.cur1.fetchone()
                print u"源表product_info_queue 字段abstract ='%s' , content = '%s' " % (abstract,content)
                print u"目的表product_info     字段abstract ='%s' , content = '%s' " % (result[0],result[1])
                try:
                    self.assertNotEqual(abstract, result[0])
                    self.assertNotEqual(content, result[1])
                except AssertionError,e:
                    print "here shoud't sync data when synch_status = 1 , but the data still sync , please check it . "
        except AssertionError,e:
            print "update sql is error  , please check it . "
            print e    
        finally:
            #数据还原  
            self.cur1.execute("update product_info set abstract = '',content = '' where product_id = %s" % pid) 
            self.conn1.commit()     
    
    def test_sync_abstract_field(self):
        u''' 验证同步 abstract字段，前后空格是否被过滤 ，预测结果：前后及中间 空格不被过滤 ''' 
        print 100 * "*"
        print "start testing the function test_sync_abstract_field()"
        self.cur2.execute("select queue_id,product_id from product_info_queue where queue_id > 0  limit 1")
        temp = self.cur2.fetchone()
        if temp == None:
            print "product_info_queue has no data , please check it ."
        else:
            pid = temp[1]
            queue_id = temp[0]
            abstract_field = [u'<p>    <p>哈哈O(∩_∩)O~<p>   <p>',  
                              u'  这里是测试内容，头部有空格', 
                              u'测试内容，首尾均没有空格', 
                              u'  测试内容，首尾均有空格  ', 
                              u'  测试内容，  首尾及中间均有空格  ' ]
            for abstract in abstract_field:
                self.cur2.execute("update product_info_queue set abstract= '%s',synch_status = 0 where queue_id = %s" % (abstract,queue_id ))
                self.conn2.commit()
                time.sleep(20)
                self.cur2.execute("select synch_status from product_info_queue where queue_id = %s" % queue_id)
                self.conn2.commit()
                synch_status = self.cur2.fetchone()[0]
                print "synch_status = ",synch_status
                try:
                    if self.assertEqual(synch_status,1) is None:
                        self.cur1.execute("select abstract from product_info where product_id = %s" % pid)
                        self.conn1.commit()
                        result = self.cur1.fetchone()[0]
                        print "product_info表 abstract = ",result
                        print "product_info_queue表 abstract =",abstract
                        #验证abstract字段内容 同源表内容是否相同
                        try:
                            self.assertEqual(result, abstract)
                        except AssertionError,e:
                            print "abstract filed is not same,please check it"
                            print e
                except AssertionError,e:
                    print "sync_status is not right, please check it"
                    print e
                    break
                
                finally:
                    #数据还原
                    self.cur1.execute("update product_info set abstract='' where product_id = %s" % pid) 
                    self.conn1.commit()       
                   
    def test_sync_content_field(self):
        u''' 验证同步 content字段，前后空格是否被过滤 ，预测结果：前后及中间 空格不被过滤 '''
        print 100 * "*"
        print "start testing the function test_sync_content_field()"
        self.cur2.execute("select queue_id,product_id from product_info_queue where queue_id > 0  limit 2")
        temp = self.cur2.fetchone()
        if temp == None:
            print "product_info_queue has no data , please check it ."
        else:
            pid = temp[1]
            queue_id = temp[0]
            content_field = [u'<p>    <p>测试content字段<p>   <p>',  
                              u'  测试content字段，头部有空格', 
                              u'测试content字段，首尾均没有空格', 
                              u'  测试content字段，首尾均有空格  ', 
                              u'  测试content字段，  首尾及中间均有空格  ' ]
            for content in content_field:
                self.cur2.execute("update product_info_queue set content= '%s',synch_status = 0 where queue_id = %s" % (content,queue_id ))
                self.conn2.commit()
                time.sleep(20)
                self.cur2.execute("select synch_status from product_info_queue where queue_id = %s" % queue_id)
                self.conn2.commit()
                synch_status = self.cur2.fetchone()[0]
                print "synch_status = ",synch_status
                try:
                    if self.assertEqual(synch_status,1) is None:
                        self.cur1.execute("select content from product_info where product_id = %s" % pid)
                        self.conn1.commit()
                        result = self.cur1.fetchone()[0]
                        print "product_info表 content = ",result
                        print "product_info_queue表 content =",content
                        #验证abstract字段内容 同源表内容是否相同
                        try:
                            self.assertEqual(result, content)
                        except AssertionError,e:
                            print "content filed is not same,please check it"
                            print e
                except AssertionError,e:
                    print "sync data failure , please check it"
                    print e
                    break
                
                finally:
                    #数据还原
                    self.cur1.execute("update product_info set content='' where product_id = %s" % pid) 
                    self.conn1.commit() 
                    
    def test_sync_insert_new_data(self):
        u''' 验证新增数据能否同步成功，预测结果：新增数据可以同步成功 '''
        print 100 * '*'
        print "start testing the function test_sync_insert_new_data()"
        abstract = u'  ceshi  '
        content = u'  内 容 简 介 '
        self.cur2.execute("insert into product_info_queue values (987654321,987654321,'%s','%s','1234','1234','1234','1234','1234','1234',0,now(),now()) " % (abstract,content))
        self.conn2.commit()
        time.sleep(30)
        self.cur2.execute("select synch_status from product_info_queue where product_id = 987654321")
        self.conn2.commit()
        synch_status = self.cur2.fetchone()[0]
        print "synch_status=",synch_status
        try:
            if self.assertEqual(synch_status,1) is None:
                self.cur1.execute("select abstract,content from product_info where product_id = 987654321")
                self.conn1.commit()
                result = self.cur1.fetchone()
                print u"源表product_info_queue 字段abstract ='%s' , content = '%s' " % (abstract,content)
                print u"目的表product_info     字段abstract ='%s' , content = '%s' " % (result[0],result[1])
                try:
                    self.assertEqual(abstract, result[0])
                    self.assertEqual(content, result[1])
                except AssertionError,e:
                    print "sync insert new data failure , abstract or content  field is not same ,please check it ."
                    print e    
        except AssertionError,e:
            print "sync insert new data failure , sync_status is not right, please check it ."
            print e
            
        finally:
            #数据还原
            self.cur2.execute("delete from product_info_queue where product_id = 987654321") 
            self.conn2.commit()  
            self.cur1.execute("delete from product_info where product_id = 987654321") 
            self.conn1.commit()     
    
    def test_sync_update_data(self):
        u''' 验证更新数据能否同步成功，预测结果：更新数据可以同步成功 '''
        print 100 * '*'
        print "start testing the function test_sync_update_data()"
        data1 = u' 测试abstract字段， 中间 两侧都有空格  '
        data2 = u' 测试content字段， 中间 两侧都有空格  '
        self.cur2.execute("update product_info_queue set abstract = '%s',content = '%s',synch_status = 0 where queue_id = 1" % (data1,data2))
        self.conn2.commit()
        time.sleep(20)
        self.cur2.execute("select product_id,synch_status,abstract,content from product_info_queue where queue_id =1 ")
        self.conn2.commit()
        result = self.cur2.fetchone()
        pid = result[0]
        synch_status = result[1]
        abstract = result[2]
        content = result[3]
        print "synch_status =",synch_status
        try:
            if self.assertEqual(synch_status, 1) is None:
                self.cur1.execute("select abstract,content from product_info where product_id = %s " % pid )
                self.conn1.commit()
                result = self.cur1.fetchone()
                print u"源表product_info_queue 字段abstract ='%s' , content = '%s' " % (abstract,content)
                print u"目的表product_info     字段abstract ='%s' , content = '%s' " % (result[0],result[1])
                try:
                    self.assertEqual(abstract, result[0])
                    self.assertEqual(content, result[1])
                except AssertionError,e:
                    print "sync update data failure , abstract or content  field is not same ,please check it ."
                    print e
        except AssertionError,e:
            print "sync update data failure , synch_status is not right , please check it ."
            print e
            
        finally:
            #数据还原  
            self.cur1.execute("update product_info set abstract = '',content = '' where product_id = %s" % pid) 
            self.conn1.commit()     
            
    def tearDown(self):
        self.cur1.close()
        self.cur2.close()
        self.conn1.close()
        self.conn2.close()
    
if __name__ == "__main__":
    suite = unittest.makeSuite(TestProductInfoSync,"test")
    runner =unittest.TextTestRunner()
    runner.run(suite)
    
    