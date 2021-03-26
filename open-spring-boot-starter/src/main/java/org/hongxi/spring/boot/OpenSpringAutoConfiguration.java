package org.hongxi.spring.boot;

import org.hongxi.spring.boot.service.VersionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by shenhongxi on 2021/3/26.
 */
@Configuration
public class OpenSpringAutoConfiguration {

    @Bean
    public VersionService versionService() {
        return new VersionService();
    }
}
