package com.yaroslav.list_products_final.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslav.list_products_final.R;
import com.yaroslav.list_products_final.adapter.ProductWithInfoAdapter;
import com.yaroslav.list_products_final.entity.Info;
import com.yaroslav.list_products_final.entity.Product;
import com.yaroslav.list_products_final.entity.ProductWithInfo;
import com.yaroslav.list_products_final.ui.InfoSelectProductActivity;
import com.yaroslav.list_products_final.viewmodel.ListProductsViewModel;

import java.util.List;

public class ListFragment extends Fragment {

    private TextView textViewTotalAmount;
    private RecyclerView recyclerView;
    private View view;

    private ListProductsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        textViewTotalAmount = view.findViewById(R.id.total_amount);

        viewModel = new ViewModelProvider(requireActivity()).get(ListProductsViewModel.class);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        final ProductWithInfoAdapter adapter = new ProductWithInfoAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.getAllProductsWithInfo().observe(getViewLifecycleOwner(), new Observer<List<ProductWithInfo>>() {
            @Override
            public void onChanged(List<ProductWithInfo> productsWithInfo) {
                initTextViewTotalAmount(productsWithInfo);
                adapter.submitList(productsWithInfo);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Product product = adapter.getProductWithInfoAtPosition(viewHolder.getAdapterPosition()).getProduct();
                product.setChecked(false);
                viewModel.update(product); //update
                viewModel.delete(adapter.getProductWithInfoAtPosition(viewHolder.getAdapterPosition()).getInfo());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ProductWithInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductWithInfo productWithInfo) {
                Intent intent = new Intent(getActivity(), InfoSelectProductActivity.class);
                intent.putExtra(ProductWithInfo.class.getSimpleName(), productWithInfo);
                startActivity(intent);
            }
        });
    }

    private void initTextViewTotalAmount(List<ProductWithInfo> productsWithInfo){
        double sum = 0;
        for (ProductWithInfo productWithInfo : productsWithInfo){
            Info info = productWithInfo.getInfo();
            sum += info.getPrice();
        }
        textViewTotalAmount.setText(view.getContext().getResources().getString(R.string.text_total_price) + " " + sum);
    }
}
