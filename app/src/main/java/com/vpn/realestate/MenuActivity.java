package com.vpn.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cvSearch, cvPost/*, cvBookmark*/, cvProfile;

    public static final String PROFILE = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        cvSearch = findViewById(R.id.cvSearch);
        cvPost = findViewById(R.id.cvPost);
        //cvBookmark = findViewById(R.id.cvBookmark);
        cvProfile = findViewById(R.id.cvProfile);

        cvSearch.setOnClickListener(this);
        cvPost.setOnClickListener(this);
        //cvBookmark.setOnClickListener(this);
        cvProfile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvSearch:
                Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
                startActivity(intent);
                break;

            case R.id.cvPost:
                Intent intent1 = new Intent(MenuActivity.this, PostPropertyActivity.class);
                startActivity(intent1);
                break;

//            case R.id.cvBookmark:
//                Intent intent2 = new Intent(MenuActivity.this, BookmarkActivity.class);
//                startActivity(intent2);
//                break;

            case R.id.cvProfile:
                Intent intent3 = new Intent(MenuActivity.this, MyProfileActivity.class);
                startActivity(intent3);
                break;
        }
    }

}