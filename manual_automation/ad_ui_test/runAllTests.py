import sys
import HTMLTestRunner
import time
sys.path.append(".\\adsmart_testcases\\CPM_ad_test")

if __name__ == "__main__":

    import unittest
    from CpmListPicturePage import *
    suite_cpm = unittest.TestLoader().loadTestsFromTestCase(CpmListPicturePage)
    suite = unittest.TestSuite([suite_cpm])
    test_report = "D:\\testreport%s.html" %str(time.strftime('%m%d%H%M%S'))
    fp = file(test_report, 'wb')
    runner = HTMLTestRunner.HTMLTestRunner(
        stream=fp,
        title=u"test_title",
        description=u"test_description",
    )
    runner.run(suite)
    subject = runner.title
    file_list = [test_report]
    fp.close()
    #unittest.TextTestRunner(verbosity=2).run(suite)
