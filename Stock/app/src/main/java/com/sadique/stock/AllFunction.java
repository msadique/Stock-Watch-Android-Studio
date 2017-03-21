package com.sadique.stock;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sadique on 2/22/2017.
 */

public class AllFunction extends AppCompatActivity {
    private static final String TAG = "AllFunction";
    private MainActivity mainActivity;
    public AllFunction(MainActivity ma) {
        mainActivity = ma;
    }

    public void addStoke(final List<CompanySymbol> cList) {
        Log.d(TAG, "  addStoke === ");
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        final EditText et = new EditText(mainActivity);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        //et.setBackgroundColor(Color.parseColor("#05c622"));
        builder.setView(et);
        //builder.setIcon(R.drawable.icon1);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if( mainActivity.network() == 1 ) {
                    String str = et.getText().toString();
                    int flag=1;
                    for(int i =0;i<cList.size();i++)
                    {
                        if(str.equals(cList.get(i).getSymbol())){
                            flag=0;
                            break;
                        }
                    }
                    if(flag==0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                        builder.setIcon(R.drawable.report);
                        builder.setMessage("Stock Symbol "+ str+ " is already diplayed");
                        builder.setTitle("Duplicate Stock ");
                        AlertDialog dialog1 = builder.create();
                        dialog1.show();
                    }
                    else {
                        AsyncStockSymbolLoader alt = new AsyncStockSymbolLoader(mainActivity);
                        alt.execute(str);
                    }
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setTitle("Stock Selection");
        builder.setMessage("Please enter a Stock Symbol:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void selectStoke(final ArrayList<CompanySymbol> cList) {

        Log.d(TAG, "  selectStoke === ");
        final CharSequence[] sArray = new CharSequence[cList.size()];
        for (int i = 0; i < cList.size(); i++) {
            sArray[i] = cList.get(i).getSymbol() + " - " + cList.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Make a selection");
            //builder.setIcon(R.drawable.icon1);

            builder.setItems(sArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AsyncStockDataLoader alt = new AsyncStockDataLoader(mainActivity);
                    alt.execute(cList.get(which).getSymbol());
                    Log.d(TAG, "  selectStoke " + cList.get(which).getName());
                     }
            });

            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel(); // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    public static Comparator<CompanyDetail> nameComparator = new Comparator<CompanyDetail>() {

        @Override
        public int compare(CompanyDetail companyDetail, CompanyDetail t1) {

            return companyDetail.getSymbol().compareTo(t1.getSymbol());
        }
    };
    }


