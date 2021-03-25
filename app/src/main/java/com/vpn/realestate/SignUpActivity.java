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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText etUserName, etEmail, etPassword, etConPassword, etMobile, etAddress, etPincode;
    TextView tvGotoSignIn;
    Spinner spCity;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        tvGotoSignIn = findViewById(R.id.tvGotoSignIn);

        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConPassword = findViewById(R.id.etConPassword);
        etMobile = findViewById(R.id.etMobile);
        etAddress = findViewById(R.id.etAddress);
        etPincode = findViewById(R.id.etPincode);

        spCity = findViewById(R.id.spCity);

        btnSignUp = findViewById(R.id.btnSignUp);

        //go to sign in screen text click event
        String text = "Already have an Account ? Sign In";
        SpannableString ss = new SpannableString(text);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(span, 26, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvGotoSignIn.setText(ss);
        tvGotoSignIn.setMovementMethod(LinkMovementMethod.getInstance());


        //sign up btn click event
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkUserName() && checkEmail() && checkPassword() && checkConPassword()
                        && checkMobile() && checkAddress() && checkPincode()) {

                    Toast.makeText(SignUpActivity.this, "ok", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SignUpActivity.this, "Enter Correct Details", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //user name validation
    private boolean checkUserName() {

        boolean isValidUserName = false;

        if (etUserName.getText().toString().trim().length() <= 0) {

            etUserName.setError("Enter Your Name");

        } else {
            isValidUserName = true;
        }

        return isValidUserName;
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

    //confirm password validation
    private boolean checkConPassword() {

        boolean isValidConPassword = false;
        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$";

        if (etConPassword.getText().toString().trim().length() <= 0) {
            etConPassword.setError("Enter Password");
        } else if (Pattern.compile(PASSWORD_PATTERN).matcher(etConPassword.getText().toString().trim()).matches()
        && etPassword.getText().toString().equals(etConPassword.getText().toString())) {
            isValidConPassword = true;
        } else {
            etConPassword.setError("Enter Correct Password");
        }

        return isValidConPassword;
    }

    //mobile number validation
    private boolean checkMobile() {

        boolean isValidMobile = false;

        if (etMobile.getText().toString().trim().length() <= 0) {
            etMobile.setError("Enter Mobile Number");
        } else if (Patterns.PHONE.matcher(etMobile.getText().toString().trim()).matches()) {
            isValidMobile = true;
        } else {
            etMobile.setError("Enter Correct Mobile Number");
        }

        return isValidMobile;
    }

    //address validation
    private boolean checkAddress() {

        boolean isValidAddress = false;

        if (etAddress.getText().toString().trim().length() <= 0) {
            etAddress.setError("Enter your address");
        } else {
            isValidAddress = true;
        }

        return isValidAddress;
    }

    //pincode validation
    private boolean checkPincode() {

        boolean isValidPincode = false;

        if (etPincode.getText().toString().trim().length() <= 0) {
            etPincode.setError("Enter Pincode");
        } else {
            isValidPincode = true;
        }

        return isValidPincode;
    }

}