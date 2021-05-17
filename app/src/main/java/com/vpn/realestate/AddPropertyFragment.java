package com.vpn.realestate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddPropertyFragment extends Fragment {
    Spinner spPropertyType, spCity, spBHK, spArea;
    EditText etAddress, etSQFTCarpet, etSQFTCovered, etPrice, etPropertyName, etPropertyDescription;
    //AutoCompleteTextView atvAreaName;
    Button btnSelectImages, btnSubmitProperty;
    //RecyclerView rvImages;
    ImageView ivImg1, ivImg2, ivImg3, ivImg4, ivImg5, ivImg6, ivImg7, ivImg8, ivImg9;

    String propertyTypeName, cityName, BHKSelected, user_id;

    Bitmap bitmap;
    String encodedImageString;

    String areaSelected[];
    String BHK[] = {"1", "2", "3", "4", "5", "6", "7"};

    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    String propertyId;

    //load data dynamically in spinners from db to following lists
    ArrayList<String> propertyTypeList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> areaList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_property, container, false);

        spPropertyType = view.findViewById(R.id.spPropertyType);
        spCity = view.findViewById(R.id.spCity);
        spBHK = view.findViewById(R.id.spBHK);
        spArea = view.findViewById(R.id.spArea);

        etAddress = view.findViewById(R.id.etAddress);
        etSQFTCarpet = view.findViewById(R.id.etSQFTCarpet);
        etSQFTCovered = view.findViewById(R.id.etSQFTCovered);
        etPrice = view.findViewById(R.id.etPrice);
        etPropertyName = view.findViewById(R.id.etPropertyName);
        etPropertyDescription = view.findViewById(R.id.etPropertyDescription);

        //atvAreaName = view.findViewById(R.id.atvAreaName);

        ivImg1 = view.findViewById(R.id.ivImg1);
        ivImg2 = view.findViewById(R.id.ivImg2);
        ivImg3 = view.findViewById(R.id.ivImg3);
        ivImg4 = view.findViewById(R.id.ivImg4);
        ivImg5 = view.findViewById(R.id.ivImg5);
        ivImg6 = view.findViewById(R.id.ivImg6);
        ivImg7 = view.findViewById(R.id.ivImg7);
        ivImg8 = view.findViewById(R.id.ivImg8);
        ivImg9 = view.findViewById(R.id.ivImg9);

        btnSelectImages = view.findViewById(R.id.btnSelectImages);
        btnSubmitProperty = view.findViewById(R.id.btnSubmitProperty);

        SharedPreferences preferences = getContext().getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
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
                        && !etPropertyDescription.getText().toString().equals("")) {

                    sendPostPropertyRequest();

                } else {
                    Toast.makeText(getContext(), "Enter Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set bhk values
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, BHK);
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
                areaSelected = areaList.get(position).split("-",2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {

            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {

            if (data != null) {

                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {

                    try {

                        InputStream inputStream = getContext().getContentResolver().openInputStream(selectedImageUri);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        ivImg1.setImageBitmap(bitmap);
                        encodeBitmapImage(bitmap);

                    } catch (Exception exception) {
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

        }

    }

    private void encodeBitmapImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] bytesOfImage = byteArrayOutputStream.toByteArray();
        encodedImageString = android.util.Base64.encodeToString(bytesOfImage, Base64.DEFAULT);

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void parseJSONPostProperty(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            if (success == 1) {

                String message = jsonObject.optString(JSONField.MSG);
                propertyId = jsonObject.optString(JSONField.PROPERTY_ID);

                sendPostPropertyPhotoRequest();

            } else {

                String msg = jsonObject.optString(JSONField.MSG);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendPostPropertyPhotoRequest() {

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
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                params.put(JSONField.PROPERTY_ID, propertyId);
                params.put(JSONField.PROPERTY_PHOTO, encodedImageString);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }

    private void parseJSONPhotoRequest(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            if (success == 1) {

                String msg = jsonObject.optString(JSONField.MSG);
                Log.d("SUCCESS PHOTO", msg);

                Toast.makeText(getContext(), "Property Posted Successfully", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(), DrawerActivity.class);
                startActivity(intent);

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
        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, propertyTypeList);
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cityList);
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, areaList);
                        spArea.setAdapter(adapter);

                    }

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
