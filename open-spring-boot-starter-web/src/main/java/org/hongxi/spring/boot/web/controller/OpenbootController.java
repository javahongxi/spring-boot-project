package org.hongxi.spring.boot.web.controller;

import org.hongxi.spring.boot.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shenhongxi on 2021/3/27.
 */
@RestController
@RequestMapping("/openboot")
public class OpenbootController {

    @Autowired
    private VersionService versionService;

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/version")
    public String version() {
        return versionService.getVersion();
    }
}
