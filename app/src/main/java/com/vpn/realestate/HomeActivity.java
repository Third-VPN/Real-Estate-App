package com.vpn.realestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vpn.realestate.Adapters.PropertyAdapter;
import com.vpn.realestate.ApiManager.JSONField;
import com.vpn.realestate.ApiManager.WebURL;
import com.vpn.realestate.Models.Property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    RecyclerView rvProperty;
    ArrayList<Property> propertyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        rvProperty = findViewById(R.id.rvProperty);

        //set recycler view layout
        LinearLayoutManager manager = new LinearLayoutManager(HomeActivity.this);
        rvProperty.setLayoutManager(manager);

        //set property details
        getPropertyDetails();

    }

    private void getPropertyDetails() {

        propertyList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.PROPERTY_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                parseJSONPropertyDetails(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONPropertyDetails(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            if (success == 1) {
                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.PROPERTY_ARRAY);
                if (jsonArray.length() > 0) {

                    for (int i=0; i< jsonArray.length(); i++) {

                        JSONObject propObj = jsonArray.getJSONObject(i);

                        String propertyId = propObj.getString(JSONField.PROPERTY_ID);
                        String propertyName = propObj.getString(JSONField.PROPERTY_NAME);
                        String propertyDescription = propObj.getString(JSONField.PROPERTY_DESCRIPTION);
                        String propertyPrice = propObj.getString(JSONField.PROPERTY_PRICE);
                        String propertyBHK = propObj.getString(JSONField.PROPERTY_BHK);
                        String propertySqftCarpet = propObj.getString(JSONField.PROPERTY_SQFT_CARPET);
                        String propertySqftCovered = propObj.getString(JSONField.PROPERTY_SQFT_COVERED);
                        String propertyAddress = propObj.getString(JSONField.PROPERTY_ADDRESS);
                        String propertyTypeName = propObj.getString(JSONField.PROPERTY_TYPE_NAME);
                        String propertyCityName = propObj.getString(JSONField.PROPERTY_CITY_NAME);
                        String propertyAreaPincode = propObj.getString(JSONField.PROPERTY_AREA_PINCODE);
                        String propertyAreaName = propObj.getString(JSONField.PROPERTY_AREA_NAME);
                        String propertyPhoto = propObj.getString(JSONField.PROPERTY_PHOTO);

                        Property property = new Property();
                        property.setProperty_id(propertyId);
                        property.setProperty_name(propertyName);
                        property.setProperty_description(propertyDescription);
                        property.setProperty_bhk(propertyBHK);
                        property.setProperty_price(propertyPrice);
                        property.setProperty_sqft_carpet(propertySqftCarpet);
                        property.setProperty_sqft_covered(propertySqftCovered);
                        property.setProperty_address(propertyAddress);
                        property.setProperty_type_name(propertyTypeName);
                        property.setProperty_city(propertyCityName);
                        property.setProperty_area_pincode(propertyAreaPincode);
                        property.setProperty_area_name(propertyAreaName);
                        property.setProperty_photo(propertyPhoto);
                        propertyList.add(property);

                    }
                    PropertyAdapter adapter = new PropertyAdapter(HomeActivity.this, propertyList);
                    rvProperty.setAdapter(adapter);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}