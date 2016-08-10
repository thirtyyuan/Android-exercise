package com.geekynu.goodertest.model;

import android.app.Application;

/**
 * Created by yuanhonglei on 8/5/16.
 */
public class MyApplication extends Application{
    static final long serialVersionUID = 1 ;
    public String USERKEY = "c24e7099abc24f10a70265a770110c8d";
    public String SensorsWithGatewayURL = "http://www.lewei50.com/api/V1/user/getSensorsWithGateway";
    public String HistoryDataURL = "http://www.lewei50.com/api/v1/sensor/gethistorydata/";
    public String getUserkeyURL = "http://open.lewei50.com/api/v1/user/login?";
    public boolean isOnline;
}
