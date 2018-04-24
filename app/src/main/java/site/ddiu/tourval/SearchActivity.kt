package site.ddiu.tourval

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import com.avos.avoscloud.FindCallback
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUIFloatLayout
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import java.util.*


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标

        history_search.gravity = Gravity.LEFT //floatLayout中子节点左对齐
        history_search.maxNumber = Int.MAX_VALUE
        history_search.maxLines = Integer.MAX_VALUE
        addItemToFloatLayout(history_search,"好吃不贵")
        addItemToFloatLayout(history_search,"好吃不贵")
        addItemToFloatLayout(history_search,"好吃不贵")
        addItemToFloatLayout(history_search,"好吃不贵")
        addItemToFloatLayout(history_search,"贵")
        addItemToFloatLayout(history_search,"好吃不贵")
        addItemToFloatLayout(history_search,"好吃不贵")
        addItemToFloatLayout(history_search,"好吃不贵")

        btn_back.setOnClickListener {
            finish()
        }

        editText_search.setFocusable(true);
        editText_search.setFocusableInTouchMode(true);
        editText_search.requestFocus()


    }

    fun addItemToFloatLayout(floatLayout: QMUIFloatLayout, itemText:String) {
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

    fun search(view: View) {
        val seacrchQueryList:MutableList<MainActivity.LocItem> = ArrayList ()
        val searchText = editText_search.text.toString()
        val query = AVQuery<AVObject>("PlaceInfo")
        query.whereContains("name", searchText)
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(list:List<AVObject> , e: AVException?) {
                for (avObject in list) {
                    val objectId = avObject.objectId
                    val name = avObject.getString("name")
                    val desc = avObject.getString("desc")
                    val poi = avObject.getAVGeoPoint("location")
                    val distance = ""

                    Log.d("OBJECTID",objectId)
                    Log.d("NAME",name)
                    Log.d("DESC",desc)

                    seacrchQueryList.add(MainActivity.LocItem(objectId, name, desc, poi, distance))
                }
//                like_list.layoutManager = LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL,false)
//                like_list.adapter = MainAdapter(likeList) {
//                    val intent = Intent(act, PlaceInfoActivity::class.java)
//                    intent.putExtra("objectId",it.objectId)
//                    startActivity(intent) //启动地图界面
//                }
            }
        })
    }
}
