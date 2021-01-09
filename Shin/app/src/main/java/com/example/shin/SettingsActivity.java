package com.example.shin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shin.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView profilImage;
    private EditText PhoneText,AdresseText,NameText,EmailText;
    private TextView profileChange,closeTextBtn,saveTxtBtn;
    private ProgressBar progressBar;
    private Uri imageUrl;
    private String myUrl="";
    private StorageReference StorageProfilRef;
    private String checker="";
    private Button changePasswordBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileChange=(TextView)findViewById(R.id.profile_image_change_btn);
        changePasswordBtn=(Button) findViewById(R.id.change_password_btn);
        closeTextBtn=(TextView)findViewById(R.id.close_settings_btn);
        saveTxtBtn=(TextView)findViewById(R.id.update_account_settings_btn);
        PhoneText=(EditText) findViewById(R.id.settings_phone_number);
        EmailText=(EditText) findViewById(R.id.settings_email);
        AdresseText=(EditText) findViewById(R.id.settings_address);
        NameText=(EditText) findViewById(R.id.settings_full_name);
        profilImage=(CircleImageView) findViewById(R.id.settings_profile_image);
        progressBar=(ProgressBar) findViewById(R.id.settings_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        StorageProfilRef= FirebaseStorage.getInstance().getReference().child("Profile_pictures");

        userInfoDisplay(profilImage,NameText,PhoneText,AdresseText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                finish();
            }
        });

        saveTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        profileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                CropImage.activity(imageUrl)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,ChangePasswordActivity.class));
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK&&data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUrl=result.getUri();
            profilImage.setImageURI(imageUrl);
        }else
        {
            Toast.makeText(SettingsActivity.this,"Error try again", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }
    private void updateOnlyUserInfo()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object >hashMap=new HashMap();
        hashMap.put("name",NameText.getText().toString());
        hashMap.put("adresse",AdresseText.getText().toString());
        hashMap.put("phone",PhoneText.getText().toString());
        hashMap.put("email",EmailText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(hashMap);

        progressBar.setVisibility(View.INVISIBLE);


        Toast.makeText(SettingsActivity.this,"Informations saved",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(SettingsActivity.this,HomeActivity.class);
        startActivity(intent);
    }
    private void userInfoSaved() {
        if(TextUtils.isEmpty(NameText.getText().toString()))
        {
            Toast.makeText(SettingsActivity.this, "enter your name",Toast.LENGTH_LONG).show();

        }else if(TextUtils.isEmpty(PhoneText.getText().toString()))
        {
            Toast.makeText(SettingsActivity.this, "enter your phone number",Toast.LENGTH_LONG).show();

        }else if(TextUtils.isEmpty(AdresseText.getText().toString()))
        {
            Toast.makeText(SettingsActivity.this, "enter your adress",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(EmailText.getText().toString()))
        {
            Toast.makeText(SettingsActivity.this, "enter your email",Toast.LENGTH_LONG).show();
        }else
        {
            progressBar.setVisibility(View.VISIBLE);
            uploadImage();

        }
    }

    private void uploadImage() {
        if(imageUrl!=null)
        {
            final StorageReference fileRef=StorageProfilRef.child(Prevalent.currentOnlineUser.getPhone()+".jpg");
            StorageTask uploadTask=fileRef.putFile(imageUrl);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }
                    return fileRef.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingsActivity.this,"Image Uploaded successful",Toast.LENGTH_LONG).show();

                        Uri url=task.getResult();
                        myUrl=url.toString();
                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,Object> hashMap=new HashMap();
                        hashMap.put("name",NameText.getText().toString());
                        hashMap.put("adresse",AdresseText.getText().toString());
                        hashMap.put("phone",PhoneText.getText().toString());
                        hashMap.put("email",EmailText.getText().toString());
                        hashMap.put("image",myUrl);

                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(hashMap);

                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(SettingsActivity.this,"Informations saved",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(SettingsActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SettingsActivity.this,"Error",Toast.LENGTH_LONG).show();

                    }




                }
            });

        }else
        {
            Toast.makeText(SettingsActivity.this,"Image is not selected",Toast.LENGTH_LONG).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profilImage, EditText nameText, EditText phoneText, EditText adresseText) {
        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {


                        String name=dataSnapshot.child("name").getValue().toString();
                        String phone=dataSnapshot.child("phone").getValue().toString();


                        NameText.setText(name);
                        PhoneText.setText(phone);

                    if(dataSnapshot.child("image").exists())
                    {
                        String image=dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profilImage);
                    }
                    if(dataSnapshot.child("email").exists())
                    {
                        String email=dataSnapshot.child("email").getValue().toString();
                        EmailText.setText(email);
                    }
                    if(dataSnapshot.child("adresse").exists())
                    {
                        String adresse=dataSnapshot.child("adresse").getValue().toString();
                        AdresseText.setText(adresse);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}