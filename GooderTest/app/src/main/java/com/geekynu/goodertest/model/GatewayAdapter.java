package com.geekynu.goodertest.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.geekynu.goodertest.R;


import java.util.List;

/**
 * Created by yuanhonglei on 8/9/16.
 */
public class GatewayAdapter extends ArrayAdapter<Gateway> {
    private int resourceId;

    public GatewayAdapter(Context context, int textViewResourceId, List<Gateway> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gateway gateway = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.groupName = (TextView) view.findViewById(R.id.buddy_listview_group_name);
            viewHolder.groupNum = (TextView) view.findViewById(R.id.buddy_listview_group_num);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.groupName.setText(gateway.getName());
        viewHolder.groupNum.setText(gateway.getSensorNum() + "个传感器");
        return view;
    }

    class ViewHolder {
        TextView groupName;
        TextView groupNum;
    }
}
