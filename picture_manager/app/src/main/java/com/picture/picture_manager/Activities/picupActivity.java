package com.picture.picture_manager.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.picture.picture_manager.Adapters.GridAdapter;
import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.Bimp;
import com.picture.picture_manager.Utils.FileUtils;
import com.picture.picture_manager.Utils.GlobalFlags;
import com.picture.picture_manager.Utils.ImageItem;
import com.picture.picture_manager.Utils.PublicWay;
import com.picture.picture_manager.okhttp.OkHttpUtils;
import com.picture.picture_manager.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.RequestBody;


public class picupActivity extends Activity {

    private static final int TAKE_PICTURE = 0x000001;
    private GridView mGridView;
    private GridAdapter adapter;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    private LinearLayout parentView;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picup);
        PublicWay.activityList.add(this);
        initViews();
    }

    private void initViews() {

        initPopu();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        parentView = (LinearLayout) findViewById(R.id.ll_grid_view);
        submit = (Button) findViewById(R.id.submit);
        mGridView = (GridView) findViewById(R.id.grid_view);
        Button back = (Button) findViewById(R.id.upload_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 点击GridView时出现背景色设置为透明
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == Bimp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(picupActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(picupActivity.this, GalleryActivity.class);
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });

        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Bimp.tempSelectBitmap.size()==0) {
                    Toast.makeText(picupActivity.this, "请选择图片", Toast.LENGTH_LONG).show();
                    return;
                }
                for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
                    PublicWay.fileList.add(new File(Bimp.tempSelectBitmap.get(i).getImagePath()));
                    Log.e("niaho", Bimp.tempSelectBitmap.get(i).getImagePath());
                    PublicWay.list.add(Bimp.tempSelectBitmap.get(i).getImagePath());
                }
                doAn();
            }
        });
    }

    protected void doAn() {
        Log.d("fdf", "doAn: "+PublicWay.list.get(0));
        OkHttpUtils.post()
                .addMoreFile("upload", PublicWay.list, PublicWay.fileList).addHeader("cookie", GlobalFlags.getSessionId())
                .url(url)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void inProgress(float progress) {
                        Message message = handler.obtainMessage();
                        message.obj = progress;
                        handler.sendMessage(message);
                    }
                });
    }

    private String url = "http://114.115.130.89:80/ssh_pic/upfile";
    private ProgressBar progressBar;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            float pro = (float) msg.obj;
            progressBar.setVisibility(View.VISIBLE);
            System.out.println((int) (pro * 100) + "");
            progressBar.setProgress((int) (pro * 100));
            if (pro == 1) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(picupActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                Bimp.tempSelectBitmap.clear();
                PublicWay.fileList.clear();
                PublicWay.list.clear();
                adapter.notifyDataSetChanged();
            }
        }

        ;
    };
    private RequestBody body;

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        mGridView.setAdapter(adapter);
    }

    private void initPopu() {
        pop = new PopupWindow(picupActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(picupActivity.this, AlbumActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }
    private Uri imageURL;
    private String  path;
    protected void photo() {
        String fileName = String.valueOf(System.currentTimeMillis())+ ".jpg";
        File outptuimage = new File(getExternalCacheDir(),fileName);
        path= getExternalCacheDir()+"/"+fileName;
        Log.d("ff", "photo: "+path);
        //Toast.makeText(picupActivity.this,path,Toast.LENGTH_LONG).show();
        try{
            if(outptuimage.exists()){
                outptuimage.delete();
            }
            outptuimage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24){
            imageURL = FileProvider.getUriForFile(picupActivity.this,"com.picture.picture_manager.fileprovider",outptuimage);
        }else{
            imageURL = Uri.fromFile(outptuimage);
        }
        Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 保存uri对应的照片于指定路径
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURL);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < PublicWay.num&& resultCode == RESULT_OK) {
                    try {
                        Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageURL));
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setBitmap(bm);
                        takePhoto.setImagePath(path);
                        Bimp.tempSelectBitmap.add(takePhoto);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*for (int i = 0; i < PublicWay.activityList.size(); i++) {
                if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			System.exit(0);*/
            finish();
        }
        return true;
    }
}
