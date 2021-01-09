package com.example.shin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shin.Model.Products;
import com.example.shin.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartBtn;
    private ImageView productImage,addFavoris;
    private ElegantNumberButton numberButton;
    private TextView productName,productDescription,productPrice;
    private String ProductId=" ";
    private String imageUrl;
    private int i=0;
    DatabaseReference WishRef;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ProductId=getIntent().getStringExtra("pid");

        addToCartBtn=(Button)findViewById(R.id.pd_add_to_cart_button);
        productImage=(ImageView)findViewById(R.id.product_image_details);
        productDescription=(TextView)findViewById(R.id.product_description_details);
        productName=(TextView)findViewById(R.id.product_name_details);
        productPrice=(TextView)findViewById(R.id.product_price_details);
        numberButton=(ElegantNumberButton)findViewById(R.id.number_btn);
        addFavoris=(ImageView)findViewById(R.id.add_favoris);
         WishRef=FirebaseDatabase.getInstance().getReference().child("WishList").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(ProductId);
        WishRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    addFavoris.setImageResource(R.mipmap.ic_favoris);

                } else {
                    addFavoris.setImageResource(R.mipmap.ic_add_favoris);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        addFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingFavoris(ProductId);
            }

        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });

        getProductDetails(ProductId);
    }
    private void addingFavoris(final String ProductId)
    {
        final DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        i++;
        WishRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    addFavoris.setImageResource(R.mipmap.ic_add_favoris);
                    WishRef.removeValue();

                }else {
                    addFavoris.setImageResource(R.mipmap.ic_favoris);

                    productRef.child(ProductId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Products products = dataSnapshot.getValue(Products.class);
                                WishRef.setValue(products);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    private void addingToCartList()
    {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime=currentTime.format(calendar.getTime());

        DatabaseReference CartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("pid",ProductId);
        hashMap.put("pname",productName.getText().toString());
        hashMap.put("price",productPrice.getText().toString());
        hashMap.put("date",saveCurrentDate);
        hashMap.put("time",saveCurrentTime);
        hashMap.put("quantity",numberButton.getNumber());
        hashMap.put("discount","");
        hashMap.put("pimage",imageUrl);
        hashMap.put("description",productDescription.getText().toString());


        CartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(ProductId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ProductDetailsActivity.this,"Product added to cart",Toast.LENGTH_LONG).show();


                }
            }
        });
        CartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(ProductId).updateChildren(hashMap);


    }

    private void getProductDetails(final String productId) {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Products products=dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productDescription.setText("Description :\n"+products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);
                    imageUrl=products.getImage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
