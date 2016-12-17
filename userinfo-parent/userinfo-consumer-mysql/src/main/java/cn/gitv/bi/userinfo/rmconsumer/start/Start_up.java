package cn.gitv.bi.userinfo.rmconsumer.start;

import cn.gitv.bi.userinfo.rmconsumer.thread.TimeConditionTask;
import cn.gitv.bi.userinfo.rmconsumer.utils.ZkObserverUtils;

import java.util.Timer;

import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.DELAY_TIME;
import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.PERIOD_TIME;

/**
 *
 */
public class Start_up {
    // private static Logger log = LoggerFactory.getLogger(Start_up.class);

    public static void main(String[] args) throws Exception {
        //根据可操作partnerList启动定时器
        for (String routingKey : ZkObserverUtils.getCanUsePartnerList()) {
            new Timer().scheduleAtFixedRate(new TimeConditionTask(routingKey), DELAY_TIME, PERIOD_TIME);
        }
    }


}
