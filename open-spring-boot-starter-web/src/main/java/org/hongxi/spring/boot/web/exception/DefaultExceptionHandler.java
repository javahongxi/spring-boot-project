package org.hongxi.spring.boot.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.hongxi.spring.boot.common.exception.ResultException;
import org.hongxi.spring.boot.common.result.Result;
import org.hongxi.spring.boot.common.result.ResultHelper;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shenhongxi on 2021/3/27.
 */
@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ResultException.class)
    @ResponseBody
    public Result<Void> handleLogicException(HttpServletRequest request, ResultException e) {
        return ResultHelper.newErrorResult(e.getCode(), e.getMsg());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Void> handleException(HttpServletRequest request, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        log.error("exception handled, request:{}", request.getRequestURI(), e);
        return ResultHelper.newErrorResult(500, e.getMessage());
    }
}