package com.vpn.realestate.Models;

public class Property {
    String property_id, property_name, property_description, property_price,
            property_bhk, property_sqft_carpet, property_sqft_covered, property_address,
            property_type_name, property_city, property_area_pincode, Property_area_name,
            property_photo, user_id;

    boolean bookmark = false;

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_description() {
        return property_description;
    }

    public void setProperty_description(String property_description) {
        this.property_description = property_description;
    }

    public String getProperty_price() {
        return property_price;
    }

    public void setProperty_price(String property_price) {
        this.property_price = property_price;
    }

    public String getProperty_bhk() {
        return property_bhk;
    }

    public void setProperty_bhk(String property_bhk) {
        this.property_bhk = property_bhk;
    }

    public String getProperty_sqft_carpet() {
        return property_sqft_carpet;
    }

    public void setProperty_sqft_carpet(String property_sqft_carpet) {
        this.property_sqft_carpet = property_sqft_carpet;
    }

    public String getProperty_sqft_covered() {
        return property_sqft_covered;
    }

    public void setProperty_sqft_covered(String property_sqft_covered) {
        this.property_sqft_covered = property_sqft_covered;
    }

    public String getProperty_address() {
        return property_address;
    }

    public void setProperty_address(String property_address) {
        this.property_address = property_address;
    }

    public String getProperty_type_name() {
        return property_type_name;
    }

    public void setProperty_type_name(String property_type_name) {
        this.property_type_name = property_type_name;
    }

    public String getProperty_city() {
        return property_city;
    }

    public void setProperty_city(String property_city) {
        this.property_city = property_city;
    }

    public String getProperty_area_pincode() {
        return property_area_pincode;
    }

    public void setProperty_area_pincode(String property_area_pincode) {
        this.property_area_pincode = property_area_pincode;
    }

    public String getProperty_area_name() {
        return Property_area_name;
    }

    public void setProperty_area_name(String property_area_name) {
        Property_area_name = property_area_name;
    }

    public String getProperty_photo() {
        return property_photo;
    }

    public void setProperty_photo(String property_photo) {
        this.property_photo = property_photo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }
}
