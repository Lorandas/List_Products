package com.yaroslav.list_products_final.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding4.view.RxView;
import com.yaroslav.list_products_final.R;
import com.yaroslav.list_products_final.entity.Product;
import com.yaroslav.list_products_final.utils.Support;
import com.yaroslav.list_products_final.viewmodel.ListProductsViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;

public class CreateProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final String PRODUCT_ID = "PRODUCT_ID";

    private static final String DIR_NAME = "image";

    private ImageView imageViewIcon;
    private Button buttonComplete;
    private EditText editTextName;

    private ListProductsViewModel viewModel;
    private Product product;
    private Bitmap image;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        viewModel = new ViewModelProvider(this).get(ListProductsViewModel.class);

        initView();

        bundleArguments();

        initButton();

        initImageView();

    }

    private void initView() {
        imageViewIcon = findViewById(R.id.ic_product);
        buttonComplete = findViewById(R.id.btn_complete);
        editTextName = findViewById(R.id.name_product);
    }

    private void bundleArguments() {
        Bundle arguments = getIntent().getExtras();
        product = (Product) arguments.getSerializable(Product.class.getSimpleName());
        if (product == null) {
            long id = getIntent().getLongExtra(PRODUCT_ID, 0);
            product = new Product();
            product.setId(id);
        } else {
            editTextName.setText(product.getName());
            image = Support.getBitmapByFullPath(product.getFullPath());
            Support.loadImage(imageViewIcon, image);
        }
    }

    private void initButton() {
        RxView.clicks(buttonComplete)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())  //ВАЖНО не нужен здесь .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        String name = editTextName.getText().toString();
                        String fullPath = image == null ? "" : saveToInternalStorage(image, product.getFileName());
                        validate(name, fullPath);
                        finish();
                    }
                });
    }

    private void initImageView() {
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void validate(String name, String fullPath) {
        product.setName(name);
        product.setFullPath(fullPath);
        viewModel.update(product);
    }

    private void showFileChooser() {    //Выбор картинки из устройства пользователя
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); //вызываем метод и передаем параметры и код для проверки (Request)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();                      //вынимаем вернувшиеся данные
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);  //Доставаемая картинка с устройства пользователя
                imageViewIcon.setImageBitmap(image);
            } catch (IOException e) {
                image = null;
            }
        }
    }

    private String saveToInternalStorage(@NonNull Bitmap bitmapImage, String fileName) {
        File directory = getMainDirectory();
        File fullPath = new File(directory, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fullPath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fullPath.getAbsolutePath();
    }

    private File getMainDirectory(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        return cw.getDir(DIR_NAME, Context.MODE_PRIVATE);
    }
}