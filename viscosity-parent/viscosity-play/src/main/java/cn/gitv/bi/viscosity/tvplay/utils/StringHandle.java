package cn.gitv.bi.viscosity.tvplay.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Splitter;

public class StringHandle {
	public static String str_join(String str1, String str2) {
		StringBuilder sb = new StringBuilder();
		return sb.append(str1).append("|").append(str2).toString();
	}
	public static List<String> str_split(String content){
		return Splitter.on('|').trimResults().splitToList(content);
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
