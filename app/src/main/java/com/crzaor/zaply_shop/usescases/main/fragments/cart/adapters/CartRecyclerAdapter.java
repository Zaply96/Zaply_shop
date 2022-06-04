package com.crzaor.zaply_shop.usescases.main.fragments.cart.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crzaor.zaply_shop.R;
import com.crzaor.zaply_shop.model.Product;
import com.crzaor.zaply_shop.usescases.main.fragments.cart.CartFragment;
import com.crzaor.zaply_shop.util.SessionManager;
import com.crzaor.zaply_shop.usescases.product.ProductActivity;
import com.crzaor.zaply_shop.provider.DBController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {

    private static final DBController dbController = new DBController();
    private static List<Product> products;
    private static CartFragment fragment;
    private static CartRecyclerAdapterInterface AdapterInterface;
    public CartRecyclerAdapter(List<Product> products, CartRecyclerAdapterInterface cartRecyclerAdapterInterface){
        this.products = products;
        AdapterInterface = cartRecyclerAdapterInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_cart, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        String imageurl = product.getUrl_image();
        Picasso.get()
                .load(imageurl)
                .into(holder.image);
        holder.title.setText(product.getTitle());
        holder.price.setText(String.valueOf(product.getPrice()));

        holder.add.setOnClickListener(view ->{
            int count = AdapterInterface.sumCount(position);
            holder.countProduct.setText(Integer.toString(count));
        });

        holder.rest.setOnClickListener(view ->{
            int count = AdapterInterface.restCount(position);
            holder.countProduct.setText(Integer.toString(count));
        });
    }

    @Override
    public int getItemCount() { return products.size();}

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,price,countProduct;
        ImageView image, add, rest;
        public ViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.imageViewImageCart);
            title = v.findViewById(R.id.textViewTitleCart);
            price = v.findViewById(R.id.textViewPriceCart);
            add = v.findViewById(R.id.addButton);
            rest = v.findViewById(R.id.restButton);
            countProduct = v.findViewById(R.id.countAmount);
            Context context = v.getContext();
            SessionManager sessionManager = new SessionManager(context);

            View card = v.findViewById(R.id.product_cart);
            card.setOnClickListener((View view) -> {
                int position = getAdapterPosition();
                Product product = products.get(position);
                List<String> categories = product.getCategories();
                List<String> materials = product.getMaterials();

                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("id_product", product.getId());
                intent.putExtra("title", product.getTitle());
                intent.putExtra("description", product.getDescription());
                intent.putExtra("stock", product.getStock());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("categories", String.join(", ", categories));
                intent.putExtra("materials", String.join(", ", materials));
                intent.putExtra("url_images", product.getUrl_image());
                context.startActivity(intent);

            });

        }
    }

    public interface CartRecyclerAdapterInterface{
        int sumCount(int pos);

        int restCount(int pos);


    }
}
