#encoding:utf8
#from fg_func import CommonFunctions
import sys
from fg_func import adsystem_common
import time
from conf.pages_elements import PagesElements
import random

def test_get_table_row_and_column():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login()
    #action.goto("http://adsmart.dangdang.com/cost.php?c=cpcnew&tab=ad&start=2014-07-01&end=2015-07-06")
    action.goto("http://adsmart.dangdang.com/cost.php?c=cpcnew&tab=ad&start=2015-07-08&end=2015-07-08")
    print action.get_table_row_num("xpath->//table[@class='table']")
    print action.get_table_column_num("xpath->//table[@class='table']")
    a = action.table_row_content("xpath->//table[@class='table']")
    for i in xrange(len(a)):
        print "table_row:", i, a[i]
    #print "table_first_row:", a[0]
    b = action.table_column_content("xpath->//table[@class='table']")
    for i in xrange(len(b)):
        print "table_column:", i, b[i]
    #print "table_first_column:", b[0]
    action.quit()

def test_get_list_row_and_column():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login()
    action.goto("http://adsmart.dangdang.com/ads.cpc.php")
    #print "list_row_num: ", action.get_list_row_num("xpath->.//*[@id='form_ads']")
    #print "list_column_num: ", action.get_list_column_num("xpath->//ul[@class='title']")
    a = action.list_row_content("xpath->.//*[@id='form_ads']")
    for i in xrange(len(a)):
        print "list_row:", i, a[i]
    print "list_first_row:", a[0]
    b = action.list_column_content("xpath->.//*[@id='form_ads']")
    for i in xrange(len(b)):
        print "list_column:", i, b[i]
    print "list_first_column:", b[0]
    action.quit()

def test_create_group():
    action = adsystem_common.AdSystemCommon('chrome')
    action.create_cpc_adgroup_old()

def test_create_personal_ad():
    action = adsystem_common.AdSystemCommon('chrome')
    action.create_personal_ad()

def test_select():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login()
    action.goto("http://adsmart.dangdang.com/ads.cpc.php")
    action.select("xpath->.//*[@id='pos_id']/option", u"回头客推广位")
    action.click("xpath->.//*[@id='btn_gid']")

def test_checkbox():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login()
    action.goto("http://adsmart.dangdang.com/ads.cpc.php")
    action.checkbox()[0].click()

def test_a_click():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login()
    action.goto("http://adsmart.dangdang.com/ads.cpc.products.php?pos_type=9")
    random.choice(action.a_in_list("xpath->//li//a", "推广")).click()
    #action.get_a_in_list("xpath->//li//a", "推广")[0].click()
    action.click("xpath->.//*[@id='btn_submit']")
    print action.get_text_from_alert()
    action.quit()

def test_keyboard():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login(login_type=1, username='tangna', passwd='1234', shop_id='10439', idcode='1234')
    action.goto("http://adsmart.dangdang.com/ads.cpc.add.php?pos=0&product_id=1466375508")
    action.click(PagesElements.CreateSearchAdPage.ADD_CURRENT_PAGE_ALL_KEYWORDS)
    action.ctrl_a_delete(PagesElements.CreateSearchAdPage.KEYWORD_EDIT_INPUT)

def test_mouse_hover_and_screen_shot():
    action = adsystem_common.AdSystemCommon('chrome')
    action.max_window()
    action.goto("http://tool.chinaz.com/Tools/unixtime.aspx")
    action.mouse_hover("xpath->.//a[text()='SEO信息查询']")
    action.click("xpath->.//li/a[text()='百度权重查询']")
    url = action.get_current_url()
    print url
    action.screen_shot(url, 'd:\\test.png')

def test_current_url():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login(login_type=1, username='tangna', passwd='1234', shop_id='10439', idcode='1234')
    url = action.get_current_url()
    print url
    action.click("xpath->html/body/div[1]/div[2]/ul/li[2]/a")
    url = action.get_current_url()
    print url

def test_delete_cookie():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login(login_type=1, username='tangna', passwd='1234', shop_id='7792', idcode='1234')
    cookie = action.get_cookies()
    print cookie, type(cookie)
    for i in cookie:
        print(i)
    action.delete_cookie_by_name('__permanent_id')
    print cookie

def test_scroll():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login(login_type=1, username='tangna', passwd='1234', shop_id='7792', idcode='1234')
    #action.goto("http://adsmart.dangdang.com/ads.cpt.php")
    action.goto("http://adsmart.dangdang.com/cost.php?c=cpcnew&tab=ad&start=2014-07-01&end=2015-07-21")
    action.max_window()
    action.click("xpath->.//*[@id='custom_column']/div[1]")
    checkbox = action.get_checkbox()
    for i in checkbox:
        if not i.get_attribute("checked"):
            i.click()
    action.click("xpath->.//*[@id='custom_column']/div[2]/div[3]/input[1]")
    action.scroll_page()
    time.sleep(3)
    action.scroll_till_see("xpath->/html/body/div[2]/div[2]/div[5]/table/thead/tr/th[15]/a")

def test_alert():
    action = adsystem_common.AdSystemCommon('chrome')
    action.login(login_type=1, passwd='1234', shop_id='7792', idcode='1234')
    time.sleep(4)
    #action.accept_alert()
    action.dismiss_alert()

if __name__ == "__main__":
    #test_get_table_row_and_column()
    #test_get_list_row_and_column()
    #test_create_group()
    #test_create_personal_ad()
    #test_select()
    #test_checkbox()
    #test_a_click()
    #test_keyboard()
    #test_mouse_hover_and_screen_shot()
    #test_current_url()
    #test_delete_cookie()
    #test_scroll()
    test_alert()