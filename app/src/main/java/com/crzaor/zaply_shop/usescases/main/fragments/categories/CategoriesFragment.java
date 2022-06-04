package com.crzaor.zaply_shop.usescases.main.fragments.categories;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crzaor.zaply_shop.R;
import com.crzaor.zaply_shop.usescases.main.fragments.categories.adapters.CategoriesRecyclerAdapter;
import com.crzaor.zaply_shop.provider.DBController;
import com.crzaor.zaply_shop.util.LoadingDialog;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private DBController dbController = new DBController();
    public CategoriesFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        List<Map<String,String>> categories = dbController.getAllCategories();
        LoadingDialog loadingDialog = new LoadingDialog(v);
        loadingDialog.startLoading();

        recyclerView = v.findViewById(R.id.recyclerCategories);
        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CategoriesRecyclerAdapter(categories);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadingDialog.dissmissLoader();
        return v;
    }

}