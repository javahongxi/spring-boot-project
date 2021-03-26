package org.hongxi.spring.boot.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by shenhongxi on 2021/3/26.
 */
@Slf4j
public class ApplicationStartedListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        // register application info to redis

        log.info("register application info to redis successful");
    }
}
