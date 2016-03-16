package com.ym.cashregisterdemo.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.dbframework.DBHelper;
import com.dbframework.Field;
import com.dbframework.Table;
import com.ym.cashregisterdemo.data.field.GoodsField;
import com.ym.cashregisterdemo.data.field.GoodsRuleRelationField;
import com.ym.cashregisterdemo.data.field.RuleField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品与优惠活动的关系
 * Created by yuanmin on 2016/3/16.
 */
public class RelationData {
    /**
     * 优惠活动编号
     */
    private int rule_id;
    /**
     * 商品id
     */
    private int goods_id;
    /**
     * 优惠活动优先级
     */
    private int level;

    public static final Table table = new Table("relation_data", GoodsRuleRelationField.values(), GoodsRuleRelationField.lastmodify);

    /**
     * 初始化
     *
     * @param rule_id
     * @param goods_id
     */
    public RelationData(int rule_id, int goods_id , int level) {
        this.rule_id = rule_id;
        this.goods_id = goods_id;
        this.level = level;
    }

    /***
     * 游标转换成模型类
     *
     * @param c
     * @return
     */
    private static RelationData cursorToData(Cursor c) {
        return new RelationData(c.getInt(GoodsRuleRelationField.rule_id.index()), c.getInt(GoodsRuleRelationField.goods_id.index()) , c.getInt(GoodsRuleRelationField.level.index()));
    }

    /**
     * 添加一条数据
     *
     * @param data 关系类
     * @param db
     */
    public static void create(RelationData data, DBHelper db) {

        ContentValues cv = new ContentValues();
        cv.put(GoodsRuleRelationField.goods_id.name(), data.goods_id);
        cv.put(GoodsRuleRelationField.rule_id.name(), data.rule_id);
        cv.put(GoodsRuleRelationField.level.name() , data.level);
        cv.put(GoodsRuleRelationField.lastmodify.name(), System.currentTimeMillis());
        table.create(cv, db);
    }

    /**
     * 添加多条数据
     *
     * @param relationDatasList 关系类列表
     * @param db
     */
    public static void create(List<RelationData> relationDatasList, DBHelper db) {
        ContentValues cv;
        for (RelationData data : relationDatasList) {
            cv = new ContentValues();
            cv.put(GoodsRuleRelationField.goods_id.name(), data.goods_id);
            cv.put(GoodsRuleRelationField.rule_id.name(), data.rule_id);
            cv.put(GoodsRuleRelationField.level.name() , data.level);
            cv.put(GoodsRuleRelationField.lastmodify.name(), System.currentTimeMillis());
            table.create(cv, db);
        }
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
     *  根据goodsid和ruleid删除
     * @param args
     * @param db
     */
    public static void deleteByGoodIdRuleId(List<String> args,DBHelper db){
        List<Field> fieldList = new ArrayList<Field>();
        fieldList.add(GoodsRuleRelationField.goods_id);
        fieldList.add(GoodsRuleRelationField.rule_id);
        table.deleteByFields(fieldList, args, db);
    }

    /**
     * 查找所有
     *
     * @param db
     * @return 所有商品的列表
     */
    public static List<RelationData> getAll(DBHelper db) {
        return select(null, null, db);
    }

    /**
     * 按照条件查询商品
     *
     * @param where
     * @param args
     * @param db
     * @return 符合条件的关系列表
     */
    public static List<RelationData> select(String where, String[] args, DBHelper db) {
        Cursor c = null;
        try {
            c = table.query(where, args, RuleField.id.name() + " ASC ", db);
            List<RelationData> list = new ArrayList<>();
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
     * 按照条件查询商品
     *
     * @param where
     * @param args
     * @param db
     * @return 符合条件的关系列表
     */
    public static List<String> selectForString(String where, String[] args, DBHelper db) {
        Cursor c = null;
        try {
            c = table.query(where, args, RuleField.id.name() + " ASC ", db);
            List<String> list = new ArrayList<>();
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(c.getInt(GoodsRuleRelationField.goods_id.index())+"");
                c.moveToNext();
            }
            return list;
        } finally {
            if (c != null)
                c.close();
        }
    }

    /**
     *
     * @param arg
     * @param db
     * @return
     */
    public static List<String> selectRuleIdByGoods(String arg, DBHelper db){
        return selectForRule(GoodsRuleRelationField.goods_id.name()+"=?",new String[]{arg},GoodsRuleRelationField.level.name() + " DESC",db);
    }

    /**
     * 按照条件查询优惠活动
     *
     * @param where
     * @param args
     * @param db
     * @return 符合条件的关系列表
     */
    private static List<String> selectForRule(String where, String[] args, String orderBy , DBHelper db) {
        Cursor c = null;
        try {
            c = table.query(where, args, orderBy, db);
            List<String> list = new ArrayList<>();
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(c.getInt(GoodsRuleRelationField.rule_id.index())+"");
                c.moveToNext();
            }
            return list;
        } finally {
            if (c != null)
                c.close();
        }
    }

    /**
     * 按照条件查询商品
     *
     * @param args
     * @param db
     * @return 符合条件的商品id列表
     */
    public static List<String> selectById( String[] args, DBHelper db) {
        return  selectForString("rule_id = ?", args, db);
    }
}
