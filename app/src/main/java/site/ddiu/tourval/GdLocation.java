package site.ddiu.tourval;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created on 2018/4.
 *
 * @desc 高德定位
 */

public class GdLocation {
    /**
     * 客户端
     */
    private static AMapLocationClient aMapLocationClient = null;
    /**
     * 参数
     */
    private static AMapLocationClientOption aMapLocationClientOption = null;
    /**
     * 监听设置标志
     */
    private static boolean setListener;

    /**
     * 定位初始化（Application之onCreate调一次）
     *
     * @param context
     */
    public static void init(Context context) {
        // 客户端
        aMapLocationClient = new AMapLocationClient(context);
        // 参数
        aMapLocationClientOption = new AMapLocationClientOption();
        // 高精度定位模式
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // setOnceLocationLatest(boolean b)接口true，定位SDK返回近3s精度最高结果
        // true则setOnceLocation(boolean b)接口被设true，反之不，默认false
        aMapLocationClientOption.setOnceLocationLatest(true);
        // 定位间隔默认2000ms，最低1000
        aMapLocationClientOption.setInterval(2000);
        // 返回地址信息（默认返回）
        aMapLocationClientOption.setNeedAddress(true);
        // 允许模拟位置（默认true）
        aMapLocationClientOption.setMockEnable(true);
        // 超时默认30000ms，建议不低8000
        aMapLocationClientOption.setHttpTimeOut(20000);
        // 关闭缓存机制
        aMapLocationClientOption.setLocationCacheEnable(false);
        // 强制刷新WIFI（默认强制）
        aMapLocationClientOption.setWifiScan(true);
        // 传感器
        aMapLocationClientOption.setSensorEnable(true);
        // 客户端设参数
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
    }

    /**
     * 定位结果回调
     */
    public interface MyLocationListener {
        /**
         * result
         *
         * @param location
         */
        void result(AMapLocation location);
    }

    /**
     * 位置获取（重发获取请求）
     *
     * @param listener
     */
    public static void getCurrentLocation(final MyLocationListener listener) {
        if (aMapLocationClient == null) {
            return;
        }
        // 仅设一次防多设致结果重复输出
        if (!setListener) {
            aMapLocationClient.setLocationListener(new AMapLocationListener() {

                @Override
                public void onLocationChanged(AMapLocation location) {
                    if (location != null) {
                        aMapLocationClient.stopLocation();
                        listener.result(location);
                    }
                }
            });
            setListener = true;
        }
        // 启动定位
        aMapLocationClient.startLocation();
    }

    /**
     * 客户端销毁（退出程序调用防定位异常）
     */
    public static void destroy() {
        aMapLocationClient.onDestroy();
    }
}