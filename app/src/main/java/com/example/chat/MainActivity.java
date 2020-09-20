package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.chat.ui.profile.ProfileFragment;
import com.example.chat.ui.home.HomeFragment;
import com.example.chat.ui.notifications.NotificationsFragment;
import com.google.firebase.database.FirebaseDatabase;

import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmoothBottomBar navView = findViewById(R.id.bottom_bar);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        navView.setBottomBarCallback(new SmoothBottomBar.BottomBarCallback() {
            @Override
            public void onItemSelect(int i) {
                if(i == 0){
                    HomeFragment fragment = new HomeFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment , fragment);
                    ft.commit();
                } else if(i == 1){
                    NotificationsFragment fragment = new NotificationsFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment , fragment);
                    ft.commit();
                } else if(i == 2){
                    ProfileFragment fragment = new ProfileFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment , fragment);
                    ft.commit();
                }
            }

            @Override
            public void onItemReselect(int i) {

            }
        });
    }
}