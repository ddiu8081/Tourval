package site.ddiu.tourval

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.hchstudio.kpermissions.KPermission
import com.amap.api.location.AMapLocation
import com.avos.avoscloud.*
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUIFloatLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import java.util.*
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import java.text.DecimalFormat
import com.avos.avoscloud.AVException
import com.avos.avoscloud.FunctionCallback
import com.avos.avoscloud.AVCloud
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    data class LocItem(val objectId: String, val name: String, val desc: String, val location: AVGeoPoint, val imgSrc: String, val distance: String)
    data class LocItemMin(val objectId: String, val name: String, val location: AVGeoPoint, val imgSrc: String, val distance: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity","onCreate")

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标

        var kPermission = KPermission(this) // 请求敏感权限
        kPermission.requestPermission(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE), {
            Log.i("kPermission", "isAllow---$it")
        }, {
            Log.i("kPermission", "permission---$it")
        })

        initTags() // 初始化 热门标签 栏目

        search_bar.setOnClickListener {
            switchSearch(0)
        }


    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity","onStart")

        var likeList:MutableList<LocItemMin> = ArrayList ()

        // 定位工具初始化
        GDLocationUtil.init(this)

        like_list.layoutManager = LinearLayoutManager(act,LinearLayoutManager.HORIZONTAL,false)
        like_list.adapter = MainAdapter(likeList) {
            val intent = Intent(act, PlaceInfoActivity::class.java)
            intent.putExtra("objectId",it.objectId)
            startActivity(intent) //启动地图界面
        }

        // 获取定位
        GDLocationUtil.getCurrentLocation {
            Log.d("GaodeLocation",it.address)
            val myLoc = it
            initLike(myLoc, likeList) // 初始化 猜你喜欢 栏目
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity","onStop")

        GDLocationUtil.destroy()
    }

    private fun addItemToFloatLayout(floatLayout:QMUIFloatLayout, itemId:Int, itemText:String) {
        val currentChildCount = floatLayout.childCount

        //自定义textview样式
        val textView = TextView(this)
        textView.setPadding(30, 15, 30, 15)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        textView.setTextColor(Color.parseColor("#666666"))
        textView.background = getDrawable(R.drawable.textview_border)
        textView.text = itemText
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        floatLayout.addView(textView, layoutParams)//将textview添加到floatLayout布局中
        textView.setOnClickListener {
            switchSearch(itemId)
        }
    }

    private fun initTags () {
        qmuidemo_floatlayout.gravity = Gravity.LEFT //floatLayout中子节点左对齐
        qmuidemo_floatlayout.maxNumber = Int.MAX_VALUE
        qmuidemo_floatlayout.maxLines = Integer.MAX_VALUE

        // 查询
        val query: AVQuery<AVObject> = AVQuery("TagInfo")
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(list:List<AVObject>, e: AVException?) {
                for (avObject in list) {
                    addItemToFloatLayout(qmuidemo_floatlayout,avObject.getInt("tagId"),avObject.getString("tagName"))
                }
            }
        })
    }

    private fun initLike (myLoc: AMapLocation, likeList: MutableList<LocItemMin>) {
        var isNear = false

        // 构建参数
        val dicParameters = HashMap<String, String>()
        dicParameters["movie"] = "夏洛特烦恼"

        // 调用云函数 averageStars
//        AVCloud.rpcFunctionInBackground("getLikeList", dicParameters, object : FunctionCallback<HashMap<String, Any>>() {
//            override fun done(data: HashMap<String, Any>?, e: AVException?) {
//
//            }
//        })
        AVCloud.callFunctionInBackground("getLikeList", dicParameters, object : FunctionCallback<Iterable<HashMap<String, Any>>>() {
            override fun done(data: Iterable<HashMap<String, Any>>?, e: AVException?) {
                if (e == null) {
                    // 处理返回结果
                    Log.d("avObject in likeList",data.toString())
                    toast("ok")
                    if (data != null) {
                        data.forEach {
                            Log.d("avObject in key",it.toString())

                            val objectId = it["id"] as String
                            val name = it["name"] as String
                            val imgSrc = it["imgSrc"] as String
                            val poi= it["location"] as AVGeoPoint
                            val distance_f = AMapUtils.calculateLineDistance(LatLng(myLoc.latitude,myLoc.longitude), LatLng(poi.latitude,poi.longitude))

                            if (distance_f < 1500) {
                                nearPlace_layout.visibility = View.VISIBLE
                                textView_myLoc.text = "当前位置：" + name
                                isNear = true
                                textView_myLoc.setOnClickListener {
                                    val intent = Intent(act, PlaceInfoActivity::class.java)
                                    intent.putExtra("objectId",objectId)
                                    startActivity(intent) //启动地图界面
                                }
                            }

                            val distance = if (distance_f < 1000) {
                                val dFormat = DecimalFormat("0")
                                dFormat.format(distance_f).toString() + "m"
                            } else {
                                val dFormat = DecimalFormat(".0")
                                dFormat.format(distance_f/1000).toString() + "km"
                            }


                            Log.d("OBJECTID",objectId)
                            Log.d("NAME",name)
                            Log.d("MYLOC",myLoc.poiName)
                            Log.d("DISTANCE",distance)

                            likeList.add(LocItemMin(objectId,name,poi,imgSrc,distance))
                        }
                    }

                } else {
                    // 处理报错
                    toast("拉取推荐列表错误")
                    Log.d("avObject in error",e.toString())
                }
            }

        })

        // 查询
//        val query: AVQuery<AVObject> = AVQuery("PlaceInfo")
//        query.findInBackground(object : FindCallback<AVObject>() {
//            override fun done(list:List<AVObject>, e: AVException?) {
//                for (avObject in list) {
//                    val objectId = avObject.objectId
//                    val name = avObject.getString("name")
//                    val desc = avObject.getString("desc")
//                    val imgSrc = avObject.getString("imgSrc")
//                    val poi = avObject.getAVGeoPoint("location")
////                    val distance = "12345"
//                    val distance_f = AMapUtils.calculateLineDistance(LatLng(myLoc.latitude,myLoc.longitude), LatLng(poi.latitude,poi.longitude))
//
//                    if (distance_f < 1500) {
//                        nearPlace_layout.visibility = View.VISIBLE
//                        textView_myLoc.text = "当前位置：" + name
//                        isNear = true
//                        textView_myLoc.setOnClickListener {
//                            val intent = Intent(act, PlaceInfoActivity::class.java)
//                            intent.putExtra("objectId",objectId)
//                            startActivity(intent) //启动地图界面
//                        }
//                    }
//
//                    val distance = if (distance_f < 1000) {
//                        val dFormat = DecimalFormat("0")
//                        dFormat.format(distance_f).toString() + "m"
//                    } else {
//                        val dFormat = DecimalFormat(".0")
//                        dFormat.format(distance_f/1000).toString() + "km"
//                    }
//
//
//                    Log.d("OBJECTID",objectId)
//                    Log.d("NAME",name)
//                    Log.d("DESC",desc)
//                    Log.d("MYLOC",myLoc.poiName)
//                    Log.d("DISTANCE",distance)
//
//                    likeList.add(LocItemMin(objectId,name,poi,imgSrc,distance))
//                }
//                if (!isNear) {
//                    nearPlace_layout.visibility = View.GONE
//                }
//
//            }
//        })

    }

    private fun switchSearch(tagId: Int) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("tagId", tagId)
        startActivity(intent) //启动地图界面
    }

    fun testFun(view: View) {
        toast("testFun")
        AVUser.logOut()// 清除缓存用户对象
        AVUser.getCurrentUser()
    }

}
