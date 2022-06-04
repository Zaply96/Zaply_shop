package com.crzaor.zaply_shop.usescases.category_products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crzaor.zaply_shop.usescases.common.adapters.ProductsRecyclerAdapter;
import com.crzaor.zaply_shop.model.Product;
import com.crzaor.zaply_shop.usescases.common.ProductsViewModel;
import com.crzaor.zaply_shop.usescases.favorites.FavoritesActivity;
import com.crzaor.zaply_shop.util.Notificator;
import com.crzaor.zaply_shop.util.SessionManager;

import android.content.Intent;
import android.os.Bundle;

import com.crzaor.zaply_shop.databinding.ActivityCategoryProductsBinding;
import com.crzaor.zaply_shop.provider.DBController;

import java.util.ArrayList;
import java.util.List;

public class CategoryProducts extends AppCompatActivity implements LifecycleOwner {

    private DBController dbController = new DBController();
    private ActivityCategoryProductsBinding binding;
    private Notificator notificator = new Notificator();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProductsRecyclerAdapter adapter;

    private ProductsViewModel productsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ////    OBTENEMOS LA CATEGORÍA    ////
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        binding.textViewCategory.setText(category);

        ////    INICIALIZAMOS EL ADAPTADOR    ////
        SessionManager sessionManager = new SessionManager(this);
        String email = sessionManager.getUserDetail().get("USER_ID");
        List<Integer> favorites = dbController.getFavoriteProductsID(email);
        List<Integer> card_products = dbController.getCardProductsId(email);
        adapter = new ProductsRecyclerAdapter(favorites,card_products);

        recyclerView = binding.recyclerCategoryProduct;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ////    INICIALIZAMOS EL VIEWMODEL Y LIVEDATA FILTRANDO POR CATEGORÍA    ////
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        productsViewModel.getAll_products().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                List<Product> product_of_categories = new ArrayList<>();
                products.forEach(product -> {
                    product.getCategories().forEach(c ->{
                        if(c.contains(category)){
                            product_of_categories.add(product);
                        }
                    });
                });
                adapter.setProducts(product_of_categories);
            }
        });

        ////    BOTÓN PARA CERRAR LA VENTANA    ////
        binding.backOrder.setOnClickListener(v ->{finish();});

        binding.launchLikesCategoryProducts.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, FavoritesActivity.class);
            startActivity(intent1);
        });
    }
}