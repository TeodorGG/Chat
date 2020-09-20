package com.example.chat.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.chat.MainActivity;
import com.example.chat.R;
import com.example.chat.RegAuth.loginActivity;

public class SplashScreen extends AppCompatActivity {

    private final static String MY_PREFS = "saves";
    private final int TIME_SPALSH = 1000 * 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
                boolean _vr = prefs.getBoolean("verification", false);

                if(_vr) {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(SplashScreen.this, loginActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, TIME_SPALSH);


    }

    public void saveAuth(boolean vr_){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putBoolean("verification",vr_);
        editor.apply();
    }


}