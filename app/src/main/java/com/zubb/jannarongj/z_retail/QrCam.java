package com.zubb.jannarongj.z_retail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;



public class QrCam extends AppCompatActivity {

    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qr_cam);


        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.startCamera();
        zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {

                zXingScannerView.stopCamera();
                zXingScannerView.removeAllViews();

                Intent intent = new Intent();
                intent.putExtra("getQR", result.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }

        });
    }



    public void onBackPressed() {
        zXingScannerView.stopCamera();
        super.onBackPressed();
    }

}
