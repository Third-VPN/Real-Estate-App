package com.vpn.realestate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vpn.realestate.ApiManager.JSONField;
import com.vpn.realestate.ApiManager.WebURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    TextView tvGotoSignUp, tvForgotPassword;
    Button btnSignIn;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";
    public static final String NAME_KEY = "user_name";

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

                if (checkEmail() /*&& checkPassword()*/) {

                    sendSignInRequest();

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

    private void sendSignInRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.SIGN_IN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseSignInRequest(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put(JSONField.USER_EMAIL, etEmail.getText().toString());
                params.put(JSONField.USER_PASSWORD, etPassword.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseSignInRequest(String response) {

        Log.d("RESPONSE", response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JSONField.SUCCESS);
            String msg = jsonObject.optString(JSONField.MSG);

            if (flag == 1) {

                String userID = jsonObject.optString(JSONField.USER_ID);
                String userName = jsonObject.optString(JSONField.USER_NAME);
                Log.d("TAG", msg);

                //save user data
                SharedPreferences preferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ID_KEY, userID);
                editor.putString(NAME_KEY, userName);
                editor.commit();

                Log.d("ID", preferences.getString(ID_KEY,""));

                Toast.makeText(this, "Welcome! " + userName, Toast.LENGTH_SHORT).show();

                //moving to home screen
                Intent intent = new Intent(SignInActivity.this, DrawerActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //email validation
    private boolean checkEmail() {

        boolean isValidEmail = false;
        String email = etEmail.getText().toString().trim();

        if (email.length() <= 0) {
            etEmail.setError("Enter Email Address");
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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