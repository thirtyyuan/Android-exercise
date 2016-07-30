package com.example.locationtest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final int MULTIPLE_PERMISSION_ASK_CODE = 0 ;

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            addPermission(permissionList, Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (permissionList.size() > 0) {
            for (int i = 0; i < permissionList.size(); i++){
                //请求用户授予权限
                ActivityCompat.requestPermissions(this, new String[]{permissionList.get(i)}, MULTIPLE_PERMISSION_ASK_CODE);
                permissionList.remove(i);
            }
            return;
        } else {
            locationProvider();
        }
    }
    
    //添加权限到 permissionList 的方法
    private void addPermission(List<String> permissionList, String permission) {
        permissionList.add(permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_ASK_CODE: {
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    Toast.makeText(this,"Thank you for your grant! ",Toast.LENGTH_SHORT).show();
                    locationProvider();
                } else {
                    //申请失败，可以继续向用户解释
                    Toast.makeText(this,"OH...Permission has been Denied.",Toast.LENGTH_SHORT).show();
                }
                return;
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
    private void showLocation (Location location) {
        String currentPosition = "latitude is " + location.getLatitude() + "\n longitude is " + location.getLongitude();
        positionTextView.setText(currentPosition);
    }
}
