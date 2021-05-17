package com.vpn.realestate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vpn.realestate.Adapters.PropertyPhotoAdapter;
import com.vpn.realestate.ApiManager.JSONField;
import com.vpn.realestate.ApiManager.WebURL;
import com.vpn.realestate.Models.PropertyPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactedPropertyDetailsActivity extends AppCompatActivity {
    RecyclerView rvPropertyPhoto;
    TextView tvPrice, tvPropertyName, tvPropertyType, tvBHK, tvSQFTCarpet, tvSQFTCovered, tvCity, tvArea, tvAddress, tvPropertyDesc;

    String propertyId;

    ArrayList<PropertyPhoto> photoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_contacted_property_details);

        rvPropertyPhoto = findViewById(R.id.rvPropertyPhoto);

        tvPrice = findViewById(R.id.tvPrice);
        tvPropertyName = findViewById(R.id.tvPropertyName);
        tvPropertyType = findViewById(R.id.tvPropertyType);
        tvBHK = findViewById(R.id.tvBHK);
        tvSQFTCarpet = findViewById(R.id.tvSQFTCarpet);
        tvSQFTCovered = findViewById(R.id.tvSQFTCovered);
        tvCity = findViewById(R.id.tvCity);
        tvArea = findViewById(R.id.tvArea);
        tvAddress = findViewById(R.id.tvAddress);
        tvPropertyDesc = findViewById(R.id.tvPropertyDesc);

        Intent intent = getIntent();
        propertyId = intent.getStringExtra("ID");

        LinearLayoutManager manager = new LinearLayoutManager(ContactedPropertyDetailsActivity.this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rvPropertyPhoto.setLayoutManager(manager);

        fillPropertyPhotos();
        filPropertyData();

    }

    private void fillPropertyPhotos() {

        //fill images into recycler view
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.DISPLAY_PROPERTY_IMAGES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONImages(response);
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

                HashMap<String, String> params = new HashMap<>();

                params.put(JSONField.PROPERTY_ID, propertyId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ContactedPropertyDetailsActivity.this);
        requestQueue.add(stringRequest);

    }

    private void filPropertyData() {

        //fill property data
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, WebURL.PROPERTY_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONPropertyData(response);
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

                HashMap<String, String> params = new HashMap<>();

                params.put(JSONField.PROPERTY_ID, propertyId);

                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(ContactedPropertyDetailsActivity.this);
        requestQueue1.add(stringRequest1);

    }

    private void parseJSONPropertyData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.PROPERTY_ARRAY);
                JSONObject objPropertyData = jsonArray.getJSONObject(0);

                String propertyId = objPropertyData.optString(JSONField.PROPERTY_ID);
                String propertyName = objPropertyData.optString(JSONField.PROPERTY_NAME);
                String propertyDescription = objPropertyData.optString(JSONField.PROPERTY_DESCRIPTION);
                String propertyPrice = objPropertyData.optString(JSONField.PROPERTY_PRICE);
                String propertyBHK = objPropertyData.optString(JSONField.PROPERTY_BHK);
                String propertySqftCarpet = objPropertyData.optString(JSONField.PROPERTY_SQFT_CARPET);
                String propertySqftCovered = objPropertyData.optString(JSONField.PROPERTY_SQFT_COVERED);
                String propertyAddress = objPropertyData.optString(JSONField.PROPERTY_ADDRESS);
                String propertyTypeName = objPropertyData.optString(JSONField.PROPERTY_TYPE_NAME);
                String propertyCityName = objPropertyData.optString(JSONField.PROPERTY_CITY_NAME);
                String propertyAreaPincode = objPropertyData.optString(JSONField.PROPERTY_AREA_PINCODE);
                String propertyAreaName = objPropertyData.optString(JSONField.PROPERTY_AREA_NAME);

                tvPrice.setText("₹ " + propertyPrice + " Lac");
                tvPropertyName.setText(propertyName);
                tvPropertyType.setText(propertyTypeName);
                tvBHK.setText(propertyBHK);
                tvSQFTCarpet.setText(propertySqftCarpet);
                tvSQFTCovered.setText(propertySqftCovered);
                tvCity.setText(propertyCityName);
                tvArea.setText(propertyAreaName + " - " + propertyAreaPincode);
                tvAddress.setText(propertyAddress);
                tvPropertyDesc.setText(propertyDescription);

            } else {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseJSONImages(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            Log.d("FLAG", String.valueOf(success));
            if (success == 1) {

                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.PHOTO_ARRAY);

                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objPhoto = jsonArray.getJSONObject(i);
                        String id = objPhoto.optString(JSONField.PROPERTY_ID);
                        String photo = objPhoto.optString(JSONField.PROPERTY_PHOTO);

                        PropertyPhoto propertyPhoto = new PropertyPhoto();
                        propertyPhoto.setProperty_id(id);
                        propertyPhoto.setProperty_photo(photo);
                        photoList.add(propertyPhoto);
                    }
                    PropertyPhotoAdapter adapter = new PropertyPhotoAdapter(ContactedPropertyDetailsActivity.this, photoList);
                    rvPropertyPhoto.setAdapter(adapter);

                }

            } else {
                String msg = jsonObject.optString(JSONField.MSG);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}