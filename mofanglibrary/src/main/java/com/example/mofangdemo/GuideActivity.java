package com.example.mofangdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox checkBox;
    private Button btn_next;
    private TextView btn_mode;
    private PopupWindow mPopWindow;
    private TextView btn_back;
    private boolean isOpened = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        btn_back = findViewById(R.id.btn_back);
        checkBox = findViewById(R.id.btn_agree);
        btn_next = findViewById(R.id.btn_next);
        btn_mode = findViewById(R.id.internet_mode);

        btn_next.setEnabled(false);
        btn_next.setBackgroundResource(R.drawable.button_corner_grey);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_background));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    btn_next.setBackgroundResource(R.drawable.button_corner);
                    btn_next.setEnabled(true);
                } else {
                    btn_next.setEnabled(false);
                    btn_next.setBackgroundResource(R.drawable.button_corner_grey);
                }

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuideActivity.this, WifiActivity.class));
            }
        });

        btn_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOpened) {
                    isOpened = true;
                    showPopupWindow();
                }else {
                    mPopWindow.dismiss();
                    isOpened = !isOpened;
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void showPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv1 = (TextView) contentView.findViewById(R.id.text1);
        TextView tv2 = (TextView) contentView.findViewById(R.id.text2);
        TextView tv3 = (TextView) contentView.findViewById(R.id.text3);
        TextView tv4 = (TextView) contentView.findViewById(R.id.text4);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);

        mPopWindow.showAsDropDown(btn_mode);
//        mPopWindow.showAsDropDown(btn_mode,Gravity.BOTTOM,30,30);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.text1) {
            Toast.makeText(this, "二维码配网", Toast.LENGTH_SHORT).show();
            mPopWindow.dismiss();
            isOpened = false;
        } else if (id == R.id.text2) {
            Toast.makeText(this, "热点配网（兼容模式）", Toast.LENGTH_SHORT).show();
            mPopWindow.dismiss();
            isOpened = false;
        } else if (id == R.id.text3) {
            Toast.makeText(this, "Wi-Fi快连", Toast.LENGTH_SHORT).show();
            mPopWindow.dismiss();
            isOpened = false;
        } else if (id == R.id.text4) {
            Toast.makeText(this, "有线配网", Toast.LENGTH_SHORT).show();
            mPopWindow.dismiss();
            isOpened = false;
        }
    }

}