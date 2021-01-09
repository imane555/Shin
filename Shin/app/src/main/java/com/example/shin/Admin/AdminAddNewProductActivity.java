package com.example.shin.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.shin.Model.Products;
import com.example.shin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName;
    private Button AddNewProductButton,ChooseImage;
    private ImageView InputProductImage;
    private static final int gallerypick=1;
    private EditText InputProductName,InputProductDescription,InputProductPrice;
    private String description,price,name,saveCurrentDate,saveCurrentTime,productRandomKey,DownloadImageUrl;
    private Uri ImageUri;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressBar loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName=getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef=FirebaseDatabase.getInstance().getReference().child("Products");
        AddNewProductButton=(Button)findViewById(R.id.add_new_product);
        ChooseImage=(Button)findViewById(R.id.select_product_image);
        InputProductImage=(ImageView) findViewById(R.id.image_view);
        InputProductName=(EditText)findViewById(R.id.product_name);
        InputProductPrice=(EditText)findViewById(R.id.product_price);
        InputProductDescription=(EditText) findViewById(R.id.product_description);
        loadingBar=(ProgressBar)findViewById(R.id.progress_bar);


        ChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,gallerypick);
            }
        });

    AddNewProductButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ValidateProductData();


                    }

            }
    );





    }

    private void ValidateProductData() {
        description=InputProductDescription.getText().toString();
        name=InputProductName.getText().toString();
        price=InputProductPrice.getText().toString();
        if(ImageUri==null)
        {
            Toast.makeText(getApplicationContext(),"Please enter the product image",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(description))
        {
            Toast.makeText(getApplicationContext(),"Please enter the product description",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(getApplicationContext(),"Please enter the product name",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(getApplicationContext(),"Please enter the product price",Toast.LENGTH_LONG).show();
        }else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate+saveCurrentTime;

        final StorageReference filePath=ProductImagesRef.child(ImageUri.getLastPathSegment()+productRandomKey+".jpg");
         StorageTask uploadTask=filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.getMessage();
                Toast.makeText(AdminAddNewProductActivity.this,"Error:"+message,Toast.LENGTH_LONG);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Image Uploaded successful",Toast.LENGTH_LONG).show();
                loadingBar.setProgress(0);
                DownloadImageUrl=taskSnapshot.getDownloadUrl().toString();
                String uploadId = ProductsRef.push().getKey();
                Products product=new Products(uploadId,saveCurrentDate,saveCurrentTime,description,DownloadImageUrl,CategoryName,price,name);
                ProductsRef.child(uploadId).setValue(product);

                Toast.makeText(AdminAddNewProductActivity.this,"Product added successfully",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                startActivity(intent);

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                loadingBar.setProgress((int) progress);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==gallerypick&&resultCode==RESULT_OK&&data!=null)
        {
             ImageUri=data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }
}

