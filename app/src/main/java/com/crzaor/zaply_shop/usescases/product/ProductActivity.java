package com.crzaor.zaply_shop.usescases.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import java.time.LocalDate;

import com.crzaor.zaply_shop.R;
import com.crzaor.zaply_shop.databinding.ActivityProductBinding;
import com.crzaor.zaply_shop.provider.DBController;
import com.crzaor.zaply_shop.usescases.favorites.FavoritesActivity;
import com.crzaor.zaply_shop.usescases.order.OrderActivity;
import com.crzaor.zaply_shop.util.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    ActivityProductBinding binding;
    private List<Integer> favorites;
    private List<Integer> card_products;
    private DBController dbController = new DBController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SessionManager sessionManager = new SessionManager(this);

        Intent intent = getIntent();
        double value = (intent.getDoubleExtra("price",1));
        int id_product = intent.getIntExtra("id_product", -1);
        card_products = dbController.getCardProductsId(sessionManager.getUserDetail().get("USER_ID"));
        favorites = dbController.getFavoriteProductsID(sessionManager.getUserDetail().get("USER_ID"));

        binding.titleProduct.setText((intent.getStringExtra("title")));
        binding.descriptionProduct.setText((intent.getStringExtra("description")));
        binding.materialsProduct.setText((intent.getStringExtra("materials")));
        binding.categoriesProduct.setText((intent.getStringExtra("categories")));
        binding.priceProduct.setText(String.valueOf(value));
//        binding.titleProduct.setText((intent.getIntExtra("stock",0)));
        String imageurl = (intent.getStringExtra("url_images"));
        Picasso.get()
                .load(imageurl)
                .into(binding.imageViewProduct);

        LocalDate localDate = LocalDate.now();
        LocalDate finalDate = LocalDate.from(localDate.plusDays(7));
        binding.textViewDate.setText(localDate.getDayOfMonth()+ " "+ localDate.getMonth().toString().toLowerCase()
                                    + " - "+finalDate.getDayOfMonth()+" "+finalDate.getMonth().toString().toLowerCase());

        FloatingActionButton favorite = binding.likeProductButton;
        FloatingActionButton card = binding.cardProductButton;

        if(favorites.contains(id_product)){
            binding.likeProductButton.setImageResource(R.drawable.liked_icon);
        }

        if(card_products.contains(id_product)){
            binding.cardProductButton.setImageResource(R.drawable.cart_icon);
        }

        favorite.setOnClickListener(view ->{
            if(favorites.contains((Integer) id_product)){
                favorite.setImageResource(R.drawable.unliked_icon);
                favorites.removeAll(Arrays.asList(id_product));
            }else {
                favorite.setImageResource(R.drawable.liked_icon);
                favorites.add(id_product);
            }
            dbController.updateFavorites(sessionManager.getUserDetail().get("USER_ID"),favorites);
        });

        card.setOnClickListener(view ->{
            if(card_products.contains(id_product)){
                card.setImageResource(R.drawable.add_cart_icon);
                card_products.removeAll(Arrays.asList(id_product));
            }else {
                card.setImageResource(R.drawable.cart_icon);
                card_products.add(id_product);
            }
            dbController.updateCard(sessionManager.getUserDetail().get("USER_ID"),card_products);
        });

        binding.buttonBuy.setOnClickListener(v->{
            if(!card_products.contains(id_product)){
                card.setImageResource(R.drawable.cart_icon);
                card_products.add(id_product);
                dbController.updateCard(sessionManager.getUserDetail().get("USER_ID"),card_products);
            }
            Intent intent1 = new Intent(this, OrderActivity.class);
            intent1.putExtra("products", (Serializable) dbController.getCardProducts(sessionManager.getUserDetail().get("USER_ID")));
            startActivity(intent1);
        });

        binding.backOrder.setOnClickListener(v -> {
            finish();
        });

        binding.launchLikesProduct.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, FavoritesActivity.class);
            startActivity(intent1);
        });

    }
}