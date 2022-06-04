package com.crzaor.zaply_shop.usescases.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.crzaor.zaply_shop.model.Product;
import com.crzaor.zaply_shop.provider.DBController;

import java.util.List;

public class ProductsViewModel extends ViewModel {
    private DBController dbController;
    private MutableLiveData<List<Product>> all_products = new MutableLiveData<>();

    public ProductsViewModel(){
        dbController = new DBController();
        List<Product> products= dbController.getAllProducts();
        all_products.postValue(products);
    }

    public void updateProduct(){
    }

    public LiveData<List<Product>> getAll_products(){
        return  all_products;
    }
}
