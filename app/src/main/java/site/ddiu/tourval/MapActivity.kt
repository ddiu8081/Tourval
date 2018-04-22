package site.ddiu.tourval

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.avos.avoscloud.*
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_map.*
import org.jetbrains.anko.act
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class MapActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标
        Log.d("BAR",QMUIStatusBarHelper.getStatusbarHeight(this).toString())

        val intent = intent
        val objectId = intent.getStringExtra("objectId")


        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        amapView.onCreate(savedInstanceState)
        mapInit() //初始化

        // 查询
        val query:AVQuery<AVObject> = AVQuery("POIInfo")
        query.whereEqualTo("placeId", objectId)
        query.findInBackground(object :FindCallback<AVObject>() {
            override fun done(list:List<AVObject> , e:AVException?) {
                for (avObject in list) {
                    val name = avObject.getString("Name")
                    val desc = avObject.getString("desc")
                    val poi = avObject.getAVGeoPoint("location")

                    Log.d("NAME",name)
                    Log.d("DESC",desc)
                    // 标兴趣点
                    val aMap = amapView.map
                    val latLng = LatLng(poi.latitude,poi.longitude)
                    val marker = aMap.addMarker(MarkerOptions().position(latLng).title(name).snippet(desc))
                }
            }
        })

    }

    private fun mapInit () {
        val aMap = amapView.map

        val myLocationStyle = MyLocationStyle()
        myLocationStyle.interval(2000) //定位间隔
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE) //定位方式

        // 定位蓝圈设置
        myLocationStyle.strokeColor(Color.parseColor("#66ffc408")) //边缘颜色
        myLocationStyle.radiusFillColor(Color.parseColor("#11ffc408")) //区域颜色
        aMap.myLocationStyle = myLocationStyle //设置定位蓝点的Style
        aMap.isMyLocationEnabled = true //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.showMapText(false) //不显示地图标识

        // 自定义地图风格
//        setMapCustomFile(this, "style.data")
//        aMap.setMapCustomEnable(true)

        // UI调整
        val mUiSettings = aMap.uiSettings
        mUiSettings.isZoomControlsEnabled = false
        mUiSettings.isScaleControlsEnabled = true
        mUiSettings.isMyLocationButtonEnabled = false //设置默认定位按钮是否显示，非必需设置。

        // 手势功能设置
//        mUiSettings.isZoomGesturesEnabled = false //禁止缩放手势
//        mUiSettings.isScrollGesturesEnabled = false //禁止滑动手势
//        mUiSettings.isRotateGesturesEnabled = false //禁止旋转手势
//        mUiSettings.isTiltGesturesEnabled = false //禁止倾斜手势

        // 地图状态设置
        aMap.moveCamera(CameraUpdateFactory.changeTilt(0f)) //倾斜角度为0
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18f)) //地图缩放比例

        aMap.setOnMyLocationChangeListener {
            //当用户位置改变时回调的方法类。
//            Log.d("location/latitude",location.latitude.toString())
//            Log.d("location/longitude",location.longitude.toString())
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss")// HH:mm:ss
            val date = Date(System.currentTimeMillis())
            val timenow = simpleDateFormat.format(date)
            textView11.text = timenow+" lat:"+it.latitude.toString()+" long:"+it.longitude.toString()

            // 开始查询附近的兴趣点
            val query:AVQuery<AVObject> = AVQuery("POIInfo")
            val myLocation = AVGeoPoint(it.latitude, it.longitude)
            query.whereWithinKilometers("location",myLocation,0.04)
            query.getFirstInBackground(object : GetCallback<AVObject>() {
                override fun done(avObject: AVObject?, e: AVException?) {
                    if (avObject != null) {
                        // object 就是符合条件的第一个 AVObject
                        val name = avObject.getString("Name")
                        val desc = avObject.getString("desc")
                        val poi = avObject.getAVGeoPoint("location")

                        Log.d("NAME",name)
                        Log.d("DESC",desc)

                        // 标兴趣点
                        textView_POITitle.text = name
                        textView_POIDesc.text = desc


                        btn_read.setOnClickListener {
                            TTSUtils.getInstance().speak(desc)
                        }
                        if (!TTSUtils.getInstance().isSpeaking) {
                            TTSUtils.getInstance().speak(desc)
                        }

                        val distance = AMapUtils.calculateLineDistance(LatLng(it.latitude,it.longitude), LatLng(poi.latitude,poi.longitude))
//                    btn_read.text = distance.toString()
                    }
                }
            })
        }
    }

//    override fun TTSUtils.onBufferProgress(percent:Int, beginPos:Int, endPos:Int, info:String) {
//        // 合成进度
//        btn_read.text = percent.toString() + "%"
//    }

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
