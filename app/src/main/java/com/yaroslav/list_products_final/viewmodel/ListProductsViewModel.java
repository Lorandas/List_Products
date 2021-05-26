package com.yaroslav.list_products_final.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslav.list_products_final.entity.Info;
import com.yaroslav.list_products_final.entity.Product;
import com.yaroslav.list_products_final.entity.ProductWithInfo;
import com.yaroslav.list_products_final.repository.ListProductsRepository;

import java.util.List;

public class ListProductsViewModel extends AndroidViewModel {

    private ListProductsRepository repository;

    private LiveData<List<Product>> allProducts;

    private LiveData<List<ProductWithInfo>> allProductsWithInfo;

    public ListProductsViewModel(@NonNull Application application) {
        super(application);
        repository = new ListProductsRepository(application);
        allProducts = repository.getAllProducts();
        allProductsWithInfo = repository.getAllProductsWithInfo();
    }

    public void insert(Product product){
        repository.insert(product);
    }

    public long insertGetId(Product product) {
        return repository.insertGetId(product);
    }

    public void update(Product product){
        repository.update(product);
    }

    public void delete(Product product){
        repository.delete(product);
    }

    public void insert(Info info){
        repository.insert(info);
    }

    public void update(Info info){
        repository.update(info);
    }

    public void delete(Info info){
        repository.delete(info);
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<ProductWithInfo>> getAllProductsWithInfo() {
        return allProductsWithInfo;
    }
}
