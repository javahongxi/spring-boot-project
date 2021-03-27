package org.hongxi.spring.boot.web.interceptor;

import org.hongxi.spring.boot.common.exception.ErrorCode;
import org.hongxi.spring.boot.common.exception.ResultException;
import org.hongxi.spring.boot.web.context.OpenSessionContext;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shenhongxi on 2020/8/16.
 */
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        OpenSessionContext context = OpenSessionContext.get();
        if (context == null || !StringUtils.hasLength(OpenSessionContext.getUserId())) {
            throw new ResultException(ErrorCode.NOT_LOGIN);
        }
        return true;
    }
}
