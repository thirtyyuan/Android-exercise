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

    public static void saveLoginResponse(Context context, String userKey, Boolean result, String message) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("userKey", userKey);
        editor.putBoolean("result", result);
        editor.putString("message", message);
        editor.commit();
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
            int alarmDeviceNum = 0;
            String alarmString = "";
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
                    if (sensorIsAlarm) {
                        alarmString = "(异常)";
                        alarmDeviceNum ++;
                    } else {
                        alarmString = "";
                    }
                    mySensor sensor = new mySensor(sensorId, sensorName + alarmString, sensorValue, sensorUnit, sensorLastUpdateTime, sensorIsOnline , sensorIsAlarm);
                    sensorList.add(sensor);
                    SensorInfo.sensorIDArray[j] = sensorId;
                    SensorInfo.isAlarm[j] = sensorIsAlarm;
                    SensorInfo.alarmDeviceNum = alarmDeviceNum;
                    needAdvice(sensorName, sensorValue);
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

    public static void needAdvice (String sensorName, String sensorValue) {
        String temp = "";
        String humi = "";
        String sunlight = "";
        String pm = "";
        String turbidity = "";
        String ph = "";
        switch (sensorName) {
            case "温度":
                if (Integer.valueOf(sensorValue) < 15) {
                    temp = "水温过低，请减缓水体散热; \n";
                } else if (Integer.valueOf(sensorValue) >= 30){
                    temp = "水温过高，请加快水体散热; \n";
                } else {
                    temp = "";
                }
                break;
            case "湿度":
                if (Integer.valueOf(sensorValue) < 40) {
                    humi = "湿度过低，请注意防火; \n";
                } else {
                    humi = "";
                }
                break;
            case "光照强度":
                if (Integer.valueOf(sensorValue) < 100) {
                    sunlight = "光照不足，请检查设备是否被遮挡; \n";
                } else if (Integer.valueOf(sensorValue) >= 12000){
                    sunlight = "光照强烈，请注意防晒 \n";
                } else {
                    sunlight = "";
                }
                break;
            case "pm2.5":
                if (Integer.valueOf(sensorValue) > 80) {
                    pm = "颗粒物超标，请注意保护口鼻; \n";
                } else {
                    pm = "";
                }
                break;
            case "水体浊度":
                if (Double.valueOf(sensorValue) > 4.0) {
                    turbidity = "水体浑浊，请检查水体是否有污染; \n";
                } else {
                    turbidity = "";
                }
                break;
            case "Ph":
                if (Double.valueOf(sensorValue) < 6.0) {
                    ph = "水体呈酸性，请检查污染源并保护水生生物; \n";
                } else if (Double.valueOf(sensorValue) > 8.0) {
                    ph = "水体呈碱性，请请检查污染源并保护水生生物; \n";
                } else {
                    ph = "";
                }
                break;
        }
        SensorInfo.advice = "";
        SensorInfo.advice += temp + humi + sunlight + pm + turbidity + ph;
    }
}
