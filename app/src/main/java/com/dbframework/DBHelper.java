package com.dbframework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ym.cashregisterdemo.data.Goods;
import com.ym.cashregisterdemo.data.RelationData;
import com.ym.cashregisterdemo.data.RuleData;

import java.math.BigDecimal;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_cashregister";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }


    private void createTables(SQLiteDatabase db) {
        Goods.table.createTable(db);
        RuleData.table.createTable(db);
        RelationData.table.createTable(db);
    }

    private void dropAllTables(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion) {


        }

    }

    public SQLiteDatabase getDatabase() {
        return getWritableDatabase();
    }

    public void beginTransaction() {
        this.getWritableDatabase().beginTransaction();
    }

    public void endTransaction() {
        this.getWritableDatabase().endTransaction();
    }

    public void setTransactionSuccess() {
        this.getWritableDatabase().setTransactionSuccessful();
    }
}