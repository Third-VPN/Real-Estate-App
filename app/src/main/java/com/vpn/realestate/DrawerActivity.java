package com.vpn.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    NavigationView navigation;
    DrawerLayout drawer;
    View navheaderView;
    TextView tvName, tvEmail;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";
    public static final String NAME_KEY = "user_name";
    public static final String EMAIL_KEY = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer);
        navigation = findViewById(R.id.nav_view);

        navheaderView = navigation.getHeaderView(0);

        tvName = navheaderView.findViewById(R.id.tvName);
        tvEmail = navheaderView.findViewById(R.id.tvEmail);

        SharedPreferences preferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        String user_name = preferences.getString(NAME_KEY, "");
        String user_email = preferences.getString(EMAIL_KEY, "");

        if (!user_name.equals("") && !user_email.equals("")) {

            tvName.setText(user_name);
            tvEmail.setText(user_email);

        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DrawerActivity.this, drawer, toolbar,
                R.string.navigation_view_open, R.string.navigation_view_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigation.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {

            this.setTitle("Search Property");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,
                    new SearchPropertyFragment()).commit();
            navigation.setCheckedItem(R.id.nav_Search);

        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_Search:
                this.setTitle("Search Property");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,
                        new SearchPropertyFragment()).commit();
                break;

            case R.id.nav_add:
                Intent intent = new Intent(DrawerActivity.this, FinalAddPropertyActivity.class);
                startActivity(intent);
//                this.setTitle("Add Your Property");
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,
//                        new AddPropertyFragment()).commit();
                break;

//            case R.id.nav_Bookmark:
//                this.setTitle("Bookmarks");
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,
//                        new BookmarkFragment()).commit();
//                break;

            case R.id.nav_Activity:
                this.setTitle("My Activity Log");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,
                        new ActivityLogFragment()).commit();
                break;

            case R.id.nav_Profile:
                this.setTitle("My Profile");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,
                        new MyProfileFragment()).commit();
                break;

            case R.id.nav_ChangePassword:
                this.setTitle("Change Password");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,
                        new ChangePasswordFragment()).commit();
                break;

            case R.id.nav_RateUs:
                this.setTitle("Rate Us");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,
                        new RateUsFragment()).commit();
                break;

            case R.id.nav_LogOut:

                AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);

                builder.setTitle("Log out");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

                        Toast.makeText(DrawerActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(DrawerActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}