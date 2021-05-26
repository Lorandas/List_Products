package com.yaroslav.list_products_final.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslav.list_products_final.R;
import com.yaroslav.list_products_final.entity.Product;
import com.yaroslav.list_products_final.utils.Support;

public class ProductAdapter extends ListAdapter<Product, ProductAdapter.ProductHolder> {

    private OnItemClickListener listener;

    public ProductAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {

        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getName().equals(newItem.getName())
                    && oldItem.getFullPath().equals(newItem.getFullPath())
                    && oldItem.isChecked() == newItem.isChecked();
        }
    };

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product current = getItem(position);
        holder.bind(current);
    }

    public Product getProductAtPosition(int position) {
        return getItem(position);
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView name;
        private CheckBox checkBox;
        private View mView;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            icon = itemView.findViewById(R.id.ic_product);
            name = itemView.findViewById(R.id.name);
            checkBox = itemView.findViewById(R.id.checked);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onCheckboxClick(getItem(position), checkBox, checkBox.isChecked());
                    }
                }
            });
        }

        public void bind(Product product) {
            name.setText(product.getName());
            Support.loadImage(icon, Support.getBitmapByFullPath(product.getFullPath()));
            checkBox.setChecked(product.isChecked());
            Support.loadStringCheckBox(checkBox, product.isChecked());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onCheckboxClick(Product product, CheckBox checkBox, boolean isChecked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
