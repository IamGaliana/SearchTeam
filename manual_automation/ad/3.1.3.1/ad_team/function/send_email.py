# -*- coding: UTF-8 -*-
__author__ = 'xiyucheng'


from email.mime.multipart import MIMEMultipart
from email.mime.base import MIMEBase
from email.mime.text import MIMEText

from email.utils import COMMASPACE,formatdate
from email import encoders

import os

def send_mail(subject, files=[]):

    server = {'name':'sl2k8mail-01.dangdang.com', 'user':'xiyucheng', 'passwd':'ca$hc0wH', 'port':587}
    fro = 'xiyucheng@dangdang.com'
    #to = ['xiyucheng@dangdang.com','liweina@dangdang.com','zhangruichao@dangdang.com','zhaohongqiang@dangdang.com']
    to = ['xiyucheng@dangdang.com', 'liaoxiqiang@dangdang.com', 'zhangruichao@dangdang.com']


    text = '自动化测试报告'

    assert type(server) == dict
    assert type(to) == list
    assert type(files) == list

    msg = MIMEMultipart()
    msg['From'] = fro
    msg['Subject'] = subject
    msg['To'] = COMMASPACE.join(to) #COMMASPACE==', '
    msg['Date'] = formatdate(localtime=True)
    msg.attach(MIMEText(text))


    for f in files:
        part = MIMEBase('application', 'octet-stream') #'octet-stream': binary data
        part.set_payload(open(f, 'rb').read())
        encoders.encode_base64(part)
        part.add_header('Content-Disposition', 'attachment; filename="%s"' % os.path.basename(f))
        msg.attach(part)

    import smtplib
    smtp = smtplib.SMTP(server['name'], server['port'])
    smtp.ehlo()
    smtp.starttls()
    smtp.ehlo()
    smtp.login(server['user'], server['passwd'])
    smtp.sendmail(fro, to, msg.as_string())
    smtp.close()


def new_send_mail(subject, files=[], to_list = [], contents = ""):

    server = {'name':'sl2k8mail-01.dangdang.com', 'user':'xiyucheng', 'passwd':'ca$hc0wI', 'port':587}
    fro = 'xiyucheng@dangdang.com'
    to = to_list
    text = str(contents)
    assert type(server) == dict
    assert type(to) == list
    assert type(files) == list
    msg = MIMEMultipart()
    msg['From'] = fro
    msg['Subject'] = subject
    msg['To'] = COMMASPACE.join(to) #COMMASPACE==', '
    msg['Date'] = formatdate(localtime=True)
    msg.attach(MIMEText(text))

    for f in files:
        part = MIMEBase('application', 'octet-stream') #'octet-stream': binary data
        part.set_payload(open(f, 'rb').read())
        encoders.encode_base64(part)
        part.add_header('Content-Disposition', 'attachment; filename="%s"' % os.path.basename(f))
        msg.attach(part)

    import smtplib
    smtp = smtplib.SMTP(server['name'], server['port'])
    smtp.ehlo()
    smtp.starttls()
    smtp.ehlo()
    smtp.login(server['user'], server['passwd'])
    smtp.sendmail(fro, to, msg.as_string())
    smtp.close()

#xx = "D:\\TDD_Report\\personal\\2015-01-09_09.48.10_ad_calc_personal_compare.html"
#new_send_mail("xx", [])

#email_contents = "回头客计算模块版本比对测试完毕\n测试报告地址：\n测试日志地址：\n"
#new_send_mail("xxx", files=[], to_list=['liuzhipengjs@dangdang.com'], contents= email_contents)