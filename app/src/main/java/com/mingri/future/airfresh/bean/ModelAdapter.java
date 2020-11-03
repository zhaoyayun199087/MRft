package com.mingri.future.airfresh.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingri.future.airfresh.R;

/**
 * Created by Administrator on 2017/6/27.
 */
public class ModelAdapter extends BaseAdapter {
    private Context context;
    public ModelAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler;
        if(convertView==null){
            hodler=new ViewHodler();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_model,null);
            hodler.icon= (ImageView) convertView.findViewById(R.id.iv_icon);
            hodler.name= (TextView) convertView.findViewById(R.id.tv_name);
            hodler.ll= (LinearLayout) convertView.findViewById(R.id.ll_layout);

            convertView.setTag(hodler);
        }else{
            hodler= (ViewHodler) convertView.getTag();
        }
        if(position==2){
              hodler.ll.setBackgroundResource(R.mipmap.caid_bg_xuanz);
        }else{
              hodler.ll.setBackgroundColor(00000000);
        }
        return convertView;
    }

    class ViewHodler
    {
        LinearLayout ll;
        ImageView icon;
        TextView name;
    }



}
