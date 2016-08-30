package com.pop.util;

/**
 * Created by xugang on 16/8/24.
 */
public class StringUtil {
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
