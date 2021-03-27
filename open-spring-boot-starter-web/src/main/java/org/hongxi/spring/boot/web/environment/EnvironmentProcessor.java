package org.hongxi.spring.boot.web.environment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Created by shenhongxi on 2021/3/27.
 */
public class EnvironmentProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!environment.containsProperty("spring.servlet.multipart.max-file-size")) {
            System.setProperty("spring.servlet.multipart.max-file-size", "5MB");
        }
    }
}