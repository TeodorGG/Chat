package com.example.chat.RegAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

     private static String TAG_FIREBASE= "FIREBASE" ;

    EditText email_box;
    TextView go_back_button, send_email_button;

    MediaPlayer FXPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email_box = (EditText) findViewById(R.id.registered_email);

        go_back_button = (TextView) findViewById(R.id.backToLoginBtn);
        send_email_button = (TextView) findViewById(R.id.forgot_button);


        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.click);
                startActivity(new Intent(forgotPassword.this , loginActivity.class));
            }
        });

        send_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.click);
                sendInstruction();
            }
        });
    }

    private void sendInstruction() {
        if(!email_box.getText().toString().equals("")){
            FirebaseAuth.getInstance().sendPasswordResetEmail(email_box.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgotPassword.this, "Email sent success" ,Toast.LENGTH_SHORT).show();
                                Log.d(TAG_FIREBASE, "Email sent.");
                                startActivity(new Intent(forgotPassword.this , loginActivity.class));
                            }
                            else{
                                playSound(R.raw.error);
                                Toast.makeText(forgotPassword.this, "Email sent error" ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
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