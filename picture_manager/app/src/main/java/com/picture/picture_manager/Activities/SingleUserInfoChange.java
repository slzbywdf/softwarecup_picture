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

public class SingleUserInfoChange extends AppCompatActivity {
    private TextView userview, telepview, emailview, majorview, interbtn;
    private ImageView userIcon;
    private AlertDialog alertDialog;
    private String telephone, cnick, cemail, cmajar, cinter;
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
                Intent intent = new Intent(SingleUserInfoChange.this, SelectUserIcon.class);
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
                Intent intent = new Intent(SingleUserInfoChange.this, SelectInterests.class);
                intent.putExtra("old_interests", cinter);
                startActivityForResult(intent, 1);
            }
        });

        //获取用户信息并加载
        setUserInfo();

        Button confirm = (Button) findViewById(R.id.button_change_pwd);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当用户点击返回并且用户信息有修改时提交用户修改后的个人信息到服务器
                if (isChanged || GlobalFlags.isIconChanged()) {
                    changeInfo();
                    GlobalFlags.setIsIconChanged(false);
                }
                finish();
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
    private void changeInfo() {
        String address = GlobalFlags.getIpAddress() + "changemessage.jsp";
        String params = "pptelephone=" + telephone + "&pnick=" + cnick + "&pemail=" + cemail + "&pmajor=" + cmajar + "&pinter=" + cinter + "&icon=" + GlobalFlags.getIconIndex();
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("cha_answer");
                            if (result.equals("1"))
                                Toast.makeText(SingleUserInfoChange.this, "用户信息修改成功！", Toast.LENGTH_SHORT).show();
                            else if (result.equals("-1"))
                                Toast.makeText(SingleUserInfoChange.this, "用户信息修改失败！", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(SingleUserInfoChange.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SingleUserInfoChange.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //获取用户信息并显示
    private void setUserInfo() {
        Intent intent = getIntent();
        telepview.setText((telephone = intent.getStringExtra("ptelephone")));
        userview.setText((cnick = intent.getStringExtra("pnick")));
        emailview.setText((cemail = intent.getStringExtra("pemail")));
        majorview.setText((cmajar = intent.getStringExtra("major")));
        cinter = intent.getStringExtra("interests");
        int iconIdx = intent.getIntExtra("icon", 0);
        GlobalFlags.setIconIndex(iconIdx);
        Glide.with(this).load(GlobalFlags.UserIcons[iconIdx]).into(userIcon);
    }

    //单个用户信息修改对话框
    private void ChangeDialog(String title, final int flag) {
        alertDialog = new AlertDialog.Builder(SingleUserInfoChange.this, R.style.NoBackGroundDialog).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View view = LayoutInflater.from(SingleUserInfoChange.this).inflate(R.layout.change_info_card, null);
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
                /*if (isChanged || GlobalFlags.isIconChanged()) {
                    changeInfo();
                    GlobalFlags.setIsIconChanged(false);
                    //Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                    //Log.d("test", "onOptionsItemSelected: "+"shit");
                }*/
                finish();
                break;
            default:
        }
        return true;
    }

    /*@Override
    public void onBackPressed() {
        if (isChanged || GlobalFlags.isIconChanged()) {
            changeInfo();
        }
        finish();
    }*/

    @Override
    protected void onRestart() {
        //判断是否需要重新加载用户头像
        if (GlobalFlags.isIconChanged())
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
                        isChanged = true;
                    }
                }
        }
    }
}
