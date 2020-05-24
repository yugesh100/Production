package com.reminder.remindme.ui.history;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.reminder.remindme.R;
import com.reminder.remindme.data.model.ReminderEntity;
import com.reminder.remindme.di.injector.Injectable;
import com.reminder.remindme.ui.reminders.ReminderFragment;
import com.reminder.remindme.ui.reminders.ReminderSection;
import com.reminder.remindme.util.Constant;
import com.reminder.remindme.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class HistoryFragment extends Fragment implements Injectable {

    public static final int ACTION_DELETE = 0;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecyclerView remindersRecyclerView;

    private TaskViewModel taskViewModel;
    private SectionedRecyclerViewAdapter sectionAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.history);
        }

        taskViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel.class);

        remindersRecyclerView = (RecyclerView) view.findViewById(R.id.reminderRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        remindersRecyclerView.setLayoutManager(linearLayoutManager);
        // Create an instance of SectionedRecyclerViewAdapter
        sectionAdapter = new SectionedRecyclerViewAdapter();
        remindersRecyclerView.setAdapter(sectionAdapter);

        taskViewModel.getReminders().observe(this, response -> {
            if (response == null) return;

            if (response.isSuccessful()) {
                Map<String, List<ReminderEntity>> data = mapToReminderEntity(response.getData());
                sectionAdapter.removeAllSections();
                for (String title : data.keySet()) {
                    sectionAdapter.addSection(new ReminderSection(title, data.get(title), null));
                }
                sectionAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Reminder history not available!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    /**
     * @param entities list of the reminder model entity
     * @return provides the mapped data sorted by date
     */
    private Map<String, List<ReminderEntity>> mapToReminderEntity(List<ReminderEntity> entities) {
        Collections.sort(entities, (o1, o2) -> Long.compare(o1.getRemindTime(), o2.getRemindTime()));
        Map<String, List<ReminderEntity>> data = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DateConstant.DATE_FORMT, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        for (ReminderEntity entity : entities) {
            calendar.setTimeInMillis(entity.getRemindTime());
            if (calendar.before(today)) continue;
            String key = sdf.format(calendar.getTimeInMillis());
            List<ReminderEntity> reminders = data.get(key);
            if (reminders == null) reminders = new ArrayList<>();
            reminders.add(entity);
            data.put(key, reminders);
        }

        return data;
    }
}
