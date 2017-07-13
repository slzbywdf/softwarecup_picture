package com.picture.picture_manager.Utils;


import com.picture.picture_manager.R;

/**
 * Created by 13307 on 2017/3/22.
 */

public class GlobalFlags {
    static private String userID = new String();
    static private boolean isNeedtoRefresh = false;
    static private String IpAddress = "http://114.115.130.89:80/ssh_pic/";
    static private boolean isLoggedIn = false;
    static private String sessionId = "";
    static public final int[] UserIcons = {R.drawable.boy1, R.drawable.boy2, R.drawable.boy3, R.drawable.boy4, R.drawable.boy5, R.drawable.boy6, R.drawable.boy7, R.drawable.boy8, R.drawable.girl1, R.drawable.girl2, R.drawable.girl3, R.drawable.girl4, R.drawable.girl5, R.drawable.girl6, R.drawable.girl7, R.drawable.girl8};
    static private int IconIndex = -1;
    static private boolean isIconChanged = false;

    public static boolean isIconChanged() {
        return isIconChanged;
    }

    public static void setIsIconChanged(boolean isIconChanged) {
        GlobalFlags.isIconChanged = isIconChanged;
    }

    public static int getIconIndex() {
        return IconIndex;
    }

    public static void setIconIndex(int iconIndex) {
        IconIndex = iconIndex;
    }

    public static boolean isNeedtoRefresh() {
        return isNeedtoRefresh;
    }

    public static void setIsNeedtoRefresh(boolean isNeedtoRefresh) {
        GlobalFlags.isNeedtoRefresh = isNeedtoRefresh;
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        GlobalFlags.sessionId = sessionId;
    }

    public static String getIpAddress() {
        return IpAddress;
    }

    public static void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        GlobalFlags.userID = userID;
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(boolean isLoggedIn) {
        GlobalFlags.isLoggedIn = isLoggedIn;
    }
}
