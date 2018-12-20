package com.zubb.jannarongj.z_retail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    ProgressBar pbbar;
    ListView list_vbeln ;
    Button q_btn_save;
    TextView kunnr,vbeln,carlicense,q_remark,q_lock;
    String h_kunnr,h_vbeln,h_carlicense,g_vbeln,h_weno,h_matnr,h_arktx,h_documentid,h_lfimg,h_unit;
    String l = "";
    String rm ="";

    List<Map<String, String>> vbelnlist  = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_list);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                g_vbeln = null;

            } else {
                g_vbeln = extras.getString("vbeln").trim();

            }
        } else {
            g_vbeln = (String) savedInstanceState.getSerializable("vbeln");

        }


        kunnr = (TextView)findViewById(R.id.kunnr);
        vbeln = (TextView)findViewById(R.id.vbeln);
        carlicense = (TextView)findViewById(R.id.carlicense);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        list_vbeln = (ListView)findViewById(R.id.lvb);
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();

       // Toast.makeText(ar_list.this, g_vbeln.trim(), Toast.LENGTH_SHORT).show();

        VbelnFList fillList = new VbelnFList();
        fillList.execute(g_vbeln.trim());


    }

    public class VbelnFList extends AsyncTask<String, String, String> {

        String z = "";

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            kunnr.setText(h_kunnr);
            vbeln.setText(h_vbeln);
            carlicense.setText(h_carlicense);


            if(h_documentid==null || h_documentid.equals("")){
                h_documentid = "";
            }

            String[] from = {"posnr","arktx"};
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
                    i.putExtra("posnr", (String) obj.get("posnr"));
                    i.putExtra("vbeln", h_vbeln);

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
                    arg1.setSelected(true);
                    unlockDo(qposnr,qarktx,qlock);
                    return true;
                }
            });
         }

            pbbar.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = " SELECT * FROM vw_wsum_p8 where vbeln = '"+params[0].trim()+"' ";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    vbelnlist.clear();
                    while (rs.next()) {

                        Map<String, String> datanums = new HashMap<String, String>();
                        datanums.put("arktx", rs.getString("arktx"));
                        datanums.put("posnr", rs.getString("posnr"));
                        datanums.put("sscan", rs.getString("sscan"));
                        datanums.put("LOCK", rs.getString("LOCK"));

                        vbelnlist.add(datanums);

                        h_kunnr = rs.getString("AR_NAME");
                        h_matnr = rs.getString("matnr");
                        h_arktx = rs.getString("arktx");
                        h_vbeln = rs.getString("VBELN");
                        h_unit = rs.getString("unit");
                        h_lfimg = rs.getString("LFIMG");
                        h_carlicense = rs.getString("CARLICENSE");

                        h_documentid = rs.getString("DocumentId");

                    }

                    // z = "Success";
                    z = query ;
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
        String[] remark = {"ตัวแปร","ลูกค้าเปลี่ยน","เหล็กมีปัญหา","ระบบมีปัญหา","ตาชั่งมีปัญหา","ขึ้นเร่งด่วน รอบิล","ลืมยิง/ยิงไม่ครบ"};
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

        if (q_remark.getText().toString() == null || q_remark.getText().toString().equals("") || q_remark.getText().toString().equals("เลือกหมายเหตุ") ) {
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

    public void unlockDo(final String qposnr, final String qarktx, final String qlock){

        final Dialog qdialog = new Dialog(ar_list.this);
        qdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        qdialog.setContentView(R.layout.dialog_seq);

        qdialog.setCancelable(true);
        final TextView q_arktx,q_vbeln;

        q_arktx = (TextView)qdialog.findViewById(R.id.q_arktx);
        q_vbeln = (TextView)qdialog.findViewById(R.id.q_vbeln);
        q_lock = (TextView)qdialog.findViewById(R.id.q_lock);
        q_remark = (TextView)qdialog.findViewById(R.id.q_remark);

        q_btn_save = (Button)qdialog.findViewById(R.id.q_btn_save);

        q_arktx.setText(qarktx);
        q_vbeln.setText(h_vbeln);
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

        q_remark.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                onRemarkClick();
            }
        });


        q_btn_save.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                UpdateLockDo ulck = new UpdateLockDo();
                ulck.execute(qposnr,l);
                qdialog.dismiss();
            }
        });

        qdialog.show();

    }

    public class UpdateLockDo extends AsyncTask<String, String, String> {

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
                    if(params[1] ==null || params[1].equals("")){
                        lock = " lock = NULL ";
                    }else {
                        lock = " lock = '"+params[1].trim()+"' ";
                    }

                    String query = "update tbl_shipmentplan set "+lock+" where vbeln = '"+h_vbeln+"' and posnr = '"+params[0]+"'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
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


}

