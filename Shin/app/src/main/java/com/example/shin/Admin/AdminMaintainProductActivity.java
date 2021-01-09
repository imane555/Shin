package com.example.shin.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shin.HomeActivity;
import com.example.shin.Model.Products;
import com.example.shin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {
    private Button ApplyChangesBtn,deleteBtn;
    private ImageView image;
    private EditText name,description,price;
    private DatabaseReference ProductsRef;
    private String productId="";
    private Uri ImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);

        productId=getIntent().getStringExtra("pid");

        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        ApplyChangesBtn =findViewById(R.id.apply_changes_btn);
        price =findViewById(R.id.product_price_maintain);
        description =findViewById(R.id.product_description_maintain);
        name =findViewById(R.id.product_name_maintain);
        image =findViewById(R.id.product_image_maintain);
        deleteBtn =findViewById(R.id.delete_product_btn);


        displayProductInfo();

        ApplyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Pname=name.getText().toString();
                final String Pdescription=description.getText().toString();
                final String Pprice=price.getText().toString();
                if(Pdescription.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter the product description",Toast.LENGTH_LONG).show();
                }else if(Pname.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter the product name",Toast.LENGTH_LONG).show();
                }else if(Pprice.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter the product price",Toast.LENGTH_LONG).show();
                }else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("pid",productId);
                    hashMap.put("pname",Pname);
                    hashMap.put("description",Pdescription);
                    hashMap.put("price",Pprice);

                    ProductsRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(AdminMaintainProductActivity.this,"update informations successfully",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(AdminMaintainProductActivity.this, HomeActivity.class);
                                intent.putExtra("Admin","Admin");
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AdminMaintainProductActivity.this,"delete product successfully",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(AdminMaintainProductActivity.this, HomeActivity.class);
                            intent.putExtra("Admin","Admin");
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null)
        {
            ImageUrl=data.getData();
            image.setImageURI(ImageUrl);
        }
    }

    private void displayProductInfo() {
        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String Pname =dataSnapshot.child("pname").getValue().toString();
                    String Pdescription =dataSnapshot.child("description").getValue().toString();
                    String Pprice =dataSnapshot.child("price").getValue().toString();
                    String Pimage=dataSnapshot.child("image").getValue().toString();

                    name.setText(Pname);
                    description.setText(Pdescription);
                    price.setText(Pprice);
                    Picasso.get().load(Pimage).into(image);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
