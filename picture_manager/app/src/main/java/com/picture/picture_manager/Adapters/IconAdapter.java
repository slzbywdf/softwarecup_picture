package com.picture.picture_manager.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.GlobalFlags;


import java.util.ArrayList;

/**
 * Created by 13307 on 2017/5/9.
 */

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Integer> icons;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        public ViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.every_icon);
        }
    }

    public IconAdapter(Context context, ArrayList<Integer> allicon) {
        this.context = context;
        icons = allicon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.everyicon, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (GlobalFlags.getIconIndex() != position) {
                    GlobalFlags.setIconIndex(position);
                    GlobalFlags.setIsIconChanged(true);
                }
                if (Activity.class.isInstance(context))
                    ((Activity) context).finish();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int iconPath = icons.get(position);
        Glide.with(context).load(iconPath).placeholder(R.drawable.loading_and_failed_pic).error(R.drawable.loading_and_failed_pic).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }
}
