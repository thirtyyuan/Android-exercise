package com.example.locationtest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/*
* author: thirtyyuan
* email: thirtyyaun@gmail.com
* date: 07/29/2016
* */

public class MainActivity extends Activity {

    private String provider;
    private TextView positionTextView;
    private LocationManager locationManager;
    public static final int SHOW_LOCATION = 0;
    public static final int MULTIPLE_PERMISSION_ASK_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        positionTextView =(TextView) findViewById(R.id.position_text_view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT < 23) {
            locationProvider();
        } else {
            checkPermission();
        }
    }

    public void checkPermission(){
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestMultiplePermissions();
        } else {
            locationProvider();
        }
    }

    private void requestMultiplePermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(this, permissions, MULTIPLE_PERMISSION_ASK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSION_ASK_CODE: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //申请失败，可以继续向用户解释
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Help")
                            .setMessage("Need some permission to run this app.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkPermission();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
<<<<<<< HEAD
//                // 如果请求被拒绝，那么通常grantResults数组为空
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //申请成功，进行相应操作
//                    Toast.makeText(this,"Thank you for your grant 1! ",Toast.LENGTH_SHORT).show();
//                    locationProvider();
//                } else {
//
//                }
=======
>>>>>>> 45b85eadd64b84db64b9e8f6a48583b1dbaf22b7
            }
        }
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    //复写位置监听器
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //更新设备当前位置
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    //自定义位置提供者类，供将来需要时调用
    private void locationProvider () {
        //获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            //没有可用的位置提供器时，提醒用户
            Toast.makeText(this, "No location provider to use.", Toast.LENGTH_SHORT).show();
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //显示设备当前位置信息
            showLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    //可能将来其他地方会直接调用展示而不是再次定位，写在此处提高复用率，降低代码耦合
    private void showLocation (final Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    StringBuilder urlStr = new StringBuilder();
                    urlStr.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
                    urlStr.append(location.getLatitude()).append(",");
                    urlStr.append(location.getLongitude());
                    urlStr.append("&sensor=false");
                    URL url = new URL(urlStr.toString());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    //获取 result 节点下的位置信息
                    JSONArray resultArray = jsonObject.getJSONArray("results");
                    if (resultArray.length() > 0) {
                        JSONObject subObject = resultArray.getJSONObject(0);
                        //取出格式化后的位置信息
                        String  address = subObject.getString("formatted_address");
                        Message message = new Message();
                        message.what = SHOW_LOCATION;
                        message.obj = address;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_LOCATION:
                    String currentPosition = (String) msg.obj;
                    positionTextView.setText(currentPosition);
                    break;
                default:
                    break;
            }
        }
    };
}
