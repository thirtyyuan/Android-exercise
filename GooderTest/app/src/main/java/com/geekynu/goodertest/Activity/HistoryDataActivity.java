package com.geekynu.goodertest.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.baoyz.widget.PullRefreshLayout;
import com.geekynu.goodertest.R;
import com.geekynu.goodertest.util.PositionPointer;
import com.geekynu.goodertest.util.SensorID;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by yuanhonglei on 8/12/16.
 */
public class HistoryDataActivity extends BaseActivity {

    private WebView webView;
    PullRefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history_data_view);
        StatusBarCompat.translucentStatusBar(HistoryDataActivity.this);
        webView = (WebView) findViewById(R.id.history_data_WebView);
        layout = (PullRefreshLayout) findViewById(R.id.data_view_refresh);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initHistoryData();
            }
        });
        initHistoryData();
    }

    private void initHistoryData() {
        String historyDataHeaderUrl = "http://open.lewei50.com/m/user/doc/";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HistoryDataActivity.this);
        final String userkey = prefs.getString("userKey", "");
        int position = PositionPointer.getPositionItem();
        long sensorId = SensorID.sensorIDArray[position];
        Object localObject = webView.getSettings();
        ((WebSettings)localObject).setJavaScriptEnabled(true);
        ((WebSettings)localObject).setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        ((WebSettings)localObject).setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        final String address  = historyDataHeaderUrl + sensorId + "?userkey=" + userkey;
        webView.loadUrl(address);
        layout.setRefreshing(false);
    }
}
