package com.zubb.jannarongj.z_retail;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jannarong.j on 13/6/2560.
 */
public class UserHelper {
    Version vers;
    Context context;
    SharedPreferences sharedPerfs;
    SharedPreferences.Editor editor;

    // Prefs Keys
    static String perfsName = "UserHelper";
    static int perfsMode = 0;

    public static String getVw_name() {
        return vw_name;
    }

    public static void setVw_name(String vw_name) {

        UserHelper.vw_name = vw_name;
    }

    static String vw_name ;

    public UserHelper(Context context) {
        this.context = context;
        this.sharedPerfs = this.context.getSharedPreferences(perfsName, perfsMode);
        this.editor = sharedPerfs.edit();
    }

    public void createSession(String sUserName, String sPassword, String sPlant ,String sLevel) {
        vers = new Version();
        editor.putBoolean("LoginStatus", true);
        editor.putString("Username", sUserName);
        editor.putString("Password", sPassword);
        editor.putString("Version", vers.Version.trim());
        editor.putString("Plant", sPlant);
        editor.putString("Level", sLevel);
        editor.commit();

    }
    public void deleteSession() {
        editor.clear();
        editor.commit();
    }

    public boolean getLoginStatus() {
        return sharedPerfs.getBoolean("LoginStatus", false);
    }
    public String getUserName() {
        return sharedPerfs.getString("Username", null);
    }
    public String getPassword() {
        return sharedPerfs.getString("Password", null);
    }
    public String getVer() {
        return sharedPerfs.getString("Version", null);
    }
    public String getPlant() {
        return sharedPerfs.getString("Plant", null);
    }
    public String getLevel() {
        return sharedPerfs.getString("Level", null);
    }


}