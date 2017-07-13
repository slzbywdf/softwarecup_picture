package com.picture.picture_manager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.picture.picture_manager.Activities.SingleUserInfoChange;
import com.picture.picture_manager.Adapters.AllUserInfoAdapter;
import com.picture.picture_manager.Beans.OneUserInfo;
import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.GlobalFlags;
import com.picture.picture_manager.Utils.HttpCallbackListener;
import com.picture.picture_manager.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by 13307 on 2017/6/2.
 */

public class AllUserInfoFragment extends Fragment {
    private ArrayList<OneUserInfo> allUserInfos = new ArrayList<>();
    private ArrayList<OneUserInfo> allUserInfos1 = new ArrayList<>();
    private AllUserInfoAdapter adapter;
    private ListView listView;
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
                    allUserInfos.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "所有用户信息加载失败！", Toast.LENGTH_SHORT).show();
                    break;
                case LOAD_NO:
                    Toast.makeText(getActivity(), "没有用户信息！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view, container, false);
        getAllUserInfos();
        adapter = new AllUserInfoAdapter(getActivity(), R.layout.every_user_info, allUserInfos);
        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OneUserInfo oneUserInfo = allUserInfos.get(position);
                Intent intent = new Intent(getActivity(), SingleUserInfoChange.class);
                intent.putExtra("ptelephone", oneUserInfo.getAccount());
                intent.putExtra("pnick", (oneUserInfo.getUserName().equals("未设置"))?"":oneUserInfo.getUserName());
                intent.putExtra("icon", oneUserInfo.getIconIdx());
                intent.putExtra("pemail", oneUserInfo.getEmail());
                intent.putExtra("major", oneUserInfo.getMajor());
                intent.putExtra("interests", oneUserInfo.getInterests());
                startActivity(intent);
            }
        });
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
        return view;
    }

    public void getAllUserInfos() {
        String address = GlobalFlags.getIpAddress() + "Pallperson.jsp";
        String params = "";
        HttpUtil.sendHttpRequest(address, params, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                //Log.d("allInfo", "message: " + response);
                Message message = new Message();
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    if (jsonarray.length() > 0) {
                        allUserInfos.clear();
                        allUserInfos1.clear();
                    } else {
                        message.what = LOAD_NO;
                        handler.sendMessage(message);
                        return;
                    }
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String account = jsonObject.getString("ptelephone");
                        String username = jsonObject.getString("pnick");
                        int index = Integer.parseInt(jsonObject.getString("picon"));
                        String email = jsonObject.getString("pemail");
                        String major = jsonObject.getString("major");
                        String interests = jsonObject.getString("inter");
                        allUserInfos.add(new OneUserInfo(account, (username.length() == 0) ? "未设置" : username, index, email, major, interests));
                        allUserInfos1.add(new OneUserInfo(account, (username.length() == 0) ? "未设置" : username, index, email, major, interests));
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
        allUserInfos.clear();
        Iterator<OneUserInfo> itt1 =allUserInfos1.iterator();
        while(itt1.hasNext()){
            allUserInfos.add(itt1.next());
        }
        //String address = GlobalFlags.getIpAddress() + "picsearch";
        //String params = "search=" + searchContent + "&phone=" + GlobalFlags.getUserID();
        ArrayList<OneUserInfo> temp_search = new ArrayList<>();
        //Log.d("search", "searchPics: "+searchContent);
        Iterator<OneUserInfo> it =allUserInfos.iterator();
        while(it.hasNext()){
            OneUserInfo temp = it.next();
            String fg=temp.getAccount();
            if(fg.equalsIgnoreCase(searchContent)) {
                temp_search.add(temp);
            }
        }
        if(temp_search.size()>0){
            //adapter = new AllPicInfoAdapter(getActivity(), R.layout.every_pic_info, temp_search);
            allUserInfos.clear();
            // allpicinfo=temp_search;
            Iterator<OneUserInfo> itt =temp_search.iterator();
            while(itt.hasNext()){
                allUserInfos.add(itt.next());
            }
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getActivity(), "没有该用户信息", Toast.LENGTH_SHORT).show();
        }
    }
}
