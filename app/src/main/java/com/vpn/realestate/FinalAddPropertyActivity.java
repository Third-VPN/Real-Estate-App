package com.vpn.realestate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FinalAddPropertyActivity extends AppCompatActivity {
    Spinner spPropertyType, spCity, spBHK, spArea;
    EditText etAddress, etSQFTCarpet, etSQFTCovered, etPrice, etPropertyName, etPropertyDescription;
    Button btnSelectImages, btnSubmitProperty;
    GridLayout gvSetImage;

    String propertyTypeName, cityName, BHKSelected, user_id;

    String areaSelected[];
    String BHK[] = {"1", "2", "3", "4", "5", "6", "7"};

    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    String propertyId;

    AlertDialog dialog;

    //load data dynamically in spinners from db to following lists
    ArrayList<String> propertyTypeList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> areaList = new ArrayList<>();

    ArrayList<Uri> uriList;
    ArrayList<String> encodedImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_add_property);

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

        gvSetImage = findViewById(R.id.gvSetImage);

        btnSelectImages = findViewById(R.id.btnSelectImages);
        btnSubmitProperty = findViewById(R.id.btnSubmitProperty);

        SharedPreferences preferences = getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
        user_id = preferences.getString(ID_KEY, "");


        btnSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnSubmitProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etAddress.getText().toString().equals("")
                        && !etSQFTCarpet.getText().toString().equals("")
                        && !etSQFTCovered.getText().toString().equals("")
                        && !etPrice.getText().toString().equals("")
                        && !etPropertyName.getText().toString().equals("")
                        && !etPropertyDescription.getText().toString().equals("")
                        && uriList != null) {

                    //send property details
                    AlertDialog.Builder builder = new AlertDialog.Builder(FinalAddPropertyActivity.this);
                    LayoutInflater inflater = FinalAddPropertyActivity.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.loading_row, null));
                    builder.setCancelable(false);

                    dialog = builder.create();
                    dialog.show();

                    sendPostPropertyRequest();


                } else {
                    Toast.makeText(FinalAddPropertyActivity.this, "Enter Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set bhk values
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, BHK);
        spBHK.setAdapter(adapter);

        //filling property type and city spinner
        fillAllSpinners();

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
                areaSelected = areaList.get(position).split("-", 2);
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
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put(JSONField.CITY_NAME, cityName);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FinalAddPropertyActivity.this);
        requestQueue.add(stringRequest);

    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), REQUEST_CODE_SELECT_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {

            if (data != null) {
                uriList = new ArrayList<>();

                if (data.getData() != null) {

                    Uri uri = data.getData();
                    uriList.add(uri);

                } else if (data.getClipData() != null) {

                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        uriList.add(uri);
                    }

                }
                for (int j = 0; j < uriList.size(); j++) {

                    Log.d("ENCODE LIST", String.valueOf(uriList.get(j)));

                }
                setImages(uriList);

            }

        }

    }

    private void setImages(ArrayList<Uri> uriList) {

        for (int i = 0; i < uriList.size(); i++) {

            FrameLayout view1 = (FrameLayout) LayoutInflater.from(FinalAddPropertyActivity.this).inflate(R.layout.image_row, gvSetImage, false);

            ImageView imageView = view1.findViewById(R.id.ivImageRow);
            ImageView remove = view1.findViewById(R.id.ivRemove);
            //set image
            imageView.setImageURI(uriList.get(i));
            int position = i;
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    gvSetImage.removeView(view1);
                    uriList.remove(position);

                }
            });
            gvSetImage.addView(view1);
            gvSetImage.setPadding(0,0,0,0);

        }

    }

    private void encodeBitmapImage(ArrayList<Uri> uriList) {

        encodedImageList = new ArrayList<>();
        for (int i = 0; i < uriList.size(); i++) {

            try {
                InputStream inputStream = getContentResolver().openInputStream(uriList.get(i));
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                byte[] bytesOfImage = byteArrayOutputStream.toByteArray();
                encodedImageList.add(android.util.Base64.encodeToString(bytesOfImage, Base64.DEFAULT));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void sendPostPropertyRequest() {

        //post the property data
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.POST_PROPERTY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONPostProperty(response);
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
                params.put(JSONField.PROPERTY_NAME, etPropertyName.getText().toString());
                params.put(JSONField.PROPERTY_DESCRIPTION, etPropertyDescription.getText().toString());
                params.put(JSONField.PROPERTY_PRICE, etPrice.getText().toString());
                params.put(JSONField.PROPERTY_BHK, BHKSelected);
                params.put(JSONField.PROPERTY_ADDRESS, etAddress.getText().toString());
                params.put(JSONField.PROPERTY_SQFT_CARPET, etSQFTCarpet.getText().toString());
                params.put(JSONField.PROPERTY_SQFT_COVERED, etSQFTCovered.getText().toString());
                params.put(JSONField.PROPERTY_TYPE_NAME, propertyTypeName);
                params.put(JSONField.CITY_NAME, cityName);
                params.put(JSONField.AREA_PINCODE, areaSelected[1]);
                params.put(JSONField.USER_ID, user_id);

                Log.d("AREA NAME", areaSelected[0]);
                Log.d("AREA PINCODE", areaSelected[1]);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FinalAddPropertyActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONPostProperty(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            if (success == 1) {

                String message = jsonObject.optString(JSONField.MSG);
                propertyId = jsonObject.optString(JSONField.PROPERTY_ID);

                Log.d("MSG P. D. SEND", message);
                encodeBitmapImage(uriList);
                for (int i = 0; i < encodedImageList.size(); i++) {

                    Log.d("ENCODE LIST", encodedImageList.get(i));

                }
                for (int i = 0; i < encodedImageList.size(); i++) {

                    sendPostPropertyPhotoRequest(i);

                }
                dialog.dismiss();
                Toast.makeText(this, "Property Published Successfully", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, DrawerActivity.class);
                startActivity(intent);

            } else {

                String msg = jsonObject.optString(JSONField.MSG);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendPostPropertyPhotoRequest(int encodeNo) {

        StringRequest request = new StringRequest(Request.Method.POST, WebURL.PROPERTY_PHOTO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONPhotoRequest(response);
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
                params.put(JSONField.PROPERTY_PHOTO, encodedImageList.get(encodeNo));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FinalAddPropertyActivity.this);
        requestQueue.add(request);

    }

    private void parseJSONPhotoRequest(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            if (success == 1) {

                String msg = jsonObject.optString(JSONField.MSG);
                Log.d("SUCCESS PHOTO", msg);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAllSpinners() {

        //fill property type spinner
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, WebURL.PROPERTY_TYPE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONPropertyType(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(FinalAddPropertyActivity.this);
        requestQueue1.add(stringRequest1);


        //fill city spinner
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.CITY_URL, new Response.Listener<String>() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(FinalAddPropertyActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJSONPropertyType(String response) {

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, propertyTypeList);
                        spPropertyType.setAdapter(adapter);

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseJSONCity(String response) {

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityList);
                        spCity.setAdapter(adapter);

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, areaList);
                        spArea.setAdapter(adapter);

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}