package com.ym.cashregisterdemo.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.dbframework.DBHelper;
import com.dbframework.Table;
import com.ym.cashregisterdemo.data.field.GoodsField;
import com.ym.cashregisterdemo.data.field.RuleField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠活动类
 * Created by yuanmin on 2016/3/16.
 */
public class RuleData {
    /**
     * 编号
     */
    public int id;
    /**
     * 名称
     */
    public String rule_name;
    /**
     * 优先级,商品享受优先级最大的一项优惠
     */
    public int level;

    public static final Table table = new Table("rule_data", RuleField.values(), RuleField.lastmodify);

    /**
     * 初始化
     *
     * @param id
     * @param name
     * @param level
     */
    public RuleData(int id, String name, int level) {
        this.id = id;
        this.rule_name = name;
        this.level = level;
    }

    /**
     * 添加一条数据
     *
     * @param rule 优惠活动
     * @param db
     */
    public static void create(RuleData rule, DBHelper db) {
        ContentValues cv = new ContentValues();
        cv.put(RuleField.id.name(), rule.id);
        cv.put(RuleField.rule_name.name(), rule.rule_name);
        cv.put(RuleField.level.name(), rule.level);
        cv.put(RuleField.lastmodify.name(), System.currentTimeMillis());
        table.create(cv, db);
    }

    /**
     * 添加多条数据
     *
     * @param ruleDataList 优惠活动列表
     * @param db
     */
    public static void create(List<RuleData> ruleDataList, DBHelper db) {
        ContentValues cv;
        for (RuleData rule : ruleDataList) {
            cv = new ContentValues();
            cv.put(RuleField.id.name(), rule.id);
            cv.put(RuleField.rule_name.name(), rule.rule_name);
            cv.put(RuleField.level.name(), rule.level);
            cv.put(RuleField.lastmodify.name(), System.currentTimeMillis());
            table.create(cv, db);
        }
    }

    /***
     * 游标转换成模型类
     *
     * @param c
     * @return
     */
    private static RuleData cursorToData(Cursor c) {
        return new RuleData(c.getInt(RuleField.id.index()), c.getString(RuleField.rule_name.index()), c.getInt(RuleField.level.index()));
    }

    /**
     * 删除全部
     *
     * @param db
     */
    public static void deleteAll(DBHelper db) {
        table.deleteAll(db);
    }

    /**
     * 查找所有
     *
     * @param db
     * @return 所有优惠活动的列表
     */
    public static List<RuleData> getAll(DBHelper db) {
        return select(null, null, db, null);
    }

    public static List<RuleData> getRuleByIds(DBHelper db, List<String> ids) {
        if (ids.size() <= 0) {
            return new ArrayList<RuleData>();
        }
        String where = "";
        String[] args = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {

            args[i] = ids.get(i);
            if (i != 0) {
                where += " or ";
            }
            where += RuleField.id.name() + " = ? ";

        }
        return select(where, args, db, RuleField.level.name() + " ASC ");
    }

    /**
     * 按照条件查询
     *
     * @param where
     * @param args
     * @param db
     * @return 符合条件的优惠活动列表
     */
    private static List<RuleData> select(String where, String[] args, DBHelper db, String orderby) {
        Cursor c = null;
        try {
            c = table.query(where, args, null == orderby ? null : orderby, db);//按照优先级降序排列
            List<RuleData> list = new ArrayList<>();
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(cursorToData(c));
                c.moveToNext();
            }
            return list;
        } finally {
            if (c != null)
                c.close();
        }
    }

    /**
     * 查询所有的优惠活动
     *
     * @param db
     * @return 优惠活动名称
     */
    private static List<String> getAllRuleName(DBHelper db) {
        Cursor c = null;
        try {
            c = table.query(null, null, RuleField.id.name() + " ASC ", db);
            List<String> list = new ArrayList<>();
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(c.getString(RuleField.rule_name.index()));
                c.moveToNext();
            }
            return list;
        } finally {
            if (c != null)
                c.close();
        }
    }

    /**
     * 重写tostring,此处用于spinner的展示
     *
     * @return
     */
    @Override
    public String toString() {
        return this.rule_name;
    }
}
