package com.vpn.realestate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.vpn.realestate.Models.Property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditPropertyActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spPropertyType, spCity, spBHK, spArea;
    EditText etAddress, etSQFTCarpet, etSQFTCovered, etPrice, etPropertyName, etPropertyDescription;
    //AutoCompleteTextView atvAreaName;
    Button btnSelectImages, btnSubmitProperty;
    //RecyclerView rvImages;
    ImageView ivImg1, ivImg2, ivImg3, ivImg4, ivImg5, ivImg6, ivImg7, ivImg8, ivImg9;

    String propertyTypeName, cityName, BHKSelected;

    Bitmap bitmap;
    String encodedImageString;
    String user_area_pincode, user_area_name, bhk;

    String areaSelected[];
    String BHK[] = {"1", "2", "3", "4", "5", "6", "7"};

    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    String property_id;

    //load data dynamically in spinners from db to following lists
    ArrayList<String> propertyTypeList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> areaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_property);

        spPropertyType = findViewById(R.id.spPropertyType);
        spCity = findViewById(R.id.spCity);
        spBHK = findViewById(R.id.spBHK);
        spArea = findViewById(R.id.spArea);

        etAddress = findViewById(R.id.etAddress);
        etSQFTCarpet = findViewById(R.id.etSQFTCarpet);
        etSQFTCovered = findViewById(R.id.etSQFTCovered);
        etPrice = findViewById(R.id.etPrice);
        etPropertyName = findViewById(R.id.etPropertyName);
        etPropertyDescription = findViewById(R.id.etPropertyDescription);

        //atvAreaName = findViewById(R.id.atvAreaName);

        ivImg1 = findViewById(R.id.ivImg1);
        ivImg2 = findViewById(R.id.ivImg2);
        ivImg3 = findViewById(R.id.ivImg3);
        ivImg4 = findViewById(R.id.ivImg4);
        ivImg5 = findViewById(R.id.ivImg5);
        ivImg6 = findViewById(R.id.ivImg6);
        ivImg7 = findViewById(R.id.ivImg7);
        ivImg8 = findViewById(R.id.ivImg8);
        ivImg9 = findViewById(R.id.ivImg9);

        btnSelectImages = findViewById(R.id.btnSelectImages);
        btnSubmitProperty = findViewById(R.id.btnSubmitProperty);

        btnSelectImages.setOnClickListener(this);
        btnSubmitProperty.setOnClickListener(this);

        Intent intent = getIntent();
        property_id = intent.getStringExtra("ID");

        getPropertyDetails();

        spPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                propertyTypeName = propertyTypeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName = cityList.get(position);
                fillArea();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBHK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BHKSelected = BHK[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaSelected = areaList.get(position).trim().split("-",2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fillArea() {

        //fill area spinner
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CITY_AREA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONArea(response);
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
                params.put(JSONField.CITY_NAME, cityName);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditPropertyActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONArea(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                JSONArray jsonArray = jsonObject.getJSONArray(JSONField.AREA_ARRAY);

                if (jsonArray.length() > 0) {

                    areaList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objArea = jsonArray.getJSONObject(i);

                        String areaPincode = objArea.optString(JSONField.AREA_PINCODE);
                        String areaName = objArea.optString(JSONField.AREA_NAME);
                        String cityId = objArea.optString(JSONField.CITY_ID);

                        areaList.add(areaName + "-" + areaPincode);

                        //set area values
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditPropertyActivity.this, android.R.layout.simple_list_item_1, areaList);
                        spArea.setAdapter(adapter);

                        //set area value
                        String area1 = user_area_name + "-" + user_area_pincode;
                        for (int k = 0; k < spArea.getCount(); k++) {
                            if (spArea.getItemAtPosition(k).toString().equals(area1)) {
                                spArea.setSelection(k);
                                break;
                            }
                        }

                    }

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getPropertyDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PROPERTY_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONPropertyDetails(response);
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
                params.put(JSONField.PROPERTY_ID, property_id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditPropertyActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONPropertyDetails(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.PROPERTY_ARRAY);

                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject propObj = jsonArray.optJSONObject(i);

                        String property_id = propObj.optString(JSONField.PROPERTY_ID);
                        String property_name = propObj.optString(JSONField.PROPERTY_NAME);
                        String property_description = propObj.optString(JSONField.PROPERTY_DESCRIPTION);
                        String property_price = propObj.optString(JSONField.PROPERTY_PRICE);
                        bhk = propObj.optString(JSONField.PROPERTY_BHK);
                        String sqft_carpet = propObj.optString(JSONField.PROPERTY_SQFT_CARPET);
                        String sqft_covered = propObj.optString(JSONField.PROPERTY_SQFT_COVERED);
                        String property_address = propObj.optString(JSONField.PROPERTY_ADDRESS);
                        String property_type_name = propObj.optString(JSONField.PROPERTY_TYPE_NAME);
                        String user_city_name = propObj.optString(JSONField.PROPERTY_CITY_NAME);
                        user_area_pincode = propObj.optString(JSONField.PROPERTY_AREA_PINCODE);
                        user_area_name = propObj.optString(JSONField.PROPERTY_AREA_NAME);
                        String user_id = propObj.optString(JSONField.USER_ID);

                        etAddress.setText(property_address);
                        etSQFTCarpet.setText(sqft_carpet);
                        etSQFTCovered.setText(sqft_covered);
                        etPrice.setText(property_price);
                        etPropertyName.setText(property_name);
                        etPropertyDescription.setText(property_description);

                        //fill property type spinner
                        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, WebURL.PROPERTY_TYPE_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.optInt(JSONField.SUCCESS);
                                    if (success == 1) {

                                        JSONArray jsonArray = jsonObject.optJSONArray(JSONField.PROPERTY_TYPE_ARRAY);

                                        if (jsonArray.length() > 0) {

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject objPropertyType = jsonArray.getJSONObject(i);

                                                String propertyTypeID = objPropertyType.optString(JSONField.PROPERTY_TYPE_ID);
                                                String propertyType = objPropertyType.optString(JSONField.PROPERTY_TYPE_NAME);
                                                propertyTypeList.add(propertyType);

                                                //set property type in spinner
                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditPropertyActivity.this, android.R.layout.simple_list_item_1, propertyTypeList);
                                                spPropertyType.setAdapter(adapter);

                                                //set property value
                                                for (int j = 0; j < spPropertyType.getCount(); j++) {
                                                    if (spPropertyType.getItemAtPosition(j).toString().equals(property_type_name)) {
                                                        spPropertyType.setSelection(j);
                                                        break;
                                                    }
                                                }

                                            }
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditPropertyActivity.this);
                        requestQueue1.add(stringRequest1);

                        //fill city spinner
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.CITY_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.optInt(JSONField.SUCCESS);
                                    if (success == 1) {

                                        JSONArray jsonArray = jsonObject.optJSONArray(JSONField.CITY_ARRAY);

                                        if (jsonArray.length() > 0) {

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject objCity = jsonArray.getJSONObject(i);

                                                String city = objCity.optString(JSONField.CITY_NAME);
                                                cityList.add(city);

                                                //set city names in spinner
                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditPropertyActivity.this, android.R.layout.simple_list_item_1, cityList);
                                                spCity.setAdapter(adapter);

                                                //set city value
                                                for (int k = 0; k < spCity.getCount(); k++) {
                                                    if (spCity.getItemAtPosition(k).toString().equals(user_city_name)) {
                                                        spCity.setSelection(k);
                                                        break;
                                                    }
                                                }

                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                        RequestQueue requestQueue = Volley.newRequestQueue(EditPropertyActivity.this);
                        requestQueue.add(stringRequest);

                        //set bhk value
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditPropertyActivity.this, R.layout.support_simple_spinner_dropdown_item, BHK);
                        spBHK.setAdapter(adapter);

                        for (int l = 0; l < spBHK.getCount(); l++) {
                            if (spBHK.getItemAtPosition(l).toString().equals(bhk)) {
                                spBHK.setSelection(l);
                                break;
                            }
                        }
                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSelectImages:
                break;

            case R.id.btnSubmitProperty:
                if (!etAddress.getText().toString().equals("")
                        && !etSQFTCarpet.getText().toString().equals("")
                        && !etSQFTCovered.getText().toString().equals("")
                        && !etPrice.getText().toString().equals("")
                        && !etPropertyName.getText().toString().equals("")
                        && !etPropertyDescription.getText().toString().equals("")) {

                    updatePropertyDetails();

                } else {
                    Toast.makeText(this, "Enter Details", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void updatePropertyDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PROPERTY_UPDATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONUpdatePropertyDetails(response);
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
                params.put(JSONField.PROPERTY_ID, property_id);
                params.put(JSONField.PROPERTY_NAME, etPropertyName.getText().toString());
                params.put(JSONField.PROPERTY_DESCRIPTION, etPropertyDescription.getText().toString());
                params.put(JSONField.PROPERTY_PRICE, etPrice.getText().toString());
                params.put(JSONField.PROPERTY_BHK, BHKSelected);
                params.put(JSONField.PROPERTY_SQFT_CARPET, etSQFTCarpet.getText().toString());
                params.put(JSONField.PROPERTY_SQFT_COVERED, etSQFTCovered.getText().toString());
                params.put(JSONField.PROPERTY_TYPE_NAME, propertyTypeName);
                params.put(JSONField.PROPERTY_ADDRESS, etAddress.getText().toString());
                params.put(JSONField.CITY_NAME, cityName);
                params.put(JSONField.AREA_PINCODE, areaSelected[1]);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditPropertyActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONUpdatePropertyDetails(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                String propertyID1 = jsonObject.optString(JSONField.PROPERTY_ID);
                String msg = jsonObject.optString(JSONField.MSG);

                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

                onBackPressed();
//                Intent intent = new Intent(EditPropertyActivity.this, DrawerActivity.class);
//                startActivity(intent);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}