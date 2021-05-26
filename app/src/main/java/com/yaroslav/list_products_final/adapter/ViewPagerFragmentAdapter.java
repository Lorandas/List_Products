package com.yaroslav.list_products_final.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yaroslav.list_products_final.ui.fragment.ListFragment;
import com.yaroslav.list_products_final.ui.fragment.ProductsFragment;

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private int behavior;

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.behavior = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ListFragment();
            case 1:
                return new ProductsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return behavior;
    }
}
