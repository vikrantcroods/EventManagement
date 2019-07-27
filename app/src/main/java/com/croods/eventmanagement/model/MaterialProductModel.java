package com.croods.eventmanagement.model;

public class MaterialProductModel {
    private String productName,productBarcode,barcode;
    private  BarcodeProductVo productVo;
    private  ResponseBarcodeVo barcodeVo;
    private  long barcodeId;

    public ResponseBarcodeVo getBarcodeVo() {
        return barcodeVo;
    }

    public void setBarcodeVo(ResponseBarcodeVo barcodeVo) {
        this.barcodeVo = barcodeVo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BarcodeProductVo getProductVo() {
        return productVo;
    }

    public void setProductVo(BarcodeProductVo productVo) {
        this.productVo = productVo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public long getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(long barcodeId) {
        this.barcodeId = barcodeId;
    }
}
