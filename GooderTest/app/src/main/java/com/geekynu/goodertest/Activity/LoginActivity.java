package com.geekynu.goodertest.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.geekynu.goodertest.R;
import com.geekynu.goodertest.util.HttpUtil;
import com.geekynu.goodertest.util.Utility;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by yuanhonglei on 8/5/16.
 */
public class LoginActivity extends Activity {
    private EditText accountEdit;
    private EditText passwordEdit;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String username, password;
    private ImageButton loginButton;
    private CheckBox rememberData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.account_text);
        passwordEdit = (EditText) findViewById(R.id.password_text);
        rememberData = (CheckBox) findViewById(R.id.remember_data);
        loginButton = (ImageButton) findViewById(R.id.login_button);
        loadUsernameAndPassword();
        loginButton.setOnClickListener(new loginListener());
    }

    private class loginListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String account = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            String userkeyURL = "http://open.lewei50.com/api/v1/user/login?";
            if (!account.equals("") && !password.equals("")) {
                setDefaultUsernameAndPassword();
                NetworkInfo networkInfo = ((ConnectivityManager)getSystemService("connectivity")).getActiveNetworkInfo();
                if ((networkInfo != null) && (networkInfo.isAvailable())) {
                    loginToServer(userkeyURL, account, password);
                } else {
                    TastyToast.makeText(LoginActivity.this, "设备未连接网络", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            } else {
                TastyToast.makeText(LoginActivity.this, "账号密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
            }
        }
    }

    private void setDefaultUsernameAndPassword() {
        editor = pref.edit();
        if (rememberData.isChecked()) {
            editor.putBoolean("remember_password", true);
            username = accountEdit.getText().toString();
            editor.putString("account", username);
            password = passwordEdit.getText().toString();
            editor.putString("password", password);
        } else {
            editor.clear();
        }
        editor.commit();
    }

    private void loadUsernameAndPassword() {
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberData.setChecked(true);
        }
    }

    private void loginToServer(final String url, final String username, final String password) {

        final String address  = url + "username=" + username + "&password=" + password;
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                Utility.handleLoginResponse(LoginActivity.this, response);
                final String userkey = prefs.getString("userKey", "");
                if (!userkey.equals("null")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TastyToast.makeText(LoginActivity.this, "用户名密码错误", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TastyToast.makeText(LoginActivity.this, "服务器异常", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                });
            }
        });
    }
}
