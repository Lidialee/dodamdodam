package cc.foxtail.new_version;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.kakao.auth.KakaoSDK;


public class GlobalApplication extends Application {

    private static volatile GlobalApplication obj = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());
        Log.d("GlobalApplication","확인");

    }

    public static GlobalApplication getGlobalApplicationContext() {
        Log.d("getGlobalApplication","확인");
        return obj;
    }

    public static Activity getCurrentActivity() {
        Log.d("setCurrentActivity","확인");
        return currentActivity;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        Log.d("setCurrentActivity","확인");
        GlobalApplication.currentActivity = currentActivity;
    }
}
