package com.picture.picture_manager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.picture.picture_manager.Adapters.AllPicInfoAdapter;
import com.picture.picture_manager.Beans.OnePicInfo;
import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.GlobalFlags;
import com.picture.picture_manager.Utils.HttpCallbackListener;
import com.picture.picture_manager.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/6/4.
 */

public class ALLPicLoadFragment extends Fragment {
    private ArrayList<OnePicInfo> allpicinfo = new ArrayList<>();
    private ArrayList<OnePicInfo> allpicinfo1 = new ArrayList<>();
    AllPicInfoAdapter adapter;
    private ListView listView;
    private Button all;
    private Button cancel;
    private Button fan;
    private Button daochu;
    private EditText searchText;
    private String searchContent = "";
    private boolean isSearched = false;
    private final static int LOAD_OK = 1;
    private final static int LOAD_FAILED = 2;
    private final static int LOAD_NO = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_OK:
                    adapter.notifyDataSetChanged();
                    break;
                case LOAD_FAILED:
                    allpicinfo.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "图片信息加载失败！", Toast.LENGTH_SHORT).show();
                    break;
                case LOAD_NO:
                    Toast.makeText(getActivity(), "没有已完成图片信息！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pic_load, container, false);
        getAllpicInfos();
        adapter = new AllPicInfoAdapter(getActivity(), R.layout.every_pic_info, allpicinfo);
        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        all = (Button) view.findViewById(R.id.all);
        cancel = (Button) view.findViewById(R.id.cancel);
        fan = (Button) view.findViewById(R.id.fan);
        daochu = (Button) view.findViewById(R.id.load);
        searchText = (EditText) view.findViewById(R.id.edit_search_in_all_pic);
        ImageView searchBtn = (ImageView) view.findViewById(R.id.button_search_in_all_pic);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContent = searchText.getText().toString().trim();
                isSearched = true;//搜索标志位设为true
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);//隐藏软键盘
                searchPics();
                //只搜索内容
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<OnePicInfo> it = allpicinfo.iterator();
                while (it.hasNext()) {
                    OnePicInfo temp = it.next();
                    temp.setIsflag(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<OnePicInfo> it = allpicinfo.iterator();
                while (it.hasNext()) {
                    OnePicInfo temp = it.next();
                    temp.setIsflag(false);
                }
                adapter.notifyDataSetChanged();
            }
        });
        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<OnePicInfo> it = allpicinfo.iterator();
                while (it.hasNext()) {
                    OnePicInfo temp = it.next();
                    if (temp.isflag())
                        temp.setIsflag(false);
                    else
                        temp.setIsflag(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
        daochu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<OnePicInfo> it = allpicinfo.iterator();
                String content = "{\n";
                boolean pp = false;
                while (it.hasNext()) {
                    pp = true;
                    OnePicInfo temp = it.next();
                    if (temp.isflag()) {
                        String[] tags = temp.getTag().split("-");
                        content = content + "\"picture_name\":\"" + temp.getPicture_name() + "\",\n" +
                                "\"finish_time\":\"" + temp.getFinist_time() + "\",\n"
                                + "\"labels\":[\n";
                        content = content + "\"" + tags[0] + "\"";
                        Log.d("fdf", tags[0]);
                        for (int i = 1; i < tags.length; i++) {
                            content = content + ",\"" + tags[i] + "\"";
                        }
                        content = content + "\n]\n}\n";
                    }
                }
                if (!pp)
                    return;
                //将结果信息反馈数据库
                //导出结果
                // String path = getActivity().getExternalFilesDir(null).getAbsolutePath();
                //Log.d("fdf",path);
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar cal = Calendar.getInstance();
                String filename = df.format(cal.getTime()) + "picresult";
                //向SDCard中保存
                String en = Environment.getExternalStorageState();
                //获取SDCard状态,如果SDCard插入了手机且为非写保护状态
                if (en.equals(Environment.MEDIA_MOUNTED)) {
                    try {
                        saveToSDCard(filename, content);
                        Toast.makeText(getActivity(), "结果导出成功，目录为" + Environment.getExternalStorageDirectory()+"/_RESULTS/" + filename, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //提示用户SDCard不存在或者为写保护状态
                    Toast.makeText(getActivity(), "SDCard不存在或者为写保护状态", Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    /**
     * 向sdcard中写入文件
     *
     * @param filename 文件名
     * @param content  文件内容
     */
    public void saveToSDCard(String filename, String content) throws Exception {
        File file = new File(Environment.getExternalStorageDirectory()+"/_RESULTS/");
        if(!file.exists())
            file.mkdir();
        file = new File(Environment.getExternalStorageDirectory()+"/_RESULTS/",filename);
        OutputStream out = new FileOutputStream(file);
        out.write(content.getBytes());
        out.close();
    }

    public void getAllpicInfos() {
        String address = GlobalFlags.getIpAddress() + "Ppicresult.jsp";
        String params = "";
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Log.d("allInfo", "message: " + response);
                Message message = new Message();
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    if (jsonarray.length() > 0) {
                        allpicinfo.clear();
                    } else {
                        message.what = LOAD_NO;
                        handler.sendMessage(message);
                        return;
                    }
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String pass = jsonObject.getString("pass");
                        int index = pass.indexOf('\\');//获取\的位置，将\替换成/
                        pass = GlobalFlags.getIpAddress() + pass.substring(0, index) + "/" + pass.substring(index + 1);
                        String picture_name = jsonObject.getString("picture_name");
                        String finish_time = jsonObject.getString("finish_time");
                        String labels = jsonObject.getString("labels");
                        int isload = Integer.parseInt(jsonObject.getString("isload"));
                        allpicinfo.add(new OnePicInfo(picture_name, pass, finish_time, labels, isload));
                        allpicinfo1.add(new OnePicInfo(picture_name, pass, finish_time, labels, isload));
                    }
                    message.what = LOAD_OK;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    message.what = LOAD_FAILED;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(final Exception e) {
                Message message = new Message();
                message.what = LOAD_FAILED;
                handler.sendMessage(message);
            }
        });
    }

    public void searchPics() {
        allpicinfo.clear();
        Iterator<OnePicInfo> itt1 = allpicinfo1.iterator();
        while (itt1.hasNext()) {
            allpicinfo.add(itt1.next());
        }
        String address = GlobalFlags.getIpAddress() + "picsearch";
        String params = "search=" + searchContent + "&phone=" + GlobalFlags.getUserID();
        ArrayList<OnePicInfo> temp_search = new ArrayList<>();
        //Log.d("search", "searchPics: "+searchContent);
        Iterator<OnePicInfo> it = allpicinfo.iterator();
        while (it.hasNext()) {
            OnePicInfo temp = it.next();
            String fg = temp.getTag();
            Log.d("pp", "searchPics: " + fg);
            if (fg.indexOf(searchContent) >= 0 || fg.equalsIgnoreCase(searchContent)) {
                temp_search.add(temp);
            }
        }
        if (temp_search.size() > 0) {
            //adapter = new AllPicInfoAdapter(getActivity(), R.layout.every_pic_info, temp_search);
            allpicinfo.clear();
            // allpicinfo=temp_search;
            Iterator<OnePicInfo> itt = temp_search.iterator();
            while (itt.hasNext()) {
                allpicinfo.add(itt.next());
            }
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "没有已完成图片信息！", Toast.LENGTH_SHORT).show();
        }
    }
}
