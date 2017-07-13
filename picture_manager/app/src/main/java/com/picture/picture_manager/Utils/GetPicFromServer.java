package com.picture.picture_manager.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 13307 on 2017/4/15.
 */

public class GetPicFromServer {
    public static void sendHttpRequest(final String[] address, final PicCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                Bitmap[] allpics = new Bitmap[address.length];
                try {
                    for (int i = 0; i < address.length; i++) {
                        URL url = new URL(address[i]);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        //connection.setRequestProperty("Content-type", "text/html");
                        //connection.setRequestProperty("Accept-Charset", "utf-8");
                        //connection.setRequestProperty("contentType", "utf-8");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setDoInput(true);
                        connection.connect();
                        // 将得到的数据转化成InputStream
                        InputStream is = connection.getInputStream();
                        // 将InputStream转换成Bitmap
                        allpics[i] = BitmapFactory.decodeStream(is);
                        is.close();
                        //Log.d("Get from server", "message: " + response.toString());
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                    if (listener != null) {// 回调onFinish()方法
                        listener.onFinish(allpics);
                    }
                } catch (Exception e) {
                    if (listener != null) {// 回调onError()方法
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }
}
