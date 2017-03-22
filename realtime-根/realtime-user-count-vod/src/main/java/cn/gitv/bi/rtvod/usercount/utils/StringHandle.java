package cn.gitv.bi.rtvod.usercount.utils;

import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class StringHandle {
    /**
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 拼接两者以后的字符串
     */
    public static String str_join(String str1, String str2) {
        StringBuilder sb = new StringBuilder();
        return sb.append(str1).append("|").append(str2).toString();
    }

    /**
     * @param strs 字符串数组
     * @return 拼接以后的结果
     */
    public static String str_join(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String item : strs) {
            sb.append(item).append("|");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * @param content 字符串
     * @return 切分content以后的list
     */
    public static List<String> str_split(String content) {
        return Splitter.on('|').splitToList(content);
    }

    /**
     * @param fileds 字符串数组
     * @return 判断所有字符串是否符合规则
     */
    public static boolean isLegalField(String... fileds) {
        for (String field : fileds) {
            if (field == null || field.equalsIgnoreCase("null") || StringUtils.isBlank(field)) {
                return false;
            }
        }
        return true;
    }
}
