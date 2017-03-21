package com.sadique.stock;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Christopher on 1/30/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView cSymbol;
    public TextView cPrice;
    public TextView cChange;
    public TextView cName;


    public MyViewHolder(View view) {
        super(view);
        cSymbol = (TextView) view.findViewById(R.id.csymbol);
        cPrice = (TextView) view.findViewById(R.id.cprice);
        cChange = (TextView) view.findViewById(R.id.cchange);
        cName = (TextView) view.findViewById(R.id.cname);
    }

}
