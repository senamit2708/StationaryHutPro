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
    private String productName;
    private String imageUrl;
    private int quantity;
    private String orderStatus;
    private String cartProductKey;

    public UserCart(String productNumber) {
        this.productNumber = productNumber;
    }

    public UserCart(String productNumber, String productPrice) {
        this.productNumber = productNumber;
        this.productPrice = productPrice;
    }

    public UserCart(String productNumber, String date, String productPrice, String productName, String imageUrl) {
        this.productNumber = productNumber;
        this.date = date;
        this.productPrice = productPrice;
        this.productName = productName;
        this.imageUrl = imageUrl;
    }

    public UserCart(String productNumber, String date, String productPrice, String productName, String imageUrl, int quantity) {
        this.productNumber = productNumber;
        this.date = date;
        this.productPrice = productPrice;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public UserCart(String productNumber, String date, String productPrice, String productName, String imageUrl, int quantity, String orderStatus, String cartProductKey) {
        this.productNumber = productNumber;
        this.date = date;
        this.productPrice = productPrice;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
        this.cartProductKey = cartProductKey;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCartProductKey() {
        return cartProductKey;
    }

    public void setCartProductKey(String cartProductKey) {
        this.cartProductKey = cartProductKey;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("productNumber", productNumber);
        result.put("productPrice", productPrice);
        result.put("productName", productName);
        result.put("imageUrl",imageUrl);
        result.put("orderDate",date);
        result.put("quantity",quantity);
        return result;
    }

    @Exclude
    public Map<String, Object> toMapFinalOrderEntry(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("productNumber", productNumber);
        result.put("productPrice", productPrice);
        result.put("productName", productName);
        result.put("imageUrl",imageUrl);
        result.put("orderDate",date);
        result.put("orderStatus",orderStatus);
        result.put("quantity", quantity);
        result.put("cartProductKey",cartProductKey);
        return result;
    }


}
