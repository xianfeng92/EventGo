package com.xforg.rxeventgo;

import java.lang.reflect.Method;

import io.reactivex.Scheduler;

/**
 * Created By zhongxianfeng on 19-4-30
 * github: https://github.com/xianfeng92
 */
public class SubscriberMethod {
    final Method method;
    final Scheduler threadMode;
    final Class<?> eventType;
    final int priority;
    final boolean sticky;

    /** Used for efficient comparison */
    String methodString;

    public SubscriberMethod(Method method, Class<?> eventType, Scheduler threadMode, int priority, boolean sticky) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.priority = priority;
        this.sticky = sticky;
    }

    @Override
    public boolean equals(Object other){
        if (other == this) {
            return true;
        } else if (other instanceof SubscriberMethod) {
            checkMethodString();
            SubscriberMethod otherSubscriberMethod = (SubscriberMethod)other;
            otherSubscriberMethod.checkMethodString();
            // Don't use method.equals because of http://code.google.com/p/android/issues/detail?id=7811#c6
            return methodString.equals(otherSubscriberMethod.methodString);
        } else {
            return false;
        }
    }

    private synchronized void checkMethodString() {
        if (methodString == null) {
            // Method.toString has more overhead, just take relevant parts of the method
            StringBuilder builder = new StringBuilder(64);
            builder.append(method.getDeclaringClass().getName());
            builder.append('#').append(method.getName());
            builder.append('(').append(eventType.getName());
            methodString = builder.toString();
        }
    }

    @Override
    public int hashCode(){
        return method.hashCode();
    }


    public Method getMethod() {
        return method;
    }


    public Class<?> getEventType() {
        return eventType;
    }

    public Scheduler getThreadMode() {
        return threadMode;
    }
}
