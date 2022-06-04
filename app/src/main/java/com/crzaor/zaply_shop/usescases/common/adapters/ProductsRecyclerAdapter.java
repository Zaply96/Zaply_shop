package com.crzaor.zaply_shop.usescases.common.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crzaor.zaply_shop.R;
import com.crzaor.zaply_shop.model.Product;
import com.crzaor.zaply_shop.util.SessionManager;
import com.crzaor.zaply_shop.usescases.product.ProductActivity;
import com.crzaor.zaply_shop.provider.DBController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {

    private static final DBController dbController = new DBController();
    private static List<Product> products = new ArrayList<>();
    private static List<Integer> favorites;
    private static List<Integer> card_products;
    public ProductsRecyclerAdapter(List<Integer> fav, List<Integer> card_p){
        favorites = fav;
        card_products = card_p;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_card, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        String imageurl = (String)product.getUrl_image();
        Picasso.get()
                .load(imageurl)
                .into(holder.image);
        holder.title.setText(product.getTitle());
        holder.price.setText(String.valueOf(product.getPrice()));
        int id = (Integer) product.getId();

        if(favorites.contains(id)){
            holder.like.setImageResource(R.drawable.liked_icon);
        }

        if(card_products.contains(id)){
            holder.card.setImageResource(R.drawable.cart_icon);
        }

    }

    @Override
    public int getItemCount() { return products.size();}

    public void setProducts(List<Product> Nproducts){
        products = Nproducts;
        notifyDataSetChanged();
    }
    public void setFavorites(List<Integer> favorite_products){
        favorites = favorite_products;
        notifyDataSetChanged();
    }
    public void setCard_products(List<Integer> Ncard_products){
        card_products = Ncard_products;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,price,description,stock,categories,materials;
        ImageView image;
        ImageButton like, card;
        public ViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.imageViewImage);
            title = v.findViewById(R.id.textViewTitle);
            price = v.findViewById(R.id.textViewPrice);
            like = v.findViewById(R.id.imageButtonLike);
            card = v.findViewById(R.id.imageButtonCart);

            Context context = v.getContext();
            SessionManager sessionManager = new SessionManager(context);

            like.setOnClickListener(view ->{
                int position = getAdapterPosition();
                if(favorites.contains((Integer) products.get(position).getId())){
                    like.setImageResource(R.drawable.unliked_icon);
                    favorites.removeAll(Arrays.asList((Integer) products.get(position).getId()));
                }else {
                    like.setImageResource(R.drawable.liked_icon);
                    favorites.add((Integer) products.get(position).getId());
                }

                dbController.updateFavorites(sessionManager.getUserDetail().get("USER_ID"), (ArrayList<Integer>) favorites);
            });

            card.setOnClickListener(view ->{
                int position = getAdapterPosition();
                if(card_products.contains((Integer) products.get(position).getId())){
                    card.setImageResource(R.drawable.add_cart_icon);
                    card_products.removeAll(Arrays.asList((Integer) products.get(position).getId()));
                }else {
                    card.setImageResource(R.drawable.cart_icon);
                    card_products.add((Integer) products.get(position).getId());
                }
                dbController.updateCard(sessionManager.getUserDetail().get("USER_ID"), (ArrayList<Integer>) card_products);
            });

            View card = v.findViewById(R.id.product_card);
            card.setOnClickListener((View view) -> {
                int position = getAdapterPosition();
                List<String> categories =products.get(position).getCategories();
                List<String> materials = products.get(position).getMaterials();

                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("id_product", (Integer) products.get(position).getId());
                intent.putExtra("title", products.get(position).getTitle().toString());
                intent.putExtra("description", products.get(position).getDescription());
                intent.putExtra("stock", (Integer)products.get(position).getStock());
                intent.putExtra("price", (Double) products.get(position).getPrice());
                intent.putExtra("categories", String.join(", ", categories));
                intent.putExtra("materials", String.join(", ", materials));
                intent.putExtra("url_images", products.get(position).getUrl_image());
                context.startActivity(intent);

            });



        }
    }
}
