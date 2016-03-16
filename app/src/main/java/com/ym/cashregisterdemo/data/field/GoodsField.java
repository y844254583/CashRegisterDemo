package com.ym.cashregisterdemo.data.field;

import com.dbframework.Field;

/**
 * Created by yuanmin on 2016/3/15.
 */
public enum  GoodsField implements Field {

    goods_id,//编号
    goods_name,//商品名称
    price("FLOAT"),//单价
    unit_name,//计量单位名称
    type_id,//类型id
    type_name,//类型名称
    barcode,//条形码
    lastmodify;//最近修改时间

    private String type;

    private GoodsField(){
        this( "TEXT" );
    }

    private GoodsField( String type ){
        this.type = type;
    }

    @Override
    public int index() {
        return this.ordinal();
    }

    @Override
    public String type() {
        return type;
    }
}
