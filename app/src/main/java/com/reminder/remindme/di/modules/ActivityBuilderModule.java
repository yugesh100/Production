package com.reminder.remindme.di.modules;

import com.reminder.remindme.ui.MainActivity;
import com.reminder.remindme.ui.auth.AuthActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract AuthActivity bindAuthActivity();

    @ContributesAndroidInjector(modules = MainActivityFragmentBuilderModule.class)
    abstract MainActivity bindMainActivity();
}
