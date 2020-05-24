package com.reminder.remindme.ui.reminders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.reminder.remindme.R;
import com.reminder.remindme.data.model.ReminderEntity;
import com.reminder.remindme.di.injector.Injectable;
import com.reminder.remindme.util.Constant;
import com.reminder.remindme.viewmodel.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


public class ReminderFragment extends Fragment implements Injectable, OnReminderInteractionListener<ReminderEntity> {

    public static final int ACTION_DELETE = 0;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private RecyclerView remindersRecyclerView;
    private FloatingActionButton addReminderBtn;
    private TaskViewModel taskViewModel;
    private SectionedRecyclerViewAdapter sectionAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_list, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.app_name);
        }
        taskViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel.class);

        remindersRecyclerView = (RecyclerView) view.findViewById(R.id.reminderRecyclerView);
        addReminderBtn = (FloatingActionButton) view.findViewById(R.id.addReminderFab);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        remindersRecyclerView.setLayoutManager(linearLayoutManager);
        // Create an instance of SectionedRecyclerViewAdapter
        sectionAdapter = new SectionedRecyclerViewAdapter();
        remindersRecyclerView.setAdapter(sectionAdapter);

        taskViewModel.updateReminders();
        taskViewModel.getReminders().observe(this, response -> {
            if (response == null) return;

            if (response.isSuccessful()) {
                Map<String, List<ReminderEntity>> data = mapToReminderEntity(response.getData());
                sectionAdapter.removeAllSections();
                for (String title : data.keySet()) {
                    sectionAdapter.addSection(new ReminderSection(title, data.get(title), ReminderFragment.this));
                }
                sectionAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Reminder data not available", Toast.LENGTH_SHORT).show();
            }
        });

        addReminderBtn.setOnClickListener(v -> showAddReminderDialog());

        return view;
    }

    /**
     * Displays the create reminder view
     */
    private void showAddReminderDialog() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DateConstant.DATE_TIME_FORMT, Locale.getDefault());
        View view = View.inflate(getContext(), R.layout.create_reminder, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setView(view);

        EditText edtTitle = view.findViewById(R.id.edtTitle);
        Spinner reminderTypeSpinner = view.findViewById(R.id.reminderTypeSpinner);
        EditText edtDate = view.findViewById(R.id.edtDate);
        EditText edtTime = view.findViewById(R.id.edtTime);
        Button createReminder = view.findViewById(R.id.btnCreateReminder);

        Calendar calendar = Calendar.getInstance();
        edtDate.setText(
                String.format(
                        Locale.getDefault(),
                        "%d-%d-%d",
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                )
        );
        edtTime.setText(
                String.format(
                        Locale.getDefault(),
                        "%d:%d",
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                )
        );

        AlertDialog dialog = builder.create();
        dialog.show();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (d, year, month, dayInMonth) -> edtDate.setText(String.format(Locale.getDefault(), "%d-%d-%d", year, month, dayInMonth)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view1, hourOfDay, minute) -> {
                    if (hourOfDay < calendar.get(Calendar.HOUR_OF_DAY) ||
                            (hourOfDay < calendar.get(Calendar.HOUR_OF_DAY) &&
                                    minute < calendar.get(Calendar.MINUTE))) {
                        Toast.makeText(getContext(), "Please select future time!!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    edtTime.setText(String.format(Locale.getDefault(), "%d:%d", hourOfDay, minute));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );

        createReminder.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String type = (String) reminderTypeSpinner.getSelectedItem();
            String date = edtDate.getText().toString();
            String time = edtTime.getText().toString();

            Date dateTime = null;
            try {
                dateTime = sdf.parse(date + " " + time);
            } catch (ParseException e) {
                e.printStackTrace();
                dateTime = Calendar.getInstance().getTime();
            }
            ReminderEntity reminderEntity = new ReminderEntity(title, dateTime.getTime(), type);
            taskViewModel.createReminder(reminderEntity).observe(this, response -> {
                if (response == null) return;

                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Reminder successfully created!!", Toast.LENGTH_SHORT).show();
                    taskViewModel.updateReminders();
                    dialog.dismiss();

                } else {
                    Toast.makeText(getContext(), "Failed to create a reminder!!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        edtDate.setOnClickListener(v -> datePickerDialog.show());
        edtTime.setOnClickListener(v -> timePickerDialog.show());
    }

    /**
     * @param entities list of the reminder model entity
     * @return provides the mapped data sorted by date
     */
    private Map<String, List<ReminderEntity>> mapToReminderEntity(List<ReminderEntity> entities) {
        Collections.sort(entities, (o1, o2) -> Long.compare(o2.getRemindTime(), o1.getRemindTime()));
        Map<String, List<ReminderEntity>> data = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DateConstant.DATE_FORMT, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        for (ReminderEntity entity : entities) {
            calendar.setTimeInMillis(entity.getRemindTime());
            if (calendar.after(today)) continue;
            String key = sdf.format(calendar.getTimeInMillis());
            List<ReminderEntity> reminders = data.get(key);
            if (reminders == null) reminders = new ArrayList<>();
            reminders.add(entity);
            data.put(key, reminders);
        }

        return data;
    }

    @Override
    public void onListFragmentInteraction(ReminderSection section, ReminderEntity item, int position, int action) {
        if (action == ACTION_DELETE) {
            taskViewModel.deleteReminder(item);
            if (sectionAdapter != null)
                sectionAdapter.notifyItemRemovedFromSection(section, position);
        }
    }
}
