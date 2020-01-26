package com.lazycece.au.api.token;

/**
 * @author lazycece
 * @date 2019/11/09
 */
public class SubjectContext {

    private static ThreadLocal<Subject> context = new ThreadLocal<>();

    public static void setContext(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("subject must not null.");
        }
        context.set(subject);
    }

    public static Subject getContext() {
        return context.get();
    }

    public static void remove() {
        context.remove();
    }
}
