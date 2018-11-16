package com.zubb.jannarongj.z_retail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ar_list extends AppCompatActivity {

    ConnectionClass connectionClass;
    ProgressBar pbbar;
    ListView list_vbeln ;
    SimpleAdapter ADA ;

    List<Map<String, String>> vbelnlist  = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_list);


    }

    public class VbelnFList extends AsyncTask<String, String, String> {

        String z = "";

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            String[] from = {"VBELN","ARKTX","POSNR","CARLICENSE" };
            int[] views = {R.id.vbeln,R.id.arktx,R.id.posnr,R.id.carlicense};
             ADA = new SimpleAdapter(ar_list.this,
                    vbelnlist, R.layout.adp_listitem, from,
                    views);
            list_vbeln.setAdapter(ADA);
            list_vbeln.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    arg1.setSelected(true);

                    Intent i = new Intent(ar_list.this, AddProducts.class);
                    i.putExtra("vbeln", (String) obj.get("VBELN"));
                    i.putExtra("posnr", (String) obj.get("POSNR"));

                    //Toast.makeText(TransferList.this, (String) obj.get("POSNR"), Toast.LENGTH_SHORT).show();

                    startActivity(i);

                }
            });
            //Toast.makeText(TransferList.this, z, Toast.LENGTH_SHORT).show();

            pbbar.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String where = "";
                    if(params[0]==null || params[0].equals("")){
                        where = "";
                    }else{
                        where = " where VBELN like '%"+params[0].trim()+"%' ";
                    }

                    String query = "SELECT convert(nvarchar(20),wadat,103) as wadat,VBELN,POSNR,KUNNR,AR_NAME,CARLICENSE " +
                            "      ,MATNR,ARKTX " +
                            "  FROM vw_shipment_zubb_mmt " + where ;

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    vbelnlist.clear();
                    while (rs.next()) {
                        Map<String, String> datanums = new HashMap<String, String>();
                        datanums.put("wadat", rs.getString("wadat"));
                        datanums.put("VBELN", rs.getString("VBELN")+"-"+rs.getString("POSNR"));
                        datanums.put("POSNR", rs.getString("POSNR"));
                        datanums.put("KUNNR", rs.getString("KUNNR"));
                        datanums.put("AR_NAME", rs.getString("AR_NAME"));
                        datanums.put("CARLICENSE", rs.getString("CARLICENSE"));
                        datanums.put("MATNR",rs.getString("MATNR"));
                        datanums.put("ARKTX",rs.getString("ARKTX"));

                        vbelnlist.add(datanums);

                    }

                    // z = "Success";
                    z = query ;
                }
            } catch (Exception ex) {

                z = ex.getMessage().toString();
                //"Error retrieving data from table";

            }

            return z;
        }
    }



}

