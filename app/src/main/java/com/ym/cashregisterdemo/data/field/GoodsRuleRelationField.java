package com.ym.cashregisterdemo.data.field;

import com.dbframework.Field;

/**
 * 商品与优惠活动的关系
 * Created by yuanmin on 2016/3/16.
 */
public enum  GoodsRuleRelationField implements Field{
    id("INTEGER PRIMARY KEY"),//关系编号
    rule_id,//优惠活动的编号
    goods_id,//商品id
    level,//优惠活动优先级
    lastmodify;//最近修改时间
    private String type;

    private GoodsRuleRelationField(){
        this( "TEXT" );
    }

    private GoodsRuleRelationField( String type ){
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
