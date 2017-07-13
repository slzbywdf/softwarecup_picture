package com.picture.picture_manager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

/**
 * Created by 13307 on 2017/3/21.
 */

public class ChangePWD extends AppCompatActivity {
    private EditText text1, text2, text3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.change_pwd);
        ActivityCollector.activities.add(this);

        text1 = (EditText) findViewById(R.id.edit_old_pwd);
        text2 = (EditText) findViewById(R.id.edit_new_pwd);
        text3 = (EditText) findViewById(R.id.edit_new_pwd_confirm);
        Button button = (Button) findViewById(R.id.button_confirm_pwd_change);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpwd = text1.getText().toString();
                String newpwd = text2.getText().toString();
                String newconfirm = text3.getText().toString();
                if (oldpwd.length() == 0 || newconfirm.length() == 0 || newpwd.length() == 0) {
                    Toast.makeText(ChangePWD.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else if (!newpwd.equals(newconfirm)) {
                    Toast.makeText(ChangePWD.this, "新密码两次输入不匹配！", Toast.LENGTH_SHORT).show();
                } else {
                    //此处编写修改密码联网代码
                    changePwd(oldpwd, newpwd);
                }
            }
        });
    }

    private void changePwd(String oldpwd, String newpwd) {
        String address = GlobalFlags.getIpAddress() + "passwordchange.jsp";
        String params = "pptelephone=" + GlobalFlags.getUserID() + "&oldpassword=" + oldpwd + "&newpassword=" + newpwd;
        //Log.d("changeinfo", "setUserInfo: " + address + " " + params);
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("changepwd", "message: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int result = jsonObject.getInt("result");
                            if (result == 1) {
                                Toast.makeText(ChangePWD.this, "密码修改成功，请重新登录！", Toast.LENGTH_SHORT).show();
                                GlobalFlags.setIsLoggedIn(false);
                                Intent intent = new Intent(ChangePWD.this, Login.class);
                                startActivity(intent);
                            } else if (result == -1)
                                Toast.makeText(ChangePWD.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(ChangePWD.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChangePWD.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
