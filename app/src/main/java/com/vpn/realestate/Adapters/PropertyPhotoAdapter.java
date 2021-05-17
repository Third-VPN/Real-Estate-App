package com.vpn.realestate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vpn.realestate.ApiManager.WebURL;
import com.vpn.realestate.Models.PropertyPhoto;
import com.vpn.realestate.R;

import java.util.ArrayList;

public class PropertyPhotoAdapter extends RecyclerView.Adapter<PropertyPhotoAdapter.ViewHolder> {
    Context context;
    ArrayList<PropertyPhoto> photoList;

    public PropertyPhotoAdapter(Context context, ArrayList<PropertyPhoto> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.property_photo_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PropertyPhoto propertyPhoto = photoList.get(position);

        Glide.with(context).load(WebURL.PROPERTY_IMAGES_URL + propertyPhoto.getProperty_photo()).into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

}
