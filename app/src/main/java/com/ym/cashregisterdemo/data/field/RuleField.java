package com.ym.cashregisterdemo.data.field;

import com.dbframework.Field;

/**
 * Created by yuanmin on 2016/3/16.
 */
public enum  RuleField implements Field {

    id,//编号
    rule_name,//优惠活动的名称
    level,//优先级,商品享受优先级最大的一项优惠
    lastmodify;//最近修改时间

    private String type;

    private RuleField(){
        this( "TEXT" );
    }

    private RuleField( String type ){
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
