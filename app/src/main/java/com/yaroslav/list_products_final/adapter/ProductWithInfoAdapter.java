package com.yaroslav.list_products_final.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslav.list_products_final.R;
import com.yaroslav.list_products_final.entity.ProductWithInfo;
import com.yaroslav.list_products_final.utils.Support;

public class ProductWithInfoAdapter extends ListAdapter<ProductWithInfo, ProductWithInfoAdapter.ProductWithInfoHolder> {

    private OnItemClickListener listener;

    public ProductWithInfoAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ProductWithInfo> DIFF_CALLBACK = new DiffUtil.ItemCallback<ProductWithInfo>() {

        @Override
        public boolean areItemsTheSame(@NonNull ProductWithInfo oldItem, @NonNull ProductWithInfo newItem) {
            return oldItem.getProduct().getId() == newItem.getProduct().getId()
                    && oldItem.getInfo().getId() == newItem.getInfo().getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductWithInfo oldItem, @NonNull ProductWithInfo newItem) {
            return oldItem.getProduct().getName().equals(newItem.getProduct().getName())
                    && oldItem.getProduct().isChecked() == newItem.getProduct().isChecked()
                    && oldItem.getProduct().getFullPath().equals(newItem.getProduct().getFullPath())
                    && oldItem.getInfo().getPrice() == newItem.getInfo().getPrice()
                    && oldItem.getInfo().getWeight() == newItem.getInfo().getWeight()
                    && oldItem.getInfo().getTypeWeight().equals(newItem.getInfo().getTypeWeight());
        }
    };

    @NonNull
    @Override
    public ProductWithInfoAdapter.ProductWithInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_product, parent, false);
        return new ProductWithInfoAdapter.ProductWithInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductWithInfoAdapter.ProductWithInfoHolder holder, int position) {
        ProductWithInfo current = getItem(position);
        holder.bind(current);
    }

    public ProductWithInfo getProductWithInfoAtPosition(int position) {
        return getItem(position);
    }

    class ProductWithInfoHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView name;
        private TextView price;
        private TextView weightAndType;
        private View mView;

        public ProductWithInfoHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            icon = itemView.findViewById(R.id.ic_product);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            weightAndType = itemView.findViewById(R.id.count_and_type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }

        public void bind(ProductWithInfo productWithInfo) {
            name.setText(productWithInfo.getProduct().getName());
            Support.loadImage(icon, Support.getBitmapByFullPath(productWithInfo.getProduct().getFullPath()));
            price.setText(
                    (productWithInfo.getInfo().getPrice() != 0) ?
                            String.valueOf(productWithInfo.getInfo().getPrice()) + " " + mView.getContext().getResources().getString(R.string.symbol_currency)
                            : "");
            weightAndType.setText(
                    (productWithInfo.getInfo().getWeight() != 0 && !productWithInfo.getInfo().getTypeWeight().equals("")) ?
                            productWithInfo.getInfo().getWeight() + " " + productWithInfo.getInfo().getTypeWeight()
                            : "");
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ProductWithInfo productWithInfo);
    }

    public void setOnItemClickListener(ProductWithInfoAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
