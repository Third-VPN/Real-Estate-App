package com.vpn.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    TextView tvGotoSignUp, tvForgotPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in);

        tvGotoSignUp = findViewById(R.id.tvGotoSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnSignIn = findViewById(R.id.btnSignIn);

        //go to sign up screen text click event
        String text = "Haven't Account ? Sign Up";
        SpannableString ss = new SpannableString(text);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(span, 18, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvGotoSignUp.setText(ss);
        tvGotoSignUp.setMovementMethod(LinkMovementMethod.getInstance());


        // sign in btn click handling
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (checkEmail() && checkPassword()) {



                } else {
                    Toast.makeText(SignInActivity.this, "Enter Valid Details", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //forgot password text click event
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });

    }

    //email validation
    private boolean checkEmail() {

        boolean isValidEmail = false;
        String email = etEmail.getText().toString().trim();

        if (email.length() <= 0 ) {
            etEmail.setError("Enter Email Address");
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            isValidEmail = true;
        } else {
            etEmail.setError("Enter Correct Email");
        }

        return isValidEmail;
    }

    //password validation
    private boolean checkPassword() {

        boolean isValidPassword = false;
        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$";

        if (etPassword.getText().toString().trim().length() <= 0) {
            etPassword.setError("Enter Password");
        } else if (Pattern.compile(PASSWORD_PATTERN).matcher(etPassword.getText().toString().trim()).matches()) {
            isValidPassword = true;
        } else {
            etPassword.setError("Enter Correct Password");
        }

        return isValidPassword;
    }
}