package com.reminder.remindme.ui.reminders;


public interface OnReminderInteractionListener<I> {
    /**
     *
     * @param section which contains the provided <code>item</code> {@link I}
     * @param item which is used to interact with source and listener
     * @param position position of the <code>item</code> {@link I} in section {@link ReminderSection}
     * @param action action to be performed
     */
    void onListFragmentInteraction(ReminderSection section, I item, int position, int action);
}
