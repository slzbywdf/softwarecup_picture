package com.picture.picture_manager.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.ActivityCollector;
import com.picture.picture_manager.Utils.GlobalFlags;
import com.picture.picture_manager.Utils.HttpCallbackListener;
import com.picture.picture_manager.Utils.HttpUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 13307 on 2017/3/28.
 */

public class LabelPicture extends AppCompatActivity {
    private PhotoView imageView;
    private int clickCount = 0, rec_click_count = 0;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private AlertDialog alertDialog;
    private Button quit, recommend_button, upload;
    private String PicId;
    private ArrayList<String> PicLabels = new ArrayList<>();
    private ArrayList<String> newLabels = new ArrayList<>();
    private EditText[] edit_labels = new EditText[6];
    private TextView[] recommend_labels = new TextView[6];
    private CardView recommends_view;
    private ArrayList<String> recommends = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.label_picture);
        ActivityCollector.activities.add(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //获取传过来的各个参数
        Intent intent = getIntent();
        PicId = intent.getStringExtra("PicId");
        String PicPath = intent.getStringExtra("PicPath");
        String PicLabel = intent.getStringExtra("PicLabel");
        String recommend = intent.getStringExtra("Recommends");

        //解析传过来的推荐标签
        if (!(recommend.equals("null") || recommend == null)) {
            String[] t = recommend.split("-");
            for (int i = 0; i < t.length; i++) {
                recommends.add(t[i]);
            }
        }

        //解析传过来的图片标签
        if (PicLabel != null && PicLabel.length() > 0) {
            String temp[] = PicLabel.split("  ");
            String t[];
            for (int i = 0; i < temp.length; i++) {
                t = temp[i].split("\\.");
                PicLabels.add(t[1]);
            }
        }

        //Log.d("From", "onCreate: " + picId + ":" + PicPath);
        imageView = (PhotoView) findViewById(R.id.image_labeling);
        //imageView.setImageResource(R.drawable.bg);
        Glide.with(this).load(PicPath).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //单击隐藏ToolBar和FAB
                if (clickCount == 0) {
                    clickCount++;
                    //mainView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                    toolbar.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                } /*else if (clickCount == 1) {
                    //再单击放大
                    clickCount++;
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }*/ else {
                    //最后单击恢复隐藏的控件
                    //mainView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    clickCount = 0;
                }
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelDialog();
            }
        });
    }

    private void labelDialog() {
        alertDialog = new AlertDialog.Builder(LabelPicture.this, R.style.NoBackGroundDialog).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View view = LayoutInflater.from(LabelPicture.this).inflate(R.layout.label_card, null);

        quit = (Button) view.findViewById(R.id.lable_cancel);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                rec_click_count = 0;
            }
        });

        /*recommend_button = (Button) view.findViewById(R.id.button_recommend);
        recommend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recommends.size() == 0)
                    Toast.makeText(LabelPicture.this, "暂时没有推荐！", Toast.LENGTH_SHORT).show();
                else {
                    //按一次推荐标签按钮显示推荐页
                    if (rec_click_count == 0) {
                        recommends_view.setVisibility(View.VISIBLE);
                        rec_click_count++;
                    } else {
                        //再按一次关闭推荐页
                        recommends_view.setVisibility(View.GONE);
                        rec_click_count = 0;
                    }
                }
            }
        });*/
        recommends_view = (CardView) view.findViewById(R.id.recommend_view);
        recommends_view.setVisibility(View.GONE);//最开始推荐页是不可见的
        if (recommends.size() != 0) {
            recommends_view.setVisibility(View.VISIBLE);
            recommend_labels[0] = (TextView) view.findViewById(R.id.recommend_1);
            recommend_labels[1] = (TextView) view.findViewById(R.id.recommend_2);
            recommend_labels[2] = (TextView) view.findViewById(R.id.recommend_3);
            recommend_labels[3] = (TextView) view.findViewById(R.id.recommend_4);
            recommend_labels[4] = (TextView) view.findViewById(R.id.recommend_5);
            recommend_labels[5] = (TextView) view.findViewById(R.id.recommend_6);
            int idx = 0;
            //将推荐标签填入推荐页
            for (String item : recommends)
                if (idx < 6)
                    recommend_labels[idx++].setText(item);
            //推荐标签个数少于6个，则剩余的TextView不可见
            if (idx < 6)
                for (int i = idx; i < 6; i++) {
                    recommend_labels[i].setVisibility(View.GONE);
                }
            setRecommendLabelToEditText(idx);
        }

        edit_labels[0] = (EditText) view.findViewById(R.id.edit_label1);
        edit_labels[1] = (EditText) view.findViewById(R.id.edit_label2);
        edit_labels[2] = (EditText) view.findViewById(R.id.edit_label3);
        edit_labels[3] = (EditText) view.findViewById(R.id.edit_label4);
        edit_labels[4] = (EditText) view.findViewById(R.id.edit_label5);
        edit_labels[5] = (EditText) view.findViewById(R.id.edit_label6);
        //加载解析好的标签到EditText中
        if (PicLabels.size() > 0) {
            int index = 0;
            for (String item : PicLabels)
                edit_labels[index++].setText(item);
        }
        edit_labels[0].setSelection(edit_labels[0].getText().length());//光标移到文字最后

        upload = (Button) view.findViewById(R.id.button_upload_label);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] temp = new String[6];
                for (int m = 0; m < 6; m++)
                    temp[m] = edit_labels[m].getText().toString().trim();
                String labels = "";
                newLabels.clear();
                for (int i = 0; i < 6; i++) {
                    if (temp[i] != null && temp[i].length() > 0) {
                        labels = labels + "-" + temp[i];
                        newLabels.add(temp[i]);
                    }
                }
                if (!labels.isEmpty() && labels != null) {
                    labels = labels.substring(1);
                    if (!PicId.isEmpty() && PicId != null)
                        uploadAllLabels(labels);
                } else
                    Toast.makeText(LabelPicture.this, "请输入至少一个标签！", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setContentView(view);
    }

    /**
     * 为每一个推荐标签添加点击事件，点击后可以填入空的EditText中，如果已填入，则不重复填入
     *
     * @param index 可以点击的推荐标签，下标0~index
     */
    private void setRecommendLabelToEditText(int index) {
        for (int i = 0; i < index; i++) {
            final String label = recommends.get(i);
            recommend_labels[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp;
                    boolean filled = false;//已填入标志位
                    for (int j = 0; j < 6; j++) {
                        temp = edit_labels[j].getText().toString().trim();
                        if (temp.equals(label)) {
                            Toast.makeText(LabelPicture.this, "该标签已填入，请选择其他标签！", Toast.LENGTH_SHORT).show();
                            filled = true;
                        }
                    }
                    if (filled == false) {
                        int j;
                        for (j = 0; j < 6; j++) {
                            temp = edit_labels[j].getText().toString().trim();
                            if (temp == null || temp.length() == 0) {
                                edit_labels[j].setText(label);
                                break;
                            }
                        }
                        edit_labels[0].setSelection(edit_labels[0].getText().length());//光标移到文字最后
                        if (j == 6)
                            Toast.makeText(LabelPicture.this, "标签已填满，无法再填入！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //上传标签
    private void uploadAllLabels(String labels) {
        String address = GlobalFlags.getIpAddress() + "tagup";
        String params = "pptelephone=" + GlobalFlags.getUserID() + "&picid=" + PicId + "&pictag=" + labels;
        //Log.d("upload", "uploadAllLabels: " + params);
        //Log.d("changeinfo", "setUserInfo: " + address + " " + params);
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                //Log.d("upload", "message: " + response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("tagup");
                            if (result.equals("1")) {
                                //alertDialog.dismiss();
                                //将新标签替换掉旧标签用于填充到EditText中
                                PicLabels.clear();
                                for (String item : newLabels) {
                                    PicLabels.add(item);
                                }
                                GlobalFlags.setIsNeedtoRefresh(true);
                                Toast.makeText(LabelPicture.this, "上传成功！", Toast.LENGTH_SHORT).show();
                                upload.setVisibility(View.GONE);
                                //recommend_button.setVisibility(View.GONE);
                                recommends_view.setVisibility(View.GONE);
                                quit.setText("关闭");
                                for (int k = 0; k < 6; k++) {
                                    edit_labels[k].setFocusable(false);
                                    edit_labels[k].setFocusableInTouchMode(false);
                                    edit_labels[k].clearFocus();
                                }
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(edit_labels[0].getWindowToken(), 0);//隐藏软键盘
                            } else
                                Toast.makeText(LabelPicture.this, "上传失败！", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            Toast.makeText(LabelPicture.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LabelPicture.this, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
