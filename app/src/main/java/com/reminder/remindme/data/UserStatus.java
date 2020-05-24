package com.reminder.remindme.data;


public enum UserStatus {
    AUTHENTICATED(1), UNAUTHENTICATED(0);

    public final int value;

    UserStatus(int value) {
        this.value = value;
    }

    public static UserStatus of(int value){
        switch (value){
            case 0 : return AUTHENTICATED;
            default: return UNAUTHENTICATED;
        }
    }
}
