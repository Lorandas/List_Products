package com.yaroslav.list_products_final.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "product_table")
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    private long id;

    private String name = "";

    @ColumnInfo(name = "full_path")
    private String fullPath = "";

    private boolean checked;

    @Ignore
    public Product(){
    }

    public Product(String name, String fullPath, boolean checked) {
        this.name = name;
        this.fullPath = fullPath;
        this.checked = checked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Ignore
    public String getFileName(){
        return "IMG_" + id + ".png";
    }

}
