package com.example.shin.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shin.ItemClickListener;
import com.example.shin.Model.Products;
import com.example.shin.R;

import java.util.List;


    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView price;
        public ImageView image;
        public ItemClickListener itemListener;

        public ProductViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            image = itemView.findViewById(R.id.product_image);
            price = itemView.findViewById(R.id.product_price);
        }
    public void setItemClickListener(ItemClickListener listener)
    {
        itemListener=listener;
    }
        @Override
        public void onClick(View v) {
            itemListener.OnClick(v,getAdapterPosition(),false);

        }
    }
