package com.zubb.jannarongj.z_retail;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDo extends AppCompatActivity {
    UserHelper usrHelper ;
    ConnectionClass connectionClass;
    ToggleButton tgg1, tgg2, tgg3, tgg4,tggP8;
    ProgressBar pbbar;
    ListView lstdo;
    EditText searchBox;
    String plant;

    public String getShipp1() {
        return shipp1;
    }

    public void setShipp1(String shipp1) {
        this.shipp1 = shipp1;
    }

    String shipp1 = " ";
    Button completeDo;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    static String  filter = "";

    public static String getFilvbeln() {
        return filvbeln;
    }

    public static void setFilvbeln(String filvbeln) {
        ListDo.filvbeln = filvbeln;
    }

    static String  filvbeln = "";
    Button btnSearch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();

        setContentView(R.layout.activity_list_do);
        //
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        lstdo = (ListView) findViewById(R.id.lstdo);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        completeDo = (Button)findViewById(R.id.allcbtn);
        tggP8 = (ToggleButton) findViewById(R.id.toggleP8);
        tgg1 = (ToggleButton) findViewById(R.id.tgg1);
        tgg2 = (ToggleButton) findViewById(R.id.tgg2);
        tgg3 = (ToggleButton) findViewById(R.id.tgg3);
        tgg4 = (ToggleButton) findViewById(R.id.tgg4);

        tgg1.setText(null);
        tgg2.setText(null);
        tgg3.setText(null);
        tgg4.setText(null);

        tgg1.setTextOn(null);
        tgg2.setTextOn(null);
        tgg3.setTextOn(null);
        tgg4.setTextOn(null);

        tgg1.setTextOff(null);
        tgg2.setTextOff(null);
        tgg3.setTextOff(null);
        tgg4.setTextOff(null);

        plant = usrHelper.getPlant();

        setFilter("1");
        setFilvbeln("");

        FillList fillList = new FillList();
        fillList.execute(getFilter(),getFilvbeln(),plant);
        tgg1.setChecked(true);

        if(!usrHelper.getLevel().equals("0") || usrHelper.getPlant().equals("OPS") || usrHelper.getPlant().equals("SPS") || usrHelper.getPlant().equals("SPN")  || usrHelper.getUserName().equals("Wassana.k")){
            completeDo.setVisibility(View.VISIBLE);
        }

        completeDo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListDo.this, CompleteDo.class);
                startActivity(i);

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            String getSearch;
            @Override
            public void onClick(View v) {
                searchBox = (EditText) findViewById(R.id.searchBox);
                searchBox.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (s.toString().trim().length() == 0) {
                            getSearch = searchBox.getText().toString();
                            setFilvbeln(getSearch);

                            FillList fillList = new FillList();
                            fillList.execute(getFilter(),getFilvbeln(),plant);

                            //  Toast.makeText(ListDo.this, getTG(), Toast.LENGTH_SHORT).show();
                            //
                        } else {
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        // TODO Auto-generated method stub

                    }
                });

                getSearch = searchBox.getText().toString().trim();
                setFilvbeln(getSearch);
                FillList fillList = new FillList();
                fillList.execute(getFilter(),getFilvbeln(),plant);


            }
        });

        tgg1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tgg1.isChecked()) {
                    setFilter("1");
                    FillList fillList = new FillList();
                    fillList.execute(getFilter(),getFilvbeln(),plant);
                    tgg2.setChecked(false);
                    tgg3.setChecked(false);
                    tgg4.setChecked(false);
                }
            }
        });

        tgg2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tgg2.isChecked()) {
                    setFilter("2");
                    FillList fillList = new FillList();
                    fillList.execute(getFilter(),getFilvbeln(),plant);
                    tgg1.setChecked(false);
                    tgg3.setChecked(false);
                    tgg4.setChecked(false);
                }
            }
        });
        tgg3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tgg3.isChecked()) {
                    setFilter("3");
                    FillList fillList = new FillList();
                    fillList.execute(getFilter(),getFilvbeln(),plant);
                    tgg1.setChecked(false);
                    tgg2.setChecked(false);
                    tgg4.setChecked(false);
                }
            }
        });
        tgg4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tgg4.isChecked()) {
                    setFilter("4");
                    FillList fillList = new FillList();
                    fillList.execute(getFilter(),getFilvbeln(),plant);
                    tgg1.setChecked(false);
                    tgg2.setChecked(false);
                    tgg3.setChecked(false);
                }
            }
        });

        tggP8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tggP8.isChecked()) {
                    tggP8.setBackgroundColor(Color.parseColor("#4CAF50"));
                    FillList fillList = new FillList();
                    fillList.execute(getFilter(),getFilvbeln(),plant);
                    setShipp1(",'1011'");
                }else{
                    tggP8.setBackgroundColor(Color.parseColor("#FFC107"));
                    FillList fillList = new FillList();
                    fillList.execute(getFilter(),getFilvbeln(),plant);
                    setShipp1(" ");
                }
            }
        });

    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";

        List<Map<String, String>> dolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            //Log.d("Zs",z);
            pbbar.setVisibility(View.GONE);
            Toast.makeText(ListDo.this, r, Toast.LENGTH_SHORT).show();
           // Toast.makeText(ListDo.this, "getfil : "+getFilter()+"\ngetvbeln : "+getFilvbeln(), Toast.LENGTH_SHORT).show();

            String[] from = {"AR_NAME","VBELN","CARLICENSE"};
            int[] views = {R.id.ar_name,R.id.vbeln,R.id.carlicense};
            final SimpleAdapter ADA = new SimpleAdapter(ListDo.this,
                    dolist, R.layout.adp_list_ar, from,
                    views){
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    //LinearLayout lnx = (LinearLayout) view.findViewById(R.id.lnx);

                    //int lx = Integer.parseInt(dolist.get(position).get("sscan"));
                    /*if(!"0".equals(dolist.get(position).get("sscan"))){
                        lnx.setBackgroundColor(Color.parseColor("#CCC4FFE0"));
                    }*/

                    return view;
                }
            };

            lstdo.setAdapter(ADA);
            lstdo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    String lst_vbeln = (String) obj.get("VBELN");

                    Intent i = new Intent(ListDo.this, ar_list_back.class);
                    i.putExtra("vbeln", lst_vbeln);

                    startActivity(i);

                }
            });


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String where = "";
                    String having = "";
                    String vbeln = "";
                    String plant = "";
                    String vw = "";


                    switch (params[2]){
                        case "ZUBB" : plant = " and s.werks in ('1010','9010') "; vw = "gr_shipmentplan3";
                            break;
                        case "SPN" : plant = " and s.werks in ('1050','9050') "; vw = "gr_shipmentplan3_spn";
                            break;
                        case "SPS" : plant = " and s.werks in ('1040','9040') "; vw = "gr_shipmentplan_sps";
                            break;
                        case "OPS" : plant = " and s.werks in ('2010','9060','1020','9020') "; vw = "gr_shipmentplan_ops";
                            break;
                        case "RS" : plant = " and s.werks in ('1010','9010') "; vw = "gr_shipmentplan_p1";
                            break;
                                default: vw ="gr_shipmentplan3";
                    }

                    if(params[1]==null || params[1].equals("")){
                        vbeln = "";
                    }else{
                        vbeln = " and s.vbeln like '%"+params[1].trim()+"%' ";
                    }

                    switch (params[0]){
                        case "1" : where = " and s.INTIME = s.OUTTIME  and  s.INTIME is not null and s.OUTTIME is not null and s.wadat = convert(nvarchar(22),getdate(),112) ";
                            break;
                        case "2" : having = " having sum(i.qty) >  0  and s.wadat = convert(nvarchar(22),getdate(),112) ";
                            break;
                        case "3" : where = " and s.INTIME <> s.OUTTIME  and s.INTIME is not null and s.OUTTIME is not null and s.wadat = convert(nvarchar(22),getdate(),112) ";
                            break;
                        case "4" : where = "";
                            break;

                    }

                    String query = "SELECT s.WADAT,s.VBELN,s.AR_NAME,s.CARLICENSE " +
                            "FROM "+vw+" as s LEFT JOIN dbo.tbl_shipment_item as i " +
                            "on i.VBELN = s.VBELN and i.POSNR = s.POSNR " +
                            "where  s.VBELN is not null and left(s.MATNR,2) not in ('BL','SC') " +plant+ where+ " " + vbeln +
                            "group by s.VBELN,s.AR_NAME,s.CARLICENSE,s.WADAT " +having+
                            "order by 1 desc ";

                    Log.d("query",query);

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    dolist.clear();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("WADAT", rs.getString("WADAT"));
                        datanum.put("VBELN", rs.getString("VBELN"));
                        datanum.put("AR_NAME", rs.getString("AR_NAME"));
                        datanum.put("CARLICENSE", rs.getString("CARLICENSE"));

                        dolist.add(datanum);
                    }

                    z = "Success";
                    //z = query;
                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";

            }
            return z;
        }
    }


}
