package com.picture.picture_manager.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.picture.picture_manager.R;

import java.util.ArrayList;

/**
 * Created by 13307 on 2017/3/26.
 */

public class ImageAdapterHistory extends BaseAdapter {

    private Context context;
    ArrayList<String> imagePaths;
    ArrayList<String> allLabels;
    ArrayList<Bitmap> allImages;
    int imageWidth;

    public ImageAdapterHistory(Context context, ArrayList<String> imagePaths, ArrayList<Bitmap> allImages, int imageWidth, ArrayList<String> allLabels) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.allImages = allImages;
        this.imageWidth = imageWidth;
        this.allLabels = allLabels;
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
        int imagePadding = 60;//图片间隔
        int colums = 2;//图片列数
        int imageWidth = windowWidth / colums - imagePadding;//图片宽度（正方形）*/

        ImageView imageView;
        TextView textView;
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_everycard, parent, false);
            //设置每个图片和标签组成的“卡片”所占用的高度和宽度
            convertView.setLayoutParams(new GridView.LayoutParams(imageWidth, (int) (imageWidth / 3.0 * 4)));
            //convertView.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.image_history);
            holder.labels = (TextView) convertView.findViewById(R.id.text_history);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        imageView = holder.img;
        //设置正方形图片显示宽度
        imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
        //imageView.setImageBitmap(BitmapFactory.decodeFile((String)imagePaths.get(position)));
        /*Bitmap rawbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        Bitmap combitmap = BitmapUtil.compressBasedOnSquare(rawbitmap, imageWidth);
        Bitmap cutbitmap = BitmapUtil.cut(combitmap, imageWidth, imageWidth);
        Bitmap roundcorbitmap = BitmapUtil.fillet(cutbitmap, 20, BitmapUtil.CORNER_TOP);*/
        //imageView.setBackgroundResource(Integer.parseInt(imagePaths.get(position)));
        imageView.setImageBitmap(allImages.get(position));
        textView = holder.labels;
        textView.setText(allLabels.get(position));
        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView labels;
    }
}
