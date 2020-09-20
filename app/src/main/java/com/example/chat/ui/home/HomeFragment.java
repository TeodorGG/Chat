package com.example.chat.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.chat.R;
import com.example.chat.ui.home.chatAdapterAndClass.ChatElement;
import com.example.chat.ui.home.chatAdapterAndClass.chatListAdapter;
import com.example.chat.ui.notifications.norificationAdapterAndClass.NotificationElement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private boolean fabExpanded = false;

    ListView listView_chat ;

    LinearLayout new_chat_layout, new_group_layout;
    FloatingActionButton new_chat_fab, new_group_fab, new_create_fab;

    List<ChatElement> mChatElement;

    private DatabaseReference mDataBaseRef;
    private FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
    private String userID;
    FirebaseFirestore firestore,firestore1;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        listView_chat = (ListView) root.findViewById(R.id.list_view_home);
        mChatElement = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        firestore1 = FirebaseFirestore.getInstance();

        userID = user.getUid();

        new_chat_fab = (FloatingActionButton) root.findViewById(R.id.new_chat_fab);
        new_group_fab = (FloatingActionButton) root.findViewById(R.id.new_group_fab);
        new_create_fab = (FloatingActionButton) root.findViewById(R.id.new_create_fab);

        new_chat_layout = (LinearLayout) root.findViewById(R.id.new_chat_layout);
        new_group_layout = (LinearLayout) root.findViewById(R.id.new_group_layout);


        loadChatsInListViewFromFirebase();

        new_create_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabExpanded){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });


        new_chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestForChat();

            }
        });

        new_group_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChatGroup();
            }
        });

        return root;
    }

    private void createChatGroup() {
    }

    private void sendRequestForChat() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity());
        alertDialog.setTitle("CreateChatWith");
        alertDialog.setMessage("email");

        final EditText input = new EditText(this.getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendNotification(input.getText().toString());
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void sendNotification(final String emailStart) {

        firestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                final String docID = document.getId();

                                DocumentReference documentReferences =   firestore1.collection("usser").document(docID);
                                documentReferences.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        String emailVer = value.getString("email");

                                        if(emailStart.equals(emailVer)){
                                            addChatElementForUser(docID);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void addChatElementForUser(final String docID) {

        final DocumentReference documentReference = firestore.collection("users").document(userID);


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String name = value.getString("name");
                String imageUrl = value.getString("imageURL");
                addNotification(name, imageUrl, docID );
            }
        });


    }

    private void addNotification(String name, String imageUrl, String docID) {


        String notification_id =  mDataBaseRef.push().getKey();
        String notification_message = name + "send you request for create chat" ;

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("Notification").child(docID).child(notification_id);

        String time = LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute();

        NotificationElement element = new NotificationElement(imageUrl, notification_message, time, false, userID, docID, notification_id, name,1 );
        mDataBaseRef.setValue(element);
    }


    private void loadChatsInListViewFromFirebase() {

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("ListChatForUser").child(userID);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChatElement.clear();
                int ver = 1;
                 for(DataSnapshot postSnapshot : snapshot.getChildren()){

                     String chatInfoDirectoryName = postSnapshot.getKey();

                     for(DataSnapshot verPosition : snapshot.getChildren()) {

                         int position = Integer.parseInt(verPosition.child("User_numberPosition").getValue().toString());

                         if (!chatInfoDirectoryName.contains("|") && position == ver) {

                             ChatElement element = postSnapshot.getValue(ChatElement.class);
                             mChatElement.add(element);
                             ver++;

                         }
                     }
                 }



                chatListAdapter adapter = new chatListAdapter(getActivity(), mChatElement);
                 listView_chat.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext() , error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    ///////////// Start Menu Section //////////////////////

    private void openSubMenusFab() {
        new_chat_layout.setVisibility(View.VISIBLE);
        new_group_layout.setVisibility(View.VISIBLE);
        new_create_fab.setImageResource(R.drawable.ic_settings);
        fabExpanded=true;
    }

    private void closeSubMenusFab() {
        new_chat_layout.setVisibility(View.GONE);
        new_group_layout.setVisibility(View.GONE);
        new_create_fab.setImageResource(R.drawable.ic_plus);
        fabExpanded=false;
    }
    ///////////// End Menu Section //////////////////////

}