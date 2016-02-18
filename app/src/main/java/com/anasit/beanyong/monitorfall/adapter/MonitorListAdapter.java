package com.anasit.beanyong.monitorfall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anasit.beanyong.monitorfall.R;
import com.anasit.beanyong.monitorfall.entity.Monitor;

import java.util.List;

/**
 * Created by BeanYong on 2015/12/17.
 */
public class MonitorListAdapter extends BaseAdapter {

    private List<Monitor> mDatas;
    private LayoutInflater mInflater;
    private int mLayoutId;

    public MonitorListAdapter(Context context, List<Monitor> data, int layoutId) {
        mInflater = LayoutInflater.from(context);
        mDatas = data;
        mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutId, parent, false);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.monitor_list_item_tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Monitor monitor = (Monitor) getItem(position);
        vh.tv.setText(monitor.getTel());
        return convertView;
    }

    static final class ViewHolder {
        TextView tv;
    }
}
