package com.yaroslav.list_products_final.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yaroslav.list_products_final.R;
import com.yaroslav.list_products_final.adapter.ProductAdapter;
import com.yaroslav.list_products_final.entity.Info;
import com.yaroslav.list_products_final.entity.Product;
import com.yaroslav.list_products_final.ui.CreateProductActivity;
import com.yaroslav.list_products_final.utils.Support;
import com.yaroslav.list_products_final.viewmodel.ListProductsViewModel;

import java.io.File;
import java.util.List;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;

    private ListProductsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_products, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ListProductsViewModel.class);

        initFloatingBtn();

        initRecyclerView();

        return view;
    }

    private void initFloatingBtn() {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Product product = new Product();
            long id = viewModel.insertGetId(product);
            Intent intent = new Intent(getActivity(), CreateProductActivity.class);
            intent.putExtra(CreateProductActivity.PRODUCT_ID, id);
            startActivity(intent);
        });
    }

    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        final ProductAdapter adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.getAllProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.submitList(products); //метод не адаптера, а самого абстрактного класса
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                File file = new File(adapter.getProductAtPosition(viewHolder.getAdapterPosition()).getFullPath());
                if(file.exists())
                    file.delete();
                viewModel.delete(adapter.getProductAtPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(getActivity(), CreateProductActivity.class);
                intent.putExtra(Product.class.getSimpleName(), product);
                intent.putExtra(CreateProductActivity.PRODUCT_ID, product.getId());
                startActivity(intent);
            }

            @Override
            public void onCheckboxClick(Product product, CheckBox checkBox, boolean isChecked) {
                product.setChecked(isChecked);
                Info info = new Info(product.getId());
                if (isChecked) {
                    viewModel.insert(info);
                } else {
                    viewModel.delete(info);
                }
                Support.loadStringCheckBox(checkBox, isChecked);
                viewModel.update(product);  //update
            }
        });
    }

}
