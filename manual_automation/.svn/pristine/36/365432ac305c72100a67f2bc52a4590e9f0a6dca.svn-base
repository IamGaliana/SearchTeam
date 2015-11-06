#!/usr/bin/env python
# -*- coding: cp936 -*-
#author:lixiaolong
#date:2014-11-10
#function:kill process about dispatcher

import os;

need_kill_process_list = ["dispatcher","modules","AllServer","update_correct_data","reader_for_search"];
pid_list = [];

def KillProcess():
  '''查找有关dispatcher的进程，然后kill掉'''
  os.system("ps -aux >> a.txt");
  try:
      fobj = open("a.txt","r");
      for line in fobj.readlines():
          for key in need_kill_process_list:
              if key in line:
                  temp_list = line.split();
                  pid_list.append(temp_list[1]);
                  print line;
      for pid in pid_list:
          os.system("kill -9 %s" % pid);
      print("kill over");
  except IOError:
      print("reading file failure,please check it...");
  except IndexError:
      print("list index out of range,please check it...");
  except:
      print("there are other error,please check it...");
  
  finally:
      fobj.close();
      os.system("rm -fr a.txt");


if __name__=="__main__":
  KillProcess();
