package com.example.chat.ui.notifications.norificationAdapterAndClass;

public class NotificationElement {

    public NotificationElement(){}

    String not_ImageUrl;
    String not_message;
    String not_time;
    Boolean not_ver;
    String not_ID_sender;
    String not_ID_reciver;
    String not_ID;
    String not_name_sender;
    int type;

    public NotificationElement(String not_ImageUrl, String not_message, String not_time, Boolean not_ver, String not_ID_sender, String not_ID_reciver, String not_ID, String not_name_sender, int type) {
        this.not_ImageUrl = not_ImageUrl;
        this.not_message = not_message;
        this.not_time = not_time;
        this.not_ver = not_ver;
        this.not_ID_sender = not_ID_sender;
        this.not_ID_reciver = not_ID_reciver;
        this.not_ID = not_ID;
        this.not_name_sender = not_name_sender;
        this.type = type;
    }

    public String getNot_ImageUrl() {
        return not_ImageUrl;
    }

    public void setNot_ImageUrl(String not_ImageUrl) {
        this.not_ImageUrl = not_ImageUrl;
    }

    public String getNot_message() {
        return not_message;
    }

    public void setNot_message(String not_message) {
        this.not_message = not_message;
    }

    public String getNot_time() {
        return not_time;
    }

    public void setNot_time(String not_time) {
        this.not_time = not_time;
    }

    public Boolean getNot_ver() {
        return not_ver;
    }

    public void setNot_ver(Boolean not_ver) {
        this.not_ver = not_ver;
    }

    public String getNot_ID_sender() {
        return not_ID_sender;
    }

    public void setNot_ID_sender(String not_ID_sender) {
        this.not_ID_sender = not_ID_sender;
    }

    public String getNot_ID_reciver() {
        return not_ID_reciver;
    }

    public void setNot_ID_reciver(String not_ID_reciver) {
        this.not_ID_reciver = not_ID_reciver;
    }

    public String getNot_ID() {
        return not_ID;
    }

    public void setNot_ID(String not_ID) {
        this.not_ID = not_ID;
    }

    public String getNot_name_sender() {
        return not_name_sender;
    }

    public void setNot_name_sender(String not_name_sender) {
        this.not_name_sender = not_name_sender;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
