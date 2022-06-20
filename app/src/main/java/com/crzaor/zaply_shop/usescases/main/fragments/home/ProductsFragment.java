package com.crzaor.zaply_shop.usescases.main.fragments.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crzaor.zaply_shop.R;
import com.crzaor.zaply_shop.usescases.common.adapters.ProductsRecyclerAdapter;
import com.crzaor.zaply_shop.model.Product;
import com.crzaor.zaply_shop.usescases.common.ProductsViewModel;
import com.crzaor.zaply_shop.provider.DBController;
import com.crzaor.zaply_shop.usescases.favorites.FavoritesActivity;
import com.crzaor.zaply_shop.util.SessionManager;

import java.util.List;


public class ProductsFragment extends Fragment implements LifecycleOwner {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProductsRecyclerAdapter adapter;

    private DBController dbController = new DBController();
    private ProductsViewModel productsViewModel;

    public ProductsFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsViewModel = ViewModelProviders.of(getActivity()).get(ProductsViewModel.class);
        productsViewModel.getAll_products().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setProducts(products);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        SessionManager sessionManager = new SessionManager(container.getContext());
        String email = sessionManager.getUserDetail().get("USER_ID");
        List<Integer> favorites = dbController.getFavoriteProductsID(email);
        List<Integer> card_products = dbController.getCardProductsId(email);



        this.adapter = new ProductsRecyclerAdapter(favorites, card_products);
        recyclerView = v.findViewById(R.id.recyclerHome);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        v.findViewById(R.id.launchLikesHome).setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), FavoritesActivity.class);
            startActivity(intent);
        });

        return v;
    }


}