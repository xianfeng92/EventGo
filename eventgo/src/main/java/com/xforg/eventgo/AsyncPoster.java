package com.xforg.eventgo;

/**
 * Created By zhongxianfeng on 19-4-30
 * github: https://github.com/xianfeng92
 */
class AsyncPoster implements Runnable,Poster{
    private final PendingPostQueue queue;
    private final EventGo eventGo;

    AsyncPoster(EventGo eventGo){
        this.eventGo = eventGo;
        queue = new PendingPostQueue();
    }

    @Override
    public void enqueue(Subscription subscription, Object event) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscription,event);
        queue.enqueue(pendingPost);
        eventGo.getExecutorService().execute(this);
    }

    @Override
    public void run() {
        PendingPost pendingPost = queue.poll();
        if(pendingPost == null){
            throw new NullPointerException("No Pending post available");
        }
        eventGo.invokeSubscriber(pendingPost);
    }
}
