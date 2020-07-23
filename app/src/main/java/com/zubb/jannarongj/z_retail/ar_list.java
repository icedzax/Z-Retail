package com.zubb.jannarongj.z_retail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ar_list extends AppCompatActivity {

    UserHelper usrHelper ;
    ConnectionClass connectionClass;
    Line sLine ;
    ProgressBar pbbar;
    ListView list_vbeln ;
    Button q_btn_save,closeBtn,closedBtn;
    EditText q_lfill ;
    TextView kunnr,vbeln,carlicense,q_remark,q_lock;
    String h_apr,h_aprby,h_kunnr,h_vbeln,h_carlicense,g_vbeln,h_matnr,h_arktx,h_documentid,h_lfimg,h_unit,h_divqty;
    String l = "";
    String rm ="";
    Button btn_noship ,btn_u;
    List<Map<String, String>> vbelnlist  = new ArrayList<Map<String, String>>();
    List<Map<String, String>> picklist  = new ArrayList<Map<String, String>>();

    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_list);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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


        closeBtn =(Button)findViewById(R.id.closeBtn);
        closedBtn = (Button)findViewById(R.id.closedBtn);
        kunnr = (TextView)findViewById(R.id.seq);
        //vbeln = (TextView)findViewById(R.id.vbeln);
        carlicense = (TextView)findViewById(R.id.carlicense);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        list_vbeln = (ListView)findViewById(R.id.lvb);
        btn_noship = (Button) findViewById(R.id.btn_noship);
        btn_u = (Button) findViewById(R.id.btn_u);
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();
        sLine = new Line();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);


        // Toast.makeText(ar_list.this, g_vbeln.trim(), Toast.LENGTH_SHORT).show();

        VbelnFList fillList = new VbelnFList();
        fillList.execute(g_vbeln.trim());

        btn_noship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  LocationPicker locationPick = new LocationPicker(ar_list.this,usrHelper.getPlant());
                locationPick.onOpenDialoag();*/
                Intent i = new Intent(ar_list.this, NoShipActivity.class);
                i.putExtra("seq", g_vbeln);
                startActivity(i);
            }
        });

        btn_u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOpenWeb();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ar_list.this);
                builder.setTitle("ปิดจบ");
                builder.setMessage("ยืนยันการปิดจบ ?");
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CompleteDo cp = new CompleteDo();
                        cp.execute();
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

        if(usrHelper.getLevel().equals("10")){
            btn_u.setVisibility(View.VISIBLE);
        }

        /*FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(ar_list.this, NoShipActivity.class);
                i.putExtra("seq", g_vbeln);
                startActivity(i);
            }
        });*/

    }

    public void onOpenWeb(){
        AlertDialog.Builder alert = new AlertDialog.Builder(ar_list.this);
        alert.setTitle("ปลดล็อค");

        WebView wv = new WebView(ar_list.this);

        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        String url = "http:\\192.168.116.222/ship/unlock.php?s="+g_vbeln+"&u="+usrHelper.getUserName();
        if(usrHelper.getPlant().equals("SPN")){

        }
        wv.loadUrl("http:\\report.zubbsteel.com/ship/unlock.php?s="+g_vbeln+"&u="+usrHelper.getUserName());
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                VbelnFList fillList = new VbelnFList();
                fillList.execute(g_vbeln.trim());
            }
        });
        alert.show();
    }

    public class VbelnFList extends AsyncTask<String, String, String> {

        String z = "";

        HashMap<String, List<String>> listTemp = new HashMap<String, List<String>>();

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            kunnr.setText("คิวที่ "+g_vbeln);
            //vbeln.setText(h_vbeln);
            carlicense.setText(h_carlicense);

            if(h_documentid==null){
                h_documentid = "";
            }

            expandableListDetail = listTemp;
            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
            expandableListAdapter = new CustomExpandableListAdapter(ar_list.this, expandableListTitle, expandableListDetail);
            expandableListView.setAdapter(expandableListAdapter);

            int c = expandableListAdapter.getGroupCount();
            for (int i = 0 ; i<c;i++){
                expandableListView.expandGroup(i);
            }



            /*expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    Toast.makeText(getApplicationContext(),
                            expandableListTitle.get(groupPosition) + " List Expanded.",
                            Toast.LENGTH_SHORT).show();
                }
            });

            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {
                    Toast.makeText(getApplicationContext(),
                            expandableListTitle.get(groupPosition) + " List Collapsed.",
                            Toast.LENGTH_SHORT).show();

                }
            });*/

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                    String item = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                    item = item.substring(0,item.indexOf("-"));



                    Intent i = new Intent(ar_list.this, AddProducts.class);
                    i.putExtra("posnr", item);
                    i.putExtra("vbeln", expandableListTitle.get(groupPosition));
                    startActivity(i);

                    return false;
                }
            });



            /*if(usrHelper.getLevel().equals("10")){

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                    String item = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                    item = item.substring(0,item.indexOf("-"));
                    *//*Toast.makeText(
                            getApplicationContext(),
                            expandableListTitle.get(groupPosition)
                                    + " -> "
                                    + item, Toast.LENGTH_SHORT
                    ).show();*//*

                    Intent i = new Intent(ar_list.this, AddProducts.class);
                    i.putExtra("posnr", item);
                    i.putExtra("vbeln", expandableListTitle.get(groupPosition));
                    startActivity(i);

                    return false;
                }
            });

            }*/

            /*String[] from = {"posnr","arktx"};
            int[] views = {R.id.lposnr,R.id.larktx};
            final SimpleAdapter ADA = new SimpleAdapter(ar_list.this,
                    vbelnlist, R.layout.adp_new_do_list, from,
                    views){
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    LinearLayout lnx = (LinearLayout) view.findViewById(R.id.lnx);

                    //int lx = Integer.parseInt(vbelnlist.get(position).get("sscan"));
                    if(vbelnlist.get(position).get("LOCK")!=null){
                        lnx.setBackgroundColor(Color.parseColor("#FFA4A4"));
                    }else{
                        if(!"0".equals(vbelnlist.get(position).get("sscan"))){
                            lnx.setBackgroundColor(Color.parseColor("#CCC4FFE0"));
                        }
                    }

                    return view;
                }
            };
            list_vbeln.setAdapter(ADA);
            list_vbeln.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    Intent i = new Intent(ar_list.this, AddProducts.class);
                    i.putExtra("posnr", (String) obj.get("pposnr"));
                    i.putExtra("vbeln", (String) obj.get("vbeln"));

                    startActivity(i);

                }
            });
            if(usrHelper.getLevel().equals("10")){
                list_vbeln.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {

                        HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                                .getItem(arg2);
                        String qposnr = (String) obj.get("posnr");
                        String qarktx = (String) obj.get("arktx");
                        String qlock = (String) obj.get("LOCK");
                        String qlfimg = (String) obj.get("LFIMG");
                        arg1.setSelected(true);
                        unlockDo(qposnr,qarktx,qlock,qlfimg);
                        return true;
                    }
                });
            }else{
                *//*list_vbeln.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {

                        HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                                .getItem(arg2);
                        String qposnr = (String) obj.get("posnr");
                        String qarktx = (String) obj.get("arktx");
                        arg1.setSelected(true);
                        closeDo(h_vbeln,qposnr,qarktx);
                        return true;
                    }
                });*//*
            }*/
            //  Log.d("p8",z);

            checkComplete(h_apr);

            pbbar.setVisibility(View.GONE);

        }


        @Override
        protected String doInBackground(String... params) {
            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String vw = "";
                    switch (usrHelper.getPlant()){
                        case "ZUBB" : vw = "vw_wsum_p8";
                            break;
                        case "SPN" : vw = "vw_wsum_spn";
                            break;
                        case "SPS" : vw = "vw_wsum_sps";
                            break;
                        case "OPS" : vw = "vw_wsum_ops";
                            break;
                        case "RS" : vw = "vw_wsum_p8";
                            break;
                        default: vw ="vw_wsum_p8";
                    }


                    String qvb = " SELECT vbeln FROM "+vw+" where po_num = '"+params[0].trim()+"' group by vbeln ";
                    PreparedStatement vps = con.prepareStatement(qvb);
                    ResultSet vrs = vps.executeQuery();


                    while (vrs.next()) {

                        List<String> fetch_list = new ArrayList<String>();

                        String query = " SELECT * FROM "+vw+" where vbeln = '"+vrs.getString("vbeln")+"' and matnr is not null ";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            fetch_list.add(rs.getString("posnr") + "-" + rs.getString("arktx")+rs.getString("sscan"));

                        }
                        listTemp.put(vrs.getString("vbeln"),fetch_list);



                    }


                    /*vbelnlist.clear();
                    while (rs.next()) {

                        Map<String, String> datanums = new HashMap<String, String>();
                        datanums.put("arktx", rs.getString("arktx"));
                        datanums.put("posnr", rs.getString("vbeln")+"-"+rs.getString("posnr"));
                        datanums.put("sscan", rs.getString("sscan"));
                        datanums.put("pposnr", rs.getString("posnr"));
                        datanums.put("LOCK", rs.getString("LOCK"));
                        datanums.put("LFIMG", rs.getString("LFIMG"));
                        datanums.put("DIVQTY", rs.getString("DIVQTY"));
                        datanums.put("vbeln", rs.getString("vbeln"));

                        vbelnlist.add(datanums);

                        h_kunnr = rs.getString("AR_NAME");
                        h_matnr = rs.getString("matnr");
                        h_arktx = rs.getString("arktx");
                        h_vbeln = rs.getString("VBELN");
                        h_unit = rs.getString("unit");
                        h_lfimg = rs.getString("LFIMG");
                        h_carlicense = rs.getString("CARLICENSE");
                        h_divqty = rs.getString("DIVQTY");

                        h_documentid = rs.getString("DocumentId");

                    }*/

                    if(h_divqty==null || h_divqty.equals("") || h_divqty.equals("null")){
                        h_divqty = "0" ;
                    }

                    String qapr = "SELECT top 1 ApproveOn ,ApproveBy from tbl_shipmentplan where po_num = '"+g_vbeln+"'  order by 2 asc  " ;
                    PreparedStatement aps = con.prepareStatement(qapr);
                    ResultSet ars = aps.executeQuery();

                    while (ars.next()) {
                        h_apr = ars.getString("ApproveOn");
                        h_aprby = ars.getString("ApproveBy");

                        //sumweight = qrs.getInt("sw");
                    }

                     z = "Success";
                    //z = query ;

                }
            } catch (Exception ex) {

                z = ex.getMessage().toString();
                //"Error retrieving data from table";
                z ="ไม่ถูกต้องโปรดใส่ตัวเลข";

            }

            return z;
        }
    }

    public void onRemarkClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ar_list.this);
        builder.setTitle("สาเหตุที่ปลดล็อค DO");
        String[] remark = {"ตัวแปร","ลูกค้าเปลี่ยน","เหล็กมีปัญหา","ระบบมีปัญหา","ตาชั่งมีปัญหา","ขึ้นเร่งด่วน รอบิล","ลืมยิง/ยิงไม่ครบ","เกินในมัด","เปลี่ยนเหล็ก","น้ำหนักเกิน"};
        builder.setItems(remark, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0: rm = "ตัวแปร";
                        break;
                    case 1: rm = "ลูกค้าเปลี่ยน";
                        break;
                    case 2: rm = "เหล็กมีปัญหา";
                        break;
                    case 3: rm = "ระบบมีปัญหา";
                        break;
                    case 4: rm = "ตาชั่งมีปัญหา";
                        break;
                    case 5: rm = "ขึ้นเร่งด่วน รอบิล";
                        break;
                    case 6: rm = "ลืมยิง/ยิงไม่ครบ";
                        break;
                    case 7: rm = "เกินในมัด";
                        break;
                    case 8: rm = "เปลี่ยนเหล็ก";
                        break;
                    case 9: rm = "น้ำหนักเกิน";
                        break;

                }
                setRemark(rm);
                l = rm ;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setRemark(String rm){
        q_remark.setText(rm);
        checkRemark();
    }

    private void checkRemark() {

        if (q_remark.getText().toString() == null || q_remark.getText().toString().equals("") || q_remark.getText().toString().equals("เลือกหมายเหตุ")  ) {
            q_btn_save.setVisibility(View.GONE);
            q_lock.setBackgroundColor(Color.parseColor("#CDFFDC"));
            q_lock.setTextColor(Color.parseColor("#00932C"));
            q_lock.setText("ปกติ");
        } else {
            q_btn_save.setVisibility(View.VISIBLE);
            q_lock.setBackgroundColor(Color.parseColor("#FF9393"));
            q_lock.setTextColor(Color.parseColor("#E80000"));
            q_lock.setText("ปลดล็อค");
        }

        //Log.d("REMARK",q_remark.getText().toString());
    }

    public void unlockDo(final String qposnr, final String qarktx, final String qlock,final  String qlfimg){

        final Dialog qdialog = new Dialog(ar_list.this);
        qdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        qdialog.setContentView(R.layout.dialog_seq);

        qdialog.setCancelable(true);
        final TextView q_arktx,q_vbeln,q_lfimg;

        q_arktx = (TextView)qdialog.findViewById(R.id.q_arktx);
        q_vbeln = (TextView)qdialog.findViewById(R.id.q_vbeln);
        q_lock = (TextView)qdialog.findViewById(R.id.q_lock);
        q_remark = (TextView)qdialog.findViewById(R.id.q_remark);
        q_lfimg = (TextView)qdialog.findViewById(R.id.q_lfimg);

        q_btn_save = (Button)qdialog.findViewById(R.id.q_btn_save);

        q_lfimg.setText(qlfimg);
        q_arktx.setText(qarktx);
        q_vbeln.setText(g_vbeln);
        String premark = "";
        String plock = "";

        if(qlock==null || qlock.equals("")){
            premark = "เลือกหมายเหตุ";
            plock ="ปกติ";
        }else{
            premark = qlock;
            plock ="ปลดล็อค";
        }

        checkRemark();
        q_remark.setText(premark);
        q_lock.setText(plock);
        q_lfill = (EditText) qdialog.findViewById(R.id.edt_lfimg);
        q_lfill.setText(h_divqty);
        q_lfill.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    if (!q_lfill.getText().toString().equals(h_divqty)) {
                        q_btn_save.setVisibility(View.VISIBLE);
                        q_lock.setText("เพิ่มตัวแปร");
                    } else {
                        checkRemark();
                        //q_btn_save.setVisibility(View.GONE);
                        //q_lock.setText("ปกติ");
                    }
            }

        });

        q_remark.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                onRemarkClick();
            }
        });


        q_btn_save.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                q_lfill = (EditText) qdialog.findViewById(R.id.edt_lfimg);

                UpdateLockDo ulck = new UpdateLockDo();
                ulck.execute(qposnr,l,q_lfill.getText().toString().trim());
                qdialog.dismiss();
            }
        });

        qdialog.show();

    }

    public class CompleteDo extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(ar_list.this, r, Toast.LENGTH_SHORT).show();

            if(isSuccess==true) {

                VbelnFList fillList = new VbelnFList();
                fillList.execute(g_vbeln.trim());


            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "update tbl_shipmentplan  set ApproveOn  = CURRENT_TIMESTAMP , ApproveBy = '"+usrHelper.getUserName()+"' WHERE po_num = '"+g_vbeln+"' and ApproveBy is null and ApproveOn is null";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    String qcarvis = "update tbl_shipment_carvisit  set OutTime  = CURRENT_TIMESTAMP WHERE seq = '"+g_vbeln+"' )";
                    PreparedStatement pStmcvsi = con.prepareStatement(qcarvis);
                    pStmcvsi.executeUpdate();

                    z = "ปิดจบสำเร็จ";


                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "ปิดจบไม่สำเร็จ \n"+ex.getMessage();
            }

            return z;
        }
    }


    public class UpdateLockDo extends AsyncTask<String, String, String> {
    // TODO change un lock apr , qty
        String z = "";
        String uremark ="";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);

            Toast.makeText(ar_list.this, r, Toast.LENGTH_SHORT).show();

            if(isSuccess==true){
                //if(uremark.equals("ตาชั่งมีปัญหา") || uremark.equals("ระบบชั่งมีปัญหา"))
                sLine.PushMessage(usrHelper.getUserName(),g_vbeln,h_carlicense,h_kunnr,h_arktx,uremark);
            }

            VbelnFList fillList = new VbelnFList();
            fillList.execute(g_vbeln.trim());


        }
        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    //1--qposnr/////2--qarktx/////3--qmatnr///////4--qlfimg/////5--qticket///////6--qunit);

                    String lock = "";
                    if(params[1] ==null || params[1].equals("") || params[1].equals("เลือกหมายเหตุ")){

                    }else {
                        lock = " lock = '"+params[1].trim()+"' ";
                        uremark = params[1].trim();

                        String query = "update tbl_shipmentplan set "+lock+" where vbeln = '"+h_vbeln+"' and posnr = '"+params[0]+"'";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();

                        String ulockApp = "update tbl_shipmentplan set ApproveOn = NULL , ApproveBy = NULL  where vbeln = '"+h_vbeln+"' and posnr = '"+params[0]+"'";
                        PreparedStatement pstulockApp = con.prepareStatement(ulockApp);
                        pstulockApp.executeUpdate();

                    }

                    String qDIVQTY = "update tbl_shipmentplan set DIVQTY = "+params[2]+" where vbeln = '"+h_vbeln+"' and posnr = '"+params[0]+"'";
                    PreparedStatement qpreparedStatement = con.prepareStatement(qDIVQTY);
                    qpreparedStatement.executeUpdate();


                    z = "บันทึกการเปลี่ยนแปลง";
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage().toString();
            }
            return z;
        }
    }


    public class DetectDo extends AsyncTask<String, String, String> {

        String z = "" ;
        String fvbeln = "", fposnr = "" , fbar_id, fmat ="";


        Boolean isSuccess = false ;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);

            Toast.makeText(ar_list.this, r, Toast.LENGTH_SHORT).show();

            if(isSuccess==true){
                Intent i = new Intent(getApplicationContext(), AddProducts.class);
                i.putExtra("posnr", fposnr);
                i.putExtra("vbeln", fvbeln);
                i.putExtra("bar_id", fbar_id);

                startActivity(i);
            }

            VbelnFList fillList = new VbelnFList();
            fillList.execute(g_vbeln.trim());

        }
        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String getMat = "";
                    fbar_id = params[0];
                    String fMat = "select top 1 mat_code,r_qty from vw_barcode_item where bar_id = '"+params[0]+"' ";
                    PreparedStatement fps = con.prepareStatement(fMat);
                    ResultSet frs = fps.executeQuery();

                    while (frs.next()){
                        getMat = frs.getString("mat_code");
                    }

                    String fDo = "select matnr,vbeln,posnr from tbl_shipment_item where ponum = '"+g_vbeln+"' and mat_code  = '"+getMat+"' ";
                    PreparedStatement fds = con.prepareStatement(fDo);
                    ResultSet drs = fds.executeQuery();
                    int cv = 0;
                    Map<String, String> mapdocs = new HashMap<String, String>();
                    picklist.clear();
                    while (drs.next()){

                        fvbeln = drs.getString("vbeln");
                        fposnr = drs.getString("posnr");
                        fmat = drs.getString("matnr");

                        cv++;
                        mapdocs.put("mat",fmat);
                        mapdocs.put("vbeln",fvbeln);
                        mapdocs.put("posnr",fposnr);
                    }
                    picklist.add(mapdocs);

                    if(cv>1){
                        popPickMat(fbar_id);
                    }

                    z = "ปิดจบเรียบร้อย";
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage().toString();
            }
            return z;
        }
    }

    public void popPickMat(final String barid) {

        final Dialog dialog = new Dialog(ar_list.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pickmat);
        dialog.setCancelable(true);

        ListView lvpickmat = (ListView) dialog.findViewById(R.id.lvpickmat);

        String[] from = {"arktx","vbeln","ar_name"};
        int[] views = {R.id.p_arktx,R.id.p_vbeln,R.id.p_arname };
        final SimpleAdapter ADA = new SimpleAdapter(ar_list.this,
                picklist, R.layout.adp_list_pickmat, from,
                views){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                return view;
            }
        };

        lvpickmat.setAdapter(ADA);
        lvpickmat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                        .getItem(arg2);
                String pposnr = (String) obj.get("loc");
                String pvbeln = (String) obj.get("Storage_Bin");

                Intent i = new Intent(getApplicationContext(), AddProducts.class);
                i.putExtra("posnr", pposnr);
                i.putExtra("vbeln", pvbeln);
                i.putExtra("bar_id", barid);

                startActivity(i);

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(2000);
                arg1.startAnimation(animation1);



                dialog.dismiss();

            }

        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {


            }
        });

        dialog.show();

    }

    void checkComplete(String cStat){
        if(!usrHelper.getLevel().equals("0") || usrHelper.getPlant().equals("OPS") || usrHelper.getPlant().equals("SPS") || usrHelper.getPlant().equals("SPN") || usrHelper.getUserName().equals("Wassana.k")){


            if(cStat!=null){
                closedBtn.setVisibility(View.VISIBLE);
                closeBtn.setVisibility(View.GONE);

            }else{
                closedBtn.setVisibility(View.GONE);
                closeBtn.setVisibility(View.VISIBLE);

            }
        }else{
            closeBtn.setVisibility(View.GONE);
        }
    }



}

