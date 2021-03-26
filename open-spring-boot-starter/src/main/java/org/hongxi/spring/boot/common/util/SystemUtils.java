package org.hongxi.spring.boot.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by shenhongxi on 2021/3/26.
 */
public abstract class SystemUtils {

    public static String getProperty(String key) {
        return System.getProperty(key, System.getenv(key));
    }

    public static boolean contains(String key) {
        return StringUtils.isNotBlank(getProperty(key));
    }

    public static String getProperty(String key, String def) {
        String val = getProperty(key);
        return StringUtils.isNotBlank(val) ? val : def;
    }

    public static void setProperty(String key, String val) {
        if (val != null) {
            System.setProperty(key, val);
        }
    }
}
