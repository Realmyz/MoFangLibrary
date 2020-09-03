package com.example.mofangdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class QRCodeBarActivity extends AppCompatActivity {
    private ImageView mQRCodeImageView;
    private TextView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        String wifiap_name = i.getStringExtra("wifiap_name");
        String wifiap_pwd  = i.getStringExtra("wifiap_pwd" );

        mQRCodeImageView = (ImageView)findViewById(R.id.qrcode_image);

        String string = addLeft(wifiap_name);
        Log.d("MYZ","StringL = "+ string);
        string = addMid(string)+wifiap_pwd;
        Log.d("MYZ","StringM = "+ string);
        string = addRight(string);
        Log.d("MYZ","StringR = "+ string);

        mQRCodeImageView.setImageBitmap(createQRCodeBitmap(string, 600, 600));

    }

//    {"s":"Apical001_2.4G","p":"apicalgood"}


    private String addLeft(String s){
       return  "{\"s\":\"" + s;
    }

    private String addMid(String s){
        return s + "\""+","+"\""+"p"+"\""+":"+"\"";
    }

    private String addRight(String s){
        return s + "\""+"}";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static Bitmap createQRCodeBitmap(String content, int width, int height)
    {
        if (content == null || width < 0 || height < 0) {
            return null;
        }
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
