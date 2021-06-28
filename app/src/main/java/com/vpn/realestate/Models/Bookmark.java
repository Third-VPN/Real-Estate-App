package com.vpn.realestate.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

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

public class Bookmark {
    Context context;
    String user_id;
    //bookmark property list
    public static ArrayList<String> propertyIdList = new ArrayList<>();

    public Bookmark(Context context, String user_id) {
        this.context = context;
        this.user_id = user_id;
    }

    public void getBookmarks() {

        //set boomarked property
        //bookmark list
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, WebURL.BOOKMARK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int success = jsonObject.optInt(JSONField.SUCCESS);

                    if (success == 1) {

                        JSONArray jsonArray = jsonObject.optJSONArray(JSONField.BOOKMARK_ARRAY);
                        if (jsonArray.length() > 0) {

                            propertyIdList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject propObj = jsonArray.getJSONObject(i);

                                String propertyId = propObj.getString(JSONField.PROPERTY_ID);

                                propertyIdList.add(propertyId);
                                Log.d("ID LIST", propertyIdList.get(i));

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
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put(JSONField.USER_ID, user_id);

                return params;

            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(context);
        requestQueue1.add(stringRequest1);

    }


}
