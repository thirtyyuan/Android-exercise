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
    public View getView(int position, View contentView, ViewGroup parent) {
        Gateway gateway = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView groupName = (TextView) view.findViewById(R.id.buddy_listview_group_name);
        TextView groupNum = (TextView) view.findViewById(R.id.buddy_listview_group_num);
        groupName.setText(gateway.getName());
        groupNum.setText(gateway.getSensorNum() + "个传感器");
        return view;
    }
}
