#coding=utf-8

import sys,time,os,re

import multiprocessing

sys.path.append("D:\Users\Administrator\workspace\UnitTest\test_case")

import all_test_case_lists
import HTMLTestRunner
import unittest

import smtplib
from email.mime.text import MIMEText
from email.header import  Header


def find_newest_test_report():
    report_list = os.listdir(r"D:\Users\Administrator\workspace\UnitTest\report\\")
    if len(report_list) < 2:
        print "no result need to send ."
        return None
    else:
        return report_list[-2]

def send_email(file):
    sender = "lixiaolong2519@163.com"
    receiver = "751517507@qq.com"
    send_server = "smtp.163.com"
    username = "lixiaolong2519@163.com"
    passwd = "870128"
    
    subject = "auto test report_" + file
    if file is not None:
        content = open(r"D:\Users\Administrator\workspace\UnitTest\report\\" + file).read()
        msg = MIMEText(content,'html','utf8')
    else:
        msg = MIMEText("<html><h1>没有报告需要发送.</h1></html>",'html','utf8')
    msg['Subject'] = subject
    
    smtp = smtplib.SMTP()
    smtp.connect(send_server)
    smtp.login(username,passwd)
    smtp.sendmail(sender,receiver,msg.as_string())
    smtp.quit()

def create_suites():
    base_dir = r"D:\Users\Administrator\workspace\UnitTest\test_case"
    thread_dir = []    
    case_dir = []
    thread_suite = []
    
    all_files = os.listdir(base_dir)
    for file_name in all_files:
        if "thread" in file_name:
            thread_dir.append(file_name)
    #print "thread_dir = ",thread_dir,len(thread_dir)
    
    for thread in thread_dir:
        test_dir = []
        thread_path = os.path.join(base_dir,thread)
        if os.path.isdir(thread_path):
            tests = os.listdir(thread_path)
            if len(tests) > 0:
                for file in tests:
                    if "test_" in file:
                        test_dir.append(file)
        #print test_dir
        
        if len(test_dir) > 0:
            test_suite = unittest.TestSuite()
            for dir in test_dir: 
                test_dir_path = os.path.join(thread_path,dir)  
                #print test_dir_path     
                discover = unittest.defaultTestLoader.discover(str(test_dir_path), pattern = "test_*.py", top_level_dir = None)
                for suite in discover:
                    for test_case in suite:
                        test_suite.addTests(test_case)
                unittest.defaultTestLoader._top_level_dir = None
                #print "test_suite",test_suite
                
                '''
                test_case_path = os.path.join(thread_path,dir)
                test_files = os.listdir(test_case_path)
                for case in test_files:
                    if re.match("^test_[A-Za-z0-9]*.py", case):
                        test_suite.addTest(unittest.makeSuite(case)))
                '''
            thread_suite.append(test_suite)   
    #for suite in thread_suite:print type(suite),suite
    return thread_suite,thread_dir


def mutilty_process_run(suite,process_dir):
    now = time.strftime("%Y%m%d%H%M%S",time.localtime(time.time())) 
    report_name = r"D:\Users\Administrator\workspace\UnitTest\report\\" + "result_" + now + ".html"
    fp = file(report_name,"wb")
    #fp.write("xiaolong1")
    process_list = []
    for i in suite: 
        #print i                                                 
        runner = HTMLTestRunner.HTMLTestRunner(stream=fp,
                                               title=u"测试报告",
                                                description=u"用例执行情况")
        runner.run(i)
        #process = multiprocessing.Process(target=runner.run,args=(suite[i],))
        #process_list.append(process)
        #fp.write("xiaolong2")
    for pro in process_list:pro.start()
    for pro in process_list:pro.join()
        
    
if __name__ == "__main__":
    suite,process_dir = create_suites()
    mutilty_process_run(suite,process_dir)

    ''' 
    test_suites = unittest.TestSuite()
    test_case_lists = all_test_case_lists.all_test_case_list()
    for test_case in test_case_lists:
        test_suites.addTest(unittest.makeSuite(test_case))
    print "test_suites",type(test_suites),test_suites
    
    #test_suites.addTest(unittest.makeSuite(test_baidu.TestBaidu))
    #test_suites.addTest(unittest.makeSuite(test_xiaolong1.Test_Xiaolong1))
    
    now = time.strftime("%Y%m%d%H%M%S",time.localtime())
    filename = r"D:\Users\Administrator\workspace\UnitTest\report\result_" + now + ".html"
    fp = file(filename,"wb")
    runner = HTMLTestRunner.HTMLTestRunner(stream=fp,title=u"自动化测试报告",description=u"自动化测试报告的描述")
    runner.run(test_suites)
    send_email(find_newest_test_report())
    
    
    
    now = time.strftime("%Y%m%d%H%M%S",time.localtime())
    filename = r"D:\Users\Administrator\workspace\UnitTest\report\result_" + now + ".html"
    fp = file(filename,"wb")
    runner = HTMLTestRunner.HTMLTestRunner(stream=fp,title=u"自动化测试报告",description=u"自动化测试报告的描述")
    for i in suite:
        print i
        runner.run(i)
    '''
    
 