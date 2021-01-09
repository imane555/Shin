package com.example.shin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.shin.Admin.AdminAddNewProductActivity;
import com.example.shin.Admin.AdminCategoryActivity;

public class CategoriesActivity extends AppCompatActivity {
    private ImageView tShirts,femaleDresses;
    private  ImageView walletsBagsPurses,shoes;
    private  ImageView headPhoneHandfree,laptops,watches,mobilePhones;
private String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        tShirts=(ImageView)findViewById(R.id.male_dresses);
        femaleDresses=(ImageView)findViewById(R.id.female_dresses);
        walletsBagsPurses=(ImageView)findViewById(R.id.purses_bags_wallets);
        shoes=(ImageView)findViewById(R.id.shoes);
        headPhoneHandfree=(ImageView)findViewById(R.id.headphones_handdfree);
        laptops=(ImageView)findViewById(R.id.laptop_pc);
        watches=(ImageView)findViewById(R.id.watches);
        mobilePhones=(ImageView)findViewById(R.id.mobilephones);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            type=getIntent().getStringExtra("Admin");
        }

        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this, getCategories.class);
                intent.putExtra("category","Male Dresses");
                if(type.equals("Admin"))
                {
                    intent.putExtra("Admin","Admin");
                }
                startActivity(intent);

            }
        });

        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this,getCategories.class);
                intent.putExtra("category","Female Dresses");
                if(type.equals("Admin"))
                {
                    intent.putExtra("Admin","Admin");
                }
                startActivity(intent);

            }
        });

        walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this,getCategories.class);
                intent.putExtra("category","Wallets Bags Purses");
                if(type.equals("Admin"))
                {
                    intent.putExtra("Admin","Admin");
                }
                startActivity(intent);

            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this,getCategories.class);
                intent.putExtra("category","Shoes");
                if(type.equals("Admin"))
                {
                    intent.putExtra("Admin","Admin");
                }
                startActivity(intent);

            }
        });
        headPhoneHandfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this,getCategories.class);
                intent.putExtra("category","HeadPhone Handfree");
                if(type.equals("Admin"))
                {
                    intent.putExtra("Admin","Admin");
                }
                startActivity(intent);

            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Watches");
                if(type.equals("Admin"))
                {
                    intent.putExtra("Admin","Admin");
                }
                startActivity(intent);

            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this,getCategories.class);
                intent.putExtra("category","Laptops");
                if(type.equals("Admin"))
                {
                    intent.putExtra("Admin","Admin");
                }
                startActivity(intent);

            }
        });
        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this,getCategories.class);
                intent.putExtra("category","Mobile Phones");
                if(type.equals("Admin"))
                {
                    intent.putExtra("Admin","Admin");
                }
                startActivity(intent);

            }
        });
    }
}
