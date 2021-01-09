package com.example.shin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shin.Admin.AdminCategoryActivity;
import com.example.shin.Model.Users;
import com.example.shin.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPassword,InputPhoneNumber;
    private Button LoginButton;
    private  ProgressDialog loadingBar;
    private  String parentDbName="Users";
     private TextView AdminLink,NotAdminLink,ForgetPassLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton =(Button) findViewById(R.id.login_btn);
        InputPhoneNumber =(EditText) findViewById(R.id.login_phone_number_input);
        InputPassword =(EditText) findViewById(R.id.login_password_input);
        AdminLink=(TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);
        //ForgetPassLink=(TextView)findViewById(R.id.forget_password_link);
        loadingBar=(new ProgressDialog(this));

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();

            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";

            }

        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });
/*
        ForgetPassLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
                finish();
            }
        });*/



    }

    private void LoginUser()
    {
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"write your password please",Toast.LENGTH_SHORT).show();

        }
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"write your phone number please",Toast.LENGTH_SHORT).show();

        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait,while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            AllowAccessToAccount(phone,password);


        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData= dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                           if(parentDbName.equals("Admins"))
                           {
                              Toast.makeText(LoginActivity.this,"welcome admin , you are logged in..",Toast.LENGTH_SHORT).show();

                               loadingBar.dismiss();
                               Intent intent=new Intent(LoginActivity.this, AdminCategoryActivity.class);
                               startActivity(intent);
                               finish();

                           }
                           else if(parentDbName.equals("Users")){
                               Toast.makeText(LoginActivity.this,"logged in ..",Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();
                               Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                               Prevalent.currentOnlineUser=usersData;
                               startActivity(intent);
                               finish();

                           }
                        }
                        if (!(usersData.getPassword().equals(password))) {
                        Toast.makeText(LoginActivity.this, "incorrect password , try again", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(intent);}



                    }
                }




                else
                {
                    Toast.makeText(LoginActivity.this,"account with this "+phone+" number do not exist ",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
