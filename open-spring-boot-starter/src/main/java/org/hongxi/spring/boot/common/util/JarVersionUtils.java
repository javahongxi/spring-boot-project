package org.hongxi.spring.boot.common.util;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public abstract class JarVersionUtils {
    
    public static String getJarVersion(Class<?> clazz, String prefix) {
        ProtectionDomain domain = clazz.getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        URL url = source.getLocation();
        String fileName = StringUtils.substringAfterLast(url.toString(), "/");
        if (StringUtils.isNotEmpty(fileName)) {
            String version = StringUtils.substringAfter(fileName, prefix);
            version = StringUtils.substringBefore(version, ".jar");
            if (version.startsWith("-")) {
                version = StringUtils.substringAfter(version, "-");
            }
            version = StringUtils.substringBefore(version, "-");
            if (StringUtils.isNotEmpty(version)) {
                version = version.replaceAll(".Final", "");
                return version;
            }
        }
        return "1.0.0-SNAPSHOT";
    }
}