package com.reminder.remindme.ui.auth;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.reminder.remindme.R;
import com.reminder.remindme.data.UserStatus;
import com.reminder.remindme.data.model.State;
import com.reminder.remindme.ui.MainActivity;
import com.reminder.remindme.viewmodel.UserViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
//authorizing activity
public class AuthActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    private final String TAG = AuthActivity.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);


        // Observes the user status
        userViewModel.checkAuthentication().observe(this, response -> {
            if (response == null || response.getState() == State.LOADING) {
                Log.d(TAG, "Null response/Loading...");
                return;
            }

            // check if user is authenticated or not
            if (response.getState() == State.SUCCESS &&
                    response.getData() == UserStatus.AUTHENTICATED) {
                // redirect to the Dashboard screen
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // redirect to the Login screen
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.authContainer, new LoginFragment())
                        .commitAllowingStateLoss();
            }
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingFragmentInjector;
    }
}
