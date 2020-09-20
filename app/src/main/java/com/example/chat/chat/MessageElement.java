package com.example.chat.chat;

public class MessageElement {

    String User_ID_Sender;
    String User_ID_Receiver;
    String Message_Text;
    String Message_Time;
    int Message_Type;

    public MessageElement(){}

    public MessageElement(String user_ID_Sender, String user_ID_Receiver, String message_Text, String message_Time, int message_Type) {
        User_ID_Sender = user_ID_Sender;
        User_ID_Receiver = user_ID_Receiver;
        Message_Text = message_Text;
        Message_Time = message_Time;
        Message_Type = message_Type;
    }

    public String getUser_ID_Sender() {
        return User_ID_Sender;
    }

    public void setUser_ID_Sender(String user_ID_Sender) {
        User_ID_Sender = user_ID_Sender;
    }

    public String getUser_ID_Receiver() {
        return User_ID_Receiver;
    }

    public void setUser_ID_Receiver(String user_ID_Receiver) {
        User_ID_Receiver = user_ID_Receiver;
    }

    public String getMessage_Text() {
        return Message_Text;
    }

    public void setMessage_Text(String message_Text) {
        Message_Text = message_Text;
    }

    public String getMessage_Time() {
        return Message_Time;
    }

    public void setMessage_Time(String message_Time) {
        Message_Time = message_Time;
    }

    public int getMessage_Type() {
        return Message_Type;
    }

    public void setMessage_Type(int message_Type) {
        Message_Type = message_Type;
    }
}
