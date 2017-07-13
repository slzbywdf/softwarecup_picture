package com.picture.picture_manager.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.picture.picture_manager.Beans.OneUserInfo;
import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.GlobalFlags;

import java.util.List;

/**
 * Created by Wang Cong on 2016/9/27.
 */

public class AllUserInfoAdapter extends ArrayAdapter<OneUserInfo> {
    private int resourceId;
    private Context context;
    //private ArrayList<OneUserInfo> allUserInfos;

    public AllUserInfoAdapter(Context context, int textViewResourceId, List<OneUserInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OneUserInfo oneUserInfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.image_user_icon);
            viewHolder.account = (TextView) view.findViewById(R.id.text_account_every_user_info);
            viewHolder.username = (TextView) view.findViewById(R.id.text_user_name_every_user_info);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.icon.setImageResource(GlobalFlags.UserIcons[oneUserInfo.getIconIdx()]);
        viewHolder.account.setText(oneUserInfo.getAccount());
        viewHolder.username.setText(oneUserInfo.getUserName());
        if(position==0)
            view.setBackgroundColor(Color.parseColor("#f05b48"));
        return view;
    }

    class ViewHolder {
        ImageView icon;
        TextView account;
        TextView username;
    }
}
