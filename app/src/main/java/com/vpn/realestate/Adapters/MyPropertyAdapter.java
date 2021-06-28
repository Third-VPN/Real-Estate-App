package com.vpn.realestate.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.vpn.realestate.ContactedPropertyDetailsActivity;
import com.vpn.realestate.DrawerActivity;
import com.vpn.realestate.EditPropertyActivity;
import com.vpn.realestate.Models.Property;
import com.vpn.realestate.MyPropertyDetailsActivity;
import com.vpn.realestate.MyPropertyFragment;
import com.vpn.realestate.PropertyDetailsActivity;
import com.vpn.realestate.R;
import com.vpn.realestate.SearchPropertyFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPropertyAdapter extends RecyclerView.Adapter<MyPropertyAdapter.ViewHolder> {
    Context context;
    ArrayList<Property> propertyList;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    public MyPropertyAdapter(Context context, ArrayList<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.my_property_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = propertyList.get(position);

        //set the text
        holder.tvPrice.setText("₹ " + property.getProperty_price() + " Lac");
        holder.tvPropertyType.setText(property.getProperty_type_name());
        holder.tvBHK.setText(property.getProperty_bhk() + " BHK");
        holder.tvArea.setText(property.getProperty_area_name());
        holder.tvCity.setText(", " + property.getProperty_city());

        String property_id = property.getProperty_id();

        //set the image
        Glide.with(context).load(WebURL.PROPERTY_IMAGES_URL + property.getProperty_photo()).into(holder.ivImage);

        holder.cvProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MyPropertyDetailsActivity.class);
                intent.putExtra("ID", property_id);
                context.startActivity(intent);

            }
        });

        holder.ibtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, v);
                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.main_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.mEdit:

                                Intent intent = new Intent(context, EditPropertyActivity.class);
                                intent.putExtra("ID", property_id);
                                context.startActivity(intent);

                                break;

                            case R.id.mDelete:

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("CONFIRM");
                                builder.setMessage("Are you sure to remove this property?");

                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //getting user id for bookmark
                                        SharedPreferences preferences = context.getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
                                        String user_id = preferences.getString(ID_KEY, "");

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PROPERTY_DELETE_URL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);

                                                    int success = jsonObject.optInt(JSONField.SUCCESS);

                                                    if (success == 1) {
                                                        String msg = jsonObject.optString(JSONField.MSG);
                                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

                                                        propertyList.remove(position);
                                                        notifyItemRemoved(position);

//                                                        Intent intent = new Intent(context, DrawerActivity.class);
//                                                        context.startActivity(intent);
                                                        
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
                                        }){

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

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                builder.show();

                                break;

                        }

                        return false;
                    }
                });
                popupMenu.show();

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
        //ToggleButton tbBookmark;
        TextView tvPrice, tvPropertyType, tvBHK, tvCity, tvArea;
        ImageButton ibtnMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvProperty = itemView.findViewById(R.id.cvProperty);

            ivImage = itemView.findViewById(R.id.ivImage);

            //tbBookmark = itemView.findViewById(R.id.tbBookmark);

            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPropertyType = itemView.findViewById(R.id.tvPropertyType);
            tvBHK = itemView.findViewById(R.id.tvBHK);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvArea = itemView.findViewById(R.id.tvArea);

            ibtnMore = itemView.findViewById(R.id.ibtnMore);

        }
    }
}
