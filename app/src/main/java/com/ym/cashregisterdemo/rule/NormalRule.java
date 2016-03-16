package com.ym.cashregisterdemo.rule;

import android.content.Context;

import com.ym.cashregisterdemo.R;
import com.ym.cashregisterdemo.data.Goods;

import java.math.BigDecimal;

/**
 * Created by yuanmin on 2016/3/16.
 */
public class NormalRule extends Rule {

    private Context context;

    public NormalRule(Context context) {
        this.context = context;
    }

    @Override
    public String getTotal(Goods goods, int amount) {
        String result = context.getString(R.string.name) + goods.goods_name
                + "," + context.getString(R.string.amount)+ amount + goods.unit_name
                + "," + context.getString(R.string.price) + goods.price.setScale(2).toString() + context.getString(R.string.price_unit)
                + "," + context.getString(R.string.subtotal)
                + goods.price.multiply(new BigDecimal(amount)).setScale(2).toString() + context.getString(R.string.price_unit);
        return  result;
    }

    @Override
    public BigDecimal getTotalPrice(Goods goods, int amount) {
        return goods.price.multiply(new BigDecimal(amount)).setScale(2,BigDecimal.ROUND_HALF_UP);
    }


}
