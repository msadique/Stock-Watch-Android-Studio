package com.sadique.stock;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ComapanyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "ComapanyAdapter";
    private List<CompanyDetail> companyList;
    private MainActivity mainAct;

    public ComapanyAdapter(List<CompanyDetail> cmpList, MainActivity ma) {
        this.companyList = cmpList;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_list_row, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CompanyDetail company = companyList.get(position);
        String color ;
        String triangle;
        if(company.getPercent()>=0) {
            color = "#05c622";
            triangle="\u25B2";
        }
        else {
            triangle="\u25BC";
            color = "#d80808";
        }
        holder.cSymbol.setTextColor(Color.parseColor(color));
        holder.cPrice.setTextColor(Color.parseColor(color));
        holder.cChange.setTextColor(Color.parseColor(color));
        holder.cName.setTextColor(Color.parseColor(color));

        holder.cSymbol.setText(company.getSymbol());
        holder.cPrice.setText(Double.toString(company.getPrice()));
        holder.cChange.setText(triangle+" "+company.getChange() + "(" + company.getPercent() +"%)");
        holder.cName.setText(company.getName());
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

}