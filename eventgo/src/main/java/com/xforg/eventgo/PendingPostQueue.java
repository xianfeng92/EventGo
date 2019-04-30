package com.xforg.eventgo;

/**
 * Created By zhongxianfeng on 19-4-30
 * github: https://github.com/xianfeng92
 */
final class PendingPostQueue {
    private PendingPost head;
    private PendingPost tail;

    synchronized void enqueue(PendingPost pendingPost){
        if(pendingPost == null){
            throw new NullPointerException("null cannot be enqueue");
        }
        if(tail != null){
            tail.next = pendingPost;
            tail = pendingPost;
        }else if(head == null){
            head = tail = pendingPost;
        }else {
            throw new IllegalStateException("Head Present, but no tail");
        }
        notifyAll();
    }

    synchronized PendingPost poll(){
        PendingPost pendingPost = head;
        if(head != null){
            head = head.next;
            if(head == null){
                tail = null;
            }
        }
        return pendingPost;
    }

    synchronized PendingPost poll(int maxMillisToWait) throws InterruptedException {
        if(head == null){
            wait(maxMillisToWait);
        }
        return poll();
    }
}
