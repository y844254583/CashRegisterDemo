package com.ym.cashregisterdemo.rule;

import android.content.Context;

import com.ym.cashregisterdemo.R;
import com.ym.cashregisterdemo.data.Goods;

import java.math.BigDecimal;

/**
 * Created by yuanmin on 2016/3/16.
 */
public class DiscountRule extends Rule{


    private Context context;

    public DiscountRule(Context context) {
        this.context = context;
    }
    @Override
    public String getTotal(Goods goods, int amount) {
        String result = context.getString(R.string.name) + goods.goods_name
                + "," + context.getString(R.string.amount) + amount + goods.unit_name
                + "," + context.getString(R.string.price) + goods.price.setScale(2,BigDecimal.ROUND_HALF_UP).toString() + context.getString(R.string.price_unit)
                + "," + context.getString(R.string.subtotal) + goods.price.multiply(new BigDecimal(amount)).multiply(new BigDecimal(0.95)).setScale(2,BigDecimal.ROUND_HALF_UP).toString() + context.getString(R.string.price_unit)
                + "," + context.getString(R.string.save)+goods.price.multiply(new BigDecimal(amount)).multiply(new BigDecimal(0.05)).setScale(2,BigDecimal.ROUND_HALF_UP).toString()+context.getString(R.string.price_unit);
        return  result;
    }

    @Override
    public BigDecimal getTotalPrice(Goods goods, int amount) {
        return goods.price.multiply(new BigDecimal(amount)).multiply(new BigDecimal(0.95)).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算节省的钱
     * @return
     */
    public BigDecimal getSaved(Goods goods, int amount){
        return goods.price.multiply(new BigDecimal(amount)).multiply(new BigDecimal(0.05)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
