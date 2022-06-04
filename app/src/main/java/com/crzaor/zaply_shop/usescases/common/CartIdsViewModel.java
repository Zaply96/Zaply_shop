package com.crzaor.zaply_shop.usescases.common;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.crzaor.zaply_shop.provider.DBController;
import com.crzaor.zaply_shop.util.SessionManager;

import java.util.List;

public class CartIdsViewModel extends AndroidViewModel {
    private DBController dbController;
    private MutableLiveData<List<Integer>> all_cart_ids = new MutableLiveData<>();
    private String email;
    public CartIdsViewModel(Application application){
        super(application);
        dbController = new DBController();
        SessionManager sessionManager = new SessionManager(application.getApplicationContext());
        email = sessionManager.getUserDetail().get("USER_ID");
        all_cart_ids.postValue(dbController.getCardProductsId(email));
    }

    public void updateCartIds(List<Integer> all_cart_ids){
        this.all_cart_ids.postValue(all_cart_ids);
    }

    public LiveData<List<Integer>> getAll_cart_ids(){
        return all_cart_ids;
    }
}
