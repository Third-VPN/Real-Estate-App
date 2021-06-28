package com.vpn.realestate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

public class ResponseActivity extends AppCompatActivity {
    TextView tvEmpty, tvName;
    EditText etMobile, etEmail;
    LinearLayout llOwnerDetails;
    ImageView ivCall, ivCopy;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    String user_id, propertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_response);

        tvEmpty = findViewById(R.id.tvEmpty);
        tvName = findViewById(R.id.tvName);

        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);

        ivCall = findViewById(R.id.ivCall);
        ivCopy = findViewById(R.id.ivCopy);

        llOwnerDetails = findViewById(R.id.llOwnerDetails);

        SharedPreferences preferences = getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
        user_id = preferences.getString(ID_KEY, "");

        Intent intent = getIntent();
        propertyId = intent.getStringExtra("ID");

        if (!user_id.equals("")) {

            getOwnerDetails();

        }

    }

    private void getOwnerDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.GET_OWNER_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONOwnerDetails(response);
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
                params.put(JSONField.BUYER_ID, user_id);
                params.put(JSONField.PROPERTY_ID, propertyId);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ResponseActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONOwnerDetails(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            int success = jsonObject.optInt(JSONField.SUCCESS);
            String msg = jsonObject.optString(JSONField.MSG);

            if (success == 1) {

                tvEmpty.setVisibility(View.GONE);
                llOwnerDetails.setVisibility(View.VISIBLE);

                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.USER_DETAILS);

                if (jsonArray.length() > 0) {

                    JSONObject ownerObj = jsonArray.optJSONObject(0);

                    String user_id = ownerObj.optString(JSONField.USER_ID);
                    String user_name = ownerObj.optString(JSONField.USER_NAME);
                    String user_email = ownerObj.optString(JSONField.USER_EMAIL);
                    String user_mobile = ownerObj.optString(JSONField.USER_MOBILE);

                    tvName.setText(user_name);
                    etMobile.setText(user_mobile);
                    //make a phone call
                    ivCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + user_mobile));
                            startActivity(intent);

                        }
                    });

                    etEmail.setText(user_email);
                    //copy email in clipboard
                    ivCopy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //copy to clip board
                            ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Email", etEmail.getText().toString());
                            clipBoard.setPrimaryClip(clip);

                            Toast.makeText(ResponseActivity.this, "Email copied", Toast.LENGTH_SHORT).show();

                        }
                    });

                    Toast.makeText(this, "Congratulations! You have selected", Toast.LENGTH_LONG).show();

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}