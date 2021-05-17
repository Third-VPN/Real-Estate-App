package com.vpn.realestate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.vpn.realestate.ApiManager.JSONField;
import com.vpn.realestate.ApiManager.WebURL;
import com.vpn.realestate.Models.Property;
import com.vpn.realestate.PropertyContactActivity;
import com.vpn.realestate.PropertyDetailsActivity;
import com.vpn.realestate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {
    Context context;
    ArrayList<Property> propertyList;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    public PropertyAdapter(Context context, ArrayList<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.property_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //getting user id for bookmark
        SharedPreferences preferences = context.getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
        String user_id = preferences.getString(ID_KEY, "");


        Property property = propertyList.get(position);

        //set the text
        holder.tvPrice.setText("₹ " + property.getProperty_price() + " Lac");
        holder.tvPropertyType.setText(property.getProperty_type_name());
        holder.tvBHK.setText(property.getProperty_bhk() + " BHK");
        holder.tvArea.setText(property.getProperty_area_name());
        holder.tvCity.setText(", " + property.getProperty_city());
        holder.tbBookmark.setChecked(property.isBookmark());

        String property_id = property.getProperty_id();

        //set the image
        Glide.with(context).load(WebURL.PROPERTY_IMAGES_URL + property.getProperty_photo()).into(holder.ivImage);

        holder.cvProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PropertyDetailsActivity.class);
                intent.putExtra("ID", property_id);
                context.startActivity(intent);

            }
        });

        //bookmark event
        holder.tbBookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    addBookmark();
                } else {
                    removeBookmark();
                }

            }

            private void removeBookmark() {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.BOOKMARK_REMOVE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int success = jsonObject.optInt(JSONField.SUCCESS);

                            if (success == 1) {

                                JSONField.propertyIdList.remove(property_id);
                                for (int i = 0; i < JSONField.propertyIdList.size(); i++) {
                                    Log.d("PROPERTY ID LIST", JSONField.propertyIdList.get(i));
                                }

                                String bookmark_id = jsonObject.optString(JSONField.BOOKMARK_ID);
                                String msg = jsonObject.optString(JSONField.MSG);

                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String, String> params = new HashMap<>();
                        params.put(JSONField.USER_ID, user_id);
                        params.put(JSONField.PROPERTY_ID, property_id);

                        return params;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }

            private void addBookmark() {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.BOOKMARK_ADD_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int success = jsonObject.optInt(JSONField.SUCCESS);

                            if (success == 1) {

                                JSONField.propertyIdList.add(property_id);
                                for (int i = 0; i < JSONField.propertyIdList.size(); i++) {
                                    Log.d("PROPERTY ID LIST", JSONField.propertyIdList.get(i));
                                }

                                String bookmark_id = jsonObject.optString(JSONField.BOOKMARK_ID);
                                String msg = jsonObject.optString(JSONField.MSG);

                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String, String> params = new HashMap<>();
                        params.put(JSONField.USER_ID, user_id);
                        params.put(JSONField.PROPERTY_ID, property_id);

                        return params;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }

        });

        //property contact button click event
        holder.btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PropertyContactActivity.class);
                intent.putExtra("ID", property_id);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvProperty;
        ImageView ivImage;
        ToggleButton tbBookmark;
        TextView tvPrice, tvPropertyType, tvBHK, tvCity, tvArea;
        Button btnContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvProperty = itemView.findViewById(R.id.cvProperty);

            ivImage = itemView.findViewById(R.id.ivImage);

            tbBookmark = itemView.findViewById(R.id.tbBookmark);

            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPropertyType = itemView.findViewById(R.id.tvPropertyType);
            tvBHK = itemView.findViewById(R.id.tvBHK);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvArea = itemView.findViewById(R.id.tvArea);

            btnContact = itemView.findViewById(R.id.btnContact);

        }
    }

}
