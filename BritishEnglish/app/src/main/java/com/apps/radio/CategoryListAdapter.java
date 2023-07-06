package com.apps.radio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by xuzepei on 2018/2/9.
 */

public class CategoryListAdapter extends BaseAdapter {

    private LinkedList<Category> items;
    private Context context;

    public CategoryListAdapter(LinkedList<Category> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.cell_categorylist,parent,false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_imageview);
        TextView titleView = (TextView) convertView.findViewById(R.id.item_title);
        TextView subtitleView = (TextView) convertView.findViewById(R.id.cate_subtitle);
        imageView.setImageResource(items.get(position).getImageResID());
        titleView.setText(items.get(position).getTitle());
        subtitleView.setText(items.get(position).getSubtitle());
        return convertView;
    }

}
