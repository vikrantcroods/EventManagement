package com.croods.eventmanagement.model;

public class ReceivedMaterialToEventRequest
{
    private int productId,quantity;
    private  String remark;

    public ReceivedMaterialToEventRequest(int productId, int quantity, String remark) {
        this.productId = productId;
        this.quantity = quantity;
        this.remark = remark;
    }
}
