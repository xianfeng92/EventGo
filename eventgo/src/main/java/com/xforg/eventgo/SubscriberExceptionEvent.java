package com.xforg.eventgo;


public final class SubscriberExceptionEvent {
    /** The {@link EventGo} instance to with the original event was posted to. */
    public final EventGo eventGo;

    /** The Throwable thrown by a subscriber. */
    public final Throwable throwable;

    /** The original event that could not be delivered to any subscriber. */
    public final Object causingEvent;

    /** The subscriber that threw the Throwable. */
    public final Object causingSubscriber;

    public SubscriberExceptionEvent(EventGo eventGo, Throwable throwable, Object causingEvent,
                                    Object causingSubscriber) {
        this.eventGo = eventGo;
        this.throwable = throwable;
        this.causingEvent = causingEvent;
        this.causingSubscriber = causingSubscriber;
    }

}
