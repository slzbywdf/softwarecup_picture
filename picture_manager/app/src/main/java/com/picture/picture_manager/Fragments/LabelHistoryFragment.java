package com.picture.picture_manager.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.picture.picture_manager.Adapters.NewImageAdapterHistory;
import com.picture.picture_manager.Beans.OnePicHistory;
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

public class LabelHistoryFragment extends Fragment {
    //GridView gridView;
    //ImageAdapterHistory adapter;
    //private ArrayList<String> allPaths = new ArrayList<String>();
    //private ArrayList<String> allLabels = new ArrayList<String>();
    private ArrayList<OnePicHistory> allHisPics = new ArrayList<OnePicHistory>();//当前显示的
    private ArrayList<OnePicHistory> allHisPicsNoJudged = new ArrayList<OnePicHistory>();//保存已上传还未被系统判定的
    private ArrayList<OnePicHistory> allHisPicsJudged = new ArrayList<OnePicHistory>();//保存已上传已被系统判定的
    private NewImageAdapterHistory adapter;
    private SwipeRefreshLayout swipeRefresh;
    private Button noJudged, judged;
    private final static int LOAD_OK = 1;
    private final static int LOAD_FAILED = 2;
    private final static int LOAD_NO_PICS = 3;
    private boolean isNoJudgedView = true;
    // ArrayList<Bitmap> allImages;
    //int imageWidth;
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
                    Toast.makeText(getActivity(), "图片加载失败！", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                    break;
                case LOAD_NO_PICS:
                    Toast.makeText(getActivity(), "您还没有打过标签！", Toast.LENGTH_SHORT).show();
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
        LinearLayout search = (LinearLayout) view.findViewById(R.id.linearlayout_search_in_all_pic);
        search.setVisibility(View.GONE);
        /*gridView = (GridView) view.findViewById(R.id.gridview_history);
        adapter = new ImageAdapterHistory(getActivity(), allPaths, allImages, imageWidth, allLabels);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), LabelPicture.class);
                startActivity(intent);
            }
        });*/
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllPicPaths();
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewImageAdapterHistory(getActivity(), allHisPics);
        recyclerView.setAdapter(adapter);
        noJudged = (Button) view.findViewById(R.id.button_no_judge);
        noJudged.setTextColor(Color.parseColor("#ffffff"));//设置初始是“未判定”被选中
        noJudged.setBackgroundResource(R.drawable.redrectangle);
        judged = (Button) view.findViewById(R.id.button_judged);
        noJudged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoJudgedView = true;
                noJudged.setTextColor(Color.parseColor("#ffffff"));
                noJudged.setBackgroundResource(R.drawable.redrectangle);
                judged.setTextColor(Color.parseColor("#000000"));
                judged.setBackgroundResource(R.drawable.whitebutton);
                //allHisPics = allHisPicsNoJudged;
                allHisPics.clear();
                for (OnePicHistory item : allHisPicsNoJudged) {//切换为未判定
                    allHisPics.add(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
        judged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoJudgedView = false;
                judged.setTextColor(Color.parseColor("#ffffff"));
                judged.setBackgroundResource(R.drawable.redrectangle);
                noJudged.setTextColor(Color.parseColor("#000000"));
                noJudged.setBackgroundResource(R.drawable.whitebutton);
                //allHisPics = allHisPicsJudged;
                allHisPics.clear();
                for (OnePicHistory item : allHisPicsJudged) {//切换为已判定
                    allHisPics.add(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    /*@Override
    public void onResume() {
        //if (GlobalFlags.isNeedtoRefresh())
            //getAllPicPaths();

        super.onResume();
        Log.d("his", "onResume");
    }*/

    /*此处编写联网获取所有图片以及历史标签的功能*/
    public void getAllPicPaths() {
        //allPaths.add(String.valueOf(R.drawable.one));
        //allPaths.add(String.valueOf(R.drawable.two));
        //allPaths.add(String.valueOf(R.drawable.three));
        String address = GlobalFlags.getIpAddress() + "personhistory";
        String params = "pptelephone=" + GlobalFlags.getUserID();
        //Log.d("changeinfo", "setUserInfo: " + address + " " + params);
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Log.d("history", "message: " + response);
                Message message = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String paths = jsonObject.getString("personhistory");
                    //Log.d("paths", "onFinish: " + paths);
                    //paths = "image\\atm1.jpg,image\\atm1.jpg,image\\atm2.jpg,image\\atm2.jpg,image\\atm3.jpg,image\\atm4.jpg";
                    if (paths != null && paths.length() > 0) {
                        allHisPics.clear();
                        allHisPicsJudged.clear();
                        allHisPicsNoJudged.clear();
                        String t[] = paths.split(";");//以;分割，获取每张历史图片的信息
                        String temp[];
                        String[] labels;
                        String oklabels = "";
                        for (int i = 0; i < t.length; i++) {
                            temp = t[i].split(",");//temp[0]为图片id，temp[1]为图片路径,temp[2]为标签，temp[3]为是否可以修改，temp[4]为推荐标签
                            int index = temp[1].indexOf('\\');//获取\的位置，将\替换成/
                            //allPaths.add(GlobalFlags.getIpAddress() + temp[1].substring(0, index) + "/" + temp[1].substring(index + 1));
                            String path = GlobalFlags.getIpAddress() + temp[1].substring(0, index) + "/" + temp[1].substring(index + 1);
                            if (!temp[2].equals("null")) {
                                labels = temp[2].split("-");
                                for (int j = 1; j <= labels.length; j++) {
                                    oklabels = oklabels + j + "." + labels[j - 1] + "  ";
                                }
                            } else
                                oklabels = "没有标签信息！";
                            OnePicHistory one = new OnePicHistory(temp[0], path, oklabels.trim(), temp[3], (temp.length == 4) ? "null" : temp[4]);//如果推荐标签为空，设置为null
                            oklabels = "";
                            if (temp[3].equals("0")) {//根据是否为0判断是否已被判定
                                if (isNoJudgedView)
                                    allHisPics.add(one);//初始加载的是未判定的
                                allHisPicsNoJudged.add(one);
                            } else {
                                if (isNoJudgedView == false)
                                    allHisPics.add(one);
                                allHisPicsJudged.add(one);
                            }
                        }
                        message.what = LOAD_OK;
                        //allHisPics = allHisPicsNoJudged;
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
        int imagePadding = 60;//图片间隔
        int colums = 2;//图片列数
        imageWidth = windowWidth / colums - imagePadding;//图片宽度（正方形）
        allImages = new ArrayList<Bitmap>();
        Bitmap roundcorbitmap1 = getBitmap(R.drawable.one);
        Bitmap roundcorbitmap2 = getBitmap(R.drawable.two);
        Bitmap roundcorbitmap3 = getBitmap(R.drawable.three);
        allImages.add(roundcorbitmap1);
        allImages.add(roundcorbitmap2);
        allImages.add(roundcorbitmap3);*/

        //allLabels.add("你好，世界!你好，世界!你好，世界!你好，世界!你好，世界!你好，世界!");
        //allLabels.add("你好，世界!你好，世界!你好，世界!你好，世界!你好，世界!你好，世界!你好，世界!你好，世界!你好，世界!你好，世界!");
        //allLabels.add("你好，世界!");
    }

    /*private Bitmap getBitmap(int id) {
        Bitmap rawbitmap = BitmapFactory.decodeResource(getActivity().getResources(), id);
        Bitmap combitmap = BitmapUtil.compressBasedOnSquare(rawbitmap, imageWidth);
        Bitmap cutbitmap = BitmapUtil.cut(combitmap, imageWidth, imageWidth);
        return BitmapUtil.fillet(cutbitmap, 20, BitmapUtil.CORNER_TOP);
    }*/

}
