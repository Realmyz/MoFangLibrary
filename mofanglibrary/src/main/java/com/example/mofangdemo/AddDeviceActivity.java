package com.example.mofangdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class AddDeviceActivity extends AppCompatActivity {

    private Button btn_add_device1;
    private Button btn_add_device2;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);


        btn_add_device1 = findViewById(R.id.btn_add_device1);
        btn_add_device2 = findViewById(R.id.btn_add_device2);

        btn_add_device1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddDeviceActivity.this, GuideActivity.class));
            }
        });

        btn_add_device2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddDeviceActivity.this, GuideActivity.class));
            }
        });

    }



}