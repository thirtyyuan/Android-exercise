package com.geekynu.goodertest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.geekynu.goodertest.R;
import com.geekynu.goodertest.model.mySensor;
import com.geekynu.goodertest.model.mySensorAdapter;
import com.geekynu.goodertest.util.HttpUtil;
import com.geekynu.goodertest.util.PositionPointer;
import com.geekynu.goodertest.util.Utility;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by yuanhonglei on 8/11/16.
 */
public class SensorListActivity extends BaseActivity {
    private List<mySensor> mySensorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sensor_list);
        StatusBarCompat.translucentStatusBar(SensorListActivity.this);
        initSensors();
    }

    private void initSensors() {
        String getGatewayURL = "http://open.lewei50.com/api/V1/User/GetSensorswithgateway?userkey=";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SensorListActivity.this);
        final String userkey = prefs.getString("userKey", "");
        final String address  = getGatewayURL + userkey;
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mySensorList = Utility.handleSensorResponse(response);
                        if (mySensorList == null) {
                            TastyToast.makeText(SensorListActivity.this, "获取失败，请稍后再试", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            mySensorAdapter adapter = new mySensorAdapter(SensorListActivity.this, R.layout.sensor_list_child_item, mySensorList);
                            ListView listView = (ListView) findViewById(R.id.sensor_list_view);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    PositionPointer.setPositionItem(position);
                                    Intent intent = new Intent(SensorListActivity.this, HistoryDataActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TastyToast.makeText(SensorListActivity.this, "获取传感器数据失败", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                });
            }
        });
    }
}
