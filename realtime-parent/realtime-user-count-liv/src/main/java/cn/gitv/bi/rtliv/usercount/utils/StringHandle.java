package cn.gitv.bi.rtliv.usercount.utils;

import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;
import java.util.List;

public class StringHandle {
    public static String str_join(String str1, String str2) {
        StringBuilder sb = new StringBuilder();
        return sb.append(str1).append("|").append(str2).toString();
    }

    public static String str_join(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String item : strs) {
            sb.append(item).append("|");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public static List<String> str_split(String content) {
        return Splitter.on('|').splitToList(content);
    }

    public static boolean isLegalField(String... fileds) {
        for (String field : fileds) {
            if (field == null || field.equalsIgnoreCase("null") || StringUtils.isBlank(field)) {
                return false;
            }
        }
        return true;
    }
}
