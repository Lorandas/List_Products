package com.yaroslav.list_products_final.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.yaroslav.list_products_final.entity.Info;
import com.yaroslav.list_products_final.entity.Product;

@Database(entities = {Product.class, Info.class}, version = 1, exportSchema = false)
public abstract class ListProductsDatabase extends RoomDatabase {

    private static volatile ListProductsDatabase INSTANCE;

    public abstract ListProductsDao listProductsDao();

    public static ListProductsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ListProductsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ListProductsDatabase.class, "list_product_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
