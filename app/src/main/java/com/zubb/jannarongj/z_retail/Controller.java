package com.zubb.jannarongj.z_retail;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {

    public static String getQa_date() {
        return qa_date;
    }

    public static void setQa_date(String qa_date) {
        Controller.qa_date = qa_date;
    }

    static String qa_date;

    ConnectionClass  connectionClass;

    public String isFoundQR(String qr){
        String Rs = "";
        connectionClass = new ConnectionClass();

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {

            } else {

                String stmt = "select bar_id from vw_barcode_item where bar_id = '"+qr+"' " ;

                PreparedStatement getloc = con.prepareStatement(stmt);
                ResultSet rs = getloc.executeQuery();

                while (rs.next()) {
                    Rs =  rs.getString("bar_id");
                }
                    if(Rs==null){
                        Rs = "";
                    }

            }

        } catch (Exception ex) {
            Rs =  ex.getMessage().toString();
            if(Rs==null){
                Rs = "Error";
            }
            // errorText = ex.getMessage();
        }
            return Rs;
    }


    public String TrimQR(String qr){
        if(qr.trim().contains("DEMO")){
            qr = qr.trim().replace("DEMO","").replace("*","").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").trim();
        }else{
            qr = qr.trim().replace("*","").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").trim();
        }
        return qr ;
    }

    public String nullRs(String str){
        if(str==null){
            return "";
        }else{
            return str;
        }
    }

    public List getLocation(String plant){

        connectionClass = new ConnectionClass();
        List<Map<String, String>> locationlist  = new ArrayList<Map<String, String>>();

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                // errorText = "พบปัญหาการเชื่อมต่อ";
            } else {
                String where = "";
                if(plant.equals("OCP")){
                    where = " where plant = 'OCP' ";
                }else{
                    where = " where plant <> 'OCP' ";
                }
                String stmt = "select  * from tbl_remark  "+where ;
                Log.d("remark",stmt);
                PreparedStatement getloc = con.prepareStatement(stmt);
                ResultSet rs = getloc.executeQuery();

                locationlist.clear();
                while (rs.next()) {
                    Map<String, String> datacar = new HashMap<String, String>();
                    datacar.put("t1", rs.getString("remark"));
                    locationlist.add(datacar);

                }

            }

        } catch (Exception ex) {

            // errorText = ex.getMessage();
        }

        return locationlist;
    }

    public List getHN(String hn, String bun){

        connectionClass = new ConnectionClass();
        List<Map<String, String>> hnlist  = new ArrayList<Map<String, String>>();

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                // errorText = "พบปัญหาการเชื่อมต่อ";
            } else {

                String stbun = "";
                stbun = " and r_bundle = '"+bun+"' ";

                if(bun.equals("")){
                    stbun = "";
                }


                String where = " where rmd_charge = '"+hn+"'  "+stbun+" ";

                String stmt = "select rmd_charge,r_bundle,matcode,r_qty,rmd_qa_grade,bar_id from vw_barcode_item "+where ;
                //Log.d("remark",stmt);
                PreparedStatement getloc = con.prepareStatement(stmt);
                ResultSet rs = getloc.executeQuery();

                hnlist.clear();
                while (rs.next()) {
                    Map<String, String> datahn = new HashMap<String, String>();
                    datahn.put("hn", rs.getString("rmd_charge")+"-"+rs.getString("r_bundle"));
                    datahn.put("matcode", nullRs(rs.getString("matcode")));
                    datahn.put("r_qty", rs.getString("r_qty"));
                    datahn.put("rmd_qa_grade", rs.getString("rmd_qa_grade"));
                    datahn.put("bar_id", rs.getString("bar_id"));
                    hnlist.add(datahn);
                }
            }

        } catch (Exception ex) {

            // errorText = ex.getMessage();
        }

        return hnlist;
    }
}
