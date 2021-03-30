package com.vpn.realestate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.util.SizeF;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText etUserName, etEmail, etPassword, etConPassword, etMobile, etAddress, etPincode;
    TextView tvGotoSignIn;
    Spinner spCity;
    Button btnSignUp;
    String cityName;
    ArrayList<String> cityList = new ArrayList<>();

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

        //set the spinner value
        fillCitySpinner();

        //set spinner value to send to database
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName = cityList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //sign up btn click event
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkUserName() && checkEmail() && checkPassword() && checkConPassword()
                        && checkMobile() && checkAddress() && checkPincode()) {

                    sendSignUpRequest();

                } else {
                    Toast.makeText(SignUpActivity.this, "Enter Correct Details", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void fillCitySpinner() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CITY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONCity(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONCity(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JSONField.FLAG);
            if (flag == 1) {

                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.CITY_ARRAY);

                if (jsonArray.length() > 0) {

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject objCity = jsonArray.getJSONObject(i);

                        String city = objCity.optString(JSONField.CITY_NAME);
                        cityList.add(city);

                        //set city names in spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_list_item_1, cityList);
                        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        spCity.setAdapter(adapter);

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendSignUpRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.SIGN_UP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                parseSignUpResponse(response);

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
                params.put(JSONField.USER_NAME, etUserName.getText().toString());
                params.put(JSONField.USER_EMAIL, etEmail.getText().toString());
                params.put(JSONField.USER_PASSWORD, etPassword.getText().toString());
                params.put(JSONField.USER_MOBILE, etMobile.getText().toString());
                params.put(JSONField.USER_ADDRESS, etAddress.getText().toString());
                params.put(JSONField.AREA_PINCODE, etPincode.getText().toString());
                params.put(JSONField.CITY_NAME, cityName);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseSignUpResponse(String response) {

        Log.d("RESPONSE", response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JSONField.SUCCESS);
            String msg = jsonObject.optString(JSONField.MSG);
            if (flag == 1) {

                Log.d("TEG", "Sign Up success...");
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);

            }else {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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