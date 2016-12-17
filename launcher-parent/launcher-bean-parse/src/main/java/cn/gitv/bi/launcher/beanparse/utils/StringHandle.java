package cn.gitv.bi.launcher.beanparse.utils;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

import static cn.gitv.bi.launcher.beanparse.constant.Constant.LOGBEAN_SEPARATOR;

public class StringHandle {
    public static String str_join(String str1, String str2) {
        StringBuilder sb = new StringBuilder();
        return sb.append(str1).append(LOGBEAN_SEPARATOR).append(str2).toString();
    }

    public static String str_join(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String item : strs) {
            sb.append(item).append(LOGBEAN_SEPARATOR);
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public static List<String> str_split(String content) {
        return Splitter.on(LOGBEAN_SEPARATOR).trimResults().splitToList(content);
    }

    public static String removePattern(final String source, final Pattern regexPattern) {
        return regexPattern.matcher(source).replaceAll(StringUtils.EMPTY);
    }


    public static String replaceAll(final String source, final Pattern regexPattern, final String replace) {
        return regexPattern.matcher(source).replaceAll(replace);
    }

    public static String getPrintStringAndReplaceAll(String content, Pattern pattern, final String replace) {
        if (content == null) {
            return null;
        }
        return replaceAll(Patterns.NO_SEE_PATTERN.matcher(content).replaceAll(""), pattern, replace);
    }

    public static String getPrintString(String content) {
        return Patterns.NO_SEE_PATTERN.matcher(content).replaceAll("");
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
