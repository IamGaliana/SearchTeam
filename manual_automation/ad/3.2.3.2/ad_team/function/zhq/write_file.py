# -*- coding: UTF-8 -*-
__author__ = 'zhaohongqiang'

def writeFile(fileAddress,all_the_text):
    bugfile=open(fileAddress,'w')
    bugfile.write(all_the_text)
    bugfile.close()

def addFile(fileAddress,all_the_text):
    bugfile=open(fileAddress,'a')
    bugfile.write(all_the_text)
    bugfile.close()

def changeline(fileAddress):
    bugfile=open(fileAddress,'a')
    bugfile.write("\n")
    bugfile.close()
def addfile_changeline(fileAddress,all_the_text):
    bugfile=open(fileAddress,'a')
    bugfile.write(all_the_text)
    bugfile.write('\n')
    bugfile.close()

#fileAddress='E:\\svn\\ad_team\\test_result\\bug.txt'
#all_the_text='我是中国人'
#writeFile(fileAddress,all_the_text)
#fileAddress='E:\\mywork\\Auto_xi\\adsmart_case\\bug.txt'
#all_the_text='我是中国人'
#addFile(fileAddress,all_the_text)




