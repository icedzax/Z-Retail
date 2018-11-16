package com.zubb.jannarongj.z_retail;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.SearchManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetailList extends AppCompatActivity implements OnQueryTextListener {


    ListView lv_retail;
    ListViewAdapter adapter;
    SearchView searchDo;
    String[] vbelnNameList,vb;
    ConnectionClass connectionClass;
    ArrayList<VbelnName> arraylist = new ArrayList<VbelnName>();
    List<Map<String, String>> vbelnlist  = new ArrayList<Map<String, String>>();
    Map<String, String> mvbeln = new HashMap<String, String>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail_list);
        connectionClass = new ConnectionClass();

        FillList fillList = new FillList();
        fillList.execute();

        searchDo = (SearchView) findViewById(R.id.svdo);
        searchDo.onActionViewExpanded();
        searchDo.setQueryHint("ค้นหาเลขที่เอกสาร");


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        lv_retail = (ListView) findViewById(R.id.lv_retail);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }
    public void setList(String[]ar){

        vbelnNameList = new String[]{"Lion", "Tiger", "Dog",
                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
                "Cow", "Donkey", "Monkey"};



        for (int i = 0; i < vbelnNameList.length; i++) {
            VbelnName vbelnName = new VbelnName(vbelnNameList[i]);

            arraylist.add(vbelnName);
            adapter = new ListViewAdapter(this, arraylist);
            lv_retail.setAdapter(adapter);
            searchDo.setOnQueryTextListener(this);

            //Log.d("See2", vbelnNameList[i]);

        }
    }

    public class FillList extends AsyncTask<String, String, String> {


        String z = "";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            //pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            //Toast.makeText(MainActivity.this, z, Toast.LENGTH_SHORT).show();
            //pbbar.setVisibility(View.GONE);
            //Log.d("See", mvbeln.get("VBELN").toString());

            setList(vb);

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String where;
                    String query = "SELECT convert(nvarchar(20),WADAT_IST,103) as wadat,VBELN,POSNR,KUNNR,AR_NAME,CARLICENSE " +
                            "      ,MATNR,ARKTX,NTGEW,VRKME,LFIMG " +
                            "  FROM tbl_shipmentplan " +
                            "  where left(vbeln,1) = '3' and WADAT_IST > getdate()-1 and WADAT_IST < getdate()+1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int k = 0 ;
                        Map<String, String> datanums = new HashMap<String, String>();
                        vb = new String[]{};
                        datanums.put("wadat", rs.getString("wadat"));
                        datanums.put("VBELN", rs.getString("VBELN") + "-" + rs.getString("POSNR"));
                        datanums.put("POSNR", rs.getString("POSNR"));
                        datanums.put("KUNNR", rs.getString("KUNNR"));
                        datanums.put("AR_NAME", rs.getString("AR_NAME"));
                        datanums.put("CARLICENSE", rs.getString("CARLICENSE"));
                        datanums.put("MATNR", rs.getString("MATNR"));
                        datanums.put("ARKTX", rs.getString("ARKTX"));
                        datanums.put("NTGEW", rs.getString("NTGEW"));
                        datanums.put("VRKME", rs.getString("VRKME"));
                        datanums.put("LFIMG", rs.getString("LFIMG"));
                        vbelnlist.add(datanums);
                        vb[k]= rs.getString("VBELN") + "-" + rs.getString("POSNR");
                         k++ ;
                    }
                    // z = "Success";
                }
            } catch (Exception ex) {

                z = ex.getMessage().toString();
                //"Error retrieving data from table";

            }

            return z;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        adapter.filter(text);
        return false;
    }
}
