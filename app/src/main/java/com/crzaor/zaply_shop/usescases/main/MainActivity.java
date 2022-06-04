package com.crzaor.zaply_shop.usescases.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.crzaor.zaply_shop.R;
import com.crzaor.zaply_shop.databinding.ActivityMainBinding;
import com.crzaor.zaply_shop.util.SessionManager;
import com.crzaor.zaply_shop.usescases.main.fragments.cart.CartFragment;
import com.crzaor.zaply_shop.usescases.main.fragments.categories.CategoriesFragment;
import com.crzaor.zaply_shop.usescases.main.fragments.home.ProductsFragment;
import com.crzaor.zaply_shop.usescases.main.fragments.profile.ProfileFragment;
import com.crzaor.zaply_shop.provider.DBController;
import com.crzaor.zaply_shop.usescases.login.LoginActivity;
import com.crzaor.zaply_shop.util.DevelopingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    DBController dbController = new DBController();
    private ActivityMainBinding binding;
    SessionManager sessionManager;
    Fragment categoriesFragment = new CategoriesFragment();
    Fragment productsFragment = new ProductsFragment();
    Fragment perfilFragment = new ProfileFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(navigation);

        if(!sessionManager.isLogin()){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                new ProductsFragment()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigation =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                    new ProductsFragment()).commit();
                            break;

                        case R.id.categories:
                            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                    categoriesFragment).commit();
                            break;
                        case R.id.carro:
                            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                    new CartFragment(dbController.getCardProducts(sessionManager.getUserDetail().get("USER_ID")))).commit();
                            break;
                        case R.id.perfil:
                            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                    new DevelopingFragment()).commit();
                            break;
                    }
                    return true;
                }
            };
}