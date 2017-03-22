package cn.gitv.bi.userinfo.rmconsumer.utils;

import org.apache.curator.framework.CuratorFramework;

import java.util.ArrayList;
import java.util.List;

import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.UIF_PATH;

/**
 * Created by Kang on 2016/12/7.
 */
public class ZkObserverUtils {
    private static CuratorFramework zkclient = CuratorTools.getSimpleCurator();

    public static List<String> getCanUsePartnerList() {
        List<String> partnerList = new ArrayList<>();
        try {
            List<String> child_list = zkclient.getChildren().forPath(UIF_PATH);// 获取每个item的内容
            for (String item : child_list) {
                if (partnerTrue(item)) {
                    // 若为true,加入到遍历的集合中,获取可以同步的合作伙伴
                    partnerList.add(item);
                }
            }
        } catch (Exception e) {
//?
        }
        return partnerList;
    }

    private static boolean partnerTrue(String partner) {
        String result = null;
        try {
            result = new String(zkclient.getData().forPath(UIF_PATH + "/" + partner), "utf-8");
        } catch (Exception e) {
            // log.error("Start_up:"+Thread.currentThread().getName()+":"+e.getMessage());
        }
        return result.equals("true") ? true : false;
    }
}
