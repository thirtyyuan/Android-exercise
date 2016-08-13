package com.geekynu.goodertest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.geekynu.goodertest.R;
import com.geekynu.goodertest.model.Gateway;
import com.geekynu.goodertest.model.GatewayAdapter;
import com.geekynu.goodertest.util.HttpUtil;
import com.geekynu.goodertest.util.PositionPointer;
import com.geekynu.goodertest.util.Utility;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by yuanhonglei on 8/9/16.
 */
public class MainActivity extends BaseActivity {
    private List<Gateway> gatewayList = new ArrayList<>();
    PullRefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_list);
        StatusBarCompat.translucentStatusBar(MainActivity.this);

        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initGateways();
            }
        });
        initGateways();

    }

    private void initGateways() {
        String getGatewayURL = "http://open.lewei50.com/api/V1/User/GetSensorswithgateway?userkey=";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final String userkey = prefs.getString("userKey", "");
        final String address  = getGatewayURL + userkey;
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gatewayList = Utility.handleGatewayResponse(response);
                        if (gatewayList == null) {
                            TastyToast.makeText(MainActivity.this, "获取失败，请稍后再试", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            GatewayAdapter adapter = new GatewayAdapter(MainActivity.this, R.layout.sensor_list_group_item, gatewayList);
                            ListView listView = (ListView) findViewById(R.id.sensor_list_view);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    PositionPointer.setPosition(position);
                                    Intent intent = new Intent(MainActivity.this, SensorListActivity.class);
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
                        TastyToast.makeText(MainActivity.this, "获取网关数据失败", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                });
            }
        });
        layout.setRefreshing(false);
    }
}
