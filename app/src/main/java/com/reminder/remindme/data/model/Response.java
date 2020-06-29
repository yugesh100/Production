package com.reminder.remindme.data.model;
//creating  constructor with getter and setter

public class Response<T> {

    private String message;
    private State state;
    private T data;

    public Response(String message, State state, T data) {
        this.message = message;
        this.state = state;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return state == State.SUCCESS;
    }
}
