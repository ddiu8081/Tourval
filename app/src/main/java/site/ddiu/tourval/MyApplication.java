package site.ddiu.tourval;

import android.app.Application;
import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.avos.avoscloud.AVOSCloud;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class MyApplication extends Application {
    private static Context context;
    //声明AMapLocationClient类对象

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5acf1e82");//=号后面写自己应用的APPID
        Setting.setShowLog(true); //设置日志开关（默认为true），设置成false时关闭语音云SDK日志打印
        TTSUtils.getInstance().init(); //初始化工具类

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"BBJYSWYrjwnkymT9GxaNPhYE-gzGzoHsz","q6WeHER9mStx5bqsF3LwaYHe");
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);

        // 初始化高德定位
        GdLocation.init(this);

    }

    //获取应用上下文环境
    public static Context getContext() {
        return context;
    }
}
