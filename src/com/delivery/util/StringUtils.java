package com.delivery.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean matches(String pattern, String text) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);

        return m.find();
    }

    public static boolean areEquals(String str1, String str2) {
        if (str1 == str2) return true;
        if (str1 == null && str2 == null) return true;
        if (str1 != null && str1.equals(str2)) return true;
        return false;
    }
}
