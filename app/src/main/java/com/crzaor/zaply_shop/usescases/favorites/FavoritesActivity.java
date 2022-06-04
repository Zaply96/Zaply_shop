package com.crzaor.zaply_shop.usescases.favorites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.crzaor.zaply_shop.databinding.ActivityFavoritesBinding;
import com.crzaor.zaply_shop.model.Product;
import com.crzaor.zaply_shop.provider.DBController;
import com.crzaor.zaply_shop.usescases.common.adapters.ProductsRecyclerAdapter;
import com.crzaor.zaply_shop.usescases.common.ProductsViewModel;
import com.crzaor.zaply_shop.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private DBController dbController = new DBController();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProductsRecyclerAdapter adapter;
    private ActivityFavoritesBinding binding;

    private ProductsViewModel productsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ////    INICIALIZAMOS EL ADAPTADOR    ////
        SessionManager sessionManager = new SessionManager(this);
        String email = sessionManager.getUserDetail().get("USER_ID");
        List<Integer> favorites = dbController.getFavoriteProductsID(email);
        List<Integer> card_products = dbController.getCardProductsId(email);
        adapter = new ProductsRecyclerAdapter(favorites,card_products);

        recyclerView = binding.recyclerFavoriteProducts;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ////    INICIALIZAMOS EL VIEWMODEL Y LIVEDATA FILTRANDO POR CATEGORÍA    ////
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        productsViewModel.getAll_products().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                List<Product> favorite_products = new ArrayList<>();
                products.forEach(product -> {
                    if(favorites.contains(product.getId())){
                        favorite_products.add(product);
                    }
                });
                adapter.setProducts(favorite_products);
            }
        });

        ////    BOTÓN PARA CERRAR LA VENTANA    ////
        binding.backFavorites.setOnClickListener(v ->{finish();});
    }
}