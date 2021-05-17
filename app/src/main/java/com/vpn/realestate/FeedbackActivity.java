package com.vpn.realestate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {
    RatingBar rbFeedback;
    EditText etFeedback;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_feedback);

        rbFeedback = findViewById(R.id.rbFeedback);

        etFeedback = findViewById(R.id.etFeedback);

        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int noOfStars = rbFeedback.getNumStars();
                float rating = rbFeedback.getRating();

                Toast.makeText(FeedbackActivity.this, "Thank You For " + rating + " Rating.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(FeedbackActivity.this, DrawerActivity.class);
                startActivity(intent);

            }
        });

    }
}