package com.xforg.rxeventgo;

/**
 * Created By zhongxianfeng on 19-4-30
 * github: https://github.com/xianfeng92
 */
final class TagEvent {

    Object mEvent;
    String mTag;

    /** Used for efficient comparison */
    String tagEventString;

    TagEvent(Object event,String mTag){
        this.mEvent = event;
        this.mTag = mTag;
        tagEventString = initTagEventString();
    }

    private String initTagEventString() {
        if (tagEventString == null) {
            // Method.toString has more overhead, just take relevant parts of the method
            StringBuilder builder = new StringBuilder(64);
            builder.append(mEvent.getClass().getName());
            builder.append('#').append(mTag.getClass().getName());
            tagEventString = builder.toString();
        }
        return tagEventString;
    }

    @Override
    public String toString() {
        return "event: " + mEvent + ", tag: " + mTag;
    }

    public String getTagEventString() {
        return tagEventString;
    }
}
