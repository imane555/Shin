package com.example.shin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.shin.Admin.AdminMaintainProductActivity;
import com.example.shin.Model.Products;
import com.example.shin.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {

    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private DatabaseReference ProductsRef;
    private String searchText="",type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        inputText=(EditText) findViewById(R.id.search_product_name);
        searchBtn=(Button) findViewById(R.id.search_btn);
        searchList=(RecyclerView)findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Intent intent=getIntent();
        type=intent.getStringExtra("Admin");
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText=inputText.getText().toString();

                onStart();
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef.orderByChild("pname").startAt(searchText),Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, final int i, @NonNull final Products products) {
                holder.name.setText(products.getPname());
                holder.price.setText(products.getPrice());
                Picasso.get().load(products.getImage()).into(holder.image);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(type.equals("yes"))
                        {
                            Intent intent=new Intent(SearchProductsActivity.this, AdminMaintainProductActivity.class);
                            intent.putExtra("pid",products.getPid().trim());
                            startActivity(intent);
                        }else
                        {
                            Intent intent=new Intent(SearchProductsActivity.this,ProductDetailsActivity.class);
                            intent.putExtra("pid",products.getPid().trim());
                            startActivity(intent);
                        }


                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}