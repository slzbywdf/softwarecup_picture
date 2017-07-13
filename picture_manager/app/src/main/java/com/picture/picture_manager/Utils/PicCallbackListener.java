package com.picture.picture_manager.Utils;

import android.graphics.Bitmap;

/**
 * Created by 13307 on 2017/4/15.
 */

public interface PicCallbackListener {
    void onFinish(Bitmap[] allPics);

    void onError(Exception e);
}
