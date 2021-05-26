package com.yaroslav.list_products_final.entity;

import androidx.room.Embedded;

import java.io.Serializable;

public class ProductWithInfo implements Serializable {

    @Embedded
    private Product product;

    @Embedded
    private Info info;

    public ProductWithInfo(Product product, Info info) {
        this.product = product;
        this.info = info;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
