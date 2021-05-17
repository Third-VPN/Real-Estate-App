package com.vpn.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText etMobile;
    TextView tvGotoSignIn;
    Button btnSendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_password);

        tvGotoSignIn = findViewById(R.id.tvGotoSignIn);
        etMobile = findViewById(R.id.etMobile);
        btnSendOTP = findViewById(R.id.btnSendOTP);

        //go to sign in screen text click event
        String text = "Back to Sign In";
        SpannableString ss = new SpannableString(text);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(span, 8, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvGotoSignIn.setText(ss);
        tvGotoSignIn.setMovementMethod(LinkMovementMethod.getInstance());

        //user phone otp send event
        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile;

                mobile = etMobile.getText().toString();

                if (!mobile.equals("")) {

                    Random random = new Random();
                    int OTP = random.nextInt(9999);
                    String msg = "Your OTP is : " + OTP;

                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(mobile, null, msg, null, null);

                    Intent intent = new Intent(ForgotPasswordActivity.this, OTPActivity.class);
                    intent.putExtra("OTP", OTP);
                    startActivity(intent);

                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}