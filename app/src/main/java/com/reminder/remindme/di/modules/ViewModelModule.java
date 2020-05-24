package com.reminder.remindme.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.reminder.remindme.di.ViewModelKey;
import com.reminder.remindme.viewmodel.TaskViewModel;
import com.reminder.remindme.viewmodel.UserViewModel;
import com.reminder.remindme.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel.class)
    abstract ViewModel bindTaskViewModel(TaskViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel viewModel);
}
