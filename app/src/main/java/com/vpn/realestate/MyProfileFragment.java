package com.vpn.realestate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyProfileFragment extends Fragment {
    EditText etName, etEmail, etMobile, etAddress;
    Button btnUpdate;
    Spinner spCity, spArea;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";
    public static final String NAME_KEY = "user_name";

    String user_id, user_name, city, cityArea;
    String[] areaSelected;

    String userAreaPincode, userAreaName, userCity;

    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> areaList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etMobile = view.findViewById(R.id.etMobile);
        etAddress = view.findViewById(R.id.etAddress);

        btnUpdate = view.findViewById(R.id.btnUpdate);

        spCity = view.findViewById(R.id.spCity);
        spArea = view.findViewById(R.id.spArea);

        SharedPreferences preferences = getContext().getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
        user_id = preferences.getString(ID_KEY, "");
        user_name = preferences.getString(NAME_KEY, "");

        Log.d("ID", preferences.getString(ID_KEY, ""));

        if (!user_id.equals("")) {
            fillMyProfile();
        } else {
            Toast.makeText(getContext(), "Log In First", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getContext(), SignInActivity.class);
            startActivity(intent);

        }

        fillAllSpinners();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!user_id.equals("")) {
                    sendUpdateProfileRequest();
                } else {
                    Toast.makeText(getContext(), "Log In First", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getContext(), SignInActivity.class);
                    startActivity(intent);

                }

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = cityList.get(position);
                filArea();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityArea = areaList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void filArea() {

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
                params.put(JSONField.CITY_NAME, city);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void fillAllSpinners() {

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

                        areaList.add(areaName + "-" + areaPincode);

                        //set area values
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, areaList);
                        spArea.setAdapter(adapter);

                        String area1 = userAreaName + "-" + userAreaPincode;
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

                        for (int k = 0; k < spCity.getCount(); k++) {
                            if (spCity.getItemAtPosition(k).toString().equals(userCity)) {
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
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                areaSelected = cityArea.split("-", 2);
                Log.d("AREA NAME", areaSelected[0]);
                Log.d("AREA PINCODE", areaSelected[1]);

                HashMap<String, String> params = new HashMap<>();

                params.put(JSONField.USER_ID, user_id);
                params.put(JSONField.USER_NAME, etName.getText().toString());
                params.put(JSONField.USER_EMAIL, etEmail.getText().toString());
                params.put(JSONField.USER_MOBILE, etMobile.getText().toString());
                params.put(JSONField.CITY_NAME, city);
                params.put(JSONField.AREA_NAME, areaSelected[0]);
                params.put(JSONField.AREA_PINCODE, areaSelected[1]);
                params.put(JSONField.USER_ADDRESS, etAddress.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void parseJSONUpdateProfile(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                String id = jsonObject.optString(JSONField.USER_ID);
                String msg = jsonObject.optString(JSONField.MSG);

                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), DrawerActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(getContext(), "Please Enter Details", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void parseJSONProfile(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.USER_ARRAY);

                if (jsonArray.length() == 1) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject objUser = jsonArray.getJSONObject(i);
                        String userName = objUser.optString(JSONField.USER_NAME);
                        String userEmail = objUser.optString(JSONField.USER_EMAIL);
                        String userMobile = objUser.optString(JSONField.USER_MOBILE);
                        userAreaPincode = objUser.optString(JSONField.AREA_PINCODE);
                        userAreaName = objUser.optString(JSONField.AREA_NAME);
                        userCity = objUser.optString(JSONField.CITY_NAME);
                        String userAddress = objUser.optString(JSONField.USER_ADDRESS);

                        etName.setText(userName);
                        etEmail.setText(userEmail);
                        etMobile.setText(userMobile);
                        etAddress.setText(userAddress);

                    }
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
