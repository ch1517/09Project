package com.example.doqtq.a09project;

/**
 * Created by doqtq on 2017-12-18.
 */

public class ChatData {
    private String userName;
    private String message;
    private String toUserName;
    private Long time;

    public ChatData() { }

    public ChatData(String userName, String message, String toUserName, Long time) {
        this.userName = userName;
        this.message = message;
        this.toUserName = toUserName;
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
