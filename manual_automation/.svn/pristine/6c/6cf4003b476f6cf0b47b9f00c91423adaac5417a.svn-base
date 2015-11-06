#coding=utf-8

import unittest
import xiaolong1

class TestXiaolong1(unittest.TestCase):
    def setUp(self):
        self.person = xiaolong1.PersonInfo("zhexuan","1")
        self.errors = []
    
    def test_set_name(self):
        u""" 测试set_name函数功能  """ 
        print u"testing set_name 中文......"
        person1 = self.person
        person1.set_name("xiaolong")
        self.assertEqual(person1.get_name(), "xiaolong")
    
    
    def tearDown(self):
        self.person = None
        self.assertEqual([],self.errors)

class TestXiaolong2(unittest.TestCase):
    u""" 测试 xiaolong2 这个类  """
    def setUp(self):
        self.person = xiaolong1.PersonInfo("xiaolong",27)
        self.errors = []
    
    def test_set_age(self):
        print u"测试 set_age 函数功能...."
        person = self.person
        person.set_age(30)
        self.assertEqual(person.get_age(),30)
        
    
    def tearDown(self):
        self.person = None
        self.assertEqual([],self.errors)
        

'''
if __name__ == "__main__":
    suite = unittest.TestSuite()
    suite.addTest(Test_Xiaolong1("test_set_name"))
    suite.addTest(TestXiaolong2("test_set_age"))
    """unittest.main(defaultTest = "suite")  """
    runner = unittest.TextTestRunner()
    runner.run(suite)  
    
'''