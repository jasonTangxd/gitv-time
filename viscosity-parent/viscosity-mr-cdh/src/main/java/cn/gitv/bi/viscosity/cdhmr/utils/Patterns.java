package cn.gitv.bi.viscosity.cdhmr.utils;

import java.util.regex.Pattern;

/**
 * Created by ADMIN on 2016/10/18.
 */
public class Patterns {
    public static final Pattern BLANK_PATTERN= Pattern.compile("\\s*|\t|\n|\r");
    public  static final Pattern NO_SEE_PATTERN = Pattern.compile("[\\p{C}\\p{Z}]");
    public static final Pattern VERTICAL_LINE = Pattern.compile("\\|");
    public static Pattern MAC_PATTERN = Pattern.compile("^[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}$");
    public static Pattern WORD_NUM_PATTERN = Pattern.compile("^[a-zA-Z\\d_\\s]*$");
}
