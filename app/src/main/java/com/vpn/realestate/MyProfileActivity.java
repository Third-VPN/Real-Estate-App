package com.vpn.realestate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {
    EditText etName, etEmail, etMobile, etCity, etArea, etAddress;
    Button btnUpdate;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";
    public static final String NAME_KEY = "user_name";

    String user_id, user_name;
    String[] area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_profile);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etCity = findViewById(R.id.etCity);
        etArea = findViewById(R.id.etArea);
        etAddress = findViewById(R.id.etAddress);

        btnUpdate = findViewById(R.id.btnUpdate);

        SharedPreferences preferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        user_id = preferences.getString(ID_KEY, "");
        user_name = preferences.getString(NAME_KEY, "");

        Log.d("ID", preferences.getString(ID_KEY,""));

        if (!user_id.equals("")) {
            fillMyProfile();
        } else {
            Toast.makeText(MyProfileActivity.this, "Please Log In First", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MyProfileActivity.this, SignInActivity.class);
            startActivity(intent);

        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!user_id.equals("")) {
                    sendUpdateProfileRequest();
                } else {
                    Toast.makeText(MyProfileActivity.this, "Please Log In First", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MyProfileActivity.this, SignInActivity.class);
                    startActivity(intent);

                }

            }
        });
    }

    private void sendUpdateProfileRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PROFILE_UPDATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONUpdateProfile(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                area = etArea.getText().toString().split("-",2);
                Log.d("AREA NAME", area[0]);
                Log.d("AREA PINCODE", area[1]);

                HashMap<String, String> params = new HashMap<>();
                
                params.put(JSONField.USER_ID, user_id);
                params.put(JSONField.USER_NAME, etName.getText().toString());
                params.put(JSONField.USER_EMAIL, etEmail.getText().toString());
                params.put(JSONField.USER_MOBILE, etMobile.getText().toString());
                params.put(JSONField.CITY_NAME, etName.getText().toString());
                params.put(JSONField.AREA_NAME, area[0]);
                params.put(JSONField.AREA_PINCODE, area[1].trim());
                params.put(JSONField.USER_ADDRESS, etAddress.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfileActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONUpdateProfile(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                String msg = jsonObject.optString(JSONField.MSG);
                String id = jsonObject.optString(JSONField.USER_ID);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MyProfileActivity.this, MenuActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Please Enter Details", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void fillMyProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONProfile(response);
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
                params.put(JSONField.USER_NAME, user_name);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfileActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONProfile(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.USER_ARRAY);

                if (jsonArray.length() == 1) {

                    Log.d("TEST","hi");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject objUser = jsonArray.getJSONObject(i);
                        String userName = objUser.optString(JSONField.USER_NAME);
                        String userEmail = objUser.optString(JSONField.USER_EMAIL);
                        String userMobile = objUser.optString(JSONField.USER_MOBILE);
                        String userAreaPincode = objUser.optString(JSONField.AREA_PINCODE);
                        String userAreaName = objUser.optString(JSONField.AREA_NAME);
                        String userCity = objUser.optString(JSONField.CITY_NAME);
                        String userAddress = objUser.optString(JSONField.USER_ADDRESS);

                        etName.setText(userName);
                        etEmail.setText(userEmail);
                        etMobile.setText(userMobile);
                        etCity.setText(userCity);
                        etArea.setText(userAreaName + " - " + userAreaPincode);
                        etAddress.setText(userAddress);

                    }
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}