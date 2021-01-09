package com.example.shin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shin.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {
private Button changeBtn;
private EditText passText,pass2txt;
private String password="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        changeBtn=(Button)findViewById(R.id.change_pass_btn);
        passText=(EditText)findViewById(R.id.pass1);
        pass2txt=(EditText)findViewById(R.id.pass2);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty((passText.getText().toString())))
                {
                    Toast.makeText(ChangePasswordActivity.this," password is required",Toast.LENGTH_SHORT).toString();
                }else if(TextUtils.isEmpty((pass2txt.getText().toString())))
                {
                    Toast.makeText(ChangePasswordActivity.this," password is required",Toast.LENGTH_SHORT).toString();
                }else if(!pass2txt.getText().toString().equals(passText.getText().toString()))
                {
                    Toast.makeText(ChangePasswordActivity.this," the two passwords should be the same",Toast.LENGTH_SHORT).toString();
                }else
                {
                    password=pass2txt.getText().toString();
                    final DatabaseReference userRef;
                    userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                    HashMap<String,Object> userdataMap= new HashMap<>();
                    userdataMap.put("password",password);
                    userRef.updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ChangePasswordActivity.this," Your password is updated",Toast.LENGTH_SHORT).toString();
                                Intent intent=new Intent(ChangePasswordActivity.this,SettingsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }

            }
        });
    }
}
