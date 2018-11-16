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
    ToggleButton tgg1, tgg2, tgg3, tgg4,tgg5;
    ProgressBar pbbar;
    ListView lstdo;
    EditText searchBox;
    String gs= "";
    Button btnSearch, btnU, btnAll, btnOut, btnDone;
    // String tg = " WADAT = CONVERT(VARCHAR(10), getdate(), 112) and DocumentId is not null and INTIME = OUTTIME order by vbeln" ;
    String tg = " WADAT = CONVERT(VARCHAR(10), getdate(), 112) and INTIME is not null and INTIME = OUTTIME  order by vbeln" ;

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

        tgg1 = (ToggleButton) findViewById(R.id.tgg1);
        tgg2 = (ToggleButton) findViewById(R.id.tgg2);
        tgg3 = (ToggleButton) findViewById(R.id.tgg3);
        tgg4 = (ToggleButton) findViewById(R.id.tgg4);
        tgg5 = (ToggleButton) findViewById(R.id.tgg5);
        tgg1.setText(null);
        tgg2.setText(null);
        tgg3.setText(null);
        tgg4.setText(null);
        tgg5.setText(null);
        tgg1.setTextOn(null);
        tgg2.setTextOn(null);
        tgg3.setTextOn(null);
        tgg4.setTextOn(null);
        tgg5.setTextOn(null);
        tgg1.setTextOff(null);
        tgg2.setTextOff(null);
        tgg3.setTextOff(null);
        tgg4.setTextOff(null);
        tgg5.setTextOff(null);


        FillList fillList = new FillList();
        fillList.execute("");

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
                            setSB(getSearch);
                            FillList fillList = new FillList();
                            fillList.execute("");

                            //  Toast.makeText(ListDo.this, getTG(), Toast.LENGTH_SHORT).show();
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

                getSearch = searchBox.getText().toString();
                setSB(getSearch);
                FillList fillList = new FillList();
                fillList.execute("");
                // Toast.makeText(ListDo.this, getTG(), Toast.LENGTH_SHORT).show();

            }
        });


        tgg1.setChecked(true);
        tgg1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tgg1.isChecked()) {

                    // setTG(" WADAT = CONVERT(VARCHAR(10), getdate(), 112) and DocumentId is not null and INTIME = OUTTIME and INTIME is not null and OUTTIME is not null order by vbeln");
                    setTG(" WADAT = CONVERT(VARCHAR(10), getdate(), 112) and INTIME is not null and INTIME = OUTTIME  order by vbeln");

                    FillList fillList = new FillList();
                    fillList.execute("");
                    tgg2.setChecked(false);
                    tgg3.setChecked(false);
                    tgg4.setChecked(false);
                    tgg5.setChecked(false);

                    //  Toast.makeText(ListDo.this, getTG(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        tgg2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tgg2.isChecked()) {

                    setTG(" WADAT = CONVERT(VARCHAR(10), getdate(), 112) and sscan >= 1 order by vbeln");
                    //setTG("group by WADAT,VBELN,AR_NAME,KUNNR,CARLICENSE,INTIME,POSNR,DocumentId,wsum,asum,sqty,sscan,OUTTIME having  sscan >= 1 order by vbeln ");
                    FillList fillList = new FillList();
                    fillList.execute("");
                    tgg1.setChecked(false);
                    tgg3.setChecked(false);
                    tgg4.setChecked(false);
                    tgg5.setChecked(false);
                    //  Toast.makeText(ListDo.this, getTG(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tgg3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tgg3.isChecked()) {
                    setTG(" WADAT = CONVERT(VARCHAR(10), getdate(), 112) and OUTTIME is not null and OUTTIME > INTIME order by vbeln");

                    FillList fillList = new FillList();
                    fillList.execute("");
                    tgg1.setChecked(false);
                    tgg2.setChecked(false);
                    tgg4.setChecked(false);
                    tgg5.setChecked(false);
                    // Toast.makeText(ListDo.this, getTG(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        tgg4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tgg4.isChecked()) {
                    setTG(" WADAT = CONVERT(VARCHAR(10), getdate(), 112) and vbeln is not null  order by vbeln");

                    FillList fillList = new FillList();
                    fillList.execute("");
                    tgg1.setChecked(false);
                    tgg2.setChecked(false);
                    tgg3.setChecked(false);
                    tgg5.setChecked(false);
                    // Toast.makeText(ListDo.this, getTG(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        tgg5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tgg5.isChecked()) {
                    setTG(" WADAT > CONVERT(VARCHAR(10), getdate() - 4 , 112) and  WADAT < CONVERT(VARCHAR(10), getdate() +2 , 112)  and vbeln is not null  order by wadat desc");

                    FillList fillList = new FillList();
                    fillList.execute("");
                    tgg1.setChecked(false);
                    tgg2.setChecked(false);
                    tgg3.setChecked(false);
                    tgg4.setChecked(false);
                    // Toast.makeText(ListDo.this, getTG(), Toast.LENGTH_SHORT).show();

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

            pbbar.setVisibility(View.GONE);
            Toast.makeText(ListDo.this, r, Toast.LENGTH_SHORT).show();
            Log.d("dolist",dolist.toString());


            String[] from = {"A","D","B","C","Z"};
            int[] views = {R.id.lblmatcode, R.id.lblposnr, R.id.lbldesc, R.id.llblamout};
            final SimpleAdapter ADA = new SimpleAdapter(ListDo.this,
                    dolist, R.layout.lstdo, from,
                    views){
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    LinearLayout lnx = (LinearLayout) view.findViewById(R.id.lnx);
                    //int lx = Integer.parseInt(dolist.get(position).get("sscan"));
                    if(!"0".equals(dolist.get(position).get("sscan"))){
                        lnx.setBackgroundColor(Color.parseColor("#CCC4FFE0"));
                    }
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
                    String lst_do_di = (String) obj.get("A");
                    String lst_posnr = (String) obj.get("D");
                    //String lst_wadat = (String) obj.get("E");
                    //String lst_car1 = (String) obj.get("C");
                    //String lst_doc_id1 = (String) obj.get("DocID");
                    Intent i = new Intent(ListDo.this, AddProducts.class);
                    i.putExtra("vbeln", lst_do_di);
                    i.putExtra("posnr", lst_posnr);
                   /* i.putExtra("doc_date", lst_wadat);
                    i.putExtra("doc_car", lst_car1);
                    i.putExtra("doc_id", lst_doc_id1);*/

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
                    //     String query = "select * from vw_shipmentplan_zubb where " + getTG() + " ";
                    String query = "select VBELN,AR_NAME,CARLICENSE,POSNR,WADAT,DocumentId,sscan from vw_wsum_p8 where " + getTG() + " ";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    dolist.clear();
                    ArrayList<String> data1 = new ArrayList<String>();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("VBELN"));
                        datanum.put("B", rs.getString("AR_NAME"));
                        datanum.put("C", rs.getString("CARLICENSE"));
                        datanum.put("D", rs.getString("POSNR"));
                        datanum.put("E", rs.getString("WADAT"));
                       // datanum.put("st", rs.getString("dstat"));
                        datanum.put("DocID", rs.getString("DocumentId"));
                        datanum.put("sscan", rs.getString("sscan"));

                        dolist.add(datanum);
                    }

                    z = "Success";
                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";

            }
            return z;
        }
    }

   /* @Override
    public void onResume(){

        super.onResume();

        FillList fillList = new FillList();
        fillList.execute("");

    }*/

    public void setTG(String tg){
        this.tg = tg ;
    }
    public void setSB(String gs){
        if(gs.equals("")){
            this.gs = "" ;
        }else{
            this.gs = "VBELN like '%"+gs+"%' and " ;
        }
    }
    public String getTG(){
        String stSearch = gs+" "+tg;
        return stSearch;
    }




}
