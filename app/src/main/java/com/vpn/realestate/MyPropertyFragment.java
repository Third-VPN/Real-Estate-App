package com.vpn.realestate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vpn.realestate.Adapters.MyPropertyAdapter;
import com.vpn.realestate.Adapters.PropertyAdapter;
import com.vpn.realestate.ApiManager.JSONField;
import com.vpn.realestate.ApiManager.WebURL;
import com.vpn.realestate.Models.Property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPropertyFragment extends Fragment {
    RecyclerView rvMyProperty;
    TextView tvEmpty;
    ArrayList<Property> propertyList;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    String user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_property, container, false);

        tvEmpty = view.findViewById(R.id.tvEmpty);

        rvMyProperty = view.findViewById(R.id.rvMyProperty);

        //set recycler view layout
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvMyProperty.setLayoutManager(manager);

        SharedPreferences preferences = getContext().getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
        user_id = preferences.getString(ID_KEY, "");

        if (!user_id.equals("")) {

            //set property details
            getMyPropertyDetails();

        }

        return view;
    }

    private void getMyPropertyDetails() {

        propertyList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.MY_PROPERTY_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                parseJSONMyPropertyDetails(response);

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void parseJSONMyPropertyDetails(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {
                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.MY_PROPERTY_ARRAY);
                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        rvMyProperty.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);

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
                    MyPropertyAdapter adapter = new MyPropertyAdapter(getContext(), propertyList);
                    rvMyProperty.setAdapter(adapter);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
