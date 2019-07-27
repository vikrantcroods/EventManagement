package com.croods.eventmanagement.model;

import java.util.List;

public class ProductDetailResponse
{
    private  String CurrentQty,TotalQty;
    private List<ProductStockDetailList> events;

    public String getCurrentQty() {
        return CurrentQty;
    }

    public void setCurrentQty(String currentQty) {
        CurrentQty = currentQty;
    }

    public String getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(String totalQty) {
        TotalQty = totalQty;
    }

    public List<ProductStockDetailList> getEvents() {
        return events;
    }

    public void setEvents(List<ProductStockDetailList> events) {
        this.events = events;
    }
}
