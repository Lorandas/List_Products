package com.yaroslav.list_products_final.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jakewharton.rxbinding4.view.RxView;
import com.yaroslav.list_products_final.R;
import com.yaroslav.list_products_final.entity.ProductWithInfo;
import com.yaroslav.list_products_final.utils.Support;
import com.yaroslav.list_products_final.viewmodel.ListProductsViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;

public class InfoSelectProductActivity extends AppCompatActivity {

    private ImageView imageViewIcon;
    private TextView textViewName;
    private EditText editTextPrice;
    private EditText editTextWeight;
    private Spinner spinnerTypeWeight;
    private Button buttonComplete;

    private ProductWithInfo productWithInfo;
    private ListProductsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_select_product);

        viewModel = new ViewModelProvider(this).get(ListProductsViewModel.class);

        initView();

        createSpinner();

        bundleArguments();

        initButton();
    }

    private void initView() {
        imageViewIcon = findViewById(R.id.ic_product);
        buttonComplete = findViewById(R.id.btn_complete);
        textViewName = findViewById(R.id.name_product);
        editTextPrice = findViewById(R.id.price);
        editTextWeight = findViewById(R.id.weight);
        spinnerTypeWeight = findViewById(R.id.type_weight);
    }

    private void createSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_weight, R.layout.spinner_list);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        spinnerTypeWeight.setAdapter(adapter);
    }

    private void bundleArguments() {

        Bundle arguments = getIntent().getExtras();
        if (arguments == null)
            return;
        productWithInfo = (ProductWithInfo) arguments.getSerializable(ProductWithInfo.class.getSimpleName());
        textViewName.setText(productWithInfo.getProduct().getName());
        editTextPrice.setText(productWithInfo.getInfo().getPrice() == 0 ? "" : String.valueOf(productWithInfo.getInfo().getPrice()));
        editTextWeight.setText(productWithInfo.getInfo().getWeight() == 0 ? "" : String.valueOf(productWithInfo.getInfo().getWeight()));
        Support.loadImage(imageViewIcon, Support.getBitmapByFullPath(productWithInfo.getProduct().getFullPath()));
        spinnerTypeWeight.setSelection(getIdSpinnerByValue(productWithInfo.getInfo().getTypeWeight()));
    }

    private int getIdSpinnerByValue(String value) {
        if (value == null || value.equals("") || value.equals("-"))
            return 0;
        SpinnerAdapter adapter = ((Spinner) spinnerTypeWeight).getAdapter();
        int num = adapter.getCount();
        for (int i = 0; i < num; i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private void initButton() {
        RxView.clicks(buttonComplete)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())  //ВАЖНО не нужен здесь .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        String price = editTextPrice.getText().toString();
                        String weight = editTextWeight.getText().toString();
                        String typeWeight = spinnerTypeWeight.getSelectedItemId() == 0 ? "" : spinnerTypeWeight.getSelectedItem().toString();
                        validate(price, weight, typeWeight);
                        finish();
                    }
                });
    }

    private void validate(String price, String weight, String typeWeight) {
        productWithInfo.getInfo().setPrice(price.equals("") ? 0 : Double.parseDouble(price));
        productWithInfo.getInfo().setWeight(weight.equals("")  ? 0 : Double.parseDouble(weight));
        productWithInfo.getInfo().setTypeWeight(typeWeight);
        viewModel.update(productWithInfo.getInfo());
    }

}