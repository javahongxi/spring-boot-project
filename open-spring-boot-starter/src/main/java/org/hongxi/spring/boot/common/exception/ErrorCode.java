package org.hongxi.spring.boot.common.exception;

import lombok.AllArgsConstructor;

/**
 * Created by shenhongxi on 2021/3/27.
 */
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(0, "success"),
    INTERNAL_ERROR(500, "服务内部错误"),
    PARAMS_ERROR(460, "参数错误"),
    NOT_LOGIN(403, "请先登录"),
    ;

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
