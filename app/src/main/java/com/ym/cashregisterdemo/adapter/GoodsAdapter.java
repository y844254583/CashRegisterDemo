package com.ym.cashregisterdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ym.cashregisterdemo.R;
import com.ym.cashregisterdemo.data.Goods;

import java.util.List;

/**
 * 商品列表适配器类
 * Created by yuanmin on 2016/3/16.
 */
public class GoodsAdapter extends BaseAdapter {

    public static class ItemTag {
        public CheckBox checkBox;
        public TextView nameTextView;
        public TextView bar_codeTextView;
    }

    public List<Goods> goodsList;

    private Context context;

    public GoodsAdapter(Context context, List<Goods> list){
        this.context = context;
        this.goodsList = list;
    }

    public void setList( List<Goods> list){
        this.goodsList = list;
    }

    @Override
    public int getCount() {
        if(null != goodsList){
            return goodsList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemTag tag = null;
        View view = LayoutInflater.from(context).inflate(R.layout.item, null);
        if (convertView == null) {
            tag = new ItemTag();
            tag.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            tag.nameTextView = (TextView) view.findViewById(R.id.goods_name);
            tag.bar_codeTextView = (TextView) view.findViewById(R.id.bar_code);
            view.setTag(tag);
        } else {
            view = convertView;
            tag = (ItemTag) view.getTag();
        }
        tag.checkBox.setChecked(goodsList.get(position).checked);
        tag.bar_codeTextView.setText(goodsList.get(position).bar_code);
        tag.nameTextView.setText(goodsList.get(position).goods_name);
        tag.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                goodsList.get(position).checked = isChecked;
            }
        });

        return view;
    }
}
