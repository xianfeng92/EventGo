package com.xforg.eventgo;

/**
 * Created By zhongxianfeng on 19-4-30
 * github: https://github.com/xianfeng92
 */
public interface Poster {

    void enqueue(Subscription subscription, Object event);
}
