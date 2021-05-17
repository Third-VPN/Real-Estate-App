package com.vpn.realestate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vpn.realestate.ContactedPropertyDetailsActivity;
import com.vpn.realestate.Models.PropertyContact;
import com.vpn.realestate.R;

import java.util.ArrayList;

public class PropertyContactAdapter extends RecyclerView.Adapter<PropertyContactAdapter.ViewHolder> {
    Context context;
    ArrayList<PropertyContact> contactList;

    public PropertyContactAdapter(Context context, ArrayList<PropertyContact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.my_property_contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PropertyContact propertyContact = contactList.get(position);

        holder.tvPrice.setText("₹ " + propertyContact.getPrice() + " Lac");
        holder.tvDesc.setText(propertyContact.getProperty_contact_details());

        //show contacted property
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("ID", propertyContact.getProperty_id());

                Intent intent = new Intent(context, ContactedPropertyDetailsActivity.class);
                intent.putExtra("ID", propertyContact.getProperty_id());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvPropertyContact;
        TextView tvPrice, tvDesc;
        Button btnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvPropertyContact = itemView.findViewById(R.id.cvPropertyContact);

            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDesc = itemView.findViewById(R.id.tvDesc);

            btnView = itemView.findViewById(R.id.btnView);

        }
    }

}
