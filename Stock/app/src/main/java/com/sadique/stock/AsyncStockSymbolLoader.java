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


public class AsyncStockSymbolLoader extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;
    private int count;
    private final String dataURL1 = "http://stocksearchapi.com/api/?api_key=db223e1e9021b11e3f09df09c720d6e78aa65f55&search_text=";
    private static final String TAG = "AsyncStockSymbolLoader";

    public AsyncStockSymbolLoader(MainActivity ma) {
        mainActivity = ma;
    }

    private String symbol;

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            ArrayList<CompanySymbol> symbolList = parseJSON(s);
            mainActivity.updateSymbol(symbolList);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            //builder.setIcon(R.drawable.icon1);
            builder.setMessage("Data for Stock Symbol");
            builder.setTitle("Symbol Not Found : " + symbol);
            AlertDialog dialog = builder.create();
            dialog.show();
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
            int statusCode = conn.getResponseCode();
            if(statusCode!=200)
            {
                return null;
            }
            Log.d(TAG, "doInBackground: ResponseCode " + statusCode);
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());

        return sb.toString();
    }


    private ArrayList<CompanySymbol> parseJSON(String s) {

        ArrayList<CompanySymbol> symbolList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCompany = (JSONObject) jObjMain.get(i);
                String cName = jCompany.getString("company_name");
                String cSymbol = jCompany.getString("company_symbol");

                symbolList.add(
                        new CompanySymbol(cSymbol,cName));
            }
            return symbolList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
