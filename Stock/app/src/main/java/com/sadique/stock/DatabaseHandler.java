
package com.sadique.stock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // DB Name
    private static final String DATABASE_NAME = "StockAppDB";
    // DB Table Name
    private static final String TABLE_NAME = "StockTable";
    ///DB Columns
    private static final String COMPANY_NAME = "CompanyName";
    private static final String COMPANY_SYMBOL = "CompanySymbol";
    // DB Table Create Code
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COMPANY_SYMBOL + " TEXT not null unique," +
                    COMPANY_NAME + " TEXT not null)";

    private SQLiteDatabase database;
    private static final String DATABASE_ALTER_TABLE_FOR_V2 = "ALTER TABLE "
            + TABLE_NAME + " ADD COLUMN " + COMPANY_SYMBOL + " int not null default 0;";

    private static final String DATABASE_ALTER_TABLE_FOR_V3 = "ALTER TABLE "
            + TABLE_NAME + " ADD COLUMN " + COMPANY_SYMBOL + " int not null default 0;";

        public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: C'tor DONE");

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called is the DB does not exist
        Log.d(TAG, "onCreate: Mking New DB");
        db.execSQL(SQL_CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion == 2) {
            db.execSQL(DATABASE_ALTER_TABLE_FOR_V2);
        }
        if (newVersion == 3) {
            db.execSQL(DATABASE_ALTER_TABLE_FOR_V3);
        }

    }

    public ArrayList<CompanySymbol> loadCountries() {

        Log.d(TAG, "loadCountries: LOADING COMPANY_NAME DATA FROM DB");
        ArrayList<CompanySymbol> stock = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME,  // The table to query
                new String[]{COMPANY_SYMBOL, COMPANY_NAME }, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order
        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);
                stock.add(new CompanySymbol(symbol, company));
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "loadCountries: DONE LOADING COMPANY_NAME DATA FROM DB");

        return stock;
    }

    public void addCompany(CompanySymbol country) {
        ContentValues values = new ContentValues();
        values.put(COMPANY_SYMBOL, country.getSymbol());
        values.put(COMPANY_NAME, country.getName());

        deleteCompany(country.getName());
        long key = database.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addCompany: " + key);
    }

    public void updateCountry(CompanySymbol country) {
        ContentValues values = new ContentValues();
        values.put(COMPANY_SYMBOL, country.getSymbol());
        values.put(COMPANY_NAME, country.getName());

        long key = database.update(
                TABLE_NAME, values, COMPANY_SYMBOL + " = ?", new String[]{country.getSymbol()});

        Log.d(TAG, "updateCountry: " + key);
    }

    public void deleteCompany(String symbol) {
        Log.d(TAG, "deleteCompany: " + symbol);
        int cnt = database.delete(TABLE_NAME, COMPANY_SYMBOL + " = ?", new String[]{symbol});
        Log.d(TAG, "deleteCompany: " + cnt);
    }

    public void dumpLog() {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            for (int i = 0; i < cursor.getCount(); i++) {
                String Symbol = cursor.getString(1);
                String country = cursor.getString(0);
                Log.d(TAG, "dumpLog: " +
                        String.format("%s %-18s", COMPANY_SYMBOL + ":", Symbol)+
                        String.format("%s %-18s", COMPANY_NAME + ":", country) );
                cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "dumpLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }
}
