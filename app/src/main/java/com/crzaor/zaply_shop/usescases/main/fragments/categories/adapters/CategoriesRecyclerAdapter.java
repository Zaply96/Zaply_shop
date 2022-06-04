package com.crzaor.zaply_shop.usescases.main.fragments.categories.adapters;

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
import com.crzaor.zaply_shop.usescases.category_products.CategoryProducts;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder> {


    List<Map<String,String>> categories;
    public CategoriesRecyclerAdapter(List<Map<String,String>> categories){
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_card, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageurl = categories.get(position).get("url_image");
        Picasso.get()
                .load(imageurl)
                .into(holder.image);
        holder.name.setText(categories.get(position).get("name").toString());

    }

    @Override
    public int getItemCount() {return categories.size();}


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageViewCategory);
            name = itemView.findViewById(R.id.textViewNameCategory);

            View card = itemView.findViewById(R.id.cardViewCategory);
            card.setOnClickListener(v ->{
                Context context = v.getContext();

                Intent intent = new Intent(context, CategoryProducts.class);
                intent.putExtra("category",name.getText().toString());
                context.startActivity(intent);
            });

        }
    }
}
