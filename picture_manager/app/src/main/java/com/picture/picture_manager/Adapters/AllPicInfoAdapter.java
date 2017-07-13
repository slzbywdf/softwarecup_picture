package com.picture.picture_manager.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.picture.picture_manager.Beans.OnePicInfo;
import com.picture.picture_manager.R;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/4.
 */

public class AllPicInfoAdapter extends ArrayAdapter<OnePicInfo> {
    private int resourceId;
    private Context context;
    private List<OnePicInfo> mypicinfo;
    static class ViewHolder {
        ImageView image;
        TextView name;
        TextView tag;
        TextView time;
        TextView isload;
        CheckBox ischosed;
    }

    public AllPicInfoAdapter(Context context,int resourceId,List<OnePicInfo> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
        this.context = context;
        this.mypicinfo = objects;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final OnePicInfo onePicInfo = mypicinfo.get(position);
        View view;
        ViewHolder viewHolder;
        ImageView imageView;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image= (ImageView) view.findViewById(R.id.image);
            viewHolder.name = (TextView) view.findViewById(R.id.image_name);
            viewHolder.tag = (TextView) view.findViewById(R.id.image_tag);
            viewHolder.time = (TextView)view.findViewById(R.id.image_t);
            viewHolder.isload = (TextView)view.findViewById(R.id.isfinshed);
            viewHolder.ischosed = (CheckBox)view.findViewById(R.id.check);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name .setText(onePicInfo.getPicture_name());
        String tag="";
        String[] fm = onePicInfo.getTag().split("-");
        for(int i=0;i<fm.length;i++){
            tag=tag+" "+fm[i];
        }
        viewHolder.tag.setText(tag);
        viewHolder.time.setText(onePicInfo.getFinist_time());
        if(onePicInfo.getIsload()==0){
            viewHolder.isload.setText("");
        }else{
            viewHolder.isload.setText("已下载");
        }
        if(onePicInfo.isflag()){
            viewHolder.ischosed.setChecked(true);
        }else{
            viewHolder.ischosed.setChecked(false);
        }
        String url = onePicInfo.getPass();
        //中文的utf-8范围
        Pattern p= Pattern.compile("[\\u4e00-\\u9fa5]");
        //Log.d("haha", "getView: "+url );
        //找到中文url中中文
        Matcher ma =p.matcher(url);
        while(ma.find()){
            String group = ma.group();
            //Log.e("haha", "getView: "+group );
            try{
                url = url.replaceFirst(group, URLEncoder.encode(group,"utf-8"));
            }catch (Exception e){

            }
        }
        Glide.with(context).load(url).placeholder(R.drawable.loading_and_failed_pic).error(R.drawable.loading_and_failed_pic).into(viewHolder.image);
        CheckBox mycheck = viewHolder.ischosed;
        mycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onePicInfo.setIsflag(b);//将是否选中信息保存
            }
        });
        return view;
    }
}
