package com.ym.cashregisterdemo.application;

import android.app.Application;

import com.dbframework.DBHelper;
import com.ym.cashregisterdemo.data.Goods;
import com.ym.cashregisterdemo.data.RuleData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/15.
 */
public class CashRegisterApplication extends Application {
    DBHelper db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = new DBHelper(this);
    }

    /**
     * 获取db
     * @return
     */
    public DBHelper getDB() {
        return db;
    }

}
