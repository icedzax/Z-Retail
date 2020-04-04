package com.zubb.jannarongj.z_retail;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Stock extends AppCompatActivity {

    ConnectionClass connectionClass;
    UserHelper usrHelper;
    ProgressBar pbbar;
    List<Map<String, String>> stocklist  = new ArrayList<Map<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);


        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();


    }

    public  void  onLocationClick(View v){

        final Dialog dialog = new Dialog(Stock.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


    }

    public class FetchStock extends AsyncTask<String, String, String> {

        Boolean isSuccess =false;
        String z = "";
        String Fmat = "";


        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {


            pbbar.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String plant ="";
                    switch (params[1]){
                        case "ZUBB" : plant = " and werks in ('1010','9010')";
                            break;
                        case "SPN" : plant = " and werks in ('1050','9050')";
                            break;
                        case "SPS" : plant = " and werks in ('1040','9040')";
                            break;
                        case "OPS" : plant = " and werks in ('2010','9060','1020','9020')";
                            break;
                    }

                    //todo MAT CODE char at "3" F,M fix to wildcard

                    String Sql = "select MATNR,WERKS,LGORT,CHARG,CLABS,CRETM,CINSM,rmd_charge,rmd_bundle,(CLABS+CRETM+CINSM) as qty from vw_hstock where matnr = '"+params[0].trim()+"' "+plant +" order by charg asc";

                    PreparedStatement ps = con.prepareStatement(Sql);
                    ResultSet rs = ps.executeQuery();

                    stocklist.clear();
                    while (rs.next()) {

                        Map<String, String> datanums = new HashMap<String, String>();
                        datanums.put("MATNR", rs.getString("MATNR"));
                        datanums.put("LGORT", rs.getString("LGORT"));
                        datanums.put("CHARG", rs.getString("CHARG"));
                        datanums.put("qty", rs.getString("qty"));
                        datanums.put("rmd_charge", rs.getString("rmd_charge"));
                        datanums.put("rmd_bundle", rs.getString("rmd_bundle"));

                        stocklist.add(datanums);

                    }

                    Fmat = params[0].trim();
                    isSuccess =true;
                }

            } catch (Exception ex) {

                z = ex.getMessage().toString();
                isSuccess =false;

            }
            return z;
        }
    }

}




