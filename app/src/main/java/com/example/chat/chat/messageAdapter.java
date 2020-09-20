package com.example.chat.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chat.R;
import com.example.chat.ui.home.chatAdapterAndClass.ChatElement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.util.List;

public class messageAdapter extends ArrayAdapter<MessageElement> {

    private Activity mContext;
    private List<MessageElement> mMessageElement;
    private FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
    private String userID;


    public messageAdapter(@NonNull Activity context, List<MessageElement> resource) {
        super(context, R.layout.card_view_message, resource);

        this.mContext = context;
        this.mMessageElement = resource;
    }

    Space sp1,sp2;
    TextView message_Text, message_Time;
    LinearLayout llBack;

    @NotNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint({"InflateParams", "ViewHolder"}) final View itemView = inflater.inflate(R.layout.card_view_message, null, true);

        userID = user.getUid();

        sp1 = (Space) itemView.findViewById(R.id.space1);
        sp2 = (Space) itemView.findViewById(R.id.space2);

        message_Text = (TextView) itemView.findViewById(R.id.test_message);
        message_Time = (TextView) itemView.findViewById(R.id.time_message);

        llBack = (LinearLayout) itemView.findViewById(R.id.llBack);

        final MessageElement element = mMessageElement.get(position);

        message_Text.setText(element.getMessage_Text());
        message_Time.setText(element.getMessage_Time());

        if(userID.equals(element.getUser_ID_Sender())){
            sp1.setVisibility(View.GONE);
            sp2.setVisibility(View.VISIBLE);
            llBack.setBackgroundColor(Color.parseColor("#00BCD4"));
        } else {
            sp1.setVisibility(View.VISIBLE);
            sp2.setVisibility(View.GONE);
            llBack.setBackgroundColor(Color.parseColor("#4CAF50"));
        }

        return itemView;
    }



}
