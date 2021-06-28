package com.vpn.realestate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vpn.realestate.Adapters.BuyersAdapter;
import com.vpn.realestate.Adapters.PropertyContactAdapter;
import com.vpn.realestate.ApiManager.JSONField;
import com.vpn.realestate.ApiManager.WebURL;
import com.vpn.realestate.Models.PropertyContact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewBuyersActivity extends AppCompatActivity {
    TextView tvEmpty;
    RecyclerView rvBuyers;

    ArrayList<PropertyContact> buyerList = new ArrayList<>();

    String propertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_view_buyers);

        tvEmpty = findViewById(R.id.tvEmpty);

        rvBuyers = findViewById(R.id.rvBuyers);

        Intent intent = getIntent();
        propertyId = intent.getStringExtra("ID");

        LinearLayoutManager manager = new LinearLayoutManager(ViewBuyersActivity.this);
        rvBuyers.setLayoutManager(manager);

        getBuyersList();

    }

    private void getBuyersList() {

        //get buyer list
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.BUYER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONBuyersList(response);
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
                params.put(JSONField.PROPERTY_ID, propertyId);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewBuyersActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONBuyersList(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            int success = jsonObject.optInt(JSONField.SUCCESS);
            String msg = jsonObject.optString(JSONField.MSG);
            Log.d("MSG", msg);

            if (success == 1) {

                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.PROPERTY_CONTACT_ARRAY);

                if (jsonArray.length() > 0) {

                    rvBuyers.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject contObj = jsonArray.optJSONObject(i);

                        String property_contact_id = contObj.optString(JSONField.PROPERTY_CONTACT_ID);
                        String property_contact_details = contObj.optString(JSONField.PROPERTY_CONTACT_DETAILS);
                        String price = contObj.optString(JSONField.PRICE);
                        String user_id = contObj.optString(JSONField.USER_ID);
                        String property_id = contObj.optString(JSONField.PROPERTY_ID);

                        PropertyContact propertyContact = new PropertyContact();
                        propertyContact.setProperty_contact_id(property_contact_id);
                        propertyContact.setProperty_contact_details(property_contact_details);
                        propertyContact.setPrice(price);
                        propertyContact.setUser_id(user_id);
                        propertyContact.setProperty_id(property_id);

                        buyerList.add(propertyContact);

                    }
                    BuyersAdapter adapter = new BuyersAdapter(ViewBuyersActivity.this, buyerList, propertyId);
                    rvBuyers.setAdapter(adapter);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}