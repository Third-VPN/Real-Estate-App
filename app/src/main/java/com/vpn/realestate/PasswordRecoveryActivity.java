package com.vpn.realestate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class PasswordRecoveryActivity extends AppCompatActivity {
    EditText etNewPassword, etNewConPassword;
    Button btnSubmit;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_password_recovery);

        etNewPassword = findViewById(R.id.etNewPassword);
        etNewConPassword = findViewById(R.id.etNewConPassword);

        btnSubmit = findViewById(R.id.btnSubmit);

        SharedPreferences preferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        user_id = preferences.getString(ID_KEY, "");

        //password recovery btn click event
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPassword, newConPassword;
                newPassword = etNewPassword.getText().toString();
                newConPassword = etNewConPassword.getText().toString();

                if (checkNewPassword() && checkNewConPassword() && newPassword.equals(newConPassword)) {

                    if (!user_id.equals("")) {
                        sendChangePasswordRequest();
                    } else {
                        Toast.makeText(PasswordRecoveryActivity.this, "Please Log In First", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PasswordRecoveryActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(PasswordRecoveryActivity.this, "Enter details", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendChangePasswordRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PASSWORD_CHANGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONChangePasswordRequest(response);
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

                params.put(JSONField.USER_ID, user_id);
                params.put(JSONField.USER_PASSWORD, etNewPassword.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PasswordRecoveryActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONChangePasswordRequest(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                String msg = jsonObject.optString(JSONField.MSG);

                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PasswordRecoveryActivity.this, MenuActivity.class);
                startActivity(intent);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //password validation
    private boolean checkNewPassword() {

        boolean isValidPassword = false;
        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$";

        if (etNewPassword.getText().toString().trim().length() <= 0) {
            etNewPassword.setError("Enter Password");
        } else if (Pattern.compile(PASSWORD_PATTERN).matcher(etNewPassword.getText().toString().trim()).matches()) {
            isValidPassword = true;
        } else {
            etNewPassword.setError("Enter Correct Password");
        }

        return isValidPassword;
    }

    //confirm password validation
    private boolean checkNewConPassword() {

        boolean isValidPassword = false;
        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$";

        if (etNewConPassword.getText().toString().trim().length() <= 0) {
            etNewConPassword.setError("Enter Password");
        } else if (Pattern.compile(PASSWORD_PATTERN).matcher(etNewConPassword.getText().toString().trim()).matches()) {
            isValidPassword = true;
        } else {
            etNewConPassword.setError("Enter Correct Password");
        }

        return isValidPassword;
    }

}