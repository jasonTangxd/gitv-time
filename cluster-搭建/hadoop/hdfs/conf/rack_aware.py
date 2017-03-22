#!/usr/bin/python  
#-*-coding:UTF-8 -*-  
import sys

rack = {"master2.gitv.rack2.bk":"rack2",
"slave7.gitv.rack2.bk":"rack2",
"slave8.gitv.rack2.bk":"rack2",
"slave9.gitv.rack2.bk":"rack2",
"slave10.gitv.rack2.bk":"rack2",
"slave11.gitv.rack2.bk":"rack2",
"slave1.gitv.rack1.bk":"rack1",
"slave2.gitv.rack1.bk":"rack1",
"slave3.gitv.rack1.bk":"rack1",
"slave4.gitv.rack1.bk":"rack1",
"slave5.gitv.rack1.bk":"rack1",
"slave6.gitv.rack1.bk":"rack1",
"master1.gitv.rack1.bk":"rack1",
"10.10.121.138":"rack2",
"10.10.121.119":"rack2",
"10.10.121.120":"rack2",
"10.10.121.121":"rack2",
"10.10.121.122":"rack2",
"10.10.121.123":"rack2",
"10.10.121.148":"rack1",
"10.10.121.149":"rack1",
"10.10.121.150":"rack1",
"10.10.121.151":"rack1",
"10.10.121.152":"rack1",
"10.10.121.153":"rack1",
"10.10.121.139":"rack1",
} 

if __name__=="__main__":  
    print "/" + rack.get(sys.argv[1],"rack0")
