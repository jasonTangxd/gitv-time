# coding=utf-8
__author__ = 'Kang'
import os
from cassandra.cluster import Cluster
#调用时间模块
import datetime
today =datetime.date.today()
cluster = Cluster(['10.10.121.139','10.10.121.138','10.10.121.148', '10.10.121.122','10.10.121.123'])
session = cluster.connect('userinfo')
root_path='/root/user_info/update_part'
#设置输出格式
ISOFORMAT='%Y-%m-%d'
str_today=today.strftime(ISOFORMAT)

def work_csv(file):
    try:
        file_name = '%s/%s' % (root_path, file)
        pn_csv = open(file_name, 'r')
        for line in pn_csv:
            words = line.strip('\n').split(',')
            partner = words[0]
            mac = words[1]
            user_type=0
            status=0
            try:
                user_type = int(words[2])
                status = int(words[3])
            except Exception:
                continue
            rows = session.execute('''select count(status) from user_info where partner=%s and mac_addr=%s;''',
                                   (partner, mac))
            exists_status = 0
            if rows:
                exists_status = rows[0].system_count_status
            #判断是否存在这条记录
            if exists_status==0:
                # 含有0说明不存在这条记录,这条信息不做操作
                pass
            else:
                rows=session.execute('''select count(*) from user_info where partner=%s and mac_addr=%s and status=%s ALLOW FILTERING;''',
                                     (partner, mac,status))
                need_update=0
                if rows:
                    need_update=rows[0][0]
                if need_update==1:
                    #说明记录没有变化,不需要变化
                    pass
                else:
                    session.execute('''UPDATE user_info SET status = %s,status_updatetime=%s where partner=%s and mac_addr=%s;''',(status,str_today, partner,mac))
                session.execute('''UPDATE user_info SET user_type=%s where partner=%s and mac_addr=%s;''',(user_type,partner,mac))
        pn_csv.close()
    except Exception as e:
        os.system("echo '%s'|mail -s \"exception in onedayupcas\" 122726894@qq.com"%(e))


if __name__ == "__main__":
    for root, dirs, files in os.walk(root_path):
        pass
    for file in files:
        work_csv(file)

