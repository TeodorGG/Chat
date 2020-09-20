package com.example.chat.chat;

import android.os.Bundle;

import com.example.chat.ui.home.chatAdapterAndClass.ChatElement;
import com.example.chat.ui.home.chatAdapterAndClass.chatListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.type.DateTime;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class chatActivity extends AppCompatActivity {

    ImageView goBack;

    ImageView user_Image_Chat, menu_Chat, add_File_In_Chat, send_message_In_Chat;
    TextView User_Name_Chat, User_Online_Chat;
    EditText edit_message_Chat;

    ListView listView_chat;
    List<MessageElement> mMessageElement;

    private DatabaseReference mDataBaseRef;
    private FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
    private String userID;
    FirebaseFirestore firestore;

    String chatUserID = getIntent().getStringExtra("chatUserID");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userID = user.getUid();
        firestore = FirebaseFirestore.getInstance();

        goBack = (ImageView) findViewById(R.id.go_back_button);
        user_Image_Chat = (ImageView) findViewById(R.id.user_image_chat);
        menu_Chat = (ImageView) findViewById(R.id.menu_chat);
        add_File_In_Chat = (ImageView) findViewById(R.id.add_file_chat);
        send_message_In_Chat = (ImageView) findViewById(R.id.send_button_chat);

        User_Name_Chat = (TextView) findViewById(R.id.user_name_chat);
        User_Online_Chat = (TextView) findViewById(R.id.user_online_chat);

        edit_message_Chat = (EditText) findViewById(R.id.input_message_In_chat);

        listView_chat = (ListView) findViewById(R.id.list_view_user_chat);
        mMessageElement = new ArrayList<>();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadData();
        loadMessage();

        send_message_In_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


    }

    private void loadData() {
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("ListChatForUser").child(userID).child(chatUserID);

        DocumentReference doReferens;
        doReferens = firestore.collection("users").document(chatUserID);

        doReferens.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String userChatName = value.getString("name");
                String imageURL = value.getString("imageURL");
                boolean online = value.getBoolean("online");

                User_Name_Chat.setText(userChatName);
                User_Online_Chat.setText(String.valueOf(online));
                Picasso.get().load(imageURL).into(user_Image_Chat);
            }
        });

    }

    private void sendMessage() {

        String messageText = edit_message_Chat.getText().toString();
        if (!"".equals(messageText)){
            String id =  mDataBaseRef.push().getKey();

            String time = LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute();

            mDataBaseRef = FirebaseDatabase.getInstance().getReference("ListChatForUser").child(userID).child("|"+chatUserID).child(id);
            MessageElement mElement = new MessageElement(userID, chatUserID, messageText, time, 1 );
            mDataBaseRef.setValue(mElement);

            mDataBaseRef = FirebaseDatabase.getInstance().getReference("ListChatForUser").child(chatUserID).child("|"+userID).child(id);
            MessageElement mElements = new MessageElement(userID, chatUserID, messageText, time, 1);
            mDataBaseRef.setValue(mElements);

        }
    }

    private void loadMessage() {

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("ListChatForUser").child(userID).child("|"+chatUserID);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMessageElement.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    MessageElement element = postSnapshot.getValue(MessageElement.class);
                    mMessageElement.add(element);
                }

                messageAdapter adapter = new messageAdapter(getParent(), mMessageElement);
                listView_chat.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chatActivity.this , error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}