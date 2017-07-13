package com.picture.picture_manager.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.ActivityCollector;
import com.picture.picture_manager.Utils.GlobalFlags;
import com.picture.picture_manager.Utils.HttpCallbackListener;
import com.picture.picture_manager.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText text1_username, text2_pwd;
    private String user_name, pwd;
    private CheckBox rem_pw, auto_login;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        ActivityCollector.activities.add(this);

        //Log.d("Login", "onCreate: called");

        text1_username = (EditText) findViewById(R.id.edit_login_account);
        text2_pwd = (EditText) findViewById(R.id.edit_login_password);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        rem_pw = (CheckBox) findViewById(R.id.memory);
        auto_login = (CheckBox) findViewById(R.id.autologin);

        //判断自动登录多选框状态
        if (sp.getBoolean("ISCHECK", false)) {
            rem_pw.setChecked(true);
            text1_username.setText(sp.getString("USER_NAME", ""));
            text2_pwd.setText(sp.getString("PASSWORD", ""));
            text1_username.setSelection(text1_username.getText().length());//光标移到文字最后
            //判断自动登陆多选框状态
            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                auto_login.setChecked(true);

                //此处联网判断用户名和密码
                GlobalFlags.setUserID(sp.getString("USER_NAME", ""));
                LoginValidation(sp.getString("USER_NAME", ""), sp.getString("PASSWORD", ""));

                /*调试代码
                GlobalFlags.setUserID(sp.getString("USER_NAME", ""));
                GlobalFlags.setIsLoggedIn(true);
                Intent intent = new Intent(Login.this, Main.class);
                startActivity(intent);*/
                //Toast.makeText(Login.this, "请重新输入密码！", Toast.LENGTH_SHORT).show();
            }
        }

        Button button = (Button) findViewById(R.id.button_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = text1_username.getText().toString().trim();
                pwd = text2_pwd.getText().toString().trim();
                GlobalFlags.setUserID(user_name);
                if (user_name.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(Login.this, "密码或账号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //*********调试代码
                    /*Toast.makeText(Login.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    if (rem_pw.isChecked()) {
                        //记住用户名、密码
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME", user_name);
                        editor.putString("PASSWORD", pwd);
                        editor.commit();
                    }
                    GlobalFlags.setIsLoggedIn(true);
                    Intent intent = new Intent(Login.this, Main.class);
                    startActivity(intent);*/
                    LoginValidation(user_name, pwd);
                }
            }
        });

        //转跳注册界面
        Button button1 = (Button) findViewById(R.id.button_register);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivityForResult(intent, 1);
            }
        });


        //监听记住密码多选框按钮事件
        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pw.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).commit();
                } else {
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
            }
        });

        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pw.isChecked()) {
                    if (auto_login.isChecked()) {
                        sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
                    } else {
                        sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                    }
                }
            }
        });
    }

    private void LoginValidation(final String user_name, final String pwd) {
        //登录逻辑
        String address = GlobalFlags.getIpAddress() + "P_login.jsp";
        String params = "pptelephone=" + user_name + "&ppassword=" + pwd;
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("Login", "message: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("pptelephone");
                            if (result.equals(user_name)) {
                                //此处处理从server返回的信息，如果登录成功，则执行下面代码，否则错误信息
                                Toast.makeText(Login.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                if (rem_pw.isChecked()) {
                                    //记住用户名、密码
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("USER_NAME", user_name);
                                    editor.putString("PASSWORD", pwd);
                                    editor.commit();
                                }
                                GlobalFlags.setIsLoggedIn(true);
                                Intent intent = new Intent(Login.this, Main.class);
                                startActivity(intent);
                            } else if (result.equals("-1")) {
                                Toast.makeText(Login.this, "登录失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Login.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                //Log.d("Get from server", "error message: " + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //从注册界面返回用户名和密码
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String name = intent.getStringExtra("username");
                    String password = intent.getStringExtra("password");
                    //Log.d("Login", "onActivityResult: called" + name + " " + password);
                    if (name != null && password != null && name.length() != 0 && password.length() != 0) {
                        rem_pw.setChecked(false);
                        auto_login.setChecked(false);
                        text1_username.setText(name);
                        text2_pwd.setText(password);
                        text1_username.setSelection(text1_username.getText().length());//光标移到文字最后
                    }
                }
                break;
            default:
        }
    }

    //自动登录后，在主界面按返回直接退出
    @Override
    public void onRestart() {
        super.onRestart();
        //Log.d("Login", "onRestart: called");
        if (GlobalFlags.isLoggedIn()) {
            this.finish();
        }
    }

    /*public void onStart() {
        super.onStart();
        Log.d("Login", "onStart: called");
    }
    public void onResume() {
        super.onResume();
        Log.d("Login", "onResume: called");
    }
    public void onPause() {
        super.onPause();
        Log.d("Login", "onPause: called");
    }
    public void onStop() {
        super.onStop();
        Log.d("Login", "onStop: called");
    }
    public void onDestroy() {
        super.onDestroy();
        Log.d("Login", "onDestroy: called");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }*/
}
