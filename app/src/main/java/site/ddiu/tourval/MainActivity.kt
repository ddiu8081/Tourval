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
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUIFloatLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import java.util.*


class MainActivity : AppCompatActivity() {

    data class LocItem(val _id: String,
                        val desc: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标

        var kPermission: KPermission = KPermission(this) // 请求敏感权限
        kPermission.requestPermission(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), {
            Log.i("kPermission", "isAllow---$it")
        }, {
            Log.i("kPermission", "permission---$it")
        })

        initLike()

        qmuidemo_floatlayout.gravity = Gravity.LEFT //floatLayout中子节点左对齐
        qmuidemo_floatlayout.maxNumber = Int.MAX_VALUE
        qmuidemo_floatlayout.maxLines = Integer.MAX_VALUE
        addItemToFloatLayout(qmuidemo_floatlayout,"好吃不贵")
        addItemToFloatLayout(qmuidemo_floatlayout,"离地铁线近")
        addItemToFloatLayout(qmuidemo_floatlayout,"号")
        addItemToFloatLayout(qmuidemo_floatlayout,"哈哈哈哈哈")
        addItemToFloatLayout(qmuidemo_floatlayout,"啊啊")
    }

    fun addItemToFloatLayout(floatLayout:QMUIFloatLayout, itemText:String) {
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

    override fun onStart() {
        super.onStart()
//        val oa:ObjectAnimator = ObjectAnimator.ofObject(btn_switch,"Text",StringEvaluator(),"1111111","222222","333333333")
//        oa.duration = 5000
//        oa.repeatCount = 2
//        oa.repeatMode = ValueAnimator.RESTART
//        oa.interpolator = MyInterplator()
////        oa.addUpdateListener(ValueAnimator.AnimatorUpdateListener {
////            btn_switch.text = it.getAnimatedValue().toString()
////        })
//
//        oa.start()
    }

    private fun initLike () {
        val list:MutableList<LocItem> = ArrayList ()
        list.add(LocItem("玄武湖","这是第一条介绍"))
        list.add(LocItem("中山陵","这是第二条介绍"))
        list.add(LocItem("夫子庙","这是第三条介绍"))
        list.add(LocItem("新街口","这是第四条介绍"))

        like_list.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        like_list.adapter = MainAdapter(list) {
            val intent = Intent(this, PlaceInfoActivity::class.java)
            intent.putExtra("data",it._id)
            startActivity(intent) //启动地图界面
        }
    }

    fun switchSearch (view: View) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("data","This is from MainActivity.")
        startActivity(intent) //启动地图界面
    }

    fun testFun (view: View) {
        toast("testFun")
    }

}
