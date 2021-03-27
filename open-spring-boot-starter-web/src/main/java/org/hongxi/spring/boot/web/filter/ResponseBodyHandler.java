package org.hongxi.spring.boot.web.filter;

/**
 * Created by shenhongxi on 2020/10/23.
 */
public class ResponseBodyHandler {

    public byte[] handle(byte[] body) {
        // 如果需要对 body 进行加密，则在这里处理
        return body;
    }
}
