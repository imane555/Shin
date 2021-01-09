package com.example.shin.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shin.ItemClickListener;
import com.example.shin.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView CategoryTxtName;
    public ImageView CategoryImage;
    public ItemClickListener itemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        CategoryTxtName=itemView.findViewById(R.id.category_name);
        CategoryImage=itemView.findViewById(R.id.category_icon);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
