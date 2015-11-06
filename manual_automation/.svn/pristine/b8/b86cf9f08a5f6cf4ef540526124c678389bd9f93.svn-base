# -*- coding: UTF-8 -*-'
import paramiko

def ssh(host, port, user, password, cmd):

    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(host,port,user,password)
    stdin,stdout,stderr = ssh.exec_command(cmd)
    str1 = stdout.readlines()
    return str1



#host = '10.255.255.96'
#port = 22
#user = 'root'
#passwd = '10Test96'
#cmd = "cd /var/www/hosts/uniontestall/union/hosts/app/calc_module/cron/; pwd;php run_job.php -j 'Order::pullOrderOrders' -s '2014-05-21 08:59:00' -e '2014-05-21 09:01:00'"
#print ssh(host, port, user, passwd, cmd)