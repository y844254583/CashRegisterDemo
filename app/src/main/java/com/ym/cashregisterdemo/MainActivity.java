package com.ym.cashregisterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ym.cashregisterdemo.application.CashRegisterApplication;
import com.ym.cashregisterdemo.data.Goods;
import com.ym.cashregisterdemo.data.RelationData;
import com.ym.cashregisterdemo.data.RuleData;
import com.ym.cashregisterdemo.rule.DiscountRule;
import com.ym.cashregisterdemo.rule.MoreForFreeRule;
import com.ym.cashregisterdemo.rule.NormalRule;
import com.ym.cashregisterdemo.rule.Rule;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 * Created by yuanmin on 2016/3/16.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private CashRegisterApplication app;

    /**
     * 输出小票结果
     */
    private TextView tv_result;
    /**
     * 商品数量输入框
     */
    private EditText et_cola,et_ball,et_apple;
    /**
     * 是否有买二赠一
     */
    private boolean hasMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (CashRegisterApplication) getApplication();
        //初始化数据库数据
        initDatabaseData();
        //初始化视图
        initView();

        //输出样例数据，可在界面输入商品数量后点击“结算”按钮测试各种情境
        try {
            calcTotalAmount(new JSONArray("['ITEM000001', 'ITEM000002', 'ITEM000003']"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        findViewById(R.id.btn_calc).setOnClickListener(this);
        findViewById(R.id.btn_edit).setOnClickListener(this);
        findViewById(R.id.btn_edit).requestFocus();
        tv_result = (TextView) findViewById(R.id.textview_result);
        et_cola = (EditText) findViewById(R.id.edit_cola);
        et_ball = (EditText) findViewById(R.id.edit_ball);
        et_apple = (EditText) findViewById(R.id.edit_apple);
    }

    /**
     * 根据接收到的json串，得出最后的小票结果
     *
     * @param array
     */
    public void calcTotalAmount(JSONArray array) {
        Goods goods;
        List<String> ruleList;//优惠的id列表
        //优惠活动类
        NormalRule normalRule = new NormalRule(this);
        DiscountRule discountRule = new DiscountRule(this);
        MoreForFreeRule moreRule = new MoreForFreeRule(this);
        //总价
        BigDecimal total = new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP);
        //节省
        BigDecimal save = new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP);
        //小票
        String bill = getString(R.string.bill_title) + "\n";
        //买二赠一小票内容
        String more = getString(R.string.line_1) + "\n";
        more += getString(R.string.more_for_free)+"\n";
        for (int i = 0; i < array.length(); i++) {
            String bar_code;
            int amount = 0;
            try {
                bar_code = (String) array.get(i);
                //解析商品数量
                if (bar_code.contains("-")) {
                    amount = Integer.parseInt(bar_code.substring(bar_code.indexOf("-")+1));
                    bar_code = bar_code.substring(0,bar_code.indexOf("-"));
                } else {
                    amount = 1;
                }
                //根据条形码查询商品
                goods = Goods.selectByBarcode(bar_code, app.getDB());
                if (null == goods) {
                    Toast.makeText(MainActivity.this, "结算出错，条形码对应商品不存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                //根据商品id查询享受的优惠
                ruleList = RelationData.selectRuleIdByGoods(goods.goods_id + "", app.getDB());
                //根据优惠id创建相应的优惠类
                if (null == ruleList || ruleList.size() <= 0) {
                    //没有优惠活动，直接计算商品价格
                    bill += normalRule.getTotal(goods, amount) + "\n";
                    //总计
                    total = total.add(normalRule.getTotalPrice(goods, amount));
                } else {
                    //数据库查询时是按照优先级降序排列，取第一个优惠活动即可
                    if ("1".equals(ruleList.get(0))) {
                        //95折
                        bill += discountRule.getTotal(goods, amount) + "\n";
                        //总计
                        total = total.add(discountRule.getTotalPrice(goods, amount));
                        //节省
                        save = save.add(discountRule.getSaved(goods, amount));
                    } else if ("2".equals(ruleList.get(0))) {
                        //判断是否需要展示买二赠一的节省内容
                        if(amount/3>0){
                            hasMore = true;
                        }
                        //买二赠一
                        bill += moreRule.getTotal(goods, amount) + "\n";
                        //小票内容添加
                        if(null != moreRule.getMoreforFree(goods, amount)){
                            more += moreRule.getMoreforFree(goods, amount) + "\n";
                        }
                        //总计
                        total = total.add(moreRule.getTotalPrice(goods, amount));
                        //节省
                        save = save.add(moreRule.getSaved(goods, amount));
                    } else {
                        //异常
                    }
                }
                //计算价格
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //如果有买二赠一
        if (hasMore == true) {
            bill += more;
        }
        bill += getString(R.string.line_1) + "\n";
        bill += getString(R.string.total) + total.toString() + getString(R.string.price_unit) + "\n";
        if (!save.equals(BigDecimal.ZERO)) {
            bill += getString(R.string.save) + save.toString() + getString(R.string.price_unit) + "\n";
        }
        bill += getString(R.string.line_2);
        tv_result.setText(bill);
    }

    /**
     * 初始化优惠活动95折和买二赠一(样例数据)
     * 初始化商品(样例数据)
     * 如果已经插入过数据，则不再继续执行
     */
    private void initDatabaseData() {
        CashRegisterApplication app = (CashRegisterApplication) this.getApplication();
        //暂时通过查询数据表是否有内容来断定是否已初始化过数据
        if (Goods.getAll(app.getDB()).size() > 0 && RuleData.getAll(app.getDB()).size() > 0) {
            Log.d("----------", "数据已初始化");
            return;
        }
        Log.d("-----------", "开始初始化数据...");
        //初始化商品数据
        Goods pepsi = new Goods(1, "百事可乐", new BigDecimal(3.00), "瓶", 1, "饮料", "ITEM000001");
        Goods ball = new Goods(2, "皮球", new BigDecimal(20.00), "个", 2, "体育用品", "ITEM000002");
        Goods apple = new Goods(3, "苹果", new BigDecimal(8.00), "斤", 3, "水果", "ITEM000003");
        List<Goods> goodsList = new ArrayList<Goods>();
        goodsList.add(pepsi);
        goodsList.add(ball);
        goodsList.add(apple);
        //初始化优惠活动
        RuleData discount = new RuleData(1, "95折", 1);//95折优惠活动
        RuleData freeForMore = new RuleData(2, "买二赠一", 2);//买二增益活动
        List<RuleData> ruleDataList = new ArrayList<RuleData>();
        ruleDataList.add(discount);
        ruleDataList.add(freeForMore);
        //初始化关系
        RelationData relationData1 = new RelationData(1, 1 ,1);
        RelationData relationData2 = new RelationData(2, 2 , 2);
        RelationData relationData3 = new RelationData(2, 3 , 2);
        List<RelationData> relationDataList = new ArrayList<RelationData>();
        relationDataList.add(relationData1);
        relationDataList.add(relationData2);
        relationDataList.add(relationData3);
        //开始事务
        app.getDB().beginTransaction();
        Goods.create(goodsList, app.getDB());
        RuleData.create(ruleDataList, app.getDB());
        RelationData.create(relationDataList, app.getDB());
        app.getDB().setTransactionSuccess();
        //事务结束
        app.getDB().endTransaction();
        //释放资源
        goodsList.clear();
        ruleDataList.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_calc:
                //默认没有买二赠一
                hasMore = false;
                //生成模拟json数据
                String cola = et_cola.getText().toString();
                String ball = et_ball.getText().toString();
                String apple = et_apple.getText().toString();
                int cola_num = 1;
                int ball_num = 1;
                int apple_num = 1;
                if(!cola.equals("")){
                    cola_num = Integer.parseInt(cola);
                }
                if(!ball.equals("")){
                    ball_num = Integer.parseInt(ball);
                }
                if(!apple.equals("")){
                    apple_num = Integer.parseInt(apple);
                }
                String barcode_cola = cola_num == 1?"ITEM000001":"ITEM000001-"+cola_num;
                String barcode_ball = ball_num == 1? "ITEM000002":"ITEM000002-"+ball_num;
                String barcode_apple = apple_num == 1? "ITEM000003":"ITEM000003-"+apple_num;
                JSONArray array = new JSONArray();
                array.put(barcode_cola);
                array.put(barcode_ball);
                array.put(barcode_apple);
                calcTotalAmount(array);
                //计算价格并输出小票结果
                break;
            case R.id.btn_edit:
                Intent i = new Intent(MainActivity.this,EditActivity.class);
                startActivity(i);
                break;
        }
    }
}
