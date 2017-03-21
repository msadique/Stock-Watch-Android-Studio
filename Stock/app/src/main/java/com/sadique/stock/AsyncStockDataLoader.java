package com.sadique.stock;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncStockDataLoader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private int count;
    private String symbol;
    private final String dataURL1 = "http://finance.google.com/finance/info?client=ig&q=";
    private static final String TAG = "AsyncStockDataLoader";
    public AsyncStockDataLoader(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String s) {
        if(s==null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            //builder.setIcon(R.drawable.icon1);
            builder.setMessage("Data for Stock Symbol");
            builder.setTitle("Symbol Not Found : " + symbol);
            AlertDialog dialog = builder.create();
            dialog.show(); }
            else{
        ArrayList<CompanyDetail> companyList = parseJSON(s.substring(3));
        mainActivity.updateStoke(companyList);
    }
    }


    @Override
    protected String doInBackground(String... params) {

        symbol = params[0];
        Uri dataUri = Uri.parse(dataURL1+params[0]);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            //Log.d(TAG, "doInBackground: " + sb.toString());
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        //Log.d(TAG, "doInBackground: " + sb.toString());

        return sb.toString();
    }


    private ArrayList<CompanyDetail> parseJSON(String s) {

        ArrayList<CompanyDetail> companyList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCompany = (JSONObject) jObjMain.get(i);
                //String cName = jCompany.getString("company_name");
                String cSymbol = jCompany.getString("t");
                double cPrice = Double.parseDouble(jCompany.getString("l"));
                double cChange = Double.parseDouble(jCompany.getString("c"));
                double cPercent = Double.parseDouble(jCompany.getString("cp"));

                companyList.add(
                        new CompanyDetail(cSymbol,cPrice,cChange,cPercent,"sam"));
            }
            return companyList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
