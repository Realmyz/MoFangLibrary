package com.example.mofangdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class WifiActivity extends AppCompatActivity {

    private ImageView wifi_change;
    private ImageView wifi_visual;
    private EditText wifi_name;
    private EditText wifi_password;
    private TextView btn_back;
    private Button btn_next;
    private String mSsid;
    private SharedPreferences sp;
    private String value;
    private static final int LOCATION_CODE = 3;
    //修改密码是否可见的状态
    private boolean isPwdVisible = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        wifi_change = findViewById(R.id.wifi_change);
        btn_next = findViewById(R.id.btn_next);
        wifi_name = findViewById(R.id.wifi_name);
        wifi_password = findViewById(R.id.wifi_password);
        btn_back = findViewById(R.id.btn_back);
        wifi_visual = findViewById(R.id.visual);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_background));


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WifiActivity.this, QRCodeBarActivity.class);
                i.putExtra("wifiap_name", mSsid);

                if (value.equals("")) {
                    sp = getSharedPreferences("MoFang", Context.MODE_PRIVATE);
                    Log.d("MYZ", "value null = " + value);
                    i.putExtra("wifiap_pwd", wifi_password.getText().toString());
                    SharedPreferences.Editor edit = sp.edit();
                    //通过editor对象写入数据
                    edit.putString("password", wifi_password.getText().toString());
                    //提交数据存入到xml文件中
                    edit.apply();
                } else {
                    sp = getSharedPreferences("MoFang", Context.MODE_PRIVATE);
                    Log.d("MYZ", "value 已存在上次密码 = " + value);
                    i.putExtra("wifiap_pwd", wifi_password.getText().toString());
                    SharedPreferences.Editor edit = sp.edit();
                    //通过editor对象写入数据
                    edit.putString("password", wifi_password.getText().toString());
                    //提交数据存入到xml文件中
                    edit.apply();
                }

                startActivity(i);
            }
        });

        wifi_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                startActivity(wifiSettingsIntent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        wifi_visual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPwdVisible = !isPwdVisible;
                //設置密碼是否可見
                if (isPwdVisible) {
                    //设置密码为明文，并更改眼睛图标//
                    wifi_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    wifi_visual.setImageResource(R.drawable.wifi_visable);
                } else {
                    //设置密码为暗文，并更改眼睛图标
                    wifi_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    wifi_visual.setImageResource(R.drawable.wifi_invisable);
                }
                //设置光标位置的代码需放在设置明暗文的代码后面
                wifi_password.setSelection(wifi_password.getText().toString().length());
                wifi_visual.setBackgroundResource(R.drawable.wifi_invisable);
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        RequestPermission();

        wifi_name.setText(mSsid);
        wifi_password.setText(value);
        wifi_password.setSelection(wifi_password.getText().toString().length());
    }

    private boolean isWifiOpened() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    private boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        assert wifiNetworkInfo != null;
        return wifiNetworkInfo.isConnected();
    }

    private void showWifiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Wi-Fi未打开，是否开启？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                toggleWiFi();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void toggleWiFi() {
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        startActivity(wifiSettingsIntent);
    }

    private String getWIFISSID() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID", wifiInfo.getSSID());
        return wifiInfo.getSSID().replace("\"", "");
    }

    private void RequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //申请定位权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
        } else {
            if (isWifiOpened()) {
                Log.d("MYZ", "wifi已打开");
                if (isWifiConnected()) {
                    mSsid = getWIFISSID();
                    Log.d("MYZ", "ssid = " + mSsid);
                    sp = getSharedPreferences("MoFang", Context.MODE_PRIVATE);
                    value = sp.getString("password", "");
                    assert value != null;
                    if (!value.equals("")) {
                        Log.d("MYZ", "value 权限已打开情况下 = " + value);
                    }
                }
            } else {
                Log.d("MYZ", "wifi未打开");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showWifiDialog();
                    }
                }, 1000);

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MYZ", "requestcode = " + requestCode);
        if (requestCode == LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                // 权限被用户同意。
                Toast.makeText(this, "定位权限已打开！", Toast.LENGTH_LONG).show();
                if (isWifiOpened()) {
                    Log.d("MYZ", "wifi已打开");
                    if (isWifiConnected()) {
                        mSsid = getWIFISSID();
                        Log.d("MYZ", "ssid = " + mSsid);
                        sp = getSharedPreferences("MoFang", Context.MODE_PRIVATE);
                        value = sp.getString("password", "");
                    }
                } else {
                    Log.d("MYZ", "wifi未打开");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showWifiDialog();
                        }
                    }, 1000);

                }
            } else {
                // 权限被用户拒绝了。
                Toast.makeText(this, "定位权限被禁止，无法自动获取ssid", Toast.LENGTH_LONG).show();
                if (isWifiOpened()) {
                    Log.d("MYZ", "wifi已打开");
                    if (isWifiConnected()) {
                        mSsid = getWIFISSID();
                        Log.d("MYZ", "ssid = " + mSsid);
                        sp = getSharedPreferences("MoFang", Context.MODE_PRIVATE);
                        value = sp.getString("password", "");
                    }
                } else {
                    Log.d("MYZ", "wifi未打开");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showWifiDialog();
                        }
                    }, 1000);

                }
            }
        }

    }



}