package com.xforg.rxeventgo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created By zhongxianfeng on 19-4-30
 * github: https://github.com/xianfeng92
 */
public class RxEventGo {
    private volatile static RxEventGo mRxEventGo;
    private final FlowableProcessor<Object> mFlowableProcessor;
    private final SubscriberMethodFinder mSubscriberMethodFinder;
    private static Map<Class<?>, Map<Class<?>, Disposable>> mDisposableMap = new HashMap<>();
    private final Map<Class<?>, Object> stickyEvents;

    private RxEventGo() {
        mFlowableProcessor = PublishProcessor.create().toSerialized();
        mSubscriberMethodFinder = new SubscriberMethodFinder();
        stickyEvents = new ConcurrentHashMap<>();
    }

    public static RxEventGo getDefault() {
        if (mRxEventGo == null) {
            synchronized (RxEventGo.class) {
                if (mRxEventGo == null) {
                    mRxEventGo = new RxEventGo();
                }
            }
        }
        return mRxEventGo;
    }

    public void register(Object subsciber) {
        Class<?> subsciberClass = subsciber.getClass();
        List<SubscriberMethod> subscriberMethods = mSubscriberMethodFinder.findSubscriberMethods(subsciberClass);
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            addSubscriber(subsciber, subscriberMethod);
        }
    }

    /**
     * translate the subscriberMethod to a subscriber,and put it in a cancleable container .
     */
    private void addSubscriber(final Object subsciber, final SubscriberMethod subscriberMethod) {
        Class<?> subsciberClass = subsciber.getClass();
        Class<?> eventType = subscriberMethod.getEventType();
        Disposable disposable = mFlowableProcessor.ofType(eventType)
                .observeOn(subscriberMethod.getThreadMode())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        invokeMethod(subsciber, subscriberMethod, o);
                    }
                });
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subsciberClass);
        if (disposableMap == null) {
            disposableMap = new HashMap<>();
            mDisposableMap.put(subsciberClass, disposableMap);
        }
        disposableMap.put(eventType, disposable);
    }

    /**
     * call the subscriber method annotationed with receiverd event.
     */
    private void invokeMethod(Object subscriber, SubscriberMethod subscriberMethod, Object obj) {
        try {
            subscriberMethod.getMethod().invoke(subscriber, obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Posts the given event to the RxEventGo.Not Support Sticky Event
     */
    public void post(Object obj) {
        if (mFlowableProcessor.hasSubscribers()) {
            mFlowableProcessor.onNext(obj);
        }
    }

    /**
     * Unregisters the given subscriber from all event classes.
     */
    public void unregister(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subscriberClass);
        if (disposableMap == null) {
            throw new IllegalArgumentException(subscriberClass.getSimpleName() + " haven't registered RxEventGo");
        }
        Set<Class<?>> keySet = disposableMap.keySet();
        for (Class<?> evenType : keySet) {
            Disposable disposable = disposableMap.get(evenType);
            disposable.dispose();
        }
        mDisposableMap.remove(subscriberClass);
    }

    /**
     * Unregisters the given subscriber of eventType from all event classes.
     */
    public void unregister(Object subscriber, Class<?> eventType) {
        Class<?> subscriberClass = subscriber.getClass();
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subscriberClass);
        if (disposableMap == null) {
            throw new IllegalArgumentException(subscriberClass.getSimpleName() + " haven't registered RxEventGo");
        }
        if (!disposableMap.containsKey(eventType)) {
            throw new IllegalArgumentException("The event with type of " + subscriberClass.getSimpleName() + " is not" +
                    " required in " + subscriberClass.getSimpleName());
        }
        Disposable disposable = disposableMap.get(eventType);
        disposable.dispose();
        mDisposableMap.remove(eventType);
    }
}
