package com.jml.reggie.entity;

public class Product {
    private String productId;
    private String getProductName;
    private Double productPrice;

    public Product(String productId, String getProductName, Double productPrice) {
        this.productId = productId;
        this.getProductName = getProductName;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGetProductName() {
        return getProductName;
    }

    public void setGetProductName(String getProductName) {
        this.getProductName = getProductName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

}
