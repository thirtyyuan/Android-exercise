package com.geekynu.goodertest.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekynu.goodertest.R;

import java.util.List;

/**
 * Created by yuanhonglei on 8/10/16.
 */
public class mySensorAdapter extends ArrayAdapter<mySensor> {
    private int resourceId;

    public mySensorAdapter(Context context, int textViewResourceId, List<mySensor> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mySensor sensor = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.childName = (TextView) view.findViewById(R.id.buddy_listview_child_name);
            viewHolder.childValue = (TextView) view.findViewById(R.id.buddy_listview_child_value);
            viewHolder.lastUpdateTime = (TextView) view.findViewById(R.id.last_update_time_textView);
            viewHolder.childStatus = (ImageView) view.findViewById(R.id.buddy_listview_child_status);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.childName.setText(sensor.getName());
        viewHolder.childValue.setText(sensor.getValue() + sensor.getUnit());
        viewHolder.lastUpdateTime.setText(sensor.getLastUpdateTime());
        if (sensor.getIsOnline()) {
            viewHolder.childStatus.setImageResource(R.drawable.online);
        } else {
            viewHolder.childStatus.setImageResource(R.drawable.offline);
        }
        return view;
    }

    class ViewHolder {
        TextView childName;
        TextView childValue;
        TextView lastUpdateTime;
        ImageView childStatus;
    }
}
