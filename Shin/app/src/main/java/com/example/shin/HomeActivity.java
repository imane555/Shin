package com.example.shin;

import android.content.Intent;
import android.os.Bundle;

import com.example.shin.Admin.AdminMaintainProductActivity;
import com.example.shin.Model.Category;
import com.example.shin.Model.Products;
import com.example.shin.Prevalent.Prevalent;
import com.example.shin.ViewHolder.CategoryAdapter;
import com.example.shin.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private RecyclerView recyclerView;

    private ProgressBar mProgressCircle;

    private DatabaseReference ProductsRef;
    private RecyclerView CategoryRecycleView;

    private String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            type=getIntent().getStringExtra("Admin");
        }

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!type.equals("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }


            }
        });


         drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        TextView username=header.findViewById(R.id.user_profile_name);
        CircleImageView userimage=header.findViewById(R.id.user_profile_image);

        if(!type.equals("Admin"))
        {
            username.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).into(userimage);
        }

        CategoryRecycleView = findViewById(R.id.recycle_category);
        CategoryRecycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        CategoryRecycleView.setLayoutManager(linearLayoutManager);

        final List<Category> categoryList= new ArrayList<Category>();
        categoryList.add(new Category(R.drawable.home,"Home"));
        categoryList.add(new Category(R.drawable.mobiles,"Mobile Phones"));
        categoryList.add(new Category(R.drawable.laptops,"Laptops"));
        categoryList.add(new Category(R.drawable.headphoness,"HeadPhones"));
        categoryList.add(new Category(R.drawable.female_dresses,"Dresses"));
        categoryList.add(new Category(R.drawable.tshirts,"T-Shirts"));
        categoryList.add(new Category(R.drawable.watches,"Watches"));
        categoryList.add(new Category(R.drawable.shoess,"Shoes"));
        categoryList.add(new Category(R.drawable.purses_bags,"Bags"));
        CategoryAdapter adapter=new CategoryAdapter(categoryList,HomeActivity.this);
        CategoryRecycleView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef.orderByValue(),Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, final int i, @NonNull final Products products) {
                holder.name.setText(products.getPname());
                holder.price.setText(products.getPrice());
                Picasso.get().load(products.getImage()).into(holder.image);
                mProgressCircle.setVisibility(View.INVISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(type.equals("Admin"))
                        {
                            Intent intent=new Intent(HomeActivity.this, AdminMaintainProductActivity.class);
                            intent.putExtra("pid",products.getPid().trim());
                            startActivity(intent);
                        }else
                        {
                            Intent intent=new Intent(HomeActivity.this,ProductDetailsActivity.class);
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
    if (id==R.id.nav_cart)
    {
        if(!type.equals("Admin")) {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        }

    }else if (id==R.id.nav_favoris)
    {
        if(!type.equals("Admin")) {
            Intent intent = new Intent(HomeActivity.this, WishListActivity.class);
            startActivity(intent);
        }

    }
    else if (id==R.id.nav_search)
    {
        if(!type.equals("Admin")) {
            Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
            intent.putExtra("Admin","no");
            startActivity(intent);
        }else
        {
            Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
            intent.putExtra("Admin","yes");
            startActivity(intent);
        }


    }else if (id==R.id.nav_categories)
    {

            Intent intent=new Intent(HomeActivity.this,CategoriesActivity.class);
        if(type.equals("Admin")) {
            intent.putExtra("Admin","Admin");}
            startActivity(intent);



    }else if (id==R.id.nav_setting)
    {
        if(!type.equals("Admin")) {
            Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
        }

    }else if (id==R.id.nav_logout)
    {
        Paper.book().destroy();
        Intent intent=new Intent(HomeActivity.this,Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}
