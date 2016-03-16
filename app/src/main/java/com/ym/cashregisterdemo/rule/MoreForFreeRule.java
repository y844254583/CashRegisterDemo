package com.ym.cashregisterdemo.rule;

import android.content.Context;

import com.ym.cashregisterdemo.R;
import com.ym.cashregisterdemo.data.Goods;

import java.math.BigDecimal;

/**
 * Created by yuanmin on 2016/3/16.
 */
public class MoreForFreeRule extends Rule {

    private Context context;

    public MoreForFreeRule(Context context){
        this.context = context;
    }

    @Override
    public String getTotal(Goods goods, int amount) {
        //逢3减一
        int acctualAmount = amount - amount/3;
        String result = context.getString(R.string.name) + goods.goods_name
                + "," + context.getString(R.string.amount) + amount + goods.unit_name
                + "," + context.getString(R.string.price) + goods.price.setScale(2).toString() + context.getString(R.string.price_unit)
                + "," + context.getString(R.string.subtotal)
                + goods.price.multiply(new BigDecimal(acctualAmount)).setScale(2).toString() + context.getString(R.string.price_unit);
        return  result;
    }

    /**
     * 计算节省的钱
     * @param goods
     * @param amount
     * @return
     */
    public BigDecimal getSaved(Goods goods, int amount){
        //减免的商品数量
        int num = amount/3;
        return goods.price.multiply(new BigDecimal(num));
    }

    /**
     * 小计
     * @param goods
     * @param amount
     * @return
     */
    @Override
    public BigDecimal getTotalPrice(Goods goods, int amount) {
        //逢3减一
        int acctualAmount = amount - amount/3;
        return  goods.price.multiply(new BigDecimal(acctualAmount));
    }

    /**
     * 买二赠一商品内容
     * @return
     */
    public String getMoreforFree(Goods goods, int amount){
        int num = amount/3;
        if(num <= 0){
            return null;
        }else{
            return context.getString(R.string.name) + goods.goods_name
                    + "," + context.getString(R.string.amount) + num +goods.unit_name;
        }
    }
}
