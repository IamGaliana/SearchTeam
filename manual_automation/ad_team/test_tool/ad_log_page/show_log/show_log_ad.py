 # -*- coding: UTF-8 -*-
import tornado.ioloop
import tornado.web
import paramiko
import socket
import struct


host = '10.3.255.100'
port = 22
user = 'root'
passwd = '10test100a'
# host = '10.255.255.93'
# port = 22
# user = 'root'
# passwd = '10test93a'


def ssh(host, port, user, password, cmd):

    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(host,port,user,password)
    stdin,stdout,stderr = ssh.exec_command(cmd)
    str1 = stdout.readlines()
    return str1

class MainHandler(tornado.web.RequestHandler):

    def get(self):
        x_btn = self.get_argument("btn", "1")
        x_perid = self.get_argument("per_id", '')
        x_posid = self.get_argument("pos_id", '')
        list = []
        ad_id_list = []

        if x_btn == '1':
            cmd = "echo > /data/adsmart/joblog/lazy_logs/ad_log"
            ssh(host, port, user, passwd, cmd)
            self.render("show_log_ad.html",
                        per_id = "",
                        pos_id = "",
                        show_log = "",
                        ad_id_list = [])

        if x_btn == '2':
            if x_perid != "" and x_posid !="":
                cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log | grep permanent_id=%s | grep pos=%s" % (x_perid, x_posid)
            elif x_perid == "" and x_posid != "":
                cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log | grep pos=%s" % x_posid
            elif x_posid == "" and x_perid != "":
                cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log | grep permanent_id=%s" % x_perid
                # cmd = "cat /d1/adsmart/joblog/lazy_logs/ad_log | grep permanent_id=%s" % x_perid
            elif x_posid == "" and x_perid == "":
                cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log"
            print cmd
            x_log = ssh(host, port, user, passwd, cmd)

            if cmd == "cat /data/adsmart/joblog/lazy_logs/ad_log":
                g = 1
            else:
                g = 0

            for i in range(g, x_log.__len__()):
                zidian = {}
                str_1 = x_log[i].split(" ")
                if len(str_1) == 4:
                    zidian["time"]=str_1[0]
                    zidian["url"]=str_1[3]
                    b = str_1[2].split("&")
                if len(str_1) == 5:
                    zidian["time"]=str_1[0]
                    zidian["url"]=str_1[5]
                    b = str_1[3].split("&")
                for i in b:
                    x = i.split("=")
                    zidian[x[0]]=x[1]

                zidian['ip'] = socket.inet_ntoa(struct.pack('I',socket.htonl(int(zidian['ip']))))

                try:
                    ids = zidian['ad_id'].split(",")

                    for j in ids:
                        if j == "":
                            break
                        # y = j[2:]
                        ad_id_list.append(j)
                except:
                    # y = (zidian['ad_id'])[2:]
                    ad_id_list.append(zidian['ad_id'])

                list.append(zidian)

            for i in range(0, len(ad_id_list)):
                str1 = (u" |第%s名:" % int(i+1)) + str(ad_id_list[i])
                ad_id_list[i] = str1

            self.render("show_log_ad.html",
                        per_id = x_perid,
                        pos_id = x_posid,
                        show_log = list,
                        ad_id_list = ad_id_list)

def main():

    application = tornado.web.Application([(r"/", MainHandler),])
    application.listen(8916)
    tornado.ioloop.IOLoop.instance().start()

if __name__ == "__main__":

        main()
