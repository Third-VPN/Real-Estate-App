package com.vpn.realestate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OTPActivity extends AppCompatActivity {
    EditText etOTP;
    Button btnVerify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_o_t_p);

        etOTP = findViewById(R.id.etOTP);

        btnVerify = findViewById(R.id.btnVerify);

        Intent intent = getIntent();
        int otp = intent.getIntExtra("OTP", Integer.parseInt(""));

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int enterOTP = Integer.parseInt(etOTP.getText().toString().trim());

                if (!etOTP.getText().toString().trim().equals("")
                        && enterOTP == otp) {

                    Intent intent = new Intent(OTPActivity.this, PasswordRecoveryActivity.class);
                    startActivity(intent);

                }

            }
        });

    }
}