package com.xforg.eventgo;

import java.util.logging.Level;

/**
 * Created By zhongxianfeng on 19-4-30
 * github: https://github.com/xianfeng92
 */
final class BackgroundPoster implements Runnable,Poster{
    private final PendingPostQueue queue;
    private final EventGo eventGo;
    private volatile boolean executorRunning;

    BackgroundPoster(EventGo eventGo){
        this.eventGo = eventGo;
        queue = new PendingPostQueue();
    }

    public void enqueue(Subscription subscription, Object event){
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscription,event);
        synchronized (this){
            queue.enqueue(pendingPost);
            if(!executorRunning){
                executorRunning = true;
                eventGo.getExecutorService().execute(this);
            }
        }
    }

    @Override
    public void run(){
        try {
            try {
                while (true) {
                    PendingPost pendingPost = queue.poll(1000);
                    if (pendingPost == null) {
                        synchronized (this) {
                            // Check again, this time in synchronized
                            pendingPost = queue.poll();
                            if (pendingPost == null) {
                                executorRunning = false;
                                return;
                            }
                        }
                    }
                    eventGo.invokeSubscriber(pendingPost);
                }
            } catch (InterruptedException e) {
                eventGo.getLogger().log(Level.WARNING, Thread.currentThread().getName() + " was interruppted", e);
            }
        } finally {
            executorRunning = false;
        }
    }

}
