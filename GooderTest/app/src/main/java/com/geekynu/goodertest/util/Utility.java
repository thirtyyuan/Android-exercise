package com.geekynu.goodertest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.geekynu.goodertest.model.Gateway;
import com.geekynu.goodertest.model.mySensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanhonglei on 8/5/16.
 */
public class Utility  {
    public static String handleLoginResponse(Context context, String response){
        try {
            JSONObject loginInfo = new JSONObject(response);
            String userKey = loginInfo.getString("Data");
            Boolean result = loginInfo.getBoolean("Successful");
            String message = loginInfo.getString("Message");
            saveLoginResponse(context, userKey, result,message);
            if (result){
                return userKey;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Gateway> handleGatewayResponse(String response){
        try {
            JSONArray gatewayInfo = new JSONArray(response);
            List<Gateway> gatewayList = new ArrayList<>();
            int GatewayListSize = gatewayInfo.length();
            int i = 0, j = 0;
            do {
                JSONObject JGateway = gatewayInfo.getJSONObject(i);
                long id = JGateway.getLong("id");
                String name = JGateway.getString("name");
                String description = JGateway.getString("description");
                String typeName = JGateway.getString("typeName");
                JSONArray sensorInfo = new JSONArray(JGateway.getString("sensors"));
                int sensorNum = sensorInfo.length();
                Gateway gateway = new Gateway(id, name, description, typeName, sensorNum);
                gatewayList.add(gateway);
                i++;
            } while (i < GatewayListSize);
            return gatewayList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<mySensor> handleSensorResponse(String response){
        try {
            JSONArray gatewayInfo = new JSONArray(response);
            List<mySensor> sensorList = new ArrayList<>();
            int GatewayListSize = gatewayInfo.length();
            int position = PositionPointer.getPosition();
            int j = 0;
            if (position <= GatewayListSize) {
                JSONObject JGateway = gatewayInfo.getJSONObject(position);
                JSONArray sensorInfo = new JSONArray(JGateway.getString("sensors"));
                int sensorNum = sensorInfo.length();
                do {
                    JSONObject JSensor = sensorInfo.getJSONObject(j);
                    long sensorId = JSensor.getLong("id");
                    String sensorName = JSensor.getString("name");
                    String sensorValue = JSensor.getString("value");
                    String sensorUnit = JSensor.getString("unit");
                    String sensorLastUpdateTime = JSensor.getString("lastUpdateTime");
                    boolean sensorIsOnline = JSensor.getBoolean("isOnline");
                    boolean sensorIsAlarm = JSensor.getBoolean("isAlarm");
                    SensorID.sensorIDArray[j] = sensorId;
                    mySensor sensor = new mySensor(sensorId, sensorName, sensorValue, sensorUnit, sensorLastUpdateTime, sensorIsOnline , sensorIsAlarm);
                    sensorList.add(sensor);
                    j++;
                } while (j < sensorNum);
                return sensorList;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveLoginResponse(Context context, String userKey, Boolean result, String message) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("userKey", userKey);
        editor.putBoolean("result", result);
        editor.putString("message", message);
        editor.commit();
    }
}
