package com.sadique.stock;

/**
 * Created by sadique on 3/3/2017.
 */

public class CompanySymbol {
    private String cSymbol;
    private String cName;

    public CompanySymbol(String cSymbol, String cName) {
        this.cSymbol = cSymbol;
        this.cName = cName;
    }

    public String getSymbol() {   return this.cSymbol;    }
    public String getName() {
        return this.cName;
    }
    public void setSymbol(String cSymbol) {
        this.cSymbol = cSymbol;
    }
    public void setName(String cName) {
        this.cName = cName;
    }
}
