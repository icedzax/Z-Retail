package com.zubb.jannarongj.z_retail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddProducts extends AppCompatActivity {

    final Context context = this;
    ConnectionClass connectionClass;
    UserHelper usrHelper;
    ProgressBar pbbar;
    EditText hideEdt;
    ListView lvitem;
    Button btn_del,btn_split,btn_pick,btn_complete;
    static Boolean split_flag = false;
    static Boolean transfer = false;

    TextView qweight,tv_up,txtw2,tv_splittxt,tv_limit,tv_rs,tv_w1,tv_w2,tv_documentid,tv_vbeln,tv_ar_name,tv_arktx,tv_wadat,tv_carlicense,tv_tagw,tv_sumw,tv_sumbun,tv_sumqty,tv_wdate1,tv_wdate2;
    String itxt,scanresult,h_unit,h_wdate1,h_wdate2,g_vbeln,g_posnr,h_vbeln,h_posnr,h_ar_name,h_arktx,h_wadat,h_carlicense,h_kunnr,h_matnr,tab_hn,tab_id,tab_bun,h_documentid;
    String h_apr,h_aprby,h_wadatx;
    String user;
    String rmd_date;
    String ver;
    String rmd_charge;
    String r_bundle;
    String r_qty;
    String matcode;
    String rmd_size;
    String rmd_grade;
    String rmd_weight;
    String rmd_qa_grade;
    String rmd_remark;
    String bar_id,dSize,dBar_id,dCharge,dBundle,dVbeln,dUser,dStamp;
    String hm,sm,userPlant,rfsdocid,rfscar;
    int cqty=0,cweight=0;
    int tr = 4 ;

    List<Map<String, String>> stocklist  = new ArrayList<Map<String, String>>();

    public int getErNf() {
        return erNf;
    }

    public void setErNf(int erNf) {
        this.erNf = erNf;
    }

    public int getErNm() {
        return erNm;
    }

    public void setErNm(int erNm) {
        this.erNm = erNm;
    }

    public int getErMm() {
        return erMm;
    }

    public void setErMm(int erMm) {
        this.erMm = erMm;
    }

    public int getErCar() {
        return erCar;
    }

    public void setErCar(int erCar) {
        this.erCar = erCar;
    }

    public int getErDup() {
        return erDup;
    }

    public void setErDup(int erDup) {
        this.erDup = erDup;
    }

    public int getErQi() {
        return erQi;
    }

    public void setErQi(int erQi) {
        this.erQi = erQi;
    }

    int erNf;
    int erNm;
    int erMm;
    int erCar;
    int erDup;

    public int getErApp() {
        return erApp;
    }

    public void setErApp(int erApp) {
        this.erApp = erApp;
    }

    int erApp;

    int erStock; //todo this

    int erQi,h_std = 0,h_ntgew =0;

    List<Map<String, String>> itemlist  = new ArrayList<Map<String, String>>();
    ForegroundColorSpan frcRed = new ForegroundColorSpan(Color.RED);

    Locale THLocale = new Locale("en", "TH");
    NumberFormat nf = NumberFormat.getInstance(THLocale);
    int sumcount=0,sumqty=0,sumweight=0,h_wsum=0,h_asum=0,h_limit=0,h_sqty=0,h_rs=0,h_w1=0,h_w2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                g_vbeln = null;
                g_posnr = null;
            } else {
                g_vbeln = extras.getString("vbeln").trim();
                g_posnr = extras.getString("posnr").trim();
            }
        } else {
            g_vbeln = (String) savedInstanceState.getSerializable("vbeln");
            g_posnr = (String) savedInstanceState.getSerializable("posnr");
        }

        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        hideEdt = (EditText)findViewById(R.id.hedt);
        hideEdt = (EditText)findViewById(R.id.hedt);
        pbbar.setVisibility(View.GONE);

        lvitem =(ListView)findViewById(R.id.lv_item);
        tv_vbeln = (TextView)findViewById(R.id.tv_vbeln);
        tv_arktx = (TextView)findViewById(R.id.tv_arktx);
        tv_wadat = (TextView)findViewById(R.id.tv_wadat);
        tv_carlicense = (TextView)findViewById(R.id.tv_carlicense);
        tv_ar_name = (TextView)findViewById(R.id.tv_arname);
        tv_sumbun = (TextView)findViewById(R.id.tv_sumbun);
        tv_sumqty = (TextView)findViewById(R.id.tv_sumqty);
        tv_sumw = (TextView)findViewById(R.id.tv_sumw);
        tv_tagw = (TextView)findViewById(R.id.tv_tagw);
        tv_wdate1 = (TextView)findViewById(R.id.tv_wdate1);
        tv_wdate2 = (TextView)findViewById(R.id.tv_wdate2);
        tv_w1 = (TextView)findViewById(R.id.tv_w1);
        tv_w2 = (TextView)findViewById(R.id.tv_w2);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_split = (Button)findViewById(R.id.btn_split);
        tv_documentid =(TextView)findViewById(R.id.tv_documentid);
        tv_limit = (TextView)findViewById(R.id.tv_limit);
        tv_rs = (TextView)findViewById(R.id.tv_rs);
        tv_splittxt = (TextView)findViewById(R.id.tv_splittxt);
        tv_splittxt.setVisibility(View.GONE);
        txtw2 = (TextView)findViewById(R.id.txtw2);
        tv_up = (TextView)findViewById(R.id.tv_up);
        btn_pick = (Button)findViewById(R.id.btn_pick);
        btn_complete = (Button)findViewById(R.id.btn_complete);

        btn_complete.setVisibility(View.GONE);
        btn_del.setVisibility(View.GONE);
        btn_split.setVisibility(View.GONE);
        btn_pick.setVisibility(View.GONE);


        if(!usrHelper.getLevel().equals("10")){
            btn_pick.setVisibility(View.GONE);
        }

        userPlant = usrHelper.getPlant();
        FillList fillList = new FillList();
        fillList.execute(g_vbeln.trim(),g_posnr.trim());

        /*if(h_apr!=null || !"null".equals(h_apr)){
            alreadyApp(true);
        }else{
            alreadyApp(false);
        }*/


        user = usrHelper.getUserName();
        ver = usrHelper.getVer();

        btn_split.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (getErCar()!=0) {
                    onErrorDialog(0,getErCar(),0,0,0,0,getErApp());
                }else{
                    if(split_flag==true){
                        lvitem.setVisibility(View.VISIBLE);
                        tv_splittxt.setVisibility(View.GONE);
                        btn_split.setText("เต็มมัด");
                        split_flag = false;
                        // Toast.makeText(AddProducts.this, split_flag.toString(), Toast.LENGTH_SHORT).show();
                    }else{
                        tv_splittxt.setVisibility(View.VISIBLE);
                        lvitem.setVisibility(View.GONE);
                        btn_split.setText("ขึ้นเศษ");
                        split_flag = true;
                        // Toast.makeText(AddProducts.this, split_flag.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        hideEdt.requestFocus();
        hideEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            String demo ="";
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if(hideEdt.getText().toString().trim().contains("DEMO")){
                        demo = hideEdt.getText().toString().trim().replace("DEMO","").replace("*","").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").trim();

                    }else{
                        demo = hideEdt.getText().toString().trim().replace("*","").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").trim();
                    }
                    insertSCAN(demo);
                }

                return false;
            }

        });

        btn_complete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (getErCar()!=0) {
                    onErrorDialog(0,getErCar(),0,0,0,0,getErApp()); //
                } else {

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddProducts.this);
                    builder.setTitle("ปิดจบ");
                    builder.setMessage("ยืนยันการปิดจบ ?");
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CompleteDo cp = new CompleteDo();
                            cp.execute(h_vbeln,h_posnr);
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
            }
        });


        btn_del.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (getErCar()!=0) {
                    onErrorDialog(0,getErCar(),0,0,0,0,getErApp());
                } else {

                    if (tab_hn == null) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(AddProducts.this);
                        builder.setMessage("กรุณาเลือกรายการที่จะลบ");
                        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();

                    } else {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(AddProducts.this);
                        builder.setMessage("ลบรายการ " + tab_hn + " หรือไม่ ?");
                        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DeletePro deletePro = new DeletePro();
                                deletePro.execute(tab_id);
                                FillList fillList = new FillList();
                                fillList.execute(g_vbeln.trim(),g_posnr.trim());

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
                }
            }
        });

        btn_pick.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (getErCar()!=0) {
                    onErrorDialog(0,getErCar(),0,0,0,0,getErApp());
                } else {
                    FetchStock fstock = new FetchStock();
                    fstock.execute(h_matnr,userPlant);
                }
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent KEvent)
    {
        int keyaction = KEvent.getAction();

        if(keyaction == KeyEvent.ACTION_DOWN)
        {
            int keycode = KEvent.getKeyCode();

            if(keycode == 120){
                hideEdt.requestFocus();
            }
        }
        return super.dispatchKeyEvent(KEvent);
    }
    public void insertSCAN(String rsscan){

        if(rsscan.length()>0){
            this.scanresult = rsscan;
            if(rsscan.equals(h_matnr.trim())){
                AddProScan addProScan = new AddProScan();
                addProScan.execute(rsscan,"R"); // retail
                split_flag = true;
            }else{
                AddProScan addProScan = new AddProScan();
                addProScan.execute(rsscan,"W");

            }


            this.hideEdt.setText("");
        }else{
            this.hideEdt.setText("");
        }
        this.hideEdt.setText("");
    }


    public class AddProScan extends AsyncTask<String, String, String> {

        String z = "";
        String shn,sqty;

        Boolean isSuccess = false;
        Boolean isRetail = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {

            //Toast.makeText(ReceiveTransf.this,xx,Toast.LENGTH_SHORT).show();

            if(isSuccess==true) {
                if(split_flag==false){
                    Toast.makeText(AddProducts.this, z, Toast.LENGTH_SHORT).show();
                }else{
                    if(isRetail==true){
                        splitInsert(scanresult, 1,true); //todo retail
                    }else{
                        splitInsert(shn, Integer.parseInt(sqty),false);
                    }
                }
            }else{
                onErrorDialog(getErDup(),getErCar(),getErNf(),getErMm(),getErNm(),getErQi(),getErApp());
            }
            pbbar.setVisibility(View.GONE);
            FillList fillList = new FillList();
            fillList.execute(g_vbeln.trim(),g_posnr.trim());
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "พบปัญหาการเชื่อมต่อ";
                } else {
                    String barCheck = "" ;
                    String cDup = "" ;


                    String checkEx = "Select bar_id from vw_barcode_item where bar_id = '" + params[0].trim() + "' ";
                    PreparedStatement getCheck = con.prepareStatement(checkEx);
                    ResultSet cks = getCheck.executeQuery();
                    while (cks.next()) {
                        barCheck = cks.getString("bar_id");
                    }
//todo car set get setErCar()
                    setErDup(0);/*setErCar(0);*/setErNm(0);setErMm(0);setErNf(0);setErQi(0);
                    if(params[1].equals("R")){
                        if(getErCar()==0){
                            isRetail = true;
                            isSuccess = true;
                        }else{
                            isSuccess = false;
                        }

                    }else {
                        if (barCheck == null || barCheck.equals("")) {
                            setErNf(1);
                        } else {

                            String rfsticket = "select TICKET,carlicense from tbl_shipmentplan where vbeln = '"+g_vbeln+"' and posnr = '"+g_posnr+"'";
                            PreparedStatement rfs = con.prepareStatement(rfsticket);
                            ResultSet rfst = rfs.executeQuery();

                            while (rfst.next()) {
                                rfsdocid = rfst.getString("TICKET");
                                rfscar= rfst.getString("carlicense");
                            }
                            if(rfsdocid==null){
                                rfsdocid="";
                            }
                            if(rfscar==null){
                                rfscar="";
                            }



                            String checkDup = "Select top 1 * from tbl_shipment_item where item_barcode = '" + params[0].trim() + "' and split_bundle = 0 and left(vbeln,1) <> '3'    order by add_stamp desc  ";
                            PreparedStatement cd = con.prepareStatement(checkDup);
                            ResultSet cdps = cd.executeQuery();


                            while (cdps.next()) {
                                dBar_id = cdps.getString("item_barcode");
                                dCharge = cdps.getString("charge");
                                dBundle = cdps.getString("bundle");
                                dUser = cdps.getString("user_add");
                                dStamp = cdps.getString("add_stamp");
                                dVbeln = cdps.getString("vbeln");
                                dSize = cdps.getString("size");
                                cDup = cdps.getString("item_barcode");
                            }

                            if (cDup.equals("")) {
                                setErDup(0);
                            } else {
                                setErDup(1);
                            }

                            String scanS = "SELECT bar_id,rmd_date " +
                                    " ,rmd_charge,r_bundle,r_qty,matcode " +
                                    " ,rmd_size,rmd_grade " +
                                    " ,rmd_weight,rmd_qa_grade,rmd_remark " +
                                    "  FROM  " +
                                    "  vw_barcode_item where bar_id = '" + params[0].trim() + "' ";
                            PreparedStatement getDataSt = con.prepareStatement(scanS);
                            ResultSet rs = getDataSt.executeQuery();
                            while (rs.next()) {

                                bar_id = rs.getString("bar_id");
                                rmd_date = rs.getString("rmd_date");
                                //rmd_no = rs.getString("rmd_no");
                                rmd_charge = rs.getString("rmd_charge");
                                r_bundle = rs.getString("r_bundle");
                                r_qty = rs.getString("r_qty");
                                matcode = rs.getString("matcode");
                                //rmd_period = rs.getString("rmd_period");
                                //rmd_spec = rs.getString("rmd_spec");
                                rmd_size = rs.getString("rmd_size");
                                rmd_grade = rs.getString("rmd_grade");
                                //rmd_length = rs.getString("rmd_length");
                                rmd_weight = rs.getString("rmd_weight");
                                rmd_qa_grade = rs.getString("rmd_qa_grade");
                                rmd_remark = rs.getString("rmd_remark");
                                //rmd_plant = rs.getString("rmd_plant");
                                //rmd_station = rs.getString("rmd_station");

                            }
                            /*String submat = "3";
                            String qmatcode = matcode.substring(0,3)+""+matcode.substring(4);
                            if (h_matnr.substring(0, 3).equals("RBF") || h_matnr.substring(0, 3).equals("RBM") || h_matnr.substring(0, 3).equals("DBM") || h_matnr.substring(0, 3).equals("DBF")) {
                                submat="2";
                                qmatcode = matcode.substring(0,2)+""+matcode.substring(4);
                            }

                            String sqlPcs = "SELECT sum(isnull(qty,0)) as q from tbl_shipment_item where charge = '"+rmd_charge+"' and bundle = '"+r_bundle+"' and left(mat_code,"+submat+")+''+substring(mat_code,5,len(mat_code)) = '"+qmatcode+"' and left(vbeln,1) <> '3' ";
                            PreparedStatement qpcs = con.prepareStatement(sqlPcs);
                            ResultSet rspcs = qpcs.executeQuery();


                            while (rspcs.next()) {
                                cqty = rspcs.getInt("q");

                            }
                            int rqty = Integer.parseInt(r_qty);
                            int rweight = (Integer.parseInt(rmd_weight)/Integer.parseInt(r_qty));
                            if((cqty-rqty) < 0){
                                rqty = cqty  ;
                            }else{
                                rqty = (cqty-rqty);
                            }
                            rweight = rweight*rqty;
*/
                            //  Log.d("XX-query",sqlPcs);
                            //  Log.d("XX-qmat",qmatcode);

                            shn = rmd_charge + "-" + r_bundle;
                            sqty = r_qty;

                            if (matcode == null || matcode.equals("") || matcode.length() < 4) {
                                setErNm(1);
                            } else {

                                String hmat = h_matnr.trim();
                                String smat = matcode.trim();

                                int mm = 3;
                                if (hmat.substring(0, 3).equals("RBF") || hmat.substring(0, 3).equals("RBM") || hmat.substring(0, 3).equals("DBM") || hmat.substring(0, 3).equals("DBF")) {
                                    mm = 2;
                                }

                                if(transfer==true){
                                    tr = 4 ;
                                    setErQi(0);
                                }else{
                                    tr = 3;
                                    if(rmd_qa_grade.trim().equals("QI")){
                                        setErQi(1);
                                    }
                                }

                                sm = smat.substring(0, mm) + "" + smat.substring(tr, smat.length());
                                hm = hmat.substring(0, mm) + "" + hmat.substring(tr, hmat.length());

                                if (hm.equals(sm)) {
                                    setErMm(0);
                                } else {
                                    setErMm(1);
                                }

                            }
                            // Log.d("XX-hm",hm+""+tr);
                            // Log.d("XX-sm",sm+""+tr);

                            if (getErDup() == 0 && getErCar()==0 && getErNf() == 0 && getErMm() == 0 && getErNm() == 0 && getErApp() == 0) {

                                if (split_flag == false) {

                                    String insrt = "insert into tbl_shipment_item (VBELN,POSNR,charge,bundle,grade,size,weight," +
                                            "location,add_stamp,item_barcode,user_add,del_stamp,del_user,qty,unit,mat_code,WADAT," +
                                            "carlicense,DocumentId,qa_grade,KUNNR,AR_NAME,split_bundle,rmd_date) " +
                                            "values ('" + h_vbeln + "','" + h_posnr + "','" + rmd_charge + "','" + r_bundle + "','" + rmd_grade + "'," +
                                            "'" + h_arktx + "','" + rmd_weight + "','" + usrHelper.getPlant() + "',current_timestamp,'" + params[0] + "','" + user + "_" + ver + "'," +
                                            "NULL,NULL,'" + r_qty + "','" + h_unit + "','" + matcode + "','" + h_wadatx + "','" + rfscar + "','" + rfsdocid + "'," +
                                            "'" + rmd_qa_grade + "','" + h_kunnr + "','" + h_ar_name + "',0,'" + rmd_date + "')";

                                    PreparedStatement preparedStatement = con.prepareStatement(insrt);
                                    preparedStatement.executeUpdate();
                                    isSuccess = true;
                                    z = "บันทึกเรียบร้อยแล้ว";

                                }
                                isSuccess = true;

                            } else {
                                isSuccess = false;

                            }
                        }
                    }
                }

            } catch (Exception ex) {
                isSuccess = false;
                itxt = ex.getMessage().toString();
            }
            return z ;
        }
    }

    public class SplitScan extends AsyncTask<String, String, String> {

        String z = "";

        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {


            //Toast.makeText(ReceiveTransf.this,xx,Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {

                FillList fillList = new FillList();
                fillList.execute(g_vbeln.trim(),g_posnr.trim());


                Toast.makeText(AddProducts.this, z, Toast.LENGTH_SHORT).show();
            }else{
                onErrorDialog(getErDup(),getErCar(),getErNf(),getErMm(),getErNm(),getErQi(),getErApp());

            }
            pbbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "พบปัญหาการเชื่อมต่อ";
                } else {
                    int maxspl = 0;

                    String maxSp = "if exists (select top 1 isnull(split_bundle,0)+1 as split_bundlex from tbl_shipment_item where item_barcode = '" + params[0].trim() + "')" +
                            " begin select top 1 isnull(split_bundle,0)+1 as split_bundlex from tbl_shipment_item where item_barcode = '" + params[0].trim() + "' order by add_stamp desc " +
                            " end" +
                            " else" +
                            " begin" +
                            " select top 1 '1' as split_bundlex from tbl_shipment_item " +
                            " end ";
                    PreparedStatement ms = con.prepareStatement(maxSp);
                    ResultSet rms = ms.executeQuery();

                    if (params[2].equals("R")) {

                    }

                    while (rms.next()) {
                        maxspl = rms.getInt("split_bundlex");
                    }
                    double ntgew = h_ntgew;
                    double limit = h_limit;
                    double rs = h_rs;

                    int nweight = (int) ((ntgew / limit) * rs);
                    if (getErDup() == 0 && getErCar() == 0 && getErNf() == 0 && getErMm() == 0 && getErNm() == 0 && getErApp() == 0) {
                        if (params[2].equals("R")) {

                            String insrt = "insert into tbl_shipment_item (VBELN,POSNR,charge,bundle,grade,size,weight," +
                                    "location,add_stamp,item_barcode,user_add,del_stamp,del_user,qty,unit,mat_code,WADAT," +
                                    "carlicense,DocumentId,qa_grade,KUNNR,AR_NAME,split_bundle,rmd_date) " +
                                    "values ('" + h_vbeln + "','" + h_posnr + "','RETAIL','" + maxspl + "',NULL," +
                                    "'" + h_arktx + "','" + nweight + "','" + usrHelper.getPlant() + "',current_timestamp,'" + params[0] + "','" + user + "_" + ver + "'," +
                                    "current_timestamp,'" + user + "_" + ver + "','" + h_rs + "','" + h_unit + "','" + params[0] + "','" + h_wadatx + "','" + rfscar + "','" + rfsdocid + "'," +
                                    "'A','" + h_kunnr + "','" + h_ar_name + "'," + maxspl + ",NULL)";

                            PreparedStatement preparedStatement = con.prepareStatement(insrt);
                            preparedStatement.executeUpdate();
                            isSuccess = true;
                            z = "บันทึกเรียบร้อยแล้ว";
                        } else {
                            int paramQty = Integer.parseInt(params[1]);
                            nweight = (int) ((ntgew / limit) * paramQty);
                            if (params[1] == null || (paramQty <= 0)) {
                                params[1] = r_qty;
                                nweight = Integer.parseInt(rmd_weight);
                            }

                            String insrt = "insert into tbl_shipment_item (VBELN,POSNR,charge,bundle,grade,size,weight," +
                                    "location,add_stamp,item_barcode,user_add,del_stamp,del_user,qty,unit,mat_code,WADAT," +
                                    "carlicense,DocumentId,qa_grade,KUNNR,AR_NAME,split_bundle,rmd_date) " +
                                    "values ('" + h_vbeln + "','" + h_posnr + "','" + rmd_charge + "','" + r_bundle + "','" + rmd_grade + "'," +
                                    "'" + h_arktx + "','" + nweight + "','ZUBB',current_timestamp,'" + params[0] + "','" + user + "_" + ver + "'," +
                                    "current_timestamp,'" + user + "_" + ver + "','" + params[1] + "','" + h_unit + "','" + matcode + "','" + h_wadatx + "','" + rfscar + "','" + rfsdocid + "'," +
                                    "'" + rmd_qa_grade + "','" + h_kunnr + "','" + h_ar_name + "'," + maxspl + ",'" + rmd_date + "')";

                            PreparedStatement preparedStatement = con.prepareStatement(insrt);
                            preparedStatement.executeUpdate();
                            isSuccess = true;
                            z = "บันทึกเรียบร้อยแล้ว";
                        }

                    }else{
                        isSuccess = false;
                    }
                }

            } catch (Exception ex) {
                isSuccess = false;
                itxt = ex.getMessage().toString();
            }
            return z ;
        }
    }

    public void onErrorDialog(int eDup , int eCar ,int notFound, int misMat, int nomat, int qi,int app) {
        String msg = "";
        if(scanresult==null || scanresult.equals("")){
            scanresult = "PICK" ;
        }
        if(eDup==0 && eCar==0 && notFound ==0 && misMat ==0 && nomat==0 && app==0){
            msg = itxt+"\n\n"+"Code "+scanresult;
        }
        if(eCar == 1){
            msg = "รถยังไม่ได้ชั่งเข้า ไม่สามารถทำรายการได้";
        } else if (eCar ==2){
            msg = "รถชั่งออกไปแล้ว ไม่สามารถทำรายการได้";
        }else {
            if (notFound == 1) {
                msg = "ไม่พบข้อมูลที่สแกน !" + "\n\n" + "Code " + scanresult;
            } else {

                if(app == 1) {
                    msg = "ตั๋วนี้ถูกปิดจบไปแล้ว โดย \n" +h_aprby+ " เวลา "+h_apr+"\n\n" + "Code " + scanresult;
                }
                else if (eDup == 1) {
                    msg = "ซ้ำ HN " + dCharge + "-" + dBundle + "\nสินค้า : " + dSize + "\nเอกสาร : " + dVbeln + " " + "\nโดย " + dUser.substring(0, dUser.length() - 4) + "\nวันที่ " + dStamp.substring(0, 16) + "\n\n" + "Code " + scanresult;
                } else if (misMat == 1) {
                    msg = "ชนิดสินค้าไม่ตรงกับเอกสาร \n" + "เอกสาร " + h_matnr + "\n" + "ยิงได้ " + matcode + "\n\n" + "Code " + scanresult;
                } else if (nomat == 1) {
                    msg = "ไม่พบข้อมูล Material Code" + "\n\n" + "Code " + scanresult;
                } else if (qi == 1) {
                    msg = "ไม่สามารถขึ้นของเกรด QI ได้" + "\n\n" + "Code " + scanresult;
                }

            }

                    /*else if (stock == 1) {
                        msg = "ขึ้นของเกิน STOCK\nHN: "+hn+"\nขึ้น : "+qty+" เส้น" + "\nSTOCK : เส้น"+"\n\n" + "Code " + scanresult;
                    }*/
        }

        new AlertDialog.Builder(context)

                .setTitle("ผิดพลาด")
                .setMessage(msg)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
               /* .setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })  */

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";
        Boolean head = false ;

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            if(h_apr!=null){
                setErApp(1);
                alreadyApp(true);
            }else{
                setErApp(0);
                alreadyApp(false);
            }

            String stext  = h_vbeln+"-"+h_posnr;
            String unit = "";
            int start = stext.trim().indexOf("-")+1;
            int end = stext.trim().length();



            SpannableString svbeln = new SpannableString(stext.trim());
            //SpannableStringBuilder  bvblen = new SpannableStringBuilder(stext);
            svbeln.setSpan(frcRed,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //svbeln.setSpan(frcBlack,start-1,start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if(h_vbeln.substring(0,1).equals("3")){
                transfer = true;
            }else{
                transfer = false;
            }

            split_flag = false;
            lvitem.setVisibility(View.VISIBLE);
            tv_splittxt.setVisibility(View.GONE);
            btn_split.setText("เต็มมัด");

            // Log.d("unit",h_unit);

            if(h_unit.equals("KG")){
                unit =" KG.";
            }else if(h_unit.equals("ROL")){
                unit =" ม้วน";
            }else if(h_unit.equals("PC")){
                unit =" เส้น.";
            }



            if(h_wdate1==null && h_wdate2==null){
                setErCar(1);
            }

            if(h_wdate1!=null){
                if(h_wdate1.equals(h_wdate2)){
                    setErCar(0);
                    tv_wdate2.setVisibility(View.GONE);
                    txtw2.setVisibility(View.GONE);
                    tv_up.setVisibility(View.VISIBLE);
                    tv_up.setText("ขึ้นของ");
                }else{
                    setErCar(2);
                    tv_wdate2.setVisibility(View.VISIBLE);
                    txtw2.setVisibility(View.VISIBLE);
                    tv_up.setVisibility(View.GONE);
                    tv_wdate2.setText(h_wdate2);
                }
            }

            if(h_wdate1==null){
                h_wdate1 ="--:--";
            }
            if(h_wdate2==null){
                h_wdate2 ="--:--";
            }

            tv_vbeln.setText(svbeln);
            tv_arktx.setText(h_arktx);
            tv_wadat.setText(h_wadat);
            tv_carlicense.setText(h_carlicense);
            tv_ar_name.setText(h_ar_name);
            tv_documentid.setText(h_documentid);
            tv_wdate1.setText(h_wdate1);
            tv_wdate2.setText(h_wdate2);

            tv_w1.setText(""+nf.format(h_w1));
            tv_w2.setText(""+nf.format(h_w2));
            tv_tagw.setText(""+nf.format(h_wsum));
            tv_sumw.setText(""+nf.format(h_asum));
            tv_limit.setText(""+nf.format(h_limit)+""+unit);
            tv_rs.setText(""+nf.format(h_rs)+""+unit);


            String[] from = {"ids","charge","qty","rmd_weight","rmd_qa_grade","rmd_remark" };
            final int[] views = {R.id.id,R.id.charge,R.id.qty,R.id.weight,R.id.qa_grade,R.id.remark};
            final SimpleAdapter ADA = new SimpleAdapter(AddProducts.this,
                    itemlist, R.layout.adp_listscan, from,
                    views) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tvRemark = (TextView) view.findViewById(R.id.remark);
                    if(itemlist.get(position).get("rmd_remark")==null || itemlist.get(position).get("rmd_remark").trim().equals("") || itemlist.get(position).get("rmd_remark").trim().equals("null")){
                        tvRemark.setVisibility(View.GONE);

                    }
                    return view;
                }
            };

            lvitem.setAdapter(ADA);
            lvitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    String t_id = (String) obj.get("id");
                    String t_charge = (String) obj.get("charge");
                    String t_bundle = (String) obj.get("bundle");

                    tab_hn = t_charge;
                    tab_id = t_id;
                    tab_bun = t_bundle;

                    arg1.setSelected(true);

                }
            });

            lvitem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                    if (getErCar()!=0 || getErApp()!=0) {
                        onErrorDialog(0,getErCar(),0,0,0,0,getErApp());
                    }else {
                        HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                                .getItem(arg2);
                        String qhn = (String) obj.get("charge");
                        int qqty = Integer.parseInt((String) obj.get("qty"));
                        double qweight = Integer.parseInt((String) obj.get("rmd_weight"));

                        tab_id = (String) obj.get("id");

                        arg1.setSelected(true);

                        adjQty(qhn, qqty,qweight);
                    }
                    return true;
                }
            });

            tv_sumbun.setText(""+nf.format(sumcount)+" มัด");
            tv_sumqty.setText(""+nf.format(sumqty)+" เส้น");

            //Toast.makeText(AddProducts.this,getErCar(),Toast.LENGTH_SHORT).show();

            //Log.d("h_apr", h_apr+" , "+h_aprby);



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

                    if (!params[0].equals(null)) {

                        where = "where vbeln = '" + params[0] + "' and posnr = '" + params[1] + "' ";
                    }

                    String headqry = "select convert(nvarchar(20),cast(wadat as datetime),103) as wadat2 ,*,isnull(NTGEW,0) as NTGEWx ,round(wsum,0) as wsum2,round(asum,0) as asum2 from vw_wsum_p8 " + where ;
                    PreparedStatement hps = con.prepareStatement(headqry);
                    ResultSet hrs = hps.executeQuery();

                    while (hrs.next()) {
                        h_wadat = hrs.getString("wadat2");
                        h_wadatx = hrs.getString("wadat");
                        h_vbeln = hrs.getString("vbeln");
                        h_posnr = hrs.getString("posnr");
                        h_kunnr = hrs.getString("KUNNR");
                        h_ar_name = hrs.getString("AR_NAME");
                        h_carlicense = hrs.getString("CARLICENSE");
                        h_matnr = hrs.getString("MATNR");
                        h_arktx = hrs.getString("ARKTX");
                        h_documentid = hrs.getString("documentid");
                        h_wdate1 = hrs.getString("INTIME");
                        h_wdate2 = hrs.getString("OUTTIME");
                        h_w1 = hrs.getInt("W1Weight");
                        h_w2 = hrs.getInt("W2Weight");
                        h_wsum = hrs.getInt("wsum2");
                        h_asum = hrs.getInt("asum2");
                        h_sqty = hrs.getInt("sqty");
                        h_limit = hrs.getInt("LFIMG");
                        h_unit = hrs.getString("unit");
                        h_rs = hrs.getInt("rs");
                        h_ntgew = hrs.getInt("NTGEWx");


                    }
                    String itemquery = "SELECT item_id,charge,bundle,qty,weight,qa_grade,remark from tbl_shipment_item " + where+ " order by add_stamp desc ";
                    PreparedStatement itemq = con.prepareStatement(itemquery);
                    ResultSet irs = itemq.executeQuery();
                    int ids = 0 ;
                    itemlist.clear();

                    while (irs.next()) {
                        Map<String, String> datascan = new HashMap<String, String>();
                        ids++;
                        datascan.put("ids",String.valueOf(ids));
                        datascan.put("id",irs.getString("item_id"));
                        datascan.put("charge",irs.getString("charge")+"-"+irs.getString("bundle"));
                        datascan.put("bundle",irs.getString("bundle"));
                        datascan.put("qty",irs.getString("qty"));
                        datascan.put("rmd_weight", String.valueOf(irs.getInt("weight")));
                        datascan.put("rmd_qa_grade",irs.getString("qa_grade"));
                        datascan.put("rmd_remark",irs.getString("remark"));
                        itemlist.add(datascan);
                    }

                    String sumq = "SELECT count(item_barcode) AS sc ,sum(qty) as sq ,sum(weight) AS sw  FROM tbl_shipment_item  "+where ;
                    PreparedStatement qps = con.prepareStatement(sumq);
                    ResultSet qrs = qps.executeQuery();

                    while (qrs.next()) {
                        sumcount = qrs.getInt("sc");
                        sumqty = qrs.getInt("sq");
                        //sumweight = qrs.getInt("sw");
                    }

                    String qapr = "SELECT ApproveOn,ApproveBy from tbl_shipmentplan "+where ;
                    PreparedStatement aps = con.prepareStatement(qapr);
                    ResultSet ars = aps.executeQuery();

                    while (ars.next()) {
                        h_apr = ars.getString("ApproveOn");
                        h_aprby = ars.getString("ApproveBy");
                        //sumweight = qrs.getInt("sw");
                    }



                }

            } catch (Exception ex) {

                z = ex.getMessage().toString();

            }

            return z;
        }
    }

    public class FetchStock extends AsyncTask<String, String, String> {

        Boolean isSuccess =false;
        String z = "";
        String Fmat = "";

        //TODO >>>>>>>>>>>>>>>>>>>>> haha <<<<<<<<<<<<<<<<<<<<
        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            if(isSuccess==true){
                stockPick(Fmat,userPlant);
            }else{

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

    public void ManaulDialog() {

        final Dialog mdialog = new Dialog(AddProducts.this);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.dialog_manaul);
        mdialog.setCancelable(true);
        mdialog.setTitle("คีย์ข้อมูล");

        TextView marktx;
        Button mbtnSave,mbtnCan;
        mbtnCan = (Button) mdialog.findViewById(R.id.mbtnCan);
        mbtnSave = (Button) mdialog.findViewById(R.id.mbtnSave);
        marktx =(TextView) mdialog.findViewById(R.id.marktx);
        marktx.setText(h_arktx);

        mbtnSave.setOnClickListener(new View.OnClickListener() {

            EditText edHn = (EditText) mdialog.findViewById(R.id.ed_hn);
            EditText edBn = (EditText) mdialog.findViewById(R.id.ed_bun);
            EditText edQty = (EditText) mdialog.findViewById(R.id.ed_qty);

            public void onClick(View v) {

                PickInsert pickI = new PickInsert();
                pickI.execute(h_matnr,edQty.getText().toString().trim(),edHn.getText().toString(),edBn.getText().toString().trim(),"MN");

                mdialog.dismiss();
            }
        });
        mbtnCan.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                mdialog.dismiss();
            }
        });

        mdialog.show();

    }

    public void stockPick(String pickmat, String pickplant){

        final Dialog pickdialog = new Dialog(AddProducts.this);
        pickdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pickdialog.setContentView(R.layout.dialog_stockpick);

        pickdialog.setCancelable(true);
        TextView stock_arktx;
        ListView liststock;
        Button keyBtn;

        stock_arktx = (TextView)pickdialog.findViewById(R.id.stock_arktx);
        liststock   = (ListView)pickdialog.findViewById(R.id.liststock);
        keyBtn = (Button)pickdialog.findViewById(R.id.keyBtn);

        stock_arktx.setText(h_arktx);

        String[] from = {"LGORT","CHARG","qty"};
        int[] views = {R.id.stock_loc,R.id.stock_batch ,R.id.stock_qty  };
        final SimpleAdapter AdapVbeln = new SimpleAdapter(AddProducts.this,
                stocklist, R.layout.adp_list_stock, from,
                views){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                return view;
            }
        };

        liststock.setAdapter(AdapVbeln);
        liststock.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                HashMap<String, Object> obj = (HashMap<String, Object>) AdapVbeln
                        .getItem(arg2);

                String pickqty = (String) obj.get("qty");
                String pickmatnr = (String) obj.get("MATNR");
                String pick_rmd_charge= (String) obj.get("rmd_charge");
                String pick_rmd_bundle = (String) obj.get("rmd_bundle");

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(2000);
                arg1.startAnimation(animation1);

                PickInsert pickI = new PickInsert();
                pickI.execute(pickmatnr,pickqty,pick_rmd_charge,pick_rmd_bundle,"ST");

                pickdialog.dismiss();

            }

        });

        keyBtn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                ManaulDialog();
                pickdialog.dismiss();
            }
        });


        pickdialog.show();

    }

    public void adjQty(String hn,final int qty,final Double weight){

        final Dialog adjdialog = new Dialog(AddProducts.this);
        adjdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        adjdialog.setContentView(R.layout.dialog_adjust);

        adjdialog.setCancelable(true);
        TextView thn ,tarktx,oqty;
        EditText eqty;
        Button svbtn,cnbtn;
        qweight = (TextView)adjdialog.findViewById(R.id.tv_weight);
        thn = (TextView)adjdialog.findViewById(R.id.thn);
        oqty= (TextView)adjdialog.findViewById(R.id.tv_oqty);
        eqty = (EditText) adjdialog.findViewById(R.id.eqty);
        tarktx = (TextView)adjdialog.findViewById(R.id.tarktx);
        svbtn =(Button)adjdialog.findViewById(R.id.btnQtysv) ;
        cnbtn =(Button)adjdialog.findViewById(R.id.btnCan) ;
        thn.setText(hn);
        eqty.setText("");
        tarktx.setText(h_arktx);
        oqty.setText(""+qty);
        qweight.setText(""+Math.round(weight));
        double mm = 0.0;

        mm = (weight/qty);

        final double finalMm = mm;
        eqty.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 )
                    qweight.setText(""+(Math.round(Double.parseDouble(String.valueOf(s))* finalMm)));
            }
        });


        svbtn.setOnClickListener(new View.OnClickListener() {
            EditText eqty = (EditText) adjdialog.findViewById(R.id.eqty);

            public void onClick(View v) {
                String qqty ;
                String qqweight;

                qqty = eqty.getText().toString().trim();
                qqweight = qweight.getText().toString().trim();


                AdjItem adji = new AdjItem();
                adji.execute(qqty,bar_id,qqweight);
                adjdialog.dismiss();

            }
        });
        cnbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                adjdialog.dismiss();
            }
        });

        adjdialog.show();

    }

    public void splitInsert(String hn, int qty, final Boolean retail){


        final Dialog spltdialog = new Dialog(AddProducts.this);
        spltdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        spltdialog.setContentView(R.layout.dialog_split);

        spltdialog.setCancelable(true);
        TextView thn ,tarktx,oqty;
        EditText eqty;
        Button svbtn,cnbtn;
        thn = (TextView)spltdialog.findViewById(R.id.thn);
        oqty= (TextView)spltdialog.findViewById(R.id.tv_oqty);
        eqty = (EditText) spltdialog.findViewById(R.id.eqty);
        tarktx = (TextView)spltdialog.findViewById(R.id.tarktx);
        svbtn =(Button)spltdialog.findViewById(R.id.btnQtysv) ;
        cnbtn =(Button)spltdialog.findViewById(R.id.btnCan) ;
        thn.setText(hn);
        eqty.setText("");
        tarktx.setText(h_arktx);
        oqty.setText(""+qty);


        svbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText eqty = (EditText) spltdialog.findViewById(R.id.eqty);
                String qqty  ;
                if(eqty.getText().toString().trim()==null || eqty.getText().toString().trim().equals(""))
                {
                    qqty = "0";
                }else{
                    qqty = eqty.getText().toString().trim();
                }
                if(retail==true){
                    SplitScan spScan = new SplitScan();
                    spScan.execute(scanresult,qqty,"R");
                }else{
                    SplitScan spScan = new SplitScan();
                    spScan.execute(bar_id,qqty,"W");
                }


                spltdialog.dismiss();
                split_flag = false;
                lvitem.setVisibility(View.VISIBLE);
                tv_splittxt.setVisibility(View.GONE);
                btn_split.setText("เต็มมัด");


            }
        });
        cnbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                spltdialog.dismiss();
                split_flag = false;
                lvitem.setVisibility(View.VISIBLE);
                tv_splittxt.setVisibility(View.GONE);
                btn_split.setText("เต็มมัด");

            }
        });

        if(retail==true){
            SplitScan spScan = new SplitScan();
            spScan.execute(scanresult,"0","R");
        }else{
            spltdialog.show();
        }

    }


    public class AdjItem extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);

            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();

            FillList fillList = new FillList();
            fillList.execute(g_vbeln.trim(),g_posnr.trim());

        }
        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    if(params[0].equals("")){
                        params[0] = "0";
                    }

                    int paramQty = Integer.parseInt(params[0]);


                    if((paramQty <= 0) ){
                        z = "ไม่มีการเปลี่ยนแปลง";
                        isSuccess = true;
                    }else{
                        String query = "update tbl_shipment_item set qty = '"+params[0]+"' ,  weight = '"+params[2]+"' ," +
                                "split_bundle = (select top 1 isnull(max(split_bundle),0)+1 from tbl_shipment_item where charge = '"+tab_hn+"' and bundle = '"+tab_bun+"' ) " +
                                ", del_stamp = current_timestamp,del_user ='"+user+"_"+ver+"' " +
                                "  WHERE item_id = "+tab_id+"  ";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "แก้ไขเรียบร้อยแล้ว";
                        isSuccess = true;
                    }



                    //Log.d("update",query);

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage().toString();
            }
            return z;
        }
    }

    public class DeletePro extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute(g_vbeln.trim(),g_posnr.trim());
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "delete tbl_shipment_item WHERE item_id = "+params[0]+"  ";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "ลบสำเร็จ";
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "ERROR DEL แจ้ง IT 1203";
            }

            return z;
        }
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
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            // Toast.makeText(AddProducts.this, "HAPR : "+h_apr, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {

                FillList fillList = new FillList();
                fillList.execute(g_vbeln.trim(),g_posnr.trim());

            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "update tbl_shipmentplan  set ApproveOn  = CURRENT_TIMESTAMP , ApproveBy = '"+usrHelper.getUserName()+"' WHERE vbeln = '"+h_vbeln+"' and posnr =  '"+h_posnr+"' ";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "ปิดจบสำเร็จ";
                    h_apr = "x";
                    setErApp(1);
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "ปิดจบไม่สำเร็จ";
            }

            return z;
        }
    }

    public class PickInsert extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {


            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute(g_vbeln.trim(),g_posnr.trim());
                Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            }else{
                onErrorDialog(getErDup(),getErCar(),getErNf(),getErMm(),getErNm(),getErQi(),getErApp());
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

                    int cqty = Integer.parseInt(params[1]);
                    int calweight = (h_ntgew/h_limit)*cqty;
                    String cgrade ="";


                    switch (params[0].substring(3,4)){
                        case "1" : cgrade = "A";
                            break;
                        case "2" : cgrade = "A-";
                            break;
                        case "3" : cgrade = "A2";
                            break;
                        case "4" : cgrade = "Y1";
                            break;
                    }   //pickI.execute(pickmatnr,pickqty,pick_rmd_charge,pick_rmd_bundle);
                    //                  0        1            2               3
                    setErDup(0);/*setErCar(0);*/setErNm(0);setErMm(0);setErNf(0);

                    String checkDup = "Select top 1 * from tbl_shipment_item where charge = '" + params[2].trim() + "' and bundle  = '"+params[3].trim()+"' and mat_code = '"+params[0].trim()+"' and  split_bundle = 0 and left(vbeln,1) <> '3'  order by add_stamp desc  ";
                    PreparedStatement cd = con.prepareStatement(checkDup);
                    ResultSet cdps = cd.executeQuery();

                    String cDup ="";
                    while (cdps.next()) {
                        dBar_id = cdps.getString("item_barcode");
                        dCharge = cdps.getString("charge");
                        dBundle = cdps.getString("bundle");
                        dUser = cdps.getString("user_add");
                        dStamp = cdps.getString("add_stamp");
                        dVbeln = cdps.getString("vbeln");
                        dSize = cdps.getString("size");
                        cDup = cdps.getString("item_barcode");
                    }

                    if (cDup.equals("")) {
                        setErDup(0);
                    } else {
                        setErDup(1);
                    }

                    int maxsplt = 0 ;

                    String cMaxplt = "Select max(split_bundle) as split_bundle from tbl_shipment_item where charge = '" + params[2].trim() + "' and bundle  = '"+params[3].trim()+"' and mat_code = '"+params[0].trim()+"'   ";
                    PreparedStatement mxl = con.prepareStatement(cMaxplt);
                    ResultSet mrs = mxl.executeQuery();

                    while (mrs.next()) {
                        maxsplt = mrs.getInt("split_bundle");
                    }
                    maxsplt = maxsplt+1;

                    //Log.d("params0", params[0]);

                    if (getErDup() == 0 && getErCar()==0 && getErNf() == 0 && getErMm() == 0 && getErNm() == 0 && getErApp() == 0) {

                        String pinsrt = "insert into tbl_shipment_item (VBELN,POSNR,charge,bundle,grade,size,weight," +
                                "location,add_stamp,item_barcode,user_add,del_stamp,del_user,qty,unit,mat_code,WADAT," +
                                "carlicense,DocumentId,qa_grade,KUNNR,AR_NAME,split_bundle,rmd_date) " +
                                "values ('" + h_vbeln + "','" + h_posnr + "','" +params[2]+"','" + params[3] + "',NULL," +
                                "'" + h_arktx + "','" + calweight + "','" + usrHelper.getPlant() + "',current_timestamp,'"+params[4]+"'+''+convert(nvarchar(30),(select max(item_id)+1 from tbl_shipment_item)),'" + user + "_" + ver + "'," +
                                "NULL,NULL,'" + params[1] + "','" + h_unit + "','" + h_matnr + "','" + h_wadatx + "','" + rfscar + "','" + rfsdocid + "'," +
                                "'" + cgrade + "','" + h_kunnr + "','" + h_ar_name + "',"+maxsplt+",NULL)";

                        PreparedStatement preparedStatement = con.prepareStatement(pinsrt);
                        preparedStatement.executeUpdate();
                        z = "บันทึกสำเร็จ";
                        isSuccess = true;
                    }
                    else{
                        isSuccess = false;

                    }
                }


            } catch (Exception ex) {
                isSuccess = false;
                itxt = ex.getMessage().toString();

            }

            return z;
        }
    }


    public void alreadyApp(boolean app){

        if(app == true){
            btn_del.setVisibility(View.GONE);
            btn_split.setVisibility(View.GONE);
            btn_pick.setVisibility(View.GONE);
            btn_complete.setText("ปิดจบเรียบร้อยแล้ว");
            btn_complete.setEnabled(false);
            btn_complete.setTextColor(Color.parseColor("#0C8600"));
            btn_complete.setBackgroundColor(Color.parseColor("#57F947"));
            btn_complete.setVisibility(View.VISIBLE);
        }
        else{
            btn_complete.setVisibility(View.GONE);
            btn_del.setVisibility(View.VISIBLE);
            btn_split.setVisibility(View.VISIBLE);
            if(usrHelper.getLevel().equals("10")){
                btn_pick.setVisibility(View.VISIBLE);
            }

        }



    }



}
