package com.example.a91p.data;


import com.google.android.gms.maps.model.LatLng;

public class Advert {

    private String name, postType, phone, description, date, location;

    //constructor
    public Advert(String name, String postType, String phone, String description, String date, String location) {
        this.name = name;
        this.postType = postType;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }


    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
