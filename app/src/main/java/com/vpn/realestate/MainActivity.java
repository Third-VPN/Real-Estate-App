package com.vpn.realestate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";
    public static final String NAME_KEY = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //removing the action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Thread thread = new Thread() {

            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SharedPreferences preferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
                    String user_id = preferences.getString(ID_KEY, "");

                    if (!user_id.equals("")) {
                        Intent intent = new Intent(MainActivity.this, DrawerActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent1 = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(intent1);
                    }

                }
            }

        };

        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}