package com.zubb.jannarongj.z_retail;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class Unlock extends AppCompatActivity {
    UserHelper usrHelper ;
    Button back_btn ;
    String g_vbeln ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock2);
        usrHelper = new UserHelper(this);
        back_btn = (Button)findViewById(R.id.button3) ;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                g_vbeln = null ;

            } else {
                g_vbeln = extras.getString("vbeln").trim();

            }
        } else {
            g_vbeln = (String) savedInstanceState.getSerializable("vbeln");

        }


        WebView webView = (WebView) findViewById(R.id.wv_unlock);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String url = "http:\\report.zubbsteel.com/ship/unlock.php?s="+g_vbeln+"&u="+usrHelper.getUserName();

        if(usrHelper.getPlant().equals("MMT")){
            url = "http:\\199.0.0.16/ship/unlock.php?s="+g_vbeln+"&u="+usrHelper.getUserName();
        }


        webView.loadUrl(url);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Unlock.this, ar_list.class);
                i.putExtra("vbeln", g_vbeln);
                startActivity(i);
            }
        });

    }

}
