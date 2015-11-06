# -*- coding: UTF-8 -*-'
import paramiko

def ssh(host, port, user, password, cmd):

    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(host,port,user,password)
    stdin,stdout,stderr = ssh.exec_command(cmd)
    str1 = stdout.readlines()
    return str1



#host = '10.255.242.72'
#port = 22
#user = 'root'
#passwd = '10test72a'
#cmd = "cat /data/adsmart/joblog/lazy_logs/ad_log"
#str = ssh(host, port, user, passwd, cmd)
#print len(str)
#
#str_1 = str[1].split(" ")
#print str_1