package cn.gitv.bi.launcher.beanparse.constant;

import java.util.regex.Pattern;

/**
 * Created by Kang on 2016/11/14.
 */
public interface Constant {
    String LOGBEAN_SEPARATOR="|";
    Pattern PT = Pattern.compile(LOGBEAN_SEPARATOR);
    String topicPrefix = "launcher-log-";
}
