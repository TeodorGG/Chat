package com.example.chat.ui.home.chatAdapterAndClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chat.R;
import com.example.chat.chat.chatActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

public class chatListAdapter extends ArrayAdapter<ChatElement> {

    private Activity mContext;
    private List<ChatElement> mChatElement;


    public chatListAdapter(@NonNull Activity context, List<ChatElement> resource) {
        super(context, R.layout.chats_card, resource);

        this.mContext = context;
        this.mChatElement = resource;

    }

    @NotNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();

        @SuppressLint({"InflateParams", "ViewHolder"}) final View itemView = inflater.inflate(R.layout.chats_card, null, true);

        ImageView User_Image, User_newMessage;
        TextView User_Name, User_lastMessage, User_lastMessageTime;
        LinearLayout lrBackChat;

        User_Image = (ImageView) itemView.findViewById(R.id.chats_photo);
        User_newMessage = (ImageView) itemView.findViewById(R.id.new_message);

        User_Name = (TextView) itemView.findViewById(R.id.user_name);
        User_lastMessage = (TextView) itemView.findViewById(R.id.user_last_message);
        User_lastMessageTime = (TextView) itemView.findViewById(R.id.time_of_message);

        lrBackChat = (LinearLayout) itemView.findViewById(R.id.lrBackChats);

        final ChatElement element = mChatElement.get(position);

        Picasso.get().load(element.getUser_ImageUrl()).into(User_Image);

        if(element.isUser_NewMessage()) User_newMessage.setBackgroundColor(Color.GREEN); else{
            User_newMessage.setBackgroundColor(Color.TRANSPARENT);
        }

        User_Name.setText(element.getUser_Name());
        User_lastMessageTime.setText(element.getUser_LastMessageTime());
        User_lastMessage.setText(element.getUser_LastMessage());

        lrBackChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToChatIntent = new Intent(getContext(), chatActivity.class);
                goToChatIntent.putExtra("chatUserID", element.getUser_ID());
                itemView.getContext().startActivity(goToChatIntent);
            }
        });



        return itemView;
    }
}
