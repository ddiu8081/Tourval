package site.ddiu.tourval

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.amap.api.maps.CameraUpdate
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.MyLocationStyle
import kotlinx.android.synthetic.main.activity_gaode.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import com.amap.api.maps.model.CameraPosition




class GaodeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaode)
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        amapView.onCreate(savedInstanceState)

        mapInit() //初始化

    }

    private fun mapInit () {
        val aMap = amapView.map

        val myLocationStyle = MyLocationStyle()
        myLocationStyle.interval(1000) //定位间隔
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE) //定位方式

        // 定位蓝圈设置
        myLocationStyle.strokeColor(Color.parseColor("#000000")) //边缘颜色
        myLocationStyle.radiusFillColor(Color.parseColor("#33FEDFE1")) //区域颜色
        aMap.myLocationStyle = myLocationStyle //设置定位蓝点的Style
        aMap.isMyLocationEnabled = true //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.showMapText(false) //不显示地图标识

        // 自定义地图风格
        setMapCustomFile(this, "style.data")
        aMap.setMapCustomEnable(true)

        // UI调整
        val mUiSettings = aMap.uiSettings
        mUiSettings.isZoomControlsEnabled = false
        mUiSettings.isScaleControlsEnabled = true

        // 手势功能设置
        mUiSettings.isZoomGesturesEnabled = false //禁止缩放手势
        mUiSettings.isScrollGesturesEnabled = false //禁止滑动手势
        mUiSettings.isRotateGesturesEnabled = false //禁止旋转手势
        mUiSettings.isTiltGesturesEnabled = false //禁止倾斜手势

        // 地图状态设置
//        val mCameraUpdate = CameraUpdateFactory //倾斜角度为0
        aMap.moveCamera(CameraUpdateFactory.changeTilt(0f))
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18f))

        aMap.setOnMyLocationChangeListener { location ->
            //当用户位置改变时回调的方法类。
            Log.d("location/latitude",location.latitude.toString())
            Log.d("location/longitude",location.longitude.toString())
        }
    }

    private fun setMapCustomFile(context: Context, PATH: String) {
        var out: FileOutputStream? = null
        var inputStream: InputStream? = null
        var moduleName: String? = null
        try {
            inputStream = context.assets
                    .open("customConfigdir/$PATH")
            val b = ByteArray(inputStream!!.available())
            inputStream.read(b)

            moduleName = context.filesDir.absolutePath
            val f = File("$moduleName/$PATH")
            if (f.exists()) {
                f.delete()
            }
            f.createNewFile()
            out = FileOutputStream(f)
            out.write(b)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (out != null) {
                    out.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        Log.d("assName","$moduleName/$PATH")
        amapView.map.setCustomMapStylePath("$moduleName/$PATH")

    }

    override fun onDestroy() {
        super.onDestroy()
        amapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        amapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        amapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        amapView.onSaveInstanceState(outState)
    }
}
