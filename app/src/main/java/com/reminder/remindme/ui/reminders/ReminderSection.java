package com.reminder.remindme.ui.reminders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.reminder.remindme.R;
import com.reminder.remindme.data.model.ReminderEntity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
//create a stateless section object and list of reminder model

public class ReminderSection extends StatelessSection {

    private final String title;
    private final List<ReminderEntity> reminders;
    private final OnReminderInteractionListener<ReminderEntity> listener;

    /**
     * Create a stateless Section object based on {@link SectionParameters}.
     *
     * @param reminders list of reminder model
     */
    public ReminderSection(String title, List<ReminderEntity> reminders, OnReminderInteractionListener<ReminderEntity> listener) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.reminder_item)
                .headerResourceId(R.layout.reminder_header)
                .build());
        this.title = title;
        this.reminders = reminders;
        this.listener = listener;
    }

    @Override
    public int getContentItemsTotal() {
        return reminders.size();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReminderEntity reminderEntity = reminders.get(position);

        if (holder instanceof ReminderViewHolder){
            ((ReminderViewHolder) holder).bindView(reminderEntity);
            ((ReminderViewHolder) holder).deleteBtn.setOnClickListener(v ->{
                if (listener != null){
                    reminders.remove(reminderEntity);
                    listener.onListFragmentInteraction(ReminderSection.this, reminderEntity, position, ReminderFragment.ACTION_DELETE);
                }
            });

            if (listener == null){
                ((ReminderViewHolder) holder).deleteBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        if (holder instanceof HeaderViewHolder){
            ((HeaderViewHolder) holder).titleTV.setText(title);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        private final TextView titleTV;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.headerTV);
        }
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTV;
        private final TextView timeTV;
        private final TextView reminderTypeTV;
        private final ImageButton deleteBtn;
        private ReminderEntity entity;

        public ReminderViewHolder(View itemView) {
            super(itemView);

            titleTV = (TextView) itemView.findViewById(R.id.titleTV);
            timeTV = (TextView) itemView.findViewById(R.id.reminderTimeTV);
            reminderTypeTV = (TextView) itemView.findViewById(R.id.reminderTypeTV);
            deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteBtn);
        }

        public void bindView(ReminderEntity entity){
            this.entity = entity;
            titleTV.setText(entity.getTitle());
            Date date = new Date(entity.getRemindTime());
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            timeTV.setText(sdf.format(date));
            reminderTypeTV.setText(entity.getReminderType());
        }
    }
}
