# coding=utf-8
import os
import time

import psutil

print "Welcome,current system is", os.name, " 3 seconds late cn.bi.gitv.hip.parquetdemo.start to get data"
print """"""
time.sleep(3)

line_num = 1


# 获得cpu信息
def get_cpu_state(interval=1):
    # interval 间隔时间s
    return " CPU: %s %%" % str(psutil.cpu_percent(interval))


# 获得内存信息
def get_memory_state():
    phy_mem = psutil.virtual_memory()
    line = "Memory: %5s%% %6s/%s" % (phy_mem.percent, str(int(phy_mem.used / 1024 / 1024)) + "M",
                                     str(int(phy_mem.total / 1024 / 1024)) + "M"
                                     )
    return line


# 类似于数据大小-h的功能
def bytes2human(n):
    """
    >>>bytes2human(10000)
    '9.8k'
    >>>bytes2human(100001221)
    '95.4M'
    """
    symbols = ('K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y')
    prefix = {}
    for i, s in enumerate(symbols):
        prefix[s] = 1 << (i + 1) * 10
    for s in reversed(symbols):
        if n >= prefix[s]:
            value = float(n) / prefix[s]
            return '%.2f %s' % (value, s)
    return '%.2fB' % (n)


def poll(interval):
    """Retrieve raw stats within an interval window."""
    tot_before = psutil.net_io_counters()
    pnic_before = psutil.net_io_counters(pernic=True)
    # sleep some time
    time.sleep(interval)
    tot_after = psutil.net_io_counters()
    pnic_after = psutil.net_io_counters(pernic=True)
    # get cpu stats
    cpu_state = get_cpu_state(interval)
    # get memory
    memory_state = get_memory_state()
    return (tot_before, tot_after, pnic_before, pnic_after, cpu_state, memory_state)


def refresh_window(tot_before, tot_after, pnic_before, pnic_after, cpu_state, memory_state):
    """print stats on screen"""
    # print current time,cpu state,memory
    base_info=str(time.asctime() + " | " + cpu_state + " | " +memory_state)
    print base_info
    # total
    print("-"*len(base_info)+"\r\n")
    print("[网络情况]:")
    print("-"*len(base_info))
    print(" total bytes:  sent: %-10s received: %s" % ( \
        bytes2human(tot_after.bytes_sent), \
        bytes2human(tot_after.bytes_recv)))
    print(" total packets:  sent: %-10s received: %s" % ( \
        tot_after.packets_sent, \
        tot_after.packets_recv))
    # per-network interface details: let's sort network interfaces so
    # that the ones which generated more traffic are shown first
    print("-"*len(base_info))
    nic_names = pnic_after.keys()
    # nic_names.sort(key=lambda x: sum(pnic_after[x]), reverse=True)
    for name in nic_names:
        stats_before = pnic_before[name]
        stats_after = pnic_after[name]
        templ = "%-15s %15s %15s"
        print(templ % (name, "TOTAL", "PER-SEC"))
        print(templ % (
            "bytes-sent",
            bytes2human(stats_after.bytes_sent),
            bytes2human(stats_after.bytes_sent - stats_before.bytes_sent) +
            '/s',
        ))
        print(templ % (
            "bytes-recv",
            bytes2human(stats_after.bytes_recv),
            bytes2human(stats_after.bytes_recv - stats_before.bytes_recv)
            + '/s',
        ))
        print(templ % (
            "pkts-sent",
            stats_after.packets_sent,
            stats_after.packets_sent - stats_before.packets_sent,
        ))
        print((templ % (
            "pkts-recv",
            stats_after.packets_recv,
            stats_after.packets_recv - stats_before.packets_recv,
        )))
        print(" ")

if __name__ == "__main__":
    try:
        interval = 0
        while 1:
            args = poll(interval)
            refresh_window(*args)
            interval = 1
    except (KeyboardInterrupt, SystemExit):
        pass

