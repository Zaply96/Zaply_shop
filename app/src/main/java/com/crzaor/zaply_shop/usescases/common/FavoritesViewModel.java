package com.crzaor.zaply_shop.usescases.common;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.crzaor.zaply_shop.model.Product;
import com.crzaor.zaply_shop.provider.DBController;
import com.crzaor.zaply_shop.util.SessionManager;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    private DBController dbController;
    private MutableLiveData<List<Integer>> all_favorites = new MutableLiveData<>();
    private String email;

    public FavoritesViewModel(Application application){
        super(application);
        dbController = new DBController();
        SessionManager sessionManager = new SessionManager(application.getApplicationContext());
        email = sessionManager.getUserDetail().get("USER_ID");
        all_favorites.postValue(dbController.getFavoriteProductsID(email));
    }

    public void updateFavorites(List<Integer> all_favorites){
        this.all_favorites.setValue(all_favorites);
        dbController.updateFavorites(email, all_favorites);
    }

    public LiveData<List<Integer>> getAll_favorites(){
        return  all_favorites;
    }
}
