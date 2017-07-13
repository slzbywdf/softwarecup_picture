package com.picture.picture_manager.Fragments;

import android.app.Fragment;
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
 * Created by 13307 on 2017/3/26.
 */

public class SystemPushFragment extends Fragment {
    //ImageAdapter adapter;
    //private ArrayList<String> allPaths = new ArrayList<String>();
    //private ArrayList<String> allIds = new ArrayList<String>();
    private ArrayList<OnePic> allPics = new ArrayList<>();
    private NewImageAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private final static int LOAD_OK = 1;
    private final static int LOAD_FAILED = 2;
    private final static int LOAD_NO_PICS = 3;
    //int imageWidth;
    //ArrayList<Bitmap> allPics;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_OK:
                    //加载完所有图片路径后，通知适配器进行图片加载
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    //for (String item : allPaths)
                    //Log.d("path", "onFinish: " + item);
                    //adapter.notifyItemRemoved(0);
                    //adapter.notifyItemRangeInserted(0, allPaths.size());
                    break;
                case LOAD_FAILED:
                    allPics.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "图片加载失败！", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                    break;
                case LOAD_NO_PICS:
                    Toast.makeText(getActivity(), "今日没有推送图片！", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        getAllPicPaths();
        LinearLayout radio = (LinearLayout) view.findViewById(R.id.radio_button);
        LinearLayout search = (LinearLayout) view.findViewById(R.id.linearlayout_search_in_all_pic);
        search.setVisibility(View.GONE);
        radio.setVisibility(View.GONE);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllPicPaths();
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewImageAdapter(getActivity(), allPics);
        recyclerView.setAdapter(adapter);
        return view;
    }

    /*@Override
    public void onResume() {
        if (GlobalFlags.isNeedtoRefresh())
            if (GlobalFlags.getNowFragment() == GlobalFlags.DailyPush_Fragment) {
                GlobalFlags.setIsNeedtoRefresh(false);
                getAllPicPaths();
            }
        super.onResume();
        Log.d("syspush", "onResume");
    }*/

    public void getAllPicPaths() {
        //allPaths.add(String.valueOf(R.drawable.one));
        //allPaths.add(String.valueOf(R.drawable.two));
        //allPaths.add(String.valueOf(R.drawable.three));
        //allPaths.add("http://192.168.1.101:8080/wangcong/1.jpg");
        //allPaths.add("http://192.168.1.101:8080/wangcong/2.jpg");
        //allPaths.add("http://192.168.1.101:8080/wangcong/3.jpg");
        //allPaths.add(String.valueOf(R.drawable.ic_sentiment_very_dissatisfied_black_48dp));
        String address = GlobalFlags.getIpAddress() + "picput.jsp";
        String params = "pptelephone=" + GlobalFlags.getUserID();
        //Log.d("changeinfo", "setUserInfo: " + address + " " + params);
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Log.d("push", "message: " + response);
                Message message = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String paths = jsonObject.getString("dayput");
                    //Log.d("paths", "onFinish: " + paths);
                    //paths = "image\\atm1.jpg,image\\atm1.jpg,image\\atm2.jpg,image\\atm2.jpg,image\\atm3.jpg,image\\atm4.jpg";
                    if (paths != null && paths.length() > 0) {
                        //allPaths.clear();
                        //allIds.clear();
                        allPics.clear();
                        String t[] = paths.split(";");//以;分割，获取每张图片的路径
                        String[] temp;
                        for (int i = 0; i < t.length; i++) {
                            temp = t[i].split(",");
                            int index = temp[1].indexOf('\\');//获取\的位置，将\替换成/
                            //allIds.add(temp[0]);
                            //allPaths.add(GlobalFlags.getIpAddress() + temp[1].substring(0, index) + "/" + temp[1].substring(index + 1));
                            allPics.add(new OnePic(temp[0], GlobalFlags.getIpAddress() + temp[1].substring(0, index) + "/" + temp[1].substring(index + 1), temp[2]));
                        }
                        message.what = LOAD_OK;
                    } else
                        message.what = LOAD_NO_PICS;
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

        /*WindowManager mWm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Point windowSize = new Point();
        mWm.getDefaultDisplay().getSize(windowSize);
        int windowWidth = windowSize.x;
        int imagePadding = 30;
        int colums = 3;
        imageWidth = windowWidth / colums - imagePadding;

        allPics = new ArrayList<Bitmap>();
        Bitmap roundcorbitmap1 = getBitmap(R.drawable.one);
        Bitmap roundcorbitmap2 = getBitmap(R.drawable.two);
        Bitmap roundcorbitmap3 = getBitmap(R.drawable.three);
        allPics.add(roundcorbitmap1);
        allPics.add(roundcorbitmap2);
        allPics.add(roundcorbitmap3);*/
    }

    /*private Bitmap getBitmap(int id) {
        Bitmap rawbitmap = BitmapFactory.decodeResource(getActivity().getResources(), id);
        Bitmap combitmap = BitmapUtil.compressBasedOnSquare(rawbitmap, imageWidth);
        Bitmap cutbitmap = BitmapUtil.cut(combitmap, imageWidth, imageWidth);
        return cutbitmap;
    }*/
}
