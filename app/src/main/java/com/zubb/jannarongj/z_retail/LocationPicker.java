package com.zubb.jannarongj.z_retail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LocationPicker {

    private  String rmd,site = "R",pill,ch,rs,plant,sec="F";
    private Context context;
    private static String sSite;
    private static String sPill;

    public static String getsRs() {
        return sRs;
    }

    private static String sRs;
    private static String sCh;
    private static String sSec;

    TextView tv_site,tv_rmd,tv_pill,tv_ch,tv_rs;
    Button btn_sv ;

    public LocationPicker(Context context,String plant){
        this.context = context;
        this.plant = plant;
    }

    public void onOpenDialoag(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_location_picker);
        dialog.setCancelable(true);

        tv_site = (TextView) dialog.findViewById(R.id.tv_site);
        tv_rmd = (TextView) dialog.findViewById(R.id.tv_rmd);
        tv_pill = (TextView) dialog.findViewById(R.id.tv_pil);
        tv_ch = (TextView) dialog.findViewById(R.id.tv_ch);
        tv_rs = (TextView) dialog.findViewById(R.id.tv_rs);
        btn_sv = (Button)dialog.findViewById(R.id.btn_sv) ;

        tv_site.setText(textSite(sSite));
        tv_rmd.setText(textSec(sSec));
        tv_pill.setText(sPill);
        tv_ch.setText(sCh);
        tv_rs.setText(sRs);

        tv_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSiteToggle(tv_site.getText().toString());

            }
        });


        tv_rmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSectionToggle(tv_rmd.getText().toString());
            }
        });

        tv_pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPillClick();

            }
        });

        btn_sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ch!=null) sCh = ch;
                if(sec!=null) sSec = sec;
                if(pill!=null) sPill = pill;
                if(rs!=null) sRs = rs;
                if(site!=null) sSite = site;

                NoShipActivity ns = new NoShipActivity();
                ns.setsLoc(sRs);
                Update(sRs);

                dialog.dismiss();
            }
        });

        tv_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChClick();

            }
        });

        dialog.show();

    }

    public void Update(String txt){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.tv_location);
        txtView.setText(txt);
    }



    private String textSec(String str){
        str = isNull(str);
        switch (str){
            case "F" : str = "หน้า";
                break;
            case "R" : str = "หลัง";
                break;
        }
        return str;
    }

    private String textSite(String str){
        str = isNull(str);
        switch (str){
            case "R" : str = "ขวา";
                break;
            case "L" : str = "ซ้าย";
                break;
        }
        return str;
    }

    public void onOpenDialoagOCP(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_location_picker_ocp);
        dialog.setCancelable(true);

        tv_rmd = (TextView) dialog.findViewById(R.id.tv_rmd);
        tv_ch = (TextView) dialog.findViewById(R.id.tv_ch);
        tv_rs = (TextView) dialog.findViewById(R.id.tv_rs);


        tv_rmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRmdClick();

            }
        });


        tv_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChClick();

            }
        });

        dialog.show();

    }

    private void onRmdClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("โรง");
        String[] rmd_set = {"MR7","MR8","SMD"};
        switch (plant){
            case "OPS" : rmd_set = new String[]{"โรง 5","โรง 6"};
            break;
        }

        builder.setItems(rmd_set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0: rmd = "MR7";
                        break;
                    case 1: rmd = "MR8";
                        break;
                    case 2: rmd = "SMD";
                        break;
                }

                tv_rmd.setText(rmd);
                getResult();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onChClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ช่อง");
        String[] animals = {"ช่อง 1","ช่อง 2", "ช่อง 3", "ช่อง 4", "ช่อง 5","ช่อง 6","ช่อง 7","ช่อง 8","ช่อง 9","ช่อง 10"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0: ch = "1";
                        break;
                    case 1: ch = "2";
                        break;
                    case 2: ch = "3";
                        break;
                    case 3: ch = "4";
                        break;
                    case 4: ch = "5";
                        break;
                    case 5: ch = "6";
                        break;
                    case 6: ch = "7";
                        break;
                    case 7: ch = "8";
                        break;
                    case 8: ch = "9";
                        break;
                    case 9: ch = "10";
                        break;

                }

                tv_ch.setText(ch);
                getResult();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onPillClick(){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pil_picker);
        dialog.setCancelable(true);

        final EditText edtPil = (EditText)dialog.findViewById(R.id.edtPil);
        Button btnSv = (Button)dialog.findViewById(R.id.btnSv);

        btnSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               pill = edtPil.getText().toString().trim();
               tv_pill.setText(pill);
                getResult();
               dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void onSiteToggle (String p_site){
        if(p_site.equals("ซ้าย")){
            site = "R";
            tv_site.setText("ขวา");
        }else {
            site = "L";
            tv_site.setText("ซ้าย");
        }
        getResult();

    }

    private void onSectionToggle (String p_sec){
        if(p_sec.equals("หน้า")){
            sec = "R";
            tv_rmd.setText("หลัง");
        }else {
            sec = "F";
            tv_rmd.setText("หน้า");
        }
        getResult();

    }

    private String isNull(String str){
        if(str==null){
            return "";
        }else{
            return str ;
        }

    }

    private String fPill(String p){
        String formatted = p ;
        if(p!=null && p.length()==1){
            formatted = "0"+p;
        }
        return  formatted ;
    }

    private void getResult(){
        if(ch==null) ch = isNull(sCh);
        if(sec==null) sec = isNull(sSec);
        if(site==null) site = isNull(sSite);
        if(pill==null) pill = isNull(sPill);

        rs = isNull(ch)+isNull(sec)+isNull(site)+"-"+isNull(fPill(pill));

        tv_rs.setText(rs);
    }

    public String getRs(){
        return rs;
    }




}
