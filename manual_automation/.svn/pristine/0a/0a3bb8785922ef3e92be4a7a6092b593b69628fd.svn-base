#! /usr/bin/env Python
import os
import time

# http://svn.dangdang.com/repos/test/search/search_v3/query_pack/3.1.1.23
# query_pack
# 3.1.1.23
latest_svn_path = raw_input('Please input svn path:').strip()
module_name = ''
if latest_svn_path != '':
    module_name = latest_svn_path.lstrip('http://').split('/')[5]
else:
    print 'Did not find module name, please check and re-type in svn path!'
    exit(-1)
latest_version = os.path.basename(latest_svn_path)

backup_to_path = '/d1/dispatcher/modules/'
backup_from_path = '/usr/local/dispatcher/bin/modules/'
svn_checkout_path = '/d1/search/search_v3/'
deploy_path = '/usr/local/'
date = time.strftime("%Y%m%d", time.localtime(time.time()))

# make sure svn path contains module name
if module_name in latest_svn_path:
    # back up
    print '\n' + '*' * 30
    print 'start to back up module %s... ...' % module_name
    backup_command = 'cp -r %s  %s' % (backup_from_path + module_name, backup_to_path + module_name + '_' + date)
    print (backup_command)
    if os.system(backup_command) != 0:
        print 'back up module %s is failed ' % module_name
        exit(-1)
    else:
        print ('back up succeed!')
    print '*' * 30 + '\n'

    # svn update/check out
    print '*' * 30
    if latest_version in os.listdir(svn_checkout_path + module_name):
        code_path = svn_checkout_path + module_name + '/' + latest_version     # /d1/search/search_v3/query_pack/3.1.1.2
        print 'svn version exists, update code to %s... ...' % code_path
        os.chdir(code_path)
        if os.system('svn up') != 0:
            print 'svn update is failed under %s' % svn_checkout_path
            exit(-1)
        else:
            print ('update succeed!')
    else:
        code_path = svn_checkout_path + module_name                            # /d1/search/search_v3/query_pack
        print 'start to check out code to %s... ...' % code_path
        os.chdir(code_path)
        checkout_command = 'svn co %s' % latest_svn_path
        if os.system(checkout_command) != 0:
            print 'svn check out is failed under %s  ' % svn_checkout_path
            exit(-1)
        else:
            print ('check out succeed!')
    print '*' * 30 + '\n'

    # deploy
    print '*' * 30
    print 'start to deploy module %s... ...' % module_name
    source_path = svn_checkout_path + module_name + '/' + latest_version    # /d1/search/(search_v3)/query_pack/3.1.1.2

    # some svn paths have following format: http://svn.dangdang.com/repos/test/search/search_v3/list_ranking/3.1.1.6/usr/local/...
    # need to give new value to deploy_path
    for dirs in os.walk(source_path):
        for dirname in dirs:
            if 'usr' in dirname or 'local' in dirname:
                deploy_path = '/'
                break

    deploy_command = 'rsync -av %s/* %s  --exclude ".svn"' % (source_path, deploy_path)
    print (deploy_command)
    if os.system(deploy_command) != 0:
        print 'deploy module %s is failed' % module_name
        exit(-1)
    else:
        print ('deploy succeed!')
    print '*' * 30 + '\n'
else:
    print ('Svn path does not contain this module name!')
    exit(-1)

