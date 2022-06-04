package com.crzaor.zaply_shop.usescases.main.fragments.cart;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crzaor.zaply_shop.usescases.main.fragments.cart.adapters.CartRecyclerAdapter;
import com.crzaor.zaply_shop.usescases.order.OrderActivity;
import com.crzaor.zaply_shop.R;
import com.crzaor.zaply_shop.model.Product;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CartRecyclerAdapter adapter;
    private TextView totalView;
    List<Product> products;
    public CartFragment(List<Product> products) {
        this.adapter = new CartRecyclerAdapter(products, cartRecyclerAdapterInterface);
        this.products = products;
    }
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(List<Product> products) {
        CartFragment fragment = new CartFragment(products);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        totalView = v.findViewById(R.id.totalPriceCart);

        Button btn = v.findViewById(R.id.buttonBuyCart);
        btn.setOnClickListener(view ->{
            if(products.size() > 0){
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("products", (Serializable) products);
                startActivity(intent);
            }else{
                Toast.makeText(getContext(),"Añade productos al carrito para continuar", Toast.LENGTH_LONG).show();
            }
        });

        recyclerView = v.findViewById(R.id.recyclerCart);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateTotalPrice();
        return v;
    }

    private void updateTotalPrice(){
        double total = 0;
        for(Product p: products){
           double price = p.getPrice();
           int count = p.getCount();
           total += (price * count);
        }
        totalView.setText(String.format("%.2f", total));

    }

    CartRecyclerAdapter.CartRecyclerAdapterInterface cartRecyclerAdapterInterface = new CartRecyclerAdapter.CartRecyclerAdapterInterface() {
        @Override
        public int sumCount(int pos) {
            int count = (int) products.get(pos).getCount();
            products.get(pos).setCount(count+=1);
            updateTotalPrice();
            return count;
        }

        @Override
        public int restCount(int pos) {
            int count = (int) products.get(pos).getCount();
            if(count > 1){
                products.get(pos).setCount(count-=1);
                updateTotalPrice();
            }
            return count;
        }
    };





}