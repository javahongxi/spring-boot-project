package org.hongxi.spring.boot.environment;

import org.hongxi.spring.boot.common.util.AppNameUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Created by shenhongxi on 2021/3/26.
 */
public class EnvironmentProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String applicationName = environment.getProperty("spring.application.name", AppNameUtils.getAppName());
        System.setProperty("spring.application.name", applicationName);
    }
}
