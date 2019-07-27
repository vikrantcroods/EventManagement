package com.croods.eventmanagement.local_data_manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

public class ProductRepository
{
    private ProductRoomDatabase noteDatabase;


    public ProductRepository(Context context) {
        noteDatabase = Room.databaseBuilder(context, ProductRoomDatabase.class, "product_database").build();
    }


    @SuppressLint("StaticFieldLeak")
    public void insertTask(final LocalProductModel note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.productDao().insert(note);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteTask(final int id) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    noteDatabase.productDao().deletePerticularProduct(id);
                    return null;
                }
            }.execute();
    }
    @SuppressLint("StaticFieldLeak")
    public void deleteAllTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.productDao().deleteAll();
                return null;
            }
        }.execute();
    }

    public LiveData<List<LocalProductModel>> getAllData() {
        return noteDatabase.productDao().getAllProduct();
    }

    public LiveData<List<Long>> getAllId() {
        return noteDatabase.productDao().getAllProductID();
    }

    @SuppressLint("StaticFieldLeak")
    public LiveData<List<LocalProductModel>> verifyData(String barcode)
    {
        return noteDatabase.productDao().verifyProduct(barcode);
    }

   /* @SuppressLint("StaticFieldLeak")
    public void updateTask(long productId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.productDao().updateQuentity(productId);
                return null;
            }
        }.execute();
    }*/

}
