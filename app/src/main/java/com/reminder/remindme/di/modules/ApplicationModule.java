package com.reminder.remindme.di.modules;

import com.reminder.remindme.ReminderApplication;
import com.reminder.remindme.data.source.CouchBaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yugesh sapkota on 5/8/2020.
 */
@Module
public class ApplicationModule {

    @Singleton
    @Provides
    CouchBaseDatabase provideDatabase(ReminderApplication application) {
        return new CouchBaseDatabase(application);
    }
}
