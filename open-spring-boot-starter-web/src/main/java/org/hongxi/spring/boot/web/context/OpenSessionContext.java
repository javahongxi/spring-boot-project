package org.hongxi.spring.boot.web.context;

import java.util.Objects;

/**
 * Created by shenhongxi on 2021/3/27.
 */
public class OpenSessionContext {

    private static final ThreadLocal<OpenSessionContext> CONTEXT =
            ThreadLocal.withInitial(() -> new OpenSessionContext());

    private String userId;

    /**
     * Controller 方法的参数对象，仅当用 @RequestBody 时才有值
     */
    private Object request;
    /**
     * Controller 方法的返回对象，仅当用 @ResponseBody 时才有值
     */
    private Object response;

    public static OpenSessionContext get() {
        return CONTEXT.get();
    }

    public static void set(OpenSessionContext context) {
        CONTEXT.set(context);
    }

    public static void remove() {
        CONTEXT.remove();
    }

    public static String getUserId() {
        OpenSessionContext context = get();
        if (Objects.isNull(context)) {
            return null;
        }
        return context.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static Object getRequest() {
        OpenSessionContext context = get();
        if (Objects.isNull(context)) {
            return null;
        }
        return context.request;
    }

    public void setRequest(Object body) {
        this.request = body;
    }

    public static Object getResponse() {
        OpenSessionContext context = get();
        if (Objects.isNull(context)) {
            return null;
        }
        return context.response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
