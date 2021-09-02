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

public class PropertyContactActivity extends AppCompatActivity {
    EditText etPrice, etPropertyDetails;
    Button btnSubmit;

    String property_id, use_id;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_property_contact);

        etPrice = findViewById(R.id.etPrice);
        etPropertyDetails = findViewById(R.id.etPropertyDetails);

        btnSubmit = findViewById(R.id.btnSubmit);

        SharedPreferences preferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        use_id = preferences.getString(ID_KEY, "");

        Intent intent = getIntent();
        property_id = intent.getStringExtra("ID");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etPrice.getText().toString().trim().equals("") &&
                        !etPropertyDetails.getText().toString().trim().equals("")) {

                    if (!preferences.getString(ID_KEY, "").equals("")) {
                        sendContactDetails();
                    } else {
                        Toast.makeText(PropertyContactActivity.this, "You have first Log In", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(PropertyContactActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(PropertyContactActivity.this, "Enter Valid Details", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendContactDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PROPERTY_CONTACT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONPropertyContact(response);
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

                params.put(JSONField.PROPERTY_CONTACT_PRICE, etPrice.getText().toString());
                params.put(JSONField.PROPERTY_CONTACT_DETAILS, etPropertyDetails.getText().toString());
                params.put(JSONField.USER_ID, use_id);
                params.put(JSONField.PROPERTY_ID, property_id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PropertyContactActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONPropertyContact(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                String msg = jsonObject.optString(JSONField.MSG);
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                onBackPressed();
//                //sending to main property page
//                Intent intent = new Intent(PropertyContactActivity.this, PropertyDetailsActivity.class);
//                startActivity(intent);

            } else {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}