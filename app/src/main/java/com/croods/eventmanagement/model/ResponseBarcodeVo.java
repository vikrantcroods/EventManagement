package com.croods.eventmanagement.model;

public class ResponseBarcodeVo
{
    private  int barcodeId;
    private String barcode;
    private  BarcodeProductVo productVo;

    public BarcodeProductVo getProductVo() {
        return productVo;
    }

    public void setProductVo(BarcodeProductVo productVo) {
        this.productVo = productVo;
    }

    public int getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(int barcodeId) {
        this.barcodeId = barcodeId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
