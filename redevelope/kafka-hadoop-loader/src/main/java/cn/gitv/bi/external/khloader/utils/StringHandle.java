package cn.gitv.bi.external.khloader.utils;

import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class StringHandle {
    public static String str_join(String str1, String str2) {
        StringBuilder sb = new StringBuilder();
        return sb.append(str1).append("|").append(str2).toString();
    }

    public static String fullPaht(String basePath, String fileName) {
        StringBuilder sb = new StringBuilder();
        return sb.append(basePath).append("/").append(fileName).toString();
    }

    public static String str_join(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String item : strs) {
            sb.append(item).append("|");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public static List<String> str_token_split(String content, String SEPARATOR) {
        Iterator<String> it = Splitter.on(SEPARATOR).trimResults().split(content).iterator();
        List<String> back = new ArrayList<String>();
        while (it.hasNext()) {
            back.add(it.next());
        }
        return back;
    }

    public static String removePattern(final String source, final Pattern regexPattern) {
        return regexPattern.matcher(source).replaceAll(StringUtils.EMPTY);
    }


    public static String replaceAll(final String source, final Pattern regexPattern, final String replace) {
        return regexPattern.matcher(source).replaceAll(replace);
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
