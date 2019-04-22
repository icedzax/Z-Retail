package com.zubb.jannarongj.z_retail;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by jannarong.j on 2/23/2019.
 */

public class StockTresh {

    public boolean isStockStatus() {
        return StockStatus;
    }

    public void setStockStatus(boolean stockStatus) {
        StockStatus = stockStatus;
    }

    public boolean StockStatus = false;
    ConnectionClass connectionClass;



    public class StockCheck extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

         //   Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                setStockStatus(true);
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

}
