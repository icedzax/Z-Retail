package com.zubb.jannarongj.z_retail;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompleteDo extends AppCompatActivity {

    UserHelper usrHelper ;
    ConnectionClass connectionClass;
    ProgressBar pbbar;
    ListView lstdo;
    EditText searchBox;
    String plant ;

    public static String getpCar() {
        return pCar;
    }

    public static void setpCar(String pCar) {
        CompleteDo.pCar = pCar;
    }

    static String pCar ="";


    public static void setFilvbeln(String filvbeln) {
        ListDo.filvbeln = filvbeln;
    }


    Button btnSearch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setpCar("");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();

        setContentView(R.layout.activity_complete_do);
        //
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        lstdo = (ListView) findViewById(R.id.lstdo);
        btnSearch = (Button) findViewById(R.id.btnSearch);


        plant = usrHelper.getPlant();


        FillList fillList = new FillList();
        fillList.execute(getpCar(), plant);


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
                            setpCar(getSearch.trim());

                            FillList fillList = new FillList();
                            fillList.execute(getpCar(), plant);

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
                setpCar(getSearch);
                FillList fillList = new FillList();
                fillList.execute(getpCar(), plant);


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
            Toast.makeText(CompleteDo.this, r, Toast.LENGTH_SHORT).show();
            // Toast.makeText(ListDo.this, "getfil : "+getFilter()+"\ngetvbeln : "+getFilvbeln(), Toast.LENGTH_SHORT).show();

            String[] from = {"AR_NAME","VBELN","CARLICENSE"};
            int[] views = {R.id.ar_name,R.id.vbeln,R.id.carlicense};
            final SimpleAdapter ADA = new SimpleAdapter(CompleteDo.this,
                    dolist, R.layout.adp_list_ar, from,
                    views){
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    /*LinearLayout lnx = (LinearLayout) view.findViewById(R.id.lnar);

                    if(dolist.get(position).get("ApproveOn")!=null){
                        lnx.setBackgroundColor(Color.parseColor("#FFA4A4"));
                    }
*/

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
                    final String lst_car = (String) obj.get("CARLICENSE");


                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(CompleteDo.this);
                    builder.setTitle("ปิดจบ "+lst_car);
                    builder.setMessage("ยืนยันการปิดจบ ?\nทะเบียน "+lst_car);
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            CloseAll ca = new CloseAll();
                            ca.execute(lst_car);
                        }
                    });
                    builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();

                        }
                    });
                    builder.show();


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

                    String car = "";
                    String plant = "";


                    switch (params[1]){
                        case "ZUBB" : plant = "  werks in ('1010','9010') ";
                            break;
                        case "SPN" : plant = "  werks in ('1050','9050') ";
                            break;
                        case "SPS" : plant = "  werks in ('1040','9040') ";
                            break;
                        case "OPS" : plant = "  werks in ('2010','9060','1020','9020') ";
                            break;
                        case "RS" : plant = "  ship_point ='1012' ";
                            break;
                        case "MMT" : plant = "  ship_point <>'x' ";
                            break;

                    }

                    if(params[0]==null || params[0].equals("")){
                        car = "";
                    }else{
                        car = " and carlicense like '%"+params[0].trim()+"%' ";
                    }

                    String query = "SELECT WADAT_IST,VBELN,AR_NAME,CARLICENSE " +
                            "FROM vw_shipment_carvisit_detail " +
                            "where " +plant+ " " + car + " and wadat_ist > getdate()-1 and ApproveOn is null group by WADAT_IST,VBELN,AR_NAME,CARLICENSE order by carlicense  " ;


                 //   Log.d("queryx",query);

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    dolist.clear();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("WADAT", rs.getString("WADAT_IST"));
                        datanum.put("VBELN", rs.getString("VBELN"));
                        datanum.put("AR_NAME", rs.getString("AR_NAME"));
                        datanum.put("CARLICENSE", rs.getString("CARLICENSE"));
                        //datanum.put("ApproveOn", rs.getString("ApproveOn"));


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


    public class CloseAll extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(CompleteDo.this, r, Toast.LENGTH_SHORT).show();

            if(isSuccess==true) {

                FillList fillList = new FillList();
                fillList.execute(getpCar(), plant);



            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "update tbl_shipmentplan  set ApproveOn  = CURRENT_TIMESTAMP , ApproveBy = '"+usrHelper.getUserName()+"' WHERE carlicense = '"+params[0]+"' and werks_to is null and wadat_ist > getdate()-1  and ApproveOn is null and  carlicense <> rtrim('') ";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    String qcarvis = "update tbl_shipment_carvisit  set OutTime  = CURRENT_TIMESTAMP WHERE seq in (select distinct(PO_NUM) from tbl_shipmentplan WHERE carlicense = '"+params[0]+"' and werks_to is null and wadat_ist > getdate()-1 and  carlicense <> rtrim('') )";
                    PreparedStatement pStmcvsi = con.prepareStatement(qcarvis);
                    pStmcvsi.executeUpdate();


                    z = "ปิดจบสำเร็จ";

              //      Log.d("query",qcarvis);

                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "ปิดจบไม่สำเร็จ \n"+ex.getMessage();
                //z = "ปิดจบไม่สำเร็จ";
            }

            return z;
        }
    }

}
