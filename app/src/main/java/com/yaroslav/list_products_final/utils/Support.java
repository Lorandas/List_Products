package com.yaroslav.list_products_final.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.yaroslav.list_products_final.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Support {

    public static final Bitmap getBitmapByFullPath(String fullPath) {
        try {
            File file = new File(fullPath);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return null;
        }
    }


    public static final void loadImage(ImageView imageView, Bitmap bitmap) {
        try {
            if (bitmap == null)
                throw new Exception();
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.ic_product);
        }
    }

    public static final void loadStringCheckBox(CheckBox checkBox, boolean isChecked) {
        checkBox.setText(isChecked == true ? checkBox.getContext().getResources().getString(R.string.remove) : checkBox.getContext().getResources().getString(R.string.select));
    }
}
