package org.hongxi.spring.boot.common.constants;

/**
 * Created by shenhongxi on 2021/3/27.
 */
public class Constants {

    public static final String COMMA = ".";

    public static final String OPEN_PREFIX = "open";

    public static final String OPEN_DUBBO_PREFIX = OPEN_PREFIX + COMMA + "dubbo";
    public static final String OPEN_ROCKETMQ_PREFIX = OPEN_PREFIX + COMMA + "rocketmq";
    public static final String OPEN_REDIS_PREFIX = OPEN_PREFIX + COMMA + "redis";
    public static final String OPEN_WEB_PREFIX = OPEN_PREFIX + COMMA + "web";

    public static final String MONITOR_PREFIX = OPEN_WEB_PREFIX + COMMA + "monitor";
    public static final String SESSION_PREFIX = OPEN_WEB_PREFIX + COMMA + "session";
    public static final String FIREWALL_PREFIX = OPEN_WEB_PREFIX + COMMA + "firewall";
    public static final String ETAG_PREFIX = OPEN_WEB_PREFIX + COMMA + "etag";
}
