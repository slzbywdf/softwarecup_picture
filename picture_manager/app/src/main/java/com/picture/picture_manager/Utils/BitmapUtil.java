package com.picture.picture_manager.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by 13307 on 2017/3/27.
 */

public class BitmapUtil {

    public static final int CORNER_NONE = 0;
    public static final int CORNER_TOP_LEFT = 1;
    public static final int CORNER_TOP_RIGHT = 1 << 1;
    public static final int CORNER_BOTTOM_LEFT = 1 << 2;
    public static final int CORNER_BOTTOM_RIGHT = 1 << 3;
    public static final int CORNER_ALL = CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT;
    public static final int CORNER_TOP = CORNER_TOP_LEFT | CORNER_TOP_RIGHT;
    public static final int CORNER_BOTTOM = CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT;
    public static final int CORNER_LEFT = CORNER_TOP_LEFT | CORNER_BOTTOM_LEFT;
    public static final int CORNER_RIGHT = CORNER_TOP_RIGHT | CORNER_BOTTOM_RIGHT;

    /*生成圆角图片，roundPx为圆角程度，corners用于选择圆角的位置*/
    public static Bitmap fillet(Bitmap bitmap, int roundPx, int corners) {
        try {
            // 其原理就是：先建立一个与图片大小相同的透明的Bitmap画板
            // 然后在画板上画出一个想要的形状的区域。
            // 最后把源图片帖上。
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            //画出4个圆角
            final RectF rectF = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            //把不需要的圆角去掉
            int notRoundedCorners = corners ^ CORNER_ALL;
            if ((notRoundedCorners & CORNER_TOP_LEFT) != 0) {
                clipTopLeft(canvas, paint, roundPx, width, height);
            }
            if ((notRoundedCorners & CORNER_TOP_RIGHT) != 0) {
                clipTopRight(canvas, paint, roundPx, width, height);
            }
            if ((notRoundedCorners & CORNER_BOTTOM_LEFT) != 0) {
                clipBottomLeft(canvas, paint, roundPx, width, height);
            }
            if ((notRoundedCorners & CORNER_BOTTOM_RIGHT) != 0) {
                clipBottomRight(canvas, paint, roundPx, width, height);
            }
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            //贴子图
            final Rect src = new Rect(0, 0, width, height);
            final Rect dst = src;
            canvas.drawBitmap(bitmap, src, dst, paint);
            return paintingBoard;
        } catch (Exception exp) {
            return bitmap;
        }
    }

    /*裁剪图片*/
    public static Bitmap cut(Bitmap rawbitmap, int width, int height) {
        if (rawbitmap == null)
            return null;
        else {
            final int rawWidth = rawbitmap.getWidth();
            final int rawHeight = rawbitmap.getHeight();
            //计算裁剪开始坐标
            int x = (rawWidth - width) / 2;
            int y = (rawHeight - height) / 2;
            Bitmap newbitmap = Bitmap.createBitmap(rawbitmap, x, y, width, height, null, false);
            if (!rawbitmap.isRecycled())
                rawbitmap.recycle();
            return newbitmap;
        }
    }

    /*压缩图片（压缩后的图片用于裁剪成正方形图片）*/
    public static Bitmap compressBasedOnSquare(Bitmap rawBitmap, int newWidth) {
        //得到图片原始的高宽
        int rawHeight = rawBitmap.getHeight();
        int rawWidth = rawBitmap.getWidth();
        float ratio = ((float) rawHeight) / rawWidth;
        //计算伸缩比
        float xscale, yscale;
        if (rawWidth < rawHeight) {
            xscale = ((float) newWidth) / rawWidth;
            yscale = (ratio * newWidth) / rawHeight;
        } else {
            xscale = (newWidth / ratio) / rawWidth;
            yscale = ((float) newWidth) / rawHeight;
        }
        // 新建立矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(xscale, yscale);
        // 设置图片的旋转角度
        // matrix.postRotate(-30);
        // 设置图片的倾斜
        // matrix.postSkew(0.1f, 0.1f);
        // 将图片大小压缩
        // 压缩后图片的宽和高以及kB大小均会变化
        Bitmap newBitmap = Bitmap.createBitmap(rawBitmap, 0, 0, rawWidth, rawHeight, matrix, true);
        //Log.d("bitmap", "compressBasedOnSquare: " + newBitmap.getWidth() + " " + newBitmap.getHeight());
        if (!rawBitmap.isRecycled())
            rawBitmap.recycle();
        return newBitmap;
    }

    private static void clipTopLeft(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, 0, offset, offset);
        canvas.drawRect(block, paint);
    }

    private static void clipTopRight(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(width - offset, 0, width, offset);
        canvas.drawRect(block, paint);
    }

    private static void clipBottomLeft(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, height - offset, offset, height);
        canvas.drawRect(block, paint);
    }

    private static void clipBottomRight(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(width - offset, height - offset, width, height);
        canvas.drawRect(block, paint);
    }
}
