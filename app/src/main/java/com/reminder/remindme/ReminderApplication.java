package com.reminder.remindme;

import android.app.Activity;
import android.app.Application;

import com.reminder.remindme.di.injector.ApplicationInjector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class ReminderApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    private Collection<LifeCycleListener> lifeCycleListeners = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        ApplicationInjector.init(this);

        for (LifeCycleListener listener : lifeCycleListeners) {
            listener.onCreate();
        }
    }

    @Override
    public void onTerminate() {
        for (LifeCycleListener listener : lifeCycleListeners) {
            listener.onTerminate();
        }
        super.onTerminate();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    public void registerLifeCycleListener(LifeCycleListener lifeCycleListener) {
        lifeCycleListeners.add(lifeCycleListener);
    }

    public interface LifeCycleListener{
        void onCreate();
        void onTerminate();
    }
}
