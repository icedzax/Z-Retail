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

            String[] from = {"VBELN","AR_NAME","CARLICENSE" };
            int[] views = {R.id.vbeln,R.id.ar_name,R.id.carlicense};
             ADA = new SimpleAdapter(ar_list.this,
                    vbelnlist, R.layout.adp_list_ar, from,
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
                        where = " where s.VBELN is not null and s.ARKTX not like 'billet%' ";
                    }else{
                        where = " where s.VBELN is not null and s.ARKTX not like 'billet%' and s.VBELN like '%"+params[0].trim()+"%' ";
                    }

                    String query = "SELECT s.WADAT,s.VBELN,s.AR_NAME,s.CARLICENSE" +
                            " FROM           gr_shipmentplan  as s LEFT JOIN dbo.tbl_shipment_item as i " +
                            " on i.VBELN = s.VBELN and i.POSNR = s.POSNR "
                            + where +
                            " group by s.VBELN,s.AR_NAME,s.CARLICENSE,s.WADAT" +
                            " order by 1 desc "  ;

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    vbelnlist.clear();
                    while (rs.next()) {
                        Map<String, String> datanums = new HashMap<String, String>();
                        datanums.put("WADAT", rs.getString("WADAT"));
                        datanums.put("VBELN", rs.getString("VBELN"));
                        datanums.put("AR_NAME", rs.getString("AR_NAME"));
                        datanums.put("CARLICENSE", rs.getString("CARLICENSE"));

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

