package com.reminder.remindme.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.reminder.remindme.R;
import com.reminder.remindme.ui.auth.AuthActivity;
import com.reminder.remindme.ui.history.HistoryFragment;
import com.reminder.remindme.ui.reminders.ReminderFragment;
import com.reminder.remindme.ui.reminders.sendmailActivity;
import com.reminder.remindme.viewmodel.UserViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(
                            R.id.main_container,
                            new ReminderFragment())
                    .addToBackStack(ReminderFragment.class.getName())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_dashboard:
                getSupportFragmentManager().beginTransaction()
                        .replace(
                                R.id.main_container,
                                new ReminderFragment())
                        .addToBackStack(ReminderFragment.class.getName())
                        .commit();
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction()
                        .replace(
                                R.id.main_container,
                                new HistoryFragment())
                        .addToBackStack(HistoryFragment.class.getName())
                        .commit();
                break;
            case R.id.sendmails:
                startActivity(new Intent(this, sendmailActivity.class));
                break;

            case R.id.nav_logout:
                userViewModel.logout();
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingFragmentInjector;
    }
}
