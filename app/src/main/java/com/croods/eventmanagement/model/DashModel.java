package com.croods.eventmanagement.model;

public class DashModel
{
    private String totalProduct,totalQty,totalSuppliers,totalCustomers,TotalActiveEvents;


    public String getTotalActiveEvents() {
        return TotalActiveEvents;
    }

    public void setTotalActiveEvents(String totalActiveEvents) {
        TotalActiveEvents = totalActiveEvents;
    }

    public String getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(String totalProduct) {
        this.totalProduct = totalProduct;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(String totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }

    public String getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(String totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}
