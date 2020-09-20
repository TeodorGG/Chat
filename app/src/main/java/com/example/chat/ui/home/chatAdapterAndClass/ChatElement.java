package com.example.chat.ui.home.chatAdapterAndClass;

public class ChatElement {

    public ChatElement(){}

    String User_ID;
    String User_ImageUrl;
    String User_Name;
    boolean User_NewMessage;
    String User_LastMessage;
    String User_LastMessageTime;
    int User_numberPosition;
    boolean User_block;
    boolean User_mute;

    public ChatElement(String user_ID, String user_ImageUrl, String user_Name, boolean user_NewMessage, String user_LastMessage, String user_LastMessageTime, int user_numberPosition, boolean user_block, boolean user_mute) {
        User_ID = user_ID;
        User_ImageUrl = user_ImageUrl;
        User_Name = user_Name;
        User_NewMessage = user_NewMessage;
        User_LastMessage = user_LastMessage;
        User_LastMessageTime = user_LastMessageTime;
        User_numberPosition = user_numberPosition;
        User_block = user_block;
        User_mute = user_mute;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getUser_ImageUrl() {
        return User_ImageUrl;
    }

    public void setUser_ImageUrl(String user_ImageUrl) {
        User_ImageUrl = user_ImageUrl;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public boolean isUser_NewMessage() {
        return User_NewMessage;
    }

    public void setUser_NewMessage(boolean user_NewMessage) {
        User_NewMessage = user_NewMessage;
    }

    public String getUser_LastMessage() {
        return User_LastMessage;
    }

    public void setUser_LastMessage(String user_LastMessage) {
        User_LastMessage = user_LastMessage;
    }

    public String getUser_LastMessageTime() {
        return User_LastMessageTime;
    }

    public void setUser_LastMessageTime(String user_LastMessageTime) {
        User_LastMessageTime = user_LastMessageTime;
    }

    public int getUser_numberPosition() {
        return User_numberPosition;
    }

    public void setUser_numberPosition(int user_numberPosition) {
        User_numberPosition = user_numberPosition;
    }

    public boolean isUser_block() {
        return User_block;
    }

    public void setUser_block(boolean user_block) {
        User_block = user_block;
    }

    public boolean isUser_mute() {
        return User_mute;
    }

    public void setUser_mute(boolean user_mute) {
        User_mute = user_mute;
    }
}
