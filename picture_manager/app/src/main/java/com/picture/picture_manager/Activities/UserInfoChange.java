package com.picture.picture_manager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class UserInfoChange extends AppCompatActivity {
    //private String user, email, major, interests;
    private TextView userview, telepview, emailview, majorview, interbtn;
    private ImageView userIcon;
    private Button changepwd;
    private AlertDialog alertDialog;
    private String cnick, cemail, cmajar, cinter;
    private static final int NICK = 1, EMAIL = 2, MAJOR = 3;
    private boolean isChanged = false;//是否有用户信息被修改

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_change);
        ActivityCollector.activities.add(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //加载用户头像
        userIcon = (ImageView) findViewById(R.id.user_icon1);
        if (GlobalFlags.getIconIndex() != -1)
            Glide.with(this).load(GlobalFlags.UserIcons[GlobalFlags.getIconIndex()]).into(userIcon);
        else
            Glide.with(this).load(GlobalFlags.UserIcons[0]).into(userIcon);

        userview = (TextView) findViewById(R.id.edit_user_name);
        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDialog("用户名", NICK);
            }
        });

        //点击可以修改用户头像
        telepview = (TextView) findViewById(R.id.text_telephone);
        telepview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoChange.this, SelectUserIcon.class);
                startActivity(intent);
            }
        });
        emailview = (TextView) findViewById(R.id.edit_email);
        emailview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDialog("E-mail", EMAIL);
            }
        });
        majorview = (TextView) findViewById(R.id.edit_major);
        majorview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDialog("专业", MAJOR);
            }
        });
        interbtn = (TextView) findViewById(R.id.button_interesting);
        interbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向选择兴趣界面传递旧的兴趣，并接收传回来的新的兴趣
                Intent intent = new Intent(UserInfoChange.this, SelectInterests.class);
                intent.putExtra("old_interests", cinter);
                startActivityForResult(intent, 1);
            }
        });

        //获取用户信息并加载
        setUserInfo();

        //转跳到修改密码界面
        changepwd = (Button) findViewById(R.id.button_change_pwd);
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoChange.this, ChangePWD.class);
                startActivity(intent);
            }
        });

        /*confirm = (Button) findViewById(R.id.button_confirm_info_change);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeInfo(cnick, cemail, cmajar, "暂定");
            }
        });*/
    }

    //获取到的用户信息提交服务器
    private void changeInfo(String cnick, String cemail, String cmajar, String cinter) {
        String address = GlobalFlags.getIpAddress() + "changemessage.jsp";
        String params = "pptelephone=" + GlobalFlags.getUserID() + "&pnick=" + cnick + "&pemail=" + cemail + "&pmajor=" + cmajar + "&pinter=" + cinter + "&icon=" + GlobalFlags.getIconIndex();
        //Log.d("changeinfo", "setUserInfo: " + address + "/?" + params);
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("changeinfo", "message: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("cha_answer");
                            if (result.equals("1"))
                                Toast.makeText(UserInfoChange.this, "个人信息修改成功！", Toast.LENGTH_SHORT).show();
                            else if (result.equals("-1"))
                                Toast.makeText(UserInfoChange.this, "个人信息修改失败！", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(UserInfoChange.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UserInfoChange.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //获取用户信息并显示
    private void setUserInfo() {
        String address = GlobalFlags.getIpAddress() + "person.jsp";
        String params = "pptelephone=" + GlobalFlags.getUserID();
        //Log.d("changeinfo", "setUserInfo: " + address + " " + params);
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("changeinfo", "message: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            telepview.setText(jsonObject.getString("ptelephone"));
                            userview.setText((cnick = jsonObject.getString("pnick")).equals("null") ? (cnick = "") : cnick);
                            emailview.setText((cemail = jsonObject.getString("pemail")).equals("null") ? (cemail = "") : cemail);
                            majorview.setText((cmajar = jsonObject.getString("major")).equals("null") ? (cmajar = "") : cmajar);
                            cinter = jsonObject.getString("inter");
                            //interbtn.setText((cinter = jsonObject.getString("inter")).replace("-", " | "));
                        } catch (JSONException e) {
                            //Toast.makeText(UserInfoChange.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(UserInfoChange.this, "请完善个人信息！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UserInfoChange.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //单个用户信息修改对话框
    private void ChangeDialog(String title, final int flag) {
        alertDialog = new AlertDialog.Builder(UserInfoChange.this, R.style.NoBackGroundDialog).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View view = LayoutInflater.from(UserInfoChange.this).inflate(R.layout.change_info_card, null);
        TextView head = (TextView) view.findViewById(R.id.textview_change_item);
        head.setText(title);
        final EditText editText = (EditText) view.findViewById(R.id.edit_change_item);
        //加载旧信息到对话框
        switch (flag) {
            case NICK:
                editText.setText(cnick);
                break;
            case EMAIL:
                editText.setText(cemail);
                break;
            case MAJOR:
                editText.setText(cmajar);
                break;
        }
        editText.setSelection(editText.getText().length());//光标移到文字最后
        final Button cancel = (Button) view.findViewById(R.id.button_change_item_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        Button confirm = (Button) view.findViewById(R.id.button_change_item_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = editText.getText().toString();
                //判断个人信息是否有修改，有的话则更新界面并置标志位为已修改
                switch (flag) {
                    case NICK:
                        if (!temp.equals(cnick)) {
                            cnick = temp;
                            userview.setText(cnick);
                            isChanged = true;
                        }
                        break;
                    case EMAIL:
                        if (!temp.equals(cemail)) {
                            cemail = temp;
                            emailview.setText(cemail);
                            isChanged = true;
                        }
                        break;
                    case MAJOR:
                        if (!temp.equals(cmajar)) {
                            cmajar = temp;
                            majorview.setText(cmajar);
                            isChanged = true;
                        }
                        break;
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.setContentView(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //当用户点击返回并且用户信息有修改时提交用户修改后的个人信息到服务器
                if (isChanged || GlobalFlags.isIconChanged()) {
                    changeInfo(cnick, cemail, cmajar, cinter);
                    GlobalFlags.setIsIconChanged(false);
                    //Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                    //Log.d("test", "onOptionsItemSelected: "+"shit");
                }
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isChanged || GlobalFlags.isIconChanged()) {
            changeInfo(cnick, cemail, cmajar, cinter);
        }
        finish();
    }

    @Override
    protected void onRestart() {
        //判断是否需要重新加载用户头像
        if (GlobalFlags.isIconChanged())
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplication()).load(GlobalFlags.UserIcons[GlobalFlags.getIconIndex()]).into(userIcon);
                }
            });*/
            Glide.with(getApplication()).load(GlobalFlags.UserIcons[GlobalFlags.getIconIndex()]).into(userIcon);
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String dataReturn = data.getStringExtra("interests_return");
                    if (!dataReturn.equals(cinter)) {
                        cinter = dataReturn;
                        //interbtn.setText(cinter.replace("-", " | "));
                        isChanged = true;
                    }
                }
        }
    }
}
