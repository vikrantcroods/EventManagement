package com.croods.eventmanagement.model;

public class ProductListResponse {

    private int productId, isPart;
    private int rquantity,quantity,outquantity;
    private String name, displayName, productImage,remark;


    public int getOutquantity() {
        return outquantity;
    }

    public void setOutquantity(int outquantity) {
        this.outquantity = outquantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRquantity() {
        return rquantity;
    }

    public void setRquantity(int rquantity) {
        this.rquantity = rquantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getIsPart() {
        return isPart;
    }

    public void setIsPart(int isPart) {
        this.isPart = isPart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
