#/env/bin python
#-*- coding:utf-8 -*-

import os;
import sys;

svn_path_dirname_list = ["http://svn.dangdang.com/repos/test/mix/mix_front_search","http://svn.dangdang.com/repos/test/mix/mix_admin_search"];

svn_save_path = "/d1/search/";  #��������svn�汾��Ŀ¼

#svn_save_path = "/d1/lixiaolong/";  #��������svn�汾��Ŀ¼

back_up_dir = "";  #����Ŀ¼����

deploy_dir = ""  #������Ŀ¼

deploy_filename = "";  #��������ļ�������

back_up_filename = "";  #�����ļ�������

svn_path = raw_input("������svn��ַ��").strip();  #��ȡsvn·�������������ҿո�

svn_version = os.path.basename(svn_path); #�����ļ����ƣ�����ȡsvn�汾��

svn_path_dirname = os.path.dirname(svn_path); #����Ŀ¼���ƣ�����ȡsvnĿ¼�������ж�svnĿ¼�Ƿ���ȷ


def check_dir(file_name):
    print("����check_dir����");
    try:
        f = open(svn_save_path + file_name,"r");
        try:
            for line in f.readlines():
                if "3." in line: #��ʾsvn_info.txt�ļ��������а汾����Ϣ����ȥ��
                    line=line.replace(svn_version + "/","");
                line=line.strip('\n'); #��Ϊ��ȡ�ļ�ʱ����β����һ�����з������Դ˴���Ҫ�����з����˵�������·����ʶ�𲻳���....
                temp = line.split(" ");
                print("temp=",temp);
                if temp[0] == "At" :     #�ж�svn�汾�Ƿ�Ϊ����,���Ϊ���£��򷵻�һ��"1"���������ҳ�svn���µĵ�һ���ļ�·��������md5У��
                    return "1";
                else:
                    path = temp[len(temp)-1];
                    test = svn_save_path + svn_version + "/" + path;
                    print("test=",test);
                    if os.path.isfile(test):
                        return path;
        finally:
            f.close();
    except IOError,e:
        print("�ļ���ȡʧ�ܣ�����....");
        print e;



if svn_path_dirname not in svn_path_dirname_list:
    print("svn·������ȷ������");
    sys.exit(2);

if "mix_admin_search" in svn_path:  #���ݲ������ǰ̨���̨��������Ӧ�ĳ�����Ŀ¼������Ŀ¼�������ļ���������Ϣ
    back_up_dir = deploy_dir = "/var/www/mix_admin/preview/";
    deploy_filename = "mix_search";
else:
    print("�Ҳ�����������Ҫ�������Ϣ�����飡");
    sys.exit(3);

if svn_version in os.listdir(svn_save_path): #os.listdir(path)����·���������ļ��л��ļ�����
   print("svn�汾���Ѿ����ڣ���ʼupdate svn");
   print("svn up | tee ../svn_info.txt");  #��������ն����������Ϣ��ͬʱ����Ϣ����svn_info.txt�ļ���
   os.chdir(svn_save_path + svn_version); #�л���svnĿ¼
   os.system("svn up | tee ../svn_info.txt");
else:
   os.chdir(svn_save_path); #�л���svn����Ŀ¼
   print("��ʼ��svn���ش������....");
   print("svn co " + svn_path + " | tee svn_info.txt"); #��������ն����������Ϣ��ͬʱ����Ϣ����svn_info.txt�ļ���
   os.system("svn co " + svn_path + " | tee svn_info.txt");
   #���ɱ����ļ������ƣ�ע��˴��õ���os.popen��������Ϊ#os.system����ֻ��������һ����shell����������������κν��
   back_up_filename = deploy_filename + "_" + str(os.popen("date +%Y%m%d").read());
   print("��ʼ�����ļ�...");
   print("cp -ar " + deploy_dir + deploy_filename + " " + back_up_dir + back_up_filename);
   os.system("cp -ar " + deploy_dir + deploy_filename + " " + back_up_dir + back_up_filename); #�����ļ�
   print("�ļ����ݳɹ�...");
   
   date_num_list = [];
   for key in os.listdir(deploy_dir):
      if deploy_filename in key:
          temp = key.split("_");
          date_num = temp[len(temp)-1];
          date_num_list.append(date_num);
   while len(date_num_list) > 5:
      date_num_list.sort();
      print("��ɾ�������ļ���...");
      print("rm -fr " + back_up_dir + deploy_filename + "_" + date_num_list[0]);
      os.system("rm -fr " + back_up_dir + deploy_filename + "_" + date_num_list[0]);
      date_num_list.remove(date_num_list[0]);

print("��ʼ���𻷾�....");
print("rsync -av " + svn_save_path + svn_version + "/" + "preview/" + deploy_filename + "/* " + deploy_dir + deploy_filename + ' --exclude ".svn"');
os.system("rsync -av " + svn_save_path + svn_version + "/" + "preview/" + deploy_filename + "/* " + deploy_dir + deploy_filename + ' --exclude ".svn"');
#print("��ʼͬ��159��������....");
#print("rsync -av " + svn_save_path + svn_version + "/" + deploy_filename + "/* " + "root@10.255.254.159:" +deploy_dir + deploy_filename +' --exclude ".svn"');
#os.system("rsync -av " + svn_save_path + svn_version + "/" + deploy_filename + "/* " + "root@10.255.254.159:" +deploy_dir + deploy_filename +' --exclude ".svn"');

#ͨ��md5ֵ����֤�����Ƿ���ɹ���
check_info = check_dir("svn_info.txt");
if check_info == "1":  #  "1"��ʾsvn�汾�Ѿ������µģ����κ���Ϣ���£���������md5У��...
    print("svn�������°汾��û���ļ����Ը��£���ȷ�Ͽ������ύsvn��ллO(��_��)O~");
else:
    if check_info is None:
        print("����������û���ļ�,�뵽svn����Ŀ¼�鿴svn_info.txt���svn������Ϣ��ллO(��_��)O~");
    print("md5sum " + svn_save_path + svn_version + "/" + check_info);
    svn_md5 = str(os.popen("md5sum " + svn_save_path + svn_version + "/" + check_info).read());
    print svn_md5;

    temp = svn_md5.split(" ");
    str_svn_md5 = temp[0];
    print("md5sum " + "/var/www/mix_admin/" + check_info);
    #deploy_md5 = str(os.popen("md5sum " + "/var/www/mix_admin/" + check_info[(check_info.index("/")+1):]).read());
    deploy_md5 = str(os.popen("md5sum " + "/var/www/mix_admin/" + check_info).read());
    print deploy_md5;

    temp = deploy_md5.split(" ");
    str_deploy_md5 = temp[0];
    if str_svn_md5 == str_deploy_md5:
        print("�������³ɹ���O(��_��)O~");
    else:
        print("�������������⣬����....");

