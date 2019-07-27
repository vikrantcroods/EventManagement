package com.croods.eventmanagement.local_data_manage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LocalProductModel.class}, version = 1,exportSchema = false)
public abstract class ProductRoomDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
