package com.example.chat.ui.notifications.norificationAdapterAndClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.graphics.Color;
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
import com.example.chat.ui.home.chatAdapterAndClass.ChatElement;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class notificationAdapter extends ArrayAdapter<NotificationElement> {
    private Activity mContext;
    private List<NotificationElement> mNotificationElement;

    private DatabaseReference mDataBaseRef;
    FirebaseFirestore firestore;

    String nameReciver;
    String imageURLReciver;



    public notificationAdapter(@NonNull Activity context, List<NotificationElement> resource) {
        super(context, R.layout.notifcationcardview, resource);
        this.mContext = context;
        this.mNotificationElement = resource;
    }

    @NotNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();

        @SuppressLint({"InflateParams", "ViewHolder"})
        View itemView = inflater.inflate(R.layout.chats_card, null, true);

        ImageView not_Image;
        TextView not_message, not_time, not_ok, not_cancel;
        LinearLayout lrBack;

        firestore = FirebaseFirestore.getInstance();

        not_Image = (ImageView) itemView.findViewById(R.id.not_image);

        not_message = (TextView) itemView.findViewById(R.id.text_notification);
        not_time = (TextView) itemView.findViewById(R.id.time_notification);
        not_ok = (TextView) itemView.findViewById(R.id.not_ok);
        not_cancel = (TextView) itemView.findViewById(R.id.not_cancel);

        lrBack = (LinearLayout) itemView.findViewById(R.id.lrback);

        final NotificationElement element = mNotificationElement.get(position);

        Picasso.get().load(element.getNot_ImageUrl()).into(not_Image);

        getNeedElementFromFirestore(element.getNot_ID_reciver());

        not_message.setText(element.getNot_message());
        not_time.setText(element.getNot_time());

        if(!element.getNot_ver()){
            lrBack.setBackgroundColor(Color.parseColor("#FFEB3B"));
        } else {
            lrBack.setBackgroundColor(Color.parseColor("#D4D1D1"));
        }

        not_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNotification(element.getNot_ID_reciver(), element.getNot_ID());
                switch (element.getType()){
                    case 1:
                        acceptCreationChat(element);
                        break;
                    case 2 :
                        userRejectYouRequest(element.getNot_ID_sender(), element.getNot_ID_reciver(), element.getNot_ID());
                        break;
                    case 3:
                        userAcceptYouRequest(element.getNot_ID_sender(), element.getNot_ID_reciver(), element.getNot_ID());
                        break;
                }
            }
        });

        not_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return itemView;
    }


    private void userAcceptYouRequest(String not_id_sender, String not_id_reciver, String not_id) {

    }

    private void userRejectYouRequest(String not_id_sender, String not_id_reciver, String not_id) {

    }

    private void acceptCreationChat(NotificationElement element) {
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("Notification").child(element.getNot_ID_reciver());

        String notification_id =  mDataBaseRef.push().getKey();
        String notification_message = nameReciver + "accept you chat request" ;

        String time = getCurrentTime();

        NotificationElement value = new NotificationElement(imageURLReciver, notification_message, time, false, element.getNot_ID_reciver(), element.getNot_ID_sender(), notification_id, element.getNot_name_sender(), 2 );
        mDataBaseRef.setValue(value);

    }

    private String getCurrentTime() {
        return java.time.LocalTime.now().toString();
    }

    private void getNeedElementFromFirestore(String not_id_reciver) {
        final DocumentReference documentReference = firestore.collection("users").document(not_id_reciver);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                nameReciver = value.getString("name");
                imageURLReciver = value.getString("imageURL");
            }
        });
    }


    private void deleteNotification(String not_id_reciver, String not_id) {
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("Notification").child(not_id_reciver);
        mDataBaseRef.child(not_id).removeValue();
    }

}
