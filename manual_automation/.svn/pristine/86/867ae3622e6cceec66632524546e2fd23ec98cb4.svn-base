#encoding=utf-8

from selenium import webdriver
import unittest,time
import baidu
#import HTMLTestRunner

class TestBaidu(unittest.TestCase):
    
    u""" 中文  """
    
    def setUp(self):
        self.driver = webdriver.Chrome()
        self.driver.implicitly_wait(10)
        self.base_url = "http://www.baidu.com"
        self.verificationErrors = []
        self.accept_next_alert = True
        self.baidu = baidu.Baidu()
        
    def test_baidu(self):
        print "testing test_baidu ......"
        driver = self.driver
        driver.get(self.base_url)
        driver.find_element_by_id("kw").send_keys("shouji")
        driver.find_element_by_id("su").submit()
        driver.close()
    
    def test_get_size(self):
        print "testing get_size......."
        self.assertEqual(self.baidu.get_size(), (40,40))
    
    def test_set_size(self):
        """ ceshi  she  zhi  da  xiao  gongneng   """
        print "testing set_size......."
        self.baidu.set_size(100, 150)
        self.assertEqual(self.baidu.get_size(),(100, 150))
    
    def tearDown(self):
        self.driver.quit()
        self.baidu = None
        self.assertEqual([], self.verificationErrors)

'''
def suite():
    suite2 = unittest.TestSuite()
    suite2.addTest(TestBaidu("test_get_size"))
    suite2.addTest(TestBaidu("test_set_size"))
    #suite2 = unittest.makeSuite(TestBaidu, "test")
    return suite2

if __name__ == "__main__":
    #unittest.main()
    #unittest.main(defaultTest = "suite")
    runner = unittest.TextTestRunner()
    runner.run(suite())
""" filename = r"D:\Users\\Administrator\workspace\UnitTest\report\result.html"
    fp = file(filename,"w")
    runner = HTMLTestRunner.HTMLTestRunner(stream=fp,title= "ceshi",description="testing")
    runner.run(suite())  """
    
'''