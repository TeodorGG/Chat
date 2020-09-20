package com.example.chat.RegAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.MainActivity;
import com.example.chat.R;
import com.example.chat.splash.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginActivity extends AppCompatActivity {

    private final static String MY_PREFS = "saves";

    EditText email_edit, password_edit;
    CheckBox show_password;
    Button login_button;
    TextView forgot_password, create_account;
    ProgressBar progressBar;

    MediaPlayer FXPlayer;

    private FirebaseAuth mAuth;

    private static String TAG_FIREBASE = "FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_edit =(EditText) findViewById(R.id.login_emailid);
        password_edit =(EditText) findViewById(R.id.login_password);

        show_password = (CheckBox) findViewById(R.id.show_hide_password);

        login_button = (Button) findViewById(R.id.loginBtn);

        forgot_password = (TextView) findViewById(R.id.forgot_password);
        create_account = (TextView) findViewById(R.id.createAccount);

        progressBar = (ProgressBar) findViewById(R.id.progresscircular);



        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                playSound(R.raw.clickswitch);
                if(isChecked){
                    password_edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        

        //Go to forgot_password activity
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, forgotPassword.class));

                //click sound
                playSound(R.raw.click);
            }
        });
        
        //go to register activity
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, registerActivity.class));

                //click sound
                playSound(R.raw.click);
            }
        });
        
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                //click sound
                playSound(R.raw.click);

                login_verification();
            }
        });



    }

    private void login_verification() {

        progressBar.setVisibility(View.VISIBLE);

        String _login = null,_password = null;

        _login = email_edit.getText().toString();
        _password = password_edit.getText().toString();

        email_edit.setTextAppearance(getApplicationContext(),
                R.style.normForLogActMail);
        password_edit.setTextAppearance(getApplicationContext(),
                R.style.normForLogActPasw);

        mAuth = FirebaseAuth.getInstance();

        final String final_login = _login;
        final String final_password = _password;

        if(!_login.equals("") && !_password.equals("")) {
            mAuth.signInWithEmailAndPassword(_login, _password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
                                editor.putBoolean("verification", true);
                                editor.apply();

                                startActivity(new Intent(loginActivity.this, MainActivity.class));
                            } else {
                                if (!isValidEmailAddress(final_login)) {
                                    Toast.makeText(loginActivity.this, "Email is incorrectly write",
                                            Toast.LENGTH_SHORT).show();

                                    //red shadow effect
                                    email_edit.setTextAppearance(getApplicationContext(),
                                            R.style.ErrorForLogAct);

                                    //error sound
                                    playSound(R.raw.error);

                                }

                                if (final_password.length() < 6) {
                                    //red shadow effect
                                    password_edit.setTextAppearance(getApplicationContext(),
                                            R.style.ErrorForLogAct);

                                    //error sound
                                    playSound(R.raw.error);

                                    Toast.makeText(loginActivity.this, "minim length of password is 6 ", Toast.LENGTH_SHORT).show();
                                } else {
                                    //red shadow effect
                                    password_edit.setTextAppearance(getApplicationContext(),
                                            R.style.ErrorForLogAct);

                                    //error sound
                                    playSound(R.raw.error);


                                    Toast.makeText(loginActivity.this, "auth_error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(loginActivity.this, "auth_error", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);


    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
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


