package com.example.shin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {
    private Button login,Join_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Join_us = (Button)findViewById(R.id.main_join_now_btn);
        login=(Button) findViewById(R.id.main_login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Main2Activity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        Join_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Main2Activity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
