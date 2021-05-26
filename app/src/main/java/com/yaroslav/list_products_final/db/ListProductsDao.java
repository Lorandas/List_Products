package com.yaroslav.list_products_final.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yaroslav.list_products_final.entity.Info;
import com.yaroslav.list_products_final.entity.Product;
import com.yaroslav.list_products_final.entity.ProductWithInfo;

import java.util.List;

@Dao
public interface ListProductsDao {

    @Insert
    long insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Insert
    void insert(Info info);

    @Update
    void update(Info info);

    @Delete
    void delete(Info info);

    @Query("SELECT * FROM product_table ORDER BY name ASC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product_table p, info_table i WHERE i.info_id == p.product_id ORDER BY p.name ASC")
    LiveData<List<ProductWithInfo>> getAllProductsWithInfo();

}
