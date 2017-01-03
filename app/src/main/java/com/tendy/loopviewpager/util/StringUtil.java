package com.tendy.loopviewpager.util;

import java.util.List;

public class StringUtil {

    /**
     * 判断字符串是否为null或者空字符串
     */
    public static boolean isNullOrEmpty(String str) {
        boolean result = false;
        if (null == str || "".equals(str.trim())) {
            result = true;
        }
        return result;
    }

    public static boolean isNotNull(String string) {
        return null != string && !"".equals(string.trim());
    }
    /**
     * 判断集合是否是null或size()==0
     */
    public static boolean isNullOrEmpty(List<?> list) {
        boolean result = false;
        if (null == list || list.size() == 0) {
            result = true;
        }
        return result;
    }
}
