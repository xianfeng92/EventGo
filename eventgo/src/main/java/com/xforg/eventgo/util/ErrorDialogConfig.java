package com.xforg.eventgo.util;

import android.content.res.Resources;
import android.util.Log;
import com.xforg.eventgo.EventGo;

public class ErrorDialogConfig {
    final Resources resources;
    final int defaultTitleId;
    final int defaultErrorMsgId;
    final ExceptionToResourceMapping mapping;

    EventGo eventGo;
    boolean logExceptions = true;
    String tagForLoggingExceptions;
    int defaultDialogIconId;
    Class<?> defaultEventTypeOnDialogClosed;

    public ErrorDialogConfig(Resources resources, int defaultTitleId, int defaultMsgId) {
        this.resources = resources;
        this.defaultTitleId = defaultTitleId;
        this.defaultErrorMsgId = defaultMsgId;
        mapping = new ExceptionToResourceMapping();
    }

    public ErrorDialogConfig addMapping(Class<? extends Throwable> clazz, int msgId) {
        mapping.addMapping(clazz, msgId);
        return this;
    }

    public int getMessageIdForThrowable(final Throwable throwable) {
        Integer resId = mapping.mapThrowable(throwable);
        if (resId != null) {
            return resId;
        } else {
            Log.d(EventGo.TAG, "No specific message ressource ID found for " + throwable);
            return defaultErrorMsgId;
        }
    }

    public void setDefaultDialogIconId(int defaultDialogIconId) {
        this.defaultDialogIconId = defaultDialogIconId;
    }

    public void setDefaultEventTypeOnDialogClosed(Class<?> defaultEventTypeOnDialogClosed) {
        this.defaultEventTypeOnDialogClosed = defaultEventTypeOnDialogClosed;
    }

    public void disableExceptionLogging() {
        logExceptions = false;
    }

    public void setTagForLoggingExceptions(String tagForLoggingExceptions) {
        this.tagForLoggingExceptions = tagForLoggingExceptions;
    }

    public void setEventBus(EventGo eventBus) {
        this.eventGo = eventBus;
    }

    /** eventBus!=null ? eventBus: EventBus.getDefault() */
    EventGo getEventGo() {
        return eventGo!=null ? eventGo: EventGo.getDefault();
    }
}