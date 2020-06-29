package com.reminder.remindme.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;
import com.couchbase.lite.util.Log;
import com.reminder.remindme.data.model.ReminderEntity;
import com.reminder.remindme.data.model.Response;
import com.reminder.remindme.data.model.State;
import com.reminder.remindme.data.source.CouchBaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class TaskViewModel extends ViewModel {

    private CouchBaseDatabase database;
    MutableLiveData<Response<List<ReminderEntity>>> responseMutableLiveData = new MutableLiveData<>();

    @Inject
    public TaskViewModel(CouchBaseDatabase database) {
        this.database = database;
    }

    public LiveData<Response<List<ReminderEntity>>> getReminders() {
        return responseMutableLiveData;
    }

    public void updateReminders() {
        responseMutableLiveData.setValue(new Response<>("Loading...", State.LOADING, null));

        View view = database.getDatabase().getView("reminderList");
        if (view.getMap() == null) {
            Mapper map = (document, emitter) -> {
                if ("reminderList".equals(document.get("type"))) {
                    List<Object> keys = new ArrayList<Object>();
                    keys.add(document.get("title"));
                    keys.add(document.get("reminderType"));
                    keys.add(document.get("remindTime"));
                    keys.add(document.get("created_at"));
                    emitter.emit(keys, document);
                }
            };
            view.setMap(map, "1.0");
        }

        try {
            QueryEnumerator enumerator = view.createQuery().run();
            List<ReminderEntity> reminders = extractReminders(enumerator);
            if (reminders.isEmpty()) {
                responseMutableLiveData.setValue(new Response<>("No data available", State.ERROR, null));
            } else {
                responseMutableLiveData.setValue(new Response<>("Success", State.SUCCESS, extractReminders(enumerator)));
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    private List<ReminderEntity> extractReminders(QueryEnumerator enumerator) {
        List<ReminderEntity> entities = new ArrayList<>();
        if (enumerator == null) return entities;

        for (int i = 0; i < enumerator.getCount(); i++) {
            Document document = enumerator.getRow(i).getDocument();
            Map<String, Object> data = document.getProperties();

            String id = document.getId();
            Object title = data.get("title");
            Object reminderTime = data.get("remindTime");
            Object remindType = data.get("reminderType");
            Object createdDate = data.get("createdAt");

            entities.add(new ReminderEntity(
                    id,
                    title == null ? "" : (String) title,
                    reminderTime == null ? 0 : (long) reminderTime,
                    remindType == null ? "" : (String) remindType,
                    createdDate == null ? 0 : (long) reminderTime
            ));
        }
        return entities;
    }

    /**
     *
     * @param reminder {@link ReminderEntity} data which is to be stored
     * @return {@link LiveData} of {@link Response} which contains the create reminder process status
     */
    public LiveData<Response<ReminderEntity>> createReminder(ReminderEntity reminder) {
        MutableLiveData<Response<ReminderEntity>> result = new MutableLiveData<>();
        result.setValue(new Response<>("Loading...", State.LOADING, null));

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("type", "reminderList");
        properties.put("title", reminder.getTitle());
        properties.put("reminderType", reminder.getReminderType());
        properties.put("remindTime", reminder.getRemindTime());
        properties.put("created_at", reminder.getCreatedAt());

        Document document = database.getDatabase().createDocument();
        try {
            document.putProperties(properties);
            result.setValue(new Response<>("Reminder successfully created!", State.SUCCESS, reminder));
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            result.setValue(new Response<>(e.getMessage(), State.ERROR, null));
        }
        return result;
    }

    /**
     *
     * @param reminder {@link ReminderEntity} data which is to be deleted
     * @return {@link LiveData} of {@link Response} which contains the delete reminder process status
     */
    public LiveData<Response<Boolean>> deleteReminder(ReminderEntity reminder) {
        MutableLiveData<Response<Boolean>> result = new MutableLiveData<>();
        result.setValue(new Response<>("Loading...", State.LOADING, null));

        Document document = database.getDatabase().getDocument(reminder.getDocumentId());
        try {
            document.delete();
        } catch (CouchbaseLiteException e) {
            Log.e("DocumentDelete", "Cannot delete a task", e);
        }

        return result;
    }
}
