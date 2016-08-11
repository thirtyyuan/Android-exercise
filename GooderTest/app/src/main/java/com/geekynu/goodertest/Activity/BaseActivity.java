package com.geekynu.goodertest.Activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by yuanhonglei on 8/11/16.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
