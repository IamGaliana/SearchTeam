 # -*- coding: UTF-8 -*-
#!/usr/bin/env python

import tornado.ioloop
import tornado.web
import MySQLdb
import paramiko
import socket
import struct
import time


host = '10.255.242.72'
port = 22
user = 'root'
passwd = '10test72a'


def ssh(host, port, user, password, cmd):

    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(host,port,user,password)
    stdin,stdout,stderr = ssh.exec_command(cmd)
    str1 = stdout.readlines()
    return str1

def getdb(id,ip):

    try:
         conn = MySQLdb.connect(host='10.255.255.22',user='writeuser',passwd='ddbackend',db='adsmart_log',charset='utf8')
    except:
        print("连接数据库错误")

    cur = conn.cursor()

    clicktime = time.time()
    if (id == "" and ip ==""):
        sql = "select * from ad_log_cpc where click_time between %s and %s" % (clicktime - 300, clicktime)
    elif (id == "" and ip !=""):
        sql = "select * from ad_log_cpc where ip=%s and click_time between %s and %s" % (ip, clicktime - 300, clicktime)
    elif (id != "" and ip ==""):
        sql = "select * from ad_log_cpc where ad_info_id=%s and click_time between %s and %s" % (id, clicktime - 300, clicktime)
    else:
        sql = "select * from ad_log_cpc where ad_info_id=%s and ip=%s and click_time between %s and %s" % (id, ip, clicktime - 300, clicktime)
    cur.execute(sql)
    ret = cur.fetchall()
    cur.close()
    conn.commit()
    conn.close()
    return ret

def convert_time(timeStamp):
    timeArray = time.localtime(timeStamp)
    mytime = time.strftime("%Y-%m-%d %H:%M:%S",timeArray)
    return mytime


def gen_ad_ret(ad_list):

    ad_ret = []
    for each in ad_list:
        click_info = {}
        click_info['ad_info_id'] = each[1]
        clicktime = convert_time(each[2])
        click_info['click_time'] = clicktime
        ip = socket.inet_ntoa(struct.pack('I',socket.htonl(int(each[3]))))
        click_info['pip'] = ip
        if (each[4] == ""):
            click_info['keyword'] = "无"
        else:
            click_info['keyword'] = each[4]
        click_info['cost'] = int(each[6])/10000
        if (each[7] == 0):
            click_info['status'] = "正常"
        elif (each[7] == 1):
            click_info['status'] = "扣费超过3次"
        elif (each[7] == -1):
            click_info['status'] = "失败"
        click_info['pos_id'] = each[9]
        ad_ret.append(click_info)
    return ad_ret


class MainHandler(tornado.web.RequestHandler):

    def get(self):
        x_btn = self.get_argument("btn", "1")
        x_adid = self.get_argument("ad_id", '')
        x_perid = self.get_argument("per_id", '')
        x_pip = self.get_argument("p_ip", '')
        list = []
        if (x_pip!=""):
            try:
                ipint = socket.ntohl(struct.unpack("i",socket.inet_aton(x_pip))[0])
            except:
                ipint = 111
        else:
            ipint = ""

        if x_btn == '1':
            cmd = "rm -rf /data/adsmart/log/*"
            ssh(host, port, user, passwd, cmd)
            self.render("click_info.html",
                        ad_id = "",
                        per_id = "",
                        p_ip = "",
                        show_log = "",
                        ret = [])

        if x_btn == '2':
            time.sleep(10)
            result = getdb(x_adid,ipint)
            ad_ret = gen_ad_ret(result)
            tm = time.strftime("%Y%m%d",time.localtime(time.time()))
            if (x_adid!="" and x_perid!="" and ipint!=""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s|grep %s|grep %s" % (tm, x_adid, x_perid, ipint)
            elif (x_adid=="" and x_perid!="" and ipint!=""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s|grep %s" % (tm, x_perid, ipint)
            elif (x_adid!="" and x_perid=="" and ipint!=""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s|grep %s" % (tm, x_adid, ipint)
            elif (x_adid!="" and x_perid!="" and ipint==""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s|grep %s" % (tm, x_adid, x_perid)
            elif (x_adid=="" and x_perid=="" and ipint!=""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s" % (tm, ipint)
            elif (x_adid=="" and x_perid!="" and ipint==""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s" % (tm, x_perid)
            elif (x_adid!="" and x_perid=="" and ipint==""):
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click |grep %s" % (tm, x_adid)
            else:
                cmd = "cat /data/adsmart/log/%s* |grep cpc_click" % (tm)

            x_log = ssh(host, port, user, passwd, cmd)
            for i in range(0, x_log.__len__()):
                zidian = {}
                str_1 = x_log[i].split("	")

                mytime = convert_time(float(str_1[0]))
                zidian["lclick_time"] = mytime
                ipint = socket.inet_ntoa(struct.pack('I',socket.htonl(int(str_1[1]))))
                zidian["lip"] = ipint
                zidian["lpermanent_id"] = str_1[3]
                zidian["ladid"] = str_1[4]
                zidian["lcost"] = int(str_1[5])/10000
                print zidian["lcost"]
                zidian["lthroughid"] = str_1[14]
                zidian["lurl"] = str_1[16]
                list.append(zidian)

            self.render("click_info.html",
                         ad_id = x_adid,
                         per_id = x_perid,
                         p_ip = x_pip,
                         show_log = list,
                         ret = ad_ret)


def main():

    application = tornado.web.Application([(r"/", MainHandler),])
    application.listen(8915)
    tornado.ioloop.IOLoop.instance().start()

if __name__ == "__main__":
    main()
