package com.example.chat.RegAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.MainActivity;
import com.example.chat.R;
import com.example.chat.ui.home.chatAdapterAndClass.ChatElement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {

    private static String TAG_FIREBASE = "FIREBASE";

    MediaPlayer FXPlayer;

    EditText _name_box, _mail_box, _password_box_1, _password_box_2;
    Button _singUp;
    TextView _goLogin;

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBaseRef;
    private FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();

    ;

    private String email = null ,password = null;
;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _name_box = (EditText) findViewById(R.id.fullNames);
        _mail_box = (EditText) findViewById(R.id.userEmailId);
        _password_box_1 = (EditText) findViewById(R.id.password);
        _password_box_2 = (EditText) findViewById(R.id.confirmPassword);

        _singUp = (Button) findViewById(R.id.signUpBtn);

        _goLogin = (TextView) findViewById(R.id.already_user);


        _goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.click);
                startActivity(new Intent(registerActivity.this, loginActivity.class));
            }
        });

        _singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.click);
                singUpTes();
            }
        });
    }

    private void singUpTes() {

        _password_box_1.setTextAppearance(getApplicationContext(),
                R.style.normForLogActPasw);
        _password_box_2.setTextAppearance(getApplicationContext(),
                R.style.normForLogActPasw);


        if(verBox()){
            password = _password_box_1.getText().toString();

            email = _mail_box.getText().toString();

            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveDataInCloudFirebase();
                                createTheBaseDirectoryInFirebaseRealTime();
                                Log.d(TAG_FIREBASE, "createUserWithEmail:success");
                                startActivity(new Intent(registerActivity.this, loginActivity.class));
                            } else {
                                Log.w(TAG_FIREBASE, "createUserWithEmail:failure", task.getException());
                                if (password.length() < 6) {
                                    //error sound
                                    playSound(R.raw.error);

                                    Toast.makeText(registerActivity.this, "minim length of password is 6 ", Toast.LENGTH_SHORT).show();
                                } else {
                                    //red shadow effect


                                    //error sound
                                    playSound(R.raw.error);


                                    Toast.makeText(registerActivity.this, "auth_error", Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(registerActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                playSound(R.raw.error);
                            }

                        }
                    });

        } else {

            _password_box_1.setTextAppearance(getApplicationContext(),
                    R.style.ErrorForLogAct);
            _password_box_2.setTextAppearance(getApplicationContext(),
                    R.style.ErrorForLogAct);
            Toast.makeText(registerActivity.this, "Passwords don't match",
                    Toast.LENGTH_SHORT).show();

        }

    }

    private void createTheBaseDirectoryInFirebaseRealTime() {
        String userID = mAuth.getUid();

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("ListChatForUser").child(userID).child(userID);

        ChatElement element = new ChatElement(userID, getResources().getString(R.string.save_imageUrl),"Save Info",false," "," ",1,false,false );
        mDataBaseRef.setValue(element);
    }


    private void saveDataInCloudFirebase() {

            String userID = mAuth.getUid();

            Map<String, Object> userdata = new HashMap<>();
            userdata.put("name", _name_box.getText().toString());
            userdata.put("email", _mail_box.getText().toString());
            userdata.put("online", false);
            userdata.put("imageURL", "#");
            userdata.put("last", Calendar.getInstance().getTime().toString());

            assert userID != null;

            dbFirestore.collection("users").document(userID).set(userdata);
    }

    private boolean verBox() {
        return !_password_box_1.getText().toString().equals("") && !_password_box_2.getText().toString().equals("") && !_mail_box.getText().toString().equals("") &&  _password_box_1.getText().toString().equals(_password_box_2.getText().toString());
    }

    public void playSound(int _id)
    {
        if(FXPlayer != null)
        {
            FXPlayer.stop();
            FXPlayer.release();
        }
        FXPlayer = MediaPlayer.create(this, _id);
        if(FXPlayer != null)
            FXPlayer.start();
    }
}