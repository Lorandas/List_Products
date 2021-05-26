package com.yaroslav.list_products_final.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.yaroslav.list_products_final.db.ListProductsDao;
import com.yaroslav.list_products_final.db.ListProductsDatabase;
import com.yaroslav.list_products_final.entity.Info;
import com.yaroslav.list_products_final.entity.Product;
import com.yaroslav.list_products_final.entity.ProductWithInfo;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListProductsRepository {

    private ListProductsDao listProductsDao;

    private LiveData<List<Product>> allProducts;

    private LiveData<List<ProductWithInfo>> allProductsWithInfo;

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public ListProductsRepository(Application application) {
        ListProductsDatabase db = ListProductsDatabase.getDatabase(application);
        listProductsDao = db.listProductsDao();
        allProducts = listProductsDao.getAllProducts();
        allProductsWithInfo = listProductsDao.getAllProductsWithInfo();
    }

    public void insert(Product product) {
        Maybe.fromAction(() -> listProductsDao.insert(product))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public long insertGetId(Product product) {
        Callable<Long> insertCallable = () -> listProductsDao.insert(product);
        Future<Long> future = executorService.submit(insertCallable);
        try {
            return future.get();
        }
        catch (Exception ex){
            return 0;
        }
    }

    public void update(Product product) {
        Maybe.fromAction(() -> listProductsDao.update(product))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void delete(Product product) {
        Maybe.fromAction(() -> listProductsDao.delete(product))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    public void insert(Info info) {
        Maybe.fromAction(() -> listProductsDao.insert(info))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void update(Info info) {
        Maybe.fromAction(() -> listProductsDao.update(info))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void delete(Info info) {
        Maybe.fromAction(() -> listProductsDao.delete(info))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<ProductWithInfo>> getAllProductsWithInfo() {
        return allProductsWithInfo;
    }
}
