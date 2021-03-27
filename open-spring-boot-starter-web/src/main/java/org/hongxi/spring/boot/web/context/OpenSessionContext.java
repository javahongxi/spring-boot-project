package org.hongxi.spring.boot.web.context;

import java.util.Objects;

/**
 * Created by shenhongxi on 2021/3/27.
 */
public class OpenSessionContext {

    private static final ThreadLocal<OpenSessionContext> CONTEXT =
            ThreadLocal.withInitial(() -> new OpenSessionContext());

    private String userId;

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
}
