package com.picture.picture_manager.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.picture.picture_manager.R;

import java.util.ArrayList;

/**
 * Created by 13307 on 2017/3/26.
 */

public class ImageAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> imagePaths;
    ArrayList<Bitmap> allPics;
    int imageWidth;

    public ImageAdapter(Context context, ArrayList<String> imagePaths, ArrayList<Bitmap> allPics, int imageWidth) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.allPics = allPics;
        this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    public Object getItem(int position) {
        return imagePaths.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*WindowManager mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point windowSize = new Point();
        mWm.getDefaultDisplay().getSize(windowSize);
        int windowWidth = windowSize.x;
        int imagePadding = 30;
        int colums = 3;
        int imageWidth = windowWidth / colums - imagePadding;*/


        ImageView imageView;
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.everypic, parent, false);
            convertView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
            //convertView.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.every_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        imageView = holder.img;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setImageBitmap(BitmapFactory.decodeFile((String)imagePaths.get(position)));
        //imageView.setImageResource(Integer.parseInt(imagePaths.get(position)));
        imageView.setImageBitmap(allPics.get(position));
        return convertView;
    }

    static class ViewHolder {
        ImageView img;
    }
}
