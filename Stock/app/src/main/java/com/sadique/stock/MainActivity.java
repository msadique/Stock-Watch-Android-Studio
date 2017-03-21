package com.sadique.stock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {
    // The above lines are important - them make this class a listener
    // for click and long click events in the ViewHolders (in the recycler
    private static final String TAG = "MainActivity";
    private List<CompanySymbol> SymbolList = new ArrayList<>();  // Main content is here
    private List<CompanySymbol> symbolDBList = new ArrayList<>();  // Main content is here
    private List<CompanyDetail> companyList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout
    //Fake List
    /*private String[][] cSymbol =  new String[][]{{"AMZN", "Amazon.com, Inc."},
            {"AAPL","Apple Inc."},
            {"CSCO","Cisco Systems, Inc."},
            {"CTSH","Cognizant Technology Solutions Corporation"},
            {"FB","Facebook, Inc."},
            {"GOOG","Alphabet Inc."},
            {"IBM","International Business Machines Corporation "},
            {"FBK","FB Financial Corporation"}};
   */
    private ComapanyAdapter mAdapter; // Data to recyclerview adapter
    private DatabaseHandler databaseHandler;
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    AllFunction allFunc = new AllFunction(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        symbolDBList.clear();
        SymbolList.clear();
        //allFunc.toast();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new ComapanyAdapter(companyList, this);
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.dumpLog();
        ArrayList<CompanySymbol> list = databaseHandler.loadCountries();
        symbolDBList.addAll(list);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        // Make some data - not always needed - used to fill list
        if(network() == 1 )
        {
            if (symbolDBList.size() > 0) {
                String allSymbol = "";
                for (int j = 0; j < symbolDBList.size(); j++) {
                    allSymbol = allSymbol + symbolDBList.get(j).getSymbol() + ",";
                }
                if(symbolDBList.size()>0){
                AsyncStockDataLoader alt = new AsyncStockDataLoader(this);
                alt.execute(allSymbol);
                }
                Log.d(TAG, "Oncreate Symbol " + allSymbol);
            } else {
                SymbolList.clear();
     /*           String allSymbol = "";
                for (int k = 0; k < cSymbol.length; k++) {
                    SymbolList.add(new CompanySymbol(cSymbol[k][0], cSymbol[k][1]));
                    allSymbol = allSymbol + cSymbol[k][0] + ",";
                }
                AsyncStockDataLoader alt = new AsyncStockDataLoader(this);
                alt.execute(allSymbol);*/
            }
        }
    }
    private void doRefresh() {
       // Collections.shuffle(companyList);
       // allFunc.toast();
        if(network() == 1 ) {
            SymbolList.clear();
            String allSymbol = "";
            for (int j = 0; j < symbolDBList.size(); j++) {
                allSymbol = allSymbol + symbolDBList.get(j).getSymbol() + ",";
            }
            if(symbolDBList.size()>0) {
                AsyncStockDataLoader alt = new AsyncStockDataLoader(this);
                alt.execute(allSymbol);
            }
        }
        mAdapter.notifyDataSetChanged();
        swiper.setRefreshing(false);
        //Toast.makeText(this, "List content shuffled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        if(network()==1) {
            String uRL = "http://www.marketwatch.com/investing/stock/";
            int pos = recyclerView.getChildLayoutPosition(v);
            CompanyDetail m = companyList.get(pos);
            uRL += m.getSymbol();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(uRL));
            startActivity(i);
        }
        //Toast.makeText(v.getContext(), "SHORT " + m.toString(), Toast.LENGTH_SHORT).show();
    }

        @Override
        public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
            int pos = recyclerView.getChildLayoutPosition(v);
            click1(pos);
     //   CompanyDetail m = companyList.get(pos);
       // Toast.makeText(v.getContext(), "LONG " + m.toString(), Toast.LENGTH_SHORT).show();
        return true;
    }
    public void updateSymbol(ArrayList<CompanySymbol> cList) {
       Log.d(TAG, "updateDate "+cList);
       SymbolList.clear();
       SymbolList.addAll(cList);
       allFunc.selectStoke(cList);
   }
    public void updateStoke(ArrayList<CompanyDetail> cList) {
        Log.d(TAG, "updateDate sadique "+cList);
        if(SymbolList.size() > 0 ){
            for(int x = 0; x < cList.size(); x++ ) {
                for (int i = 0; i < SymbolList.size(); i++) {
                    if (cList.get(x).getSymbol().equals(SymbolList.get(i).getSymbol())) {
                        Log.d(TAG, "first IF ");
                        int flag = 1;
                        for (int j = 0; j < symbolDBList.size(); j++) {
                            if (cList.get(x).getSymbol().equals(symbolDBList.get(j).getSymbol())) {
                                flag = 0;
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setIcon(R.drawable.report);
                                builder.setMessage("Stock Symbol "+ cList.get(x).getSymbol()+ " is already diplayed");
                                builder.setTitle("Duplicate Stock ");
                                AlertDialog dialog1 = builder.create();
                                dialog1.show();       //cList.remove(x);
                                Log.d(TAG, "Second IF ");
                                break;
                            }
                        }
                        if (flag == 1) {
                            Log.d(TAG, "Third IF " + cList);
                            cList.get(x).setName(SymbolList.get(i).getName());
                            symbolDBList.add(SymbolList.get(i));
                            databaseHandler.addCompany(SymbolList.get(i));
                            companyList.add(cList.get(x));
                            break;
                        }
                    }
                }
            }
        }
        else if(symbolDBList.size()>0)
        {
            companyList.clear();
            Log.d(TAG, "Fourth IF "+cList);
            for( int k = 0; k < cList.size(); k++ )
            for( int j = 0; j < symbolDBList.size(); j++ )
            {
                if(cList.get(k).getSymbol().equals(symbolDBList.get(j).getSymbol())){
                    cList.get(k).setName(symbolDBList.get(j).getName());
                    break;
                }
            }

            companyList.addAll(cList);
        }
        Collections.sort(companyList, allFunc.nameComparator);
        mAdapter.notifyDataSetChanged();
    }
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
           case R.id.menuA:
               SymbolList.clear();
               allFunc.addStoke(symbolDBList);

           default:
               return super.onOptionsItemSelected(item);
       }
   }
    public void click1(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.delete);
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "onActivityResult: yes  ");
                databaseHandler.deleteCompany(companyList.get(pos).getSymbol());
                for(int i =0;i<symbolDBList.size();i++)
                {
                    if(companyList.get(pos).getSymbol().equals(symbolDBList.get(i).getSymbol()))
                    {
                        symbolDBList.remove(i);
                        break;
                    }
                }
                companyList.remove(pos);
                recyclerView.setAdapter(mAdapter);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        CompanyDetail note = companyList.get(pos);
        builder.setTitle("Delete Stock");
        builder.setMessage("Delete Stock Symbol "+note.getSymbol()+"?");

        // ("Delete Stock '"+note.getSymbol()+"'");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public int network(){

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (!(netInfo != null && netInfo.isConnectedOrConnecting())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setIcon(R.drawable.icon1);
            builder.setMessage("Stocks Cannot be updated without A Network Connection");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            return 0;
        }
        else {
            return 1;
        }
    }
}