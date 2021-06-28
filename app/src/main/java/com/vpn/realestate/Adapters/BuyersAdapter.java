package com.vpn.realestate.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.vpn.realestate.ApiManager.JSONField;
import com.vpn.realestate.ApiManager.WebURL;
import com.vpn.realestate.ContactedPropertyDetailsActivity;
import com.vpn.realestate.DrawerActivity;
import com.vpn.realestate.Models.PropertyContact;
import com.vpn.realestate.MyPropertyDetailsActivity;
import com.vpn.realestate.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyersAdapter extends RecyclerView.Adapter<BuyersAdapter.ViewHolder> {
    Context context;
    ArrayList<PropertyContact> buyersList;
    String propertyId;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    String owner_id;

    public BuyersAdapter(Context context, ArrayList<PropertyContact> buyersList, String propertyId) {
        this.context = context;
        this.buyersList = buyersList;
        this.propertyId = propertyId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.buyers_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences preferences = context.getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
        owner_id = preferences.getString(ID_KEY, "");

        PropertyContact propertyContact = buyersList.get(position);

        holder.tvPrice.setText("₹ " + propertyContact.getPrice() + " Lac");
        holder.tvDesc.setText(propertyContact.getProperty_contact_details());

        String buyer_id = propertyContact.getUser_id();

        //show contacted property
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("ID", propertyContact.getProperty_id());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("CONFIRM");
                builder.setMessage("By clicking YES your Mobile number and Email id will be send to buyer. Buyer will contact you soon.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //send mobile number and email to buyer
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.SEND_OWNER_DETAILS_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    int success = jsonObject.optInt(JSONField.SUCCESS);

                                    if (success == 1) {

                                        String msg = jsonObject.optString(JSONField.MSG);
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(context, MyPropertyDetailsActivity.class);
                                        intent.putExtra("ID", propertyContact.getProperty_id());
                                        context.startActivity(intent);

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
                                params.put(JSONField.OWNER_ID, owner_id);
                                params.put(JSONField.BUYER_ID,buyer_id);
                                params.put(JSONField.PROPERTY_ID, propertyId);

                                return params;

                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return buyersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvBuyer;
        TextView tvPrice, tvDesc;
        Button btnAccept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvBuyer = itemView.findViewById(R.id.cvBuyer);

            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDesc = itemView.findViewById(R.id.tvDesc);

            btnAccept = itemView.findViewById(R.id.btnAccept);

        }
    }

}
