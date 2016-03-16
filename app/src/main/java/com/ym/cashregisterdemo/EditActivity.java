package com.ym.cashregisterdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.ym.cashregisterdemo.adapter.GoodsAdapter;
import com.ym.cashregisterdemo.application.CashRegisterApplication;
import com.ym.cashregisterdemo.data.Goods;
import com.ym.cashregisterdemo.data.RelationData;
import com.ym.cashregisterdemo.data.RuleData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends Activity implements View.OnClickListener {
    /**
     * 优惠活动下拉菜单
     */
    private Spinner spinner;

    /**
     * application
     */
    private CashRegisterApplication app;
    /**
     * 享受和不享受优惠活动的列表
     */
    private ListView lv_preferential, lv_nonpreferential;
    /**
     * 删除、添加
     */
    private Button btn_delete, btn_add;
    /**
     * 所有优惠活动列表
     */
    private List<RuleData> ruleDataList;
    /**
     * 所有商品列表
     */
    private List<Goods> goodsList;
    /**
     * 享受优惠的商品列表
     */
    private List<Goods> goodsList_preferential = new ArrayList<Goods>();
    /**
     * 不享受优惠的商品列表
     */
    private List<Goods> goodsList_nonpreferential = new ArrayList<Goods>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        app = (CashRegisterApplication) getApplication();

        //初始化数据
        initData();
        //初始化视图
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ruleDataList = RuleData.getAll(app.getDB());
        goodsList = Goods.getAll(app.getDB());
    }


    /**
     * 初始化界面
     */
    private void initView() {
        //返回按钮
        findViewById(R.id.btn_back).setOnClickListener(this);
        //下拉菜单
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter aradapter = new ArrayAdapter(
                this.getApplicationContext(),
                R.layout.simple_item, ruleDataList);
        aradapter
                .setDropDownViewResource(R.layout.simple_item);
        spinner.setAdapter(aradapter);
        //选中某个选项后，添加事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getGoods(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //添加和删除按钮
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        //享受优惠的商品列表和不享受优惠的商品列表
        lv_preferential = (ListView) findViewById(R.id.listview_preferential);
        lv_nonpreferential = (ListView) findViewById(R.id.listview_nonpreferential);
        GoodsAdapter preferentialAdapter = new GoodsAdapter(this, null);
        GoodsAdapter nonpreferentialAdapter = new GoodsAdapter(this,null);
        lv_preferential.setAdapter(preferentialAdapter);
        lv_nonpreferential.setAdapter(nonpreferentialAdapter);
        //加载默认优惠活动下的商品列表
//        getGoods(0);
    }

    /**
     * 获取对应索引的优惠活动下的商品
     * 商品列表中剩余的则是不享受该优惠活动的商品
     * 刷新列表，展示商品
     *
     * @param i
     */
    private void getGoods(int i) {
        goodsList_preferential.clear();
        goodsList_nonpreferential.clear();
        if(null == ruleDataList){
            Log.d("editActivity.getGoods","没有优惠活动");
            return;
        }
        if (i + 1 > ruleDataList.size()) {
            Log.d("editActivity.getGoods","异常：数组越界");
            return;
        }
        //当前选择的优惠活动
        RuleData data = ruleDataList.get(i);
        //享受该优惠活动的商品id列表
        List<String> relationDataList = RelationData.selectById(new String[]{data.id + ""}, app.getDB());
        //根据上面的id列表查询商品列表
        goodsList_preferential = Goods.getGoodByIds(app.getDB(),relationDataList);
        //剩下的商品就是不享受优惠活动的
        for (int x = 0; x<goodsList.size() ;x++){
            boolean flag = false;
            for(int y = 0; y<goodsList_preferential.size() ;y++){
                if(goodsList.get(x).goods_id == goodsList_preferential.get(y).goods_id){
                    flag = true;
                }
            }
            if(flag == false){
                goodsList_nonpreferential.add(goodsList.get(x));
            }
        }
        //刷新listview
        GoodsAdapter adapter1 = (GoodsAdapter) lv_preferential.getAdapter();
        adapter1.setList(goodsList_preferential);
        adapter1.notifyDataSetChanged();
        GoodsAdapter adapter2 = (GoodsAdapter) lv_nonpreferential.getAdapter();
        adapter2.setList(goodsList_nonpreferential);
        adapter2.notifyDataSetChanged();
  }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_delete://从享受优惠列表中删除
                    for (Goods goods: goodsList_preferential){
                        if(goods.checked == true){
                            List<String> args = new ArrayList<String>();
                            args.add(goods.goods_id+"");
                            args.add(ruleDataList.get(spinner.getSelectedItemPosition()).id+"");
                            RelationData.deleteByGoodIdRuleId(args, app.getDB());
                            args.clear();
                        }
                    }
                getGoods(spinner.getSelectedItemPosition());
                break;
            case R.id.btn_add://添加到享受优惠列表
                    for (Goods goods : goodsList_nonpreferential){
                        if(goods.checked == true){
                            RelationData data = new RelationData(ruleDataList.get(spinner.getSelectedItemPosition()).id,goods.goods_id ,ruleDataList.get(spinner.getSelectedItemPosition()).level);
                            RelationData.create(data,app.getDB());
                            goods.checked = false;
                        }
                    }
                getGoods(spinner.getSelectedItemPosition());
                break;
        }
    }
}
