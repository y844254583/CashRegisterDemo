package com.ym.cashregisterdemo.rule;

import com.ym.cashregisterdemo.data.Goods;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ym on 2016/3/15.
 * description 规则虚类
 */
public abstract class Rule {
    /**
     * 根据商品计算总价,输出小票条目
     * @param goods 商品
     * @param amount 数量
     * @return
     */
    public abstract  String getTotal (Goods goods, int amount);

    /**
     * 根据商品计算本次总价格
     * @param goods
     * @param amount
     * @return
     */
    public abstract BigDecimal getTotalPrice(Goods goods, int amount);
}
