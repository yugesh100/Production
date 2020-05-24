package com.reminder.remindme.di.component;

import android.app.Application;
import android.arch.lifecycle.ViewModel;

import com.reminder.remindme.ReminderApplication;
import com.reminder.remindme.data.source.CouchBaseDatabase;
import com.reminder.remindme.di.modules.ActivityBuilderModule;
import com.reminder.remindme.di.modules.ApplicationModule;
import com.reminder.remindme.di.modules.FragmentBuildersModule;
import com.reminder.remindme.di.modules.ViewModelFactoryModule;
import com.reminder.remindme.di.modules.ViewModelModule;
import com.reminder.remindme.ui.reminders.ReminderFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;


@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                ActivityBuilderModule.class,
                FragmentBuildersModule.class,
                ViewModelModule.class,
                ViewModelFactoryModule.class
        }
        )
public interface AppComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(ReminderApplication application);
        AppComponent build();
    }

    void inject(ReminderApplication application);

    ReminderApplication application();

    CouchBaseDatabase couchbaseDatabase();
}
