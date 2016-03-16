package com.ym.cashregisterdemo.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dbframework.DBHelper;
import com.dbframework.Field;
import com.dbframework.Table;
import com.ym.cashregisterdemo.data.field.GoodsField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品类
 * Created by yuanmin on 2016/3/15.
 */
public class Goods {

    /**
     * 商品id
     */
    public int goods_id;
    /**
     * 商品名称
     */
    public String goods_name;
    /**
     * 商品价格
     */
    public BigDecimal price;
    /**
     * 商品计数单位
     */
    public String unit_name;
    /**
     * 商品类型id
     */
    public int type_id;
    /**
     * 商品类型名称
     */
    public String type_name;
    /**
     * 商品的条形码
     */
    public String bar_code;

    /**
     * 是否被选中
     */
    public boolean checked = false;

    public static final Table table = new Table("goods_data", GoodsField.values(), GoodsField.lastmodify);

    /**
     * @param id
     * @param name
     * @param price
     * @param unit
     * @param type_id
     * @param type
     * @param bar_code
     */
    public Goods(int id, String name, BigDecimal price, String unit, int type_id, String type, String bar_code) {
        this.goods_id = id;
        this.goods_name = name;
        this.price = price;
        this.unit_name = unit;
        this.type_id = type_id;
        this.type_name = type;
        this.bar_code = bar_code;
    }

    /***
     * 游标转换成模型类
     * @param c
     * @return
     */
    private static Goods cursorToData(Cursor c) {
        return new Goods(c.getInt(GoodsField.goods_id.index()), c.getString(GoodsField.goods_name.index()), new BigDecimal(c.getFloat(GoodsField.price.index())), c.getString(GoodsField.unit_name.index()), c.getInt(GoodsField.type_id.index()), c.getString(GoodsField.type_name.index()), c.getString(GoodsField.barcode.index()));
    }

    /**
     * 添加一条数据
     * @param data 商品类
     * @param db
     */
    public static void create( Goods data , DBHelper db ){

        ContentValues cv = new ContentValues();
        cv.put( GoodsField.goods_id.name() , data.goods_id);
        cv.put( GoodsField.goods_name.name() , data.goods_name );
        cv.put( GoodsField.price.name() , data.price.floatValue() );
        cv.put( GoodsField.unit_name.name() , data.unit_name );
        cv.put( GoodsField.type_id.name() , data.type_id );
        cv.put( GoodsField.type_name.name() , data.type_name );
        cv.put( GoodsField.barcode.name() , data.bar_code );
        cv.put( GoodsField.lastmodify.name(), System.currentTimeMillis());
        table.create( cv , db );
    }

    /**
     * 添加多条数据
     * @param goodsList 商品类列表
     * @param db
     */
    public static void create(List<Goods> goodsList , DBHelper db){
        ContentValues cv;
        for (Goods data : goodsList){
            cv = new ContentValues();
            cv.put( GoodsField.goods_id.name() , data.goods_id);
            cv.put( GoodsField.goods_name.name() , data.goods_name );
            cv.put( GoodsField.price.name() , data.price.floatValue() );
            cv.put( GoodsField.unit_name.name() , data.unit_name );
            cv.put( GoodsField.type_id.name() , data.type_id );
            cv.put( GoodsField.type_name.name() , data.type_name );
            cv.put( GoodsField.barcode.name() , data.bar_code );
            table.create( cv , db );
        }
    }

    /**
     * 删除全部
     * @param db
     */
    public static void deleteAll( DBHelper db ){
        table.deleteAll(db);
    }

    /**
     * 查找所有
     * @param db
     * @return 所有商品的列表
     */
    public static List<Goods> getAll( DBHelper db ){
        return select( null , null , db , GoodsField.goods_id.name() + " ASC ");
    }

    public static List<Goods> getGoodByIds( DBHelper db , List<String> ids  ){
        if(ids.size()<=0){
            return new ArrayList<Goods>();
        }
        String where = "";
        String[] args = new String[ids.size()];
        for ( int i = 0 ; i < ids.size() ; i++ ){

            args[i] = ids.get( i );
            if( i != 0 ){
                where += " or ";
            }
            where += GoodsField.goods_id.name() + " = ? ";

        }
        return select( where ,args , db , null  );
    }

    public static Goods selectByBarcode(String arg , DBHelper db){
        List<Goods> list = select(GoodsField.barcode.name()+"=?",new String[]{arg},db,null);
        if(null == list){
            return null;
        }else if(list.size()<=0){
            return null;
        }else{
            return list.get(0);
        }
    }

    /**
     * 按照条件查询商品
     * @param where
     * @param args
     * @param db
     * @return 符合条件的商品列表
     */
    private static List<Goods> select( String where , String[] args , DBHelper db , String orderBy ){
        Cursor c = null;
        try{
            c = table.query( where , args ,null == orderBy ? null : orderBy  , db );
            List<Goods> list = new ArrayList<>();
            c.moveToFirst();
            while( !c.isAfterLast() ){
                list.add( cursorToData( c ) );
                c.moveToNext();
            }
            return list;
        }
        finally {
            if( c != null )
                c.close();
        }
    }
}
