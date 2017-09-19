package cc.foxtail.new_version;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lidia on 2017. 4. 28..
 */

public final class UserInfo {
    public static final ExecutorService server = Executors.newCachedThreadPool();
    public static int childID;
    private static String childName;
    private static String FcmToken = "";

    public static String executeServer(Callable<String> callable) {
        try {
            String result = server.submit(callable).get();
            Log.e("SERVER_SUCCESS", result);
            return result;
        } catch (Exception e) {
            Log.e("SERVER_ERROR : ", e.toString());
            return null;
        }
    }

    public static void setChildID(int id) {
        childID = id;
    }

    public static int getChildIdToInt() {
        return childID;
    }

    public static String getChildIdToString() {
        return childID+"";
    }

    public static void setChildName(String name) {
        childName = name;
    }

    public static String getChildName() {
        return childName;
    }

    public static void setFcmToken(String token) {
        FcmToken = token;
    }

    public static String getFcmToken() {
        return FcmToken;
    }
}
