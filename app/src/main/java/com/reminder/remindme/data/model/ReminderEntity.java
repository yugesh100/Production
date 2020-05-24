package com.reminder.remindme.data.model;

import com.reminder.remindme.util.Constant;

import java.util.Calendar;


public class ReminderEntity {
    private String documentId;
    private String title;
    private long remindTime;
    private String reminderType;
    private long createdAt;

    public ReminderEntity() {
        this.createdAt = Calendar.getInstance().getTimeInMillis();
    }

    public ReminderEntity(String documentId, String title, long remindTime, String reminderType, long createdAt) {
        this.documentId = documentId;
        this.title = title;
        this.remindTime = remindTime;
        this.reminderType = reminderType;
        this.createdAt = createdAt;
    }

    public ReminderEntity(String title, long remindTime, String reminderType) {
        this.title = title;
        this.remindTime = remindTime;
        this.reminderType = reminderType;
        this.createdAt = Calendar.getInstance().getTimeInMillis();
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(long remindTime) {
        this.remindTime = remindTime;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
