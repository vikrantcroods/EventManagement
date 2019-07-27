package com.croods.eventmanagement.local_data_manage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "product_table")
public class LocalProductModel implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "displayName")
    private String displayName;

    @ColumnInfo(name = "barcodeId" )
    private long barcodeId;

    @ColumnInfo(name = "productId" )
    private long productId;

    @ColumnInfo(name = "barcode")
    private String barcode;

    public LocalProductModel(String displayName,String barcode, long barcodeId) {
        this.displayName = displayName;
        this.barcodeId = barcodeId;
        this.barcode = barcode;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(long barcodeId) {
        this.barcodeId = barcodeId;
    }
}
