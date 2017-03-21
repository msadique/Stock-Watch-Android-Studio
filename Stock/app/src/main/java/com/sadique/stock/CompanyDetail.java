package com.sadique.stock;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by sadique on 3/3/2017.
 */

public class CompanyDetail implements Serializable {
    private static final String TAG = "CompanyDetail";
    private String cSymbol;
    private double cPrice;
    private double cChange;
    private String cName;
    private double cPercent;

    public CompanyDetail(String cSymbol, double cPrice,double cChange,double cPercent, String cName) {

        this.cSymbol = cSymbol;
        this.cPrice = cPrice;
        this.cChange = cChange;
        this.cName = cName;
        this.cPercent = cPercent;
        Log.d(TAG, "CompanyDetail: " + cSymbol +"  "+cPrice+"  "+cChange+"  "+cPercent+"  "+cName);

    }

    public String getSymbol() {
        return this.cSymbol;
    }

    public double getPrice() {
        return this.cPrice;
    }

    public double getChange() {
        return this.cChange;
    }

    public String getName() {
        return this.cName;
    }

    public double getPercent() {
        return this.cPercent;
    }

    public void setSymbol(String cSymbol) {
        this.cSymbol = cSymbol;
    }

    public void setPrice(double cPrice) {
        this.cPrice = cPrice;
    }

    public void setChange(double cChange) {
        this.cChange = cChange;
    }

    public void setName(String cName) {
        this.cName = cName;
    }

    public void setPercent(double cPercent) { this.cPercent = cPercent;     }

}
