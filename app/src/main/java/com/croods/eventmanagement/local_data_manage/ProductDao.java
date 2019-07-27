package com.croods.eventmanagement.local_data_manage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocalProductModel productModel);

    @Query("DELETE FROM product_table")
    void deleteAll();

    @Query("DELETE FROM product_table WHERE id=:id")
    void deletePerticularProduct(int id);

    @Query("SELECT * from product_table")
    LiveData<List<LocalProductModel>> getAllProduct();

    @Query("SELECT barcodeId from product_table")
    LiveData<List<Long>> getAllProductID();

    @Query("SELECT * from product_table WHERE barcode=:barcode")
    LiveData<List<LocalProductModel>> verifyProduct(String barcode);

    /*//get total quentity of one product
    @Query("SELECT count(*) from product_table WHERE productId=:productId")
    long verifyTotalProduct(long productId);

    @Query("UPDATE product_table SET rqty=rqty+1 WHERE productId = :productId")
    void updateQuentity(long productId);

    @Query("UPDATE product_table SET tqty=tqty+1 WHERE productId = :productId")
    void updateTotalQuentity(long productId);*/


}
