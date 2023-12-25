package com.example.myapplication.finalMobile.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.finalMobile.Model.Category;
import com.example.myapplication.finalMobile.Utils.Network.GetImageFromUrl;
import com.example.myapplication.finalMobile.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    public void setData(List<Category> categories){
        this.categories =  categories;
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);

        TextView textView = holder.title;
        textView.setText(category.getTitle());

        ImageView image = holder.picture;
        if (category.getUrl() == null || category.getUrl().isEmpty()) {
            image.setImageResource(R.drawable.cat1);
        } else {
            Log.i(CategoryAdapter.class.getSimpleName(), category.getUrl().toString().trim());
//            new GetImageFromUrl(image).execute(category.getUrl());
            Glide.with(holder.itemView).load(category.getUrl()).into(image);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = (ImageView) itemView.findViewById(R.id.imageCategory);
            title = (TextView) itemView.findViewById(R.id.titleCategory);

        }
    }
}
