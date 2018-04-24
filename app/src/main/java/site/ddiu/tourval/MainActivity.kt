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


class MainActivity : AppCompatActivity() {

    data class LocItem(val objectId: String, val name: String, val desc: String, val location: AVGeoPoint, val distance: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标

        var kPermission = KPermission(this) // 请求敏感权限
        kPermission.requestPermission(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE), {
            Log.i("kPermission", "isAllow---$it")
        }, {
            Log.i("kPermission", "permission---$it")
        })

        // 用户检测
        val currentUser = AVUser.getCurrentUser()
        if (currentUser != null) {
            // 跳转到首页
            toast("已登录")
        } else {
            //缓存用户对象为空时，可打开用户注册界面…
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("data","This is from MainActivity.")
            startActivity(intent) //启动界面
        }

        GdLocation.getCurrentLocation {
            Log.d("GaodeLocation",it.address)
            val myLoc = it
            initLike(myLoc) // 初始化 猜你喜欢 栏目
            textView_myLoc.text = "当前位置：" + it.address
        }

        initTags() // 初始化 热门标签 栏目


    }

    private fun addItemToFloatLayout(floatLayout:QMUIFloatLayout, itemText:String) {
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
            toast(textView.text)
        }
    }

    private fun initTags () {
        qmuidemo_floatlayout.gravity = Gravity.LEFT //floatLayout中子节点左对齐
        qmuidemo_floatlayout.maxNumber = Int.MAX_VALUE
        qmuidemo_floatlayout.maxLines = Integer.MAX_VALUE
        addItemToFloatLayout(qmuidemo_floatlayout,"地铁沿线")
        addItemToFloatLayout(qmuidemo_floatlayout,"山水")
        addItemToFloatLayout(qmuidemo_floatlayout,"自然")
        addItemToFloatLayout(qmuidemo_floatlayout,"人文")
        addItemToFloatLayout(qmuidemo_floatlayout,"历史")
        addItemToFloatLayout(qmuidemo_floatlayout,"春游景点")
    }

    private fun initLike (myLoc: AMapLocation) {
        val likeList:MutableList<LocItem> = ArrayList ()

        // 查询
        val query: AVQuery<AVObject> = AVQuery("PlaceInfo")
        query.whereNotEqualTo("name", "111")
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(list:List<AVObject>, e: AVException?) {
                for (avObject in list) {
                    val objectId = avObject.objectId
                    val name = avObject.getString("name")
                    val desc = avObject.getString("desc")
                    val poi = avObject.getAVGeoPoint("location")
//                    val distance = "12345"
                    val distance_f = AMapUtils.calculateLineDistance(LatLng(myLoc.latitude,myLoc.longitude), LatLng(poi.latitude,poi.longitude))

                    val distance = if (distance_f < 1000) {
                        val dFormat = DecimalFormat("0")
                        dFormat.format(distance_f).toString() + "m"
                    } else {
                        val dFormat = DecimalFormat(".0")
                        dFormat.format(distance_f/1000).toString() + "km"
                    }


                    Log.d("OBJECTID",objectId)
                    Log.d("NAME",name)
                    Log.d("DESC",desc)
                    Log.d("MYLOC",myLoc.poiName)
                    Log.d("DISTANCE",distance)

                    likeList.add(LocItem(objectId,name,desc,poi,distance))
                }
                like_list.layoutManager = LinearLayoutManager(act,LinearLayoutManager.HORIZONTAL,false)
                like_list.adapter = MainAdapter(likeList) {
                    val intent = Intent(act, PlaceInfoActivity::class.java)
                    intent.putExtra("objectId",it.objectId)
                    startActivity(intent) //启动地图界面
                }
            }
        })

    }

    fun switchSearch(view: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent) //启动地图界面
    }

    fun testFun(view: View) {
        toast("testFun")
        AVUser.logOut()// 清除缓存用户对象
        AVUser.getCurrentUser()
    }

}
