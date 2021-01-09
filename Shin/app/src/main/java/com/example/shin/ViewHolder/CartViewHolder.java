package com.example.shin.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shin.ItemClickListener;
import com.example.shin.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductPrice,txtProductQuality,txtProductDescription;
    public ItemClickListener itemClickListener;
    public ImageView productImage;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuality=itemView.findViewById(R.id.cart_product_quantity);
        txtProductDescription=itemView.findViewById(R.id.cart_product_description);
        productImage=itemView.findViewById(R.id.cart_product_image);

    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.OnClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
