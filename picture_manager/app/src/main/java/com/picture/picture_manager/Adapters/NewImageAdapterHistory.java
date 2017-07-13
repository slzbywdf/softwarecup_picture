package com.picture.picture_manager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.picture.picture_manager.Activities.LabelPicture;
import com.picture.picture_manager.Beans.OnePicHistory;
import com.picture.picture_manager.R;


import java.util.ArrayList;

/**
 * Created by 13307 on 2017/4/12.
 */

public class NewImageAdapterHistory extends RecyclerView.Adapter<NewImageAdapterHistory.ViewHolder> {
    private Context context;
    //private int ImageHeight;
    //ArrayList<String> imagePaths;
    //ArrayList<String> allLabels;
    private ArrayList<OnePicHistory> allHisPics;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView EveryView;
        ImageView Image;
        TextView label;

        public ViewHolder(View view) {
            super(view);
            EveryView = (CardView) view;
            Image = (ImageView) view.findViewById(R.id.image_cardview);
            label = (TextView) view.findViewById(R.id.text_cardview);
        }
    }

    public NewImageAdapterHistory(Context context, ArrayList<OnePicHistory> allPics) {
        this.context = context;
        allHisPics = allPics;
        //this.ImageHeight = imageHeight;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pic_card_view, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.EveryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                OnePicHistory one = allHisPics.get(position);
                if (one.getIsClickable().equals("0")) {
                /*Fruit fruit = mFruitList.get(position);
                Intent intent = new Intent(mContext, FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME, fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID, fruit.getImageId());
                mContext.startActivity(intent);*/
                    Intent intent = new Intent(context, LabelPicture.class);
                    intent.putExtra("PicPath", one.getPath());
                    intent.putExtra("PicId", one.getId());
                    intent.putExtra("PicLabel", one.getLabel());
                    intent.putExtra("Recommends", one.getRecommends());
                    context.startActivity(intent);
                } else
                    Toast.makeText(context, "该图片标签已判定完毕！", Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OnePicHistory one = allHisPics.get(position);
        String imagePath = one.getPath();
        //Log.d("his", "onBindViewHolder: " + imagePath);
        Glide.with(context).load(imagePath).placeholder(R.drawable.loading_and_failed_pic).error(R.drawable.loading_and_failed_pic).into(holder.Image);
        holder.label.setText(one.getLabel());
    }

    @Override
    public int getItemCount() {
        return allHisPics.size();
    }
}
