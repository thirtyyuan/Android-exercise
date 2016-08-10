package com.geekynu.goodertest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.geekynu.goodertest.model.Gateway;

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
            int i = 0;
            do {
                JSONObject jsonObject = gatewayInfo.getJSONObject(i);
                long id = jsonObject.getLong("id");
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                String typeName = jsonObject.getString("typeName");
                Object sensorInfo = new JSONArray(jsonObject.getString("sensors"));
                int sensorNum = ((JSONArray)sensorInfo).length();
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

    public static void saveLoginResponse(Context context, String userKey, Boolean result, String message) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("userKey", userKey);
        editor.putBoolean("result", result);
        editor.putString("message", message);
        editor.commit();
    }
}
