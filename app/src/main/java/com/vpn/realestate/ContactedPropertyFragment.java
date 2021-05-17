package com.vpn.realestate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ContactedPropertyFragment extends Fragment {
    RecyclerView rvContProperty;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    String user_id;

    ArrayList<PropertyContact> contactList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacted_property, container, false);

        rvContProperty = view.findViewById(R.id.rvContProperty);

        //set recycler view layout
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvContProperty.setLayoutManager(manager);

        SharedPreferences preferences = getContext().getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
        user_id = preferences.getString(ID_KEY, "");

        if (!user_id.equals("")) {

            //set property details
            getMyContactedPropertyDetails();

        }

        return view;
    }

    private void getMyContactedPropertyDetails() {

        contactList = new ArrayList<PropertyContact>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PROPERTY_CONTACT_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONMyContactedProperties(response);
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

    private void parseJSONMyContactedProperties(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            int success = jsonObject.optInt(JSONField.SUCCESS);
            String msg = jsonObject.optString(JSONField.MSG);
            Log.d("MSG", msg);

            if (success == 1) {

                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.MY_PROPERTY_CONTACT_ARRAY);

                if (jsonArray.length() > 0) {

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

                        contactList.add(propertyContact);

                    }
                    PropertyContactAdapter adapter = new PropertyContactAdapter(getContext(), contactList);
                    rvContProperty.setAdapter(adapter);

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
