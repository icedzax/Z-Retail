package com.zubb.jannarongj.z_retail;

import android.annotation.SuppressLint;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jannarong.j on 5/1/2560.
 */
public class ConnectionClass {


  /*  public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        ConnectionClass.ip = ip;
    }

    static String ip;

    public static String getPassword() {
        return password;
    }

    static String password = "" ;



    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "PP";
    String un = "sa";



    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;
        String ConnURL = null;



        if(getIp().equals("116")){
            password = "sipco77";
        }else{
            password = "";
        }
        password = "";

       // Log.d("ip",getIp()+"\n"+getPassword());

        try {

            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://192.168.100.222;"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    +password+ ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }*/

    public static String getUip() {
        return uip;
    }

    public static void setUip(String uip) {
        ConnectionClass.uip = uip;
    }

    public static String getUpass() {
        return upass;
    }

    public static void setUpass(String upass) {
        ConnectionClass.upass = upass;
    }

    public static String uip ;

    public static String getUdbn() {
        return udbn;
    }

    public static void setUdbn(String udbn) {
        ConnectionClass.udbn = udbn;
    }

    public static String udbn ;
    public static String upass ;


    String ip = "192.168.100.222";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "PP";
    String un = "sa";
    String password = "";



    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;
        String ConnURL = null;

        try {

            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + getUip() + ";"
                    + "databaseName=" + getUdbn() + ";user=" + un + ";password="
                    + getUpass() + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }





}