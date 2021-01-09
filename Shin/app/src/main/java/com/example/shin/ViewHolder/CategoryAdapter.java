package com.example.shin.ViewHolder;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shin.Admin.AdminMaintainProductActivity;
import com.example.shin.HomeActivity;
import com.example.shin.ItemClickListener;
import com.example.shin.Model.Category;
import com.example.shin.R;
import com.example.shin.getCategories;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>
{
    public List<Category> categoryList;
    public ItemClickListener itemView;
    public Context context;

    public CategoryAdapter(List<Category> categoryList,Context context) {
        this.categoryList = categoryList;
        this.context=context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        CategoryViewHolder holder=new CategoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
        final String name=categoryList.get(position).getCategoryName();
        int image=categoryList.get(position).getCategoryIcon();
        holder.CategoryTxtName.setText(name);
        Picasso.get().load(image).into(holder.CategoryImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, getCategories.class);
                intent.putExtra("category",name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
