package org.hongxi.spring.boot.common.exception;

import lombok.Builder;
import lombok.Data;

/**
 * Created by shenhongxi on 2021/3/27.
 */
@Data
@Builder
public class ResultException extends RuntimeException {

    private static final long serialVersionUID = 7771384345172570472L;
    private int code;
    private String msg;

    public ResultException(int code, String msg) {
        super("code:" + code + ", msg:" + msg);
        this.code = code;
        this.msg = msg;
    }

    public ResultException(ErrorCode errorCode) {
        super("code:" + errorCode.getCode() + ", msg:" + errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
