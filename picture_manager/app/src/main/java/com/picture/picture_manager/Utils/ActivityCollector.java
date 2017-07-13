package com.picture.picture_manager.Utils;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang Cong on 2016/10/21.
 */

public class ActivityCollector {
    public static List<AppCompatActivity> activities=new ArrayList<AppCompatActivity>();

    public static void addActivity(AppCompatActivity activity){
        activities.add(activity);
    }

    public static void finishAll(){
        for(AppCompatActivity activity:activities){
            if(!activity.isFinishing())
                activity.finish();
        }
    }
}
