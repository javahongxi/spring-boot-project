package org.hongxi.spring.boot.web.constants;

/**
 * Created by shenhongxi on 2021/3/27.
 */
public class WebConstants {

    public static final int WEB_MONITOR_FILTER_ORDER = -99;
    public static final int WEB_SESSION_FILTER_ORDER = -90;
    public static final int WEB_CRYPTO_FILTER_ORDER = -80;

    public static final String[] EXCLUDE_PATHS = new String[] {
            "/error",
            "/error/**",
            "/actuator/**",
            "/openboot/**"
    };

    public static final String URI_PATTERN_ATTR = qualify("uriPattern");

    private static String qualify(String attr) {
        return WebConstants.class.getName() + "." + attr;
    }
}