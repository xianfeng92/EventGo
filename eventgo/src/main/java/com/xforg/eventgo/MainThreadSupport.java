package com.xforg.eventgo;

import android.os.Looper;

/**
 * Created By zhongxianfeng on 19-4-30
 * github: https://github.com/xianfeng92
 */
public interface MainThreadSupport {
    boolean isMainThread();

    Poster createPoster(EventGo eventGo);

    class AndroidHandlerMainThreadSupport implements MainThreadSupport {

        private final Looper looper;

        public AndroidHandlerMainThreadSupport(Looper looper) {
            this.looper = looper;
        }

        @Override
        public boolean isMainThread() {
            return looper == Looper.myLooper();
        }

        @Override
        public Poster createPoster(EventGo eventGo) {
            return new HandlerPoster(eventGo, looper, 10);
        }
    }
}
