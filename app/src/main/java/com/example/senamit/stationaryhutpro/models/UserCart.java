package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserCart {

    private String productNumber;
    private String date="26th Aug 2019";
    private String productPrice;

    public UserCart(String productNumber) {
        this.productNumber = productNumber;
    }

    public UserCart(String productNumber, String productPrice) {
        this.productNumber = productNumber;
        this.productPrice = productPrice;
    }

    public UserCart() {
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("productNumber", productNumber);
        result.put("date", date);
        return result;
    }

}
