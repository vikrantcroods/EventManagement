package com.croods.eventmanagement.model;

public class ProductStockDetailList
{
    private String qty,name;

    public ProductStockDetailList(String qty, String name) {
        this.qty = qty;
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
