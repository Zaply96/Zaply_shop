package com.crzaor.zaply_shop.usescases.order;

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
import com.crzaor.zaply_shop.usescases.product.ProductActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {

    private static List<Product> products;
    public OrderRecyclerAdapter(List<Product> products){
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_order_card, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageurl = (String)products.get(position).getUrl_image();
        Picasso.get()
                .load(imageurl)
                .into(holder.image);
        holder.count.setText("x"+products.get(position).getCount());
        int id = (Integer) products.get(position).getId();

    }

    @Override
    public int getItemCount() { return products.size();}

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView count;
        ImageView image;
        public ViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.imageProductOrder);
            count = v.findViewById(R.id.countProductOrder);

            Context context = v.getContext();

            View card = v.findViewById(R.id.productOrderCard);
            card.setOnClickListener((View view) -> {
                int position = getAdapterPosition();
                List<String> categories = products.get(position).getCategories();
                List<String> materials = products.get(position).getMaterials();

                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("id_product", (Integer) products.get(position).getId());
                intent.putExtra("title", products.get(position).getTitle());
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
