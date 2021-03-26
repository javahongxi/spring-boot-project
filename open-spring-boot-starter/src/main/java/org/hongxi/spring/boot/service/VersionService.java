package org.hongxi.spring.boot.service;

import org.hongxi.spring.boot.OpenSpringAutoConfiguration;
import org.hongxi.spring.boot.common.util.JarVersionUtils;

/**
 * Created by shenhongxi on 2021/3/26.
 */
public class VersionService {

    public String getVersion() {
        return JarVersionUtils.getJarVersion(OpenSpringAutoConfiguration.class, "open-spring-boot-starter");
    }
}
