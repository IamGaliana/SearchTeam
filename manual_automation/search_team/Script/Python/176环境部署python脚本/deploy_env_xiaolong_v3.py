#/env/bin python
#-*- coding:utf-8 -*-

import os;
import sys;

svn_path_dirname_list = ["http://svn.dangdang.com/repos/test/mix/mix_front_search","http://svn.dangdang.com/repos/test/mix/mix_admin_search"];

svn_save_path = "/d1/Mix_Search/";  #本机保存svn版本的目录

#svn_save_path = "/d1/lixiaolong/";  #本机保存svn版本的目录

back_up_dir = "";  #备份目录名称

deploy_dir = ""  #程序部署目录

deploy_filename = "";  #部署程序文件夹名称

back_up_filename = "";  #备份文件夹名称

svn_path = raw_input("请输入svn地址：").strip();  #获取svn路径，并过滤左右空格

svn_version = os.path.basename(svn_path); #返回文件名称，即获取svn版本号

svn_path_dirname = os.path.dirname(svn_path); #返回目录名称，即获取svn目录，用以判断svn目录是否正确


def check_dir(file_name):
    print("进入check_dir函数");
    try:
        f = open(svn_save_path + file_name,"r");
        try:
            for line in f.readlines():
                if "3." in line: #表示svn_info.txt文件内容中有版本号信息，需去掉
                    line=line.replace(svn_version + "/","");
                line=line.strip('\n'); #因为读取文件时，行尾会多出一个换行符，所以此处需要将换行符过滤掉，否则路径会识别不出来....
                temp = line.split(" ");
                if temp[0] == "At" :     #判断svn版本是否为最新,如果为最新，则返回一个"1"，否则则找出svn更新的第一个文件路径，用于md5校验
                    return "1";
                else:
                    path = temp[len(temp)-1];
                    test = svn_save_path + svn_version + "/" + path;
                    if os.path.isfile(test):
                        return path;
        finally:
            f.close();
    except IOError,e:
        print("文件读取失败，请检查....");
        print e;



if svn_path_dirname not in svn_path_dirname_list:
    print("svn路径不正确，请检查");
    sys.exit(2);

if "mix_front_search" in svn_path:  #根据部署的是前台或后台，生成相应的程序部署目录、备份目录、部署文件夹名称信息
    back_up_dir = deploy_dir = "/var/www/";
    deploy_filename = "Mix_Search";
else:
    print("找不到程序部署需要的相关信息，请检查！");
    sys.exit(3);

if svn_version in os.listdir(svn_save_path): #os.listdir(path)返回路径下所有文件夹或文件名称
   print("svn版本号已经存在，开始update svn");
   print("svn up | tee ../svn_info.txt");  #此命令即在终端输出更新信息，同时将信息输入svn_info.txt文件中
   os.chdir(svn_save_path + svn_version); #切换至svn目录
   os.system("svn up | tee ../svn_info.txt");
else:
   os.chdir(svn_save_path); #切换至svn保存目录
   print("开始从svn下载代码程序....");
   print("svn co " + svn_path + " | tee svn_info.txt"); #此命令即在终端输出更新信息，同时将信息输入svn_info.txt文件中
   os.system("svn co " + svn_path + " | tee svn_info.txt");
   #生成备份文件夹名称，注意此处用的是os.popen函数，因为#os.system函数只是启用了一个子shell运行命令，并不返回任何结果
   back_up_filename = deploy_filename + "_" + str(os.popen("date +%Y%m%d").read());
   print("开始备份文件...");
   print("cp -ar " + deploy_dir + deploy_filename + " " + back_up_dir + back_up_filename);
   os.system("cp -ar " + deploy_dir + deploy_filename + " " + back_up_dir + back_up_filename); #备份文件
   print("文件备份成功...");
   
   date_num_list = [];
   for key in os.listdir(deploy_dir):
      if deploy_filename in key:
          temp = key.split("_");
          date_num = temp[len(temp)-1];
          date_num_list.append(date_num);
   while len(date_num_list) > 5:
      date_num_list.sort();
      print("该删除备份文件啦...");
      print("rm -fr " + back_up_dir + deploy_filename + "_" + date_num_list[0]);
      os.system("rm -fr " + back_up_dir + deploy_filename + "_" + date_num_list[0]);
      date_num_list.remove(date_num_list[0]);

print("开始部署环境....")
print("rsync -av " + svn_save_path + svn_version + "/" + deploy_filename + "/* " + deploy_dir + deploy_filename +' --exclude ".svn"');
os.system("rsync -av " + svn_save_path + svn_version + "/" + deploy_filename + "/* " + deploy_dir + deploy_filename +' --exclude ".svn"' );
print("开始同步159机器程序....");
print("rsync -av " + svn_save_path + svn_version + "/" + deploy_filename + "/* " + "root@10.255.254.159:" +deploy_dir + deploy_filename +' --exclude ".svn"');
os.system("rsync -av " + svn_save_path + svn_version + "/" + deploy_filename + "/* " + "root@10.255.254.159:" +deploy_dir + deploy_filename +' --exclude ".svn"');

#通过md5值，验证环境是否部署成功。
check_info = check_dir("svn_info.txt");
if check_info == "1":  #  "1"表示svn版本已经是最新的，无任何信息更新，所以无需md5校验...
    print("svn已是最新版本，没有文件可以更新，请确认开发已提交svn，谢谢O(∩_∩)O~");
else:
    if check_info is None: #表示svn_info.txt文件内容中有版本号信息，需去掉
        print("更新内容中没有文件,请到svn保存目录查看svn_info.txt检查svn更新信息，谢谢O(∩_∩)O~");
    print("md5sum " + svn_save_path + svn_version + "/" + check_info);
    svn_md5 = str(os.popen("md5sum " + svn_save_path + svn_version + "/" + check_info).read());
    print svn_md5;

    temp = svn_md5.split(" ");
    str_svn_md5 = temp[0];

    print("md5sum " + deploy_dir + check_info);
    deploy_md5 = str(os.popen("md5sum " + deploy_dir + check_info).read());
    print deploy_md5;

    temp = deploy_md5.split(" ");
    str_deploy_md5 = temp[0];
    if str_svn_md5 == str_deploy_md5:
        print("环境更新成功，O(∩_∩)O~");
    else:
        print("环境部署有问题，请检查....");


