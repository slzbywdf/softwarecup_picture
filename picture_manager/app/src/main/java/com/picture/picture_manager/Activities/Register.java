package com.picture.picture_manager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.ActivityCollector;
import com.picture.picture_manager.Utils.GlobalFlags;
import com.picture.picture_manager.Utils.HttpCallbackListener;
import com.picture.picture_manager.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    private EditText text1, text2, text3;
    private String user_name, pwd;
    private HttpCallbackListener listener = new HttpCallbackListener() {
        @Override
        public void onFinish(final String response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Log.d("JSON", "onFinish: " + response);
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("reg_answer");
                        if (result.equals("1")) {
                            Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            //注册成功，将用户名和密码传到登录界面
                            String name = user_name;
                            String password = pwd;
                            Intent intent = new Intent();
                            intent.putExtra("username", name);
                            intent.putExtra("password", password);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else if (result.equals("-1")) {
                            //Log.d("JSON", "onFinish: " + result);
                            Toast.makeText(Register.this, "手机号已被注册！", Toast.LENGTH_SHORT).show();
                        }
                        //Log.d("JSON", "onFinish: " + result);
                    } catch (JSONException e) {
                        Toast.makeText(Register.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onError(final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Register.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            //Log.d("Error", "onError: " + e.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);
        ActivityCollector.activities.add(this);

        text1 = (EditText) findViewById(R.id.edit_register_account);
        text2 = (EditText) findViewById(R.id.edit_register_password);
        text3 = (EditText) findViewById(R.id.edit_password_confirm);
        Button button = (Button) findViewById(R.id.button_register);

        //注册逻辑
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = text1.getText().toString().trim();
                pwd = text2.getText().toString().trim();
                String confirm = text3.getText().toString().trim();
                if (user_name.length() == 0) {
                    Toast.makeText(Register.this, "请输入手机号注册！", Toast.LENGTH_SHORT).show();
                } else if (pwd.length() == 0 || confirm.length() == 0) {
                    Toast.makeText(Register.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(confirm)) {
                    Toast.makeText(Register.this, "两次密码不匹配！", Toast.LENGTH_SHORT).show();
                } else {
                    //注册逻辑
                    String ip = GlobalFlags.getIpAddress() + "P_register.jsp";
                    String params = "pptelephone=" + user_name + "&ppassword=" + pwd;
                    HttpUtil.sendHttpRequest(ip, params, listener);
                }
            }
        });
    }
}
