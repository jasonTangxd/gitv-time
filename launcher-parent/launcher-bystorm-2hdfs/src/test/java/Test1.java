import static cn.gitv.bi.launcher.beanparse.constant.Constant.topicPrefix;

/**
 * Created by Kang on 2016/12/2.
 */
public class Test1 {
    public static void main(String args[]){
        String topic="launcher-log-1";
        String Action=topic.substring(topicPrefix.length(),topic.length());
        System.out.println(Action);
    }
}
