package com.xforg.eventgo;

public final class NoSubscriberEvent {
    /** The {@link EventGo} instance to with the original event was posted to. */
    public final EventGo eventGo;

    /** The original event that could not be delivered to any subscriber. */
    public final Object originalEvent;

    public NoSubscriberEvent(EventGo eventGo, Object originalEvent) {
        this.eventGo = eventGo;
        this.originalEvent = originalEvent;
    }
}
