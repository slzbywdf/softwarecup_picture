package com.picture.picture_manager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.picture.picture_manager.Adapters.NewImageAdapter;
import com.picture.picture_manager.Beans.OnePic;
import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.GlobalFlags;
import com.picture.picture_manager.Utils.HttpCallbackListener;
import com.picture.picture_manager.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by 13307 on 2017/3/22.
 */

public class AllPictureFragment extends Fragment {
    //GridView gridView;
    //ImageAdapter adapter;
    //private ArrayList<String> allPaths = new ArrayList<String>();
    //private ArrayList<String> allIds = new ArrayList<String>();
    private ArrayList<OnePic> allPics = new ArrayList<>();
    private NewImageAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private EditText searchText;
    private String searchContent = "";
    private final static int LOAD_OK = 1;
    private final static int LOAD_FAILED = 2;
    private final static int SEARCH_NO_PICS = 4;
    private boolean isSearched = false;

    //int imageWidth;
    //ArrayList<Bitmap> allPics;
    //private static final String TAG = "AllPictureFragment";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_OK:
                    //加载完所有图片路径后，通知适配器进行图片加载
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    break;
                case LOAD_FAILED:
                    allPics.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "图片加载失败！", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                    break;
                case SEARCH_NO_PICS:
                    Toast.makeText(getActivity(), "未找到图片！", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

    public boolean isSearched() {
        return isSearched;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        getAllPicPaths();
        LinearLayout radio = (LinearLayout) view.findViewById(R.id.radio_button);
        radio.setVisibility(View.GONE);
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
            }
        });

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        //swipeRefresh.setVisibility(View.GONE);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新后清除搜索信息以及搜索标志位，并加载所有图片
                searchText.setText("");
                isSearched = false;
                getAllPicPaths();
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewImageAdapter(getActivity(), allPics);
        recyclerView.setAdapter(adapter);
        //Log.d(TAG, "onCreateView");
        return view;
    }

    public void getAllPicPaths() {
        //allPaths.add(String.valueOf(R.drawable.one));
        // allPaths.add(String.valueOf(R.drawable.two));
        //allPaths.add(String.valueOf(R.drawable.three));
        String address = GlobalFlags.getIpAddress() + "P_showallpicture";
        String params = "pptelephone=" + GlobalFlags.getUserID();
        //Log.d("changeinfo", "setUserInfo: " + address + " " + params);
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Log.d("all", "message: " + response);
                Message message = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String paths = jsonObject.getString("showallpicture");
                    Log.d("paths", "onFinish: " + paths);
                    //paths = "image\\atm1.jpg,image\\atm1.jpg,image\\atm2.jpg,image\\atm2.jpg,image\\atm3.jpg,image\\atm4.jpg";
                    if (paths != null && paths.length() > 0) {
                        //allPaths.clear();
                        //allIds.clear();
                        allPics.clear();
                        String t[] = paths.split(";");//以;分割，获取每张图片的路径
                        for (int i = 0; i < t.length; i++) {
                            int index = t[i].indexOf('\\');//获取\的位置，将\替换成/
                            //allIds.add(temp[0]);
                            //allPaths.add(GlobalFlags.getIpAddress() + temp[1].substring(0, index) + "/" + temp[1].substring(index + 1));
                            allPics.add(new OnePic("", GlobalFlags.getIpAddress() + t[i].substring(0, index) + "/" + t[i].substring(index + 1), ""));
                        }
                        message.what = LOAD_OK;
                    }
                    handler.sendMessage(message);//所有图片加载完毕，发送加载完毕message
                    //adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    message.what = LOAD_FAILED;
                    handler.sendMessage(message);
                    //Toast.makeText(context, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(final Exception e) {
                //Log.d("Get from server", "error message: " + e.toString());
                //Toast.makeText(context, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Message message = new Message();
                message.what = LOAD_FAILED;
                handler.sendMessage(message);
            }
        });
    }

    public void searchPics() {
        String address = GlobalFlags.getIpAddress() + "P_picsearch";
        String params = "search=" + searchContent + "&phone=" + GlobalFlags.getUserID();
        //Log.d("search", "searchPics: "+searchContent);
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Log.d("search", "message: " + response);
                Message message = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String paths = jsonObject.getString("result");
                    if (paths != null && paths.length() > 0) {
                        allPics.clear();
                        String t[] = paths.split(";");//以;分割，获取每张图片的路径
                        String temp[];
                        for (int i = 0; i < t.length; i++) {
                            int index = t[i].indexOf('\\');//获取\的位置，将\替换成/
                            allPics.add(new OnePic("", GlobalFlags.getIpAddress() + t[i].substring(0, index) + "/" + t[i].substring(index + 1), ""));
                        }
                        message.what = LOAD_OK;
                    } else
                        message.what = SEARCH_NO_PICS;
                    handler.sendMessage(message);//所有图片加载完毕，发送加载完毕message
                } catch (JSONException e) {
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
}
