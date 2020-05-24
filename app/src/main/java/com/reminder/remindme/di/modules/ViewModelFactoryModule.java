package com.reminder.remindme.di.modules;

import android.arch.lifecycle.ViewModelProvider;

import com.reminder.remindme.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
