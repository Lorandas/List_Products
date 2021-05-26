package com.yaroslav.list_products_final.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "info_table",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE, entity = Product.class, parentColumns = "product_id", childColumns = "info_id")
        }
)
public class Info implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "info_id")
    private long id;

    private double price;

    private double weight;

    @ColumnInfo(name = "type_weight")
    private String typeWeight = "";

    @Ignore
    public Info(long id) {
        this.id = id;
    }

    public Info(long id, double price, double weight, String typeWeight) {
        this.id = id;
        this.price = price;
        this.weight = weight;
        this.typeWeight = typeWeight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getTypeWeight() {
        return typeWeight;
    }

    public void setTypeWeight(String typeWeight) {
        this.typeWeight = typeWeight;
    }
}
