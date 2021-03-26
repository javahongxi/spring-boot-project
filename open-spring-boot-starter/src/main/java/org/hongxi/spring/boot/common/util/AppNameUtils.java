package org.hongxi.spring.boot.common.util;

import java.io.File;

/**
 * Util class for getting application name. This class uses the flowing order to get app's name:
 *
 * <ol>
 * <li>get {@code project.name} from System Properties, if not null, use the value as app name;</li>
 * <li>get {@code sun.java.command} from System properties, remove path, arguments and ".jar" or ".JAR"
 * suffix, use the result as app name. Note that whitespace in file name or path is not allowed, or a
 * wrong app name may be gotten, For example:
 * <p>
 * <code>
 * "test.Main" -> test.Main<br/>
 * "/target/test.Main" -> test.Main<br/>
 * "/target/test.Main args1" -> test.Main<br/>
 * "Main.jar" -> Main<br/>
 * "/target/Main.JAR args1" -> Main<br/>
 * "Mai n.jar" -> Mai // whitespace in file name is not allowed<br/>
 * </code>
 * </p>
 * </li>
 * </ol>
 *
 * @author Eric Zhao
 * @author leyou
 * Update by shen.hongxi on 2021/3/26.
 */
public abstract class AppNameUtils {

    public static final String APP_NAME = "PROJECT";
    public static final String SUN_JAVA_COMMAND = "sun.java.command";
    private static final String JAR_SUFFIX_LOWER = ".jar";
    private static final String JAR_SUFFIX_UPPER = ".JAR";

    private static String appName;

    static {
        resolveAppName();
    }

    public static void resolveAppName() {
        String app = SystemUtils.getProperty(APP_NAME);
        // use -Dproject.name first
        if (!isEmpty(app)) {
            appName = app;
            return;
        }

        // parse sun.java.command property
        String command = System.getProperty(SUN_JAVA_COMMAND);
        if (isEmpty(command)) {
            return;
        }
        command = command.split("\\s")[0];
        String separator = File.separator;
        if (command.contains(separator)) {
            String[] strs;
            if ("\\".equals(separator)) {
                strs = command.split("\\\\");
            } else {
                strs = command.split(separator);
            }
            command = strs[strs.length - 1];
        }
        if (command.endsWith(JAR_SUFFIX_LOWER) || command.endsWith(JAR_SUFFIX_UPPER)) {
            command = command.substring(0, command.length() - 4);
        }
        appName = command;
    }

    public static String getAppName() {
        return appName;
    }

    private static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }
}