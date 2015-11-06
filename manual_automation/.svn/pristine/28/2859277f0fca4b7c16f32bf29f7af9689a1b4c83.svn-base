from conf import config as c
from conf.pages_elements import PagesElements
from fg_func.BaseFunc import *
from fg_func.adsystem_common import *
import unittest

class CpmListPicturePage(unittest.TestCase):

    def setUp(self):
        self.common = AdSystemCommon('firefox')
        self.common.login()

    def test_checkbox_of_cpm_ad_list(self):
        self.common.click(PagesElements.IndexPage.CPM_HEADER_NAVIGATION)
        self.common.click(PagesElements.CpmPages.CPM_PICTURE_TAB)
        effective_check = self.common.get_attribute(PagesElements.CpmPages.CHECK_BOX_FOR_EFFECTIVE_STATUS_AD, "disabled")
        refused_check = self.common.get_attribute(PagesElements.CpmPages.CHECK_BOX_FOR_REFUSED_STATUS_AD, "disabled")
        print effective_check, refused_check

        self.assertEqual(effective_check, 'true')
        self.assertEqual(refused_check, None)

    def tearDown(self):
        self.common.quit()
if __name__ == "__main__":
    unittest.main()




