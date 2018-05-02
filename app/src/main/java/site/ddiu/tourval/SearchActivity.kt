package site.ddiu.tourval

import android.content.Context
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
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUIFloatLayout
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import java.util.*
import android.support.v7.widget.DividerItemDecoration
import com.avos.avoscloud.*
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.GetCallback
import com.avos.avoscloud.AVQuery






class SearchActivity : AppCompatActivity() {

    private val userId = AVUser.getCurrentUser().mobilePhoneNumber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标

        val tagId = intent.getIntExtra("tagId",0)

        history_search.gravity = Gravity.LEFT //floatLayout中子节点左对齐
        history_search.maxNumber = Int.MAX_VALUE
        history_search.maxLines = Integer.MAX_VALUE

        initSearchHistory()

        btn_back.setOnClickListener {
            finish()
        }
        btn_search.setOnClickListener {
            val searchText = editText_search.text.toString()
            search(searchText)
        }

        if (tagId > 0) {
            searchTag(tagId)
        }
        else {
            editText_search.isFocusable = true
            editText_search.isFocusableInTouchMode = true
            editText_search.requestFocus()
        }

    }

    private fun initSearchHistory() {
        history_search.removeAllViews()

        val query = AVQuery<AVObject>("SearchLog")
        query.whereEqualTo("user", userId)
        query.limit(10)
        query.orderByDescending("createdAt")
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(list:List<AVObject> , e: AVException?) {
                for (avObject in list) {
                    addItemToFloatLayout(history_search,avObject.getString("searchText"),avObject.objectId)
                }
            }
        })
    }

    fun addItemToFloatLayout(floatLayout: QMUIFloatLayout, itemText:String, objcetId: String) {
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
            search(itemText)
        }
        textView.setOnLongClickListener {
            val fetch = AVObject.createWithoutData("SearchLog", objcetId)
            fetch.fetchInBackground(object : GetCallback<AVObject>() {
                override fun done(avObject: AVObject?, e: AVException?) {

                }
            })
            fetch.deleteInBackground(object : DeleteCallback() {
                override fun done(e: AVException?) {
                    initSearchHistory()
                }
            })
            return@setOnLongClickListener true
        }
    }

    fun search(searchText: String) {
        val searchQueryList:MutableList<MainActivity.LocItem> = ArrayList ()

        if (searchText != "") {
            val query1: AVQuery<AVObject> = AVQuery("SearchLog")
            query1.whereEqualTo("user", userId)
            val query2: AVQuery<AVObject> = AVQuery("SearchLog")
            query2.whereEqualTo("searchText", searchText)
            val query = AVQuery.and(Arrays.asList(query1, query2))
            query.getFirstInBackground(object : GetCallback<AVObject>() {
                override fun done(avObject: AVObject?, e: AVException?) {
                    if (avObject != null) {
                        val fetch = AVObject.createWithoutData("SearchLog", avObject.objectId)
                        fetch.deleteInBackground()
                    }
                    val fav = AVObject("SearchLog")// 构建对象
                    fav.put("user",userId) // 用户手机号
                    fav.put("searchText",searchText) // 搜索内容
                    fav.saveInBackground() // 保存到服务端
                }
            })
        }

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

                    searchQueryList.add(MainActivity.LocItem(objectId, name, desc, poi, distance))
                }
                if (list.isEmpty()) {
                    toast("没有结果")
                    historySearch_view.visibility = View.VISIBLE
                    searchResult_view.visibility = View.GONE

                    initSearchHistory()
                }
                else {
                    toast("搜索到" + list.size + "条结果")
                    historySearch_view.visibility = View.GONE
                    searchResult_view.visibility = View.VISIBLE
                }
                searchResult_list.layoutManager = LinearLayoutManager(act, LinearLayoutManager.VERTICAL,false)
                searchResult_list.adapter = SearchResultAdapter(searchQueryList) {
                    val intent = Intent(act, PlaceInfoActivity::class.java)
                    intent.putExtra("objectId",it.objectId)
                    startActivity(intent) //启动地图界面
                }
                searchResult_list.addItemDecoration(DividerItemDecoration(act, DividerItemDecoration.VERTICAL))
            }
        })
    }

    fun searchTag(tagId: Int) {
        val searchQueryList:MutableList<MainActivity.LocItem> = ArrayList ()

        val query = AVQuery<AVObject>("PlaceInfo")
        query.whereEqualTo("tags", tagId)
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

                    searchQueryList.add(MainActivity.LocItem(objectId, name, desc, poi, distance))
                }
                if (list.isEmpty()) {
                    toast("没有结果")
                    historySearch_view.visibility = View.VISIBLE
                    searchResult_view.visibility = View.GONE

                    initSearchHistory()
                }
                else {
                    toast("搜索到" + list.size + "条结果")
                    historySearch_view.visibility = View.GONE
                    searchResult_view.visibility = View.VISIBLE
                }
                searchResult_list.layoutManager = LinearLayoutManager(act, LinearLayoutManager.VERTICAL,false)
                searchResult_list.adapter = SearchResultAdapter(searchQueryList) {
                    val intent = Intent(act, PlaceInfoActivity::class.java)
                    intent.putExtra("objectId",it.objectId)
                    startActivity(intent) //启动地图界面
                }
                searchResult_list.addItemDecoration(DividerItemDecoration(act, DividerItemDecoration.VERTICAL))
            }
        })
    }
}
