package site.ddiu.tourval

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.avos.avoscloud.AVCloud
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVGeoPoint
import com.avos.avoscloud.FunctionCallback
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_all_like_list.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import java.math.BigDecimal
import java.util.ArrayList

class AllLikeListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_like_list)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标

        btn_back2.setOnClickListener {
            finish()
        }

        fetchList()
    }

    private fun fetchList () {
        val allLikeList:MutableList<MainActivity.LocItem> = ArrayList()
        // 拉取列表
        AVCloud.callFunctionInBackground("getLikeList", null, object : FunctionCallback<ArrayList<HashMap<String, Any>>>() {
            override fun done(data: ArrayList<HashMap<String, Any>>?, e: AVException?) {
                if (e == null) {
                    // 处理返回结果
                    Log.d("avObject in likeList",data.toString())
                    if (data != null) {
                        data.forEach {
                            Log.d("avObject in key",it.toString())

                            val objectId = it["id"] as String
                            val name = it["name"] as String
                            val imgSrc = it["imgSrc"] as String
                            val poi= it["location"] as AVGeoPoint
                            val similarity= it["similarity"] as BigDecimal
                            val similarity_f = similarity.toFloat()

                            allLikeList.add(MainActivity.LocItem(objectId, name, "", poi, imgSrc, "", similarity_f))
                        }
                        RecyclerView_allLikeList.layoutManager = LinearLayoutManager(act, LinearLayoutManager.VERTICAL,false)
                        RecyclerView_allLikeList.adapter = AllLikeListAdapter(allLikeList) {
                            val intent = Intent(act, PlaceInfoActivity::class.java)
                            intent.putExtra("objectId",it.objectId)
                            startActivity(intent) //启动地图界面
                        }
                        RecyclerView_allLikeList.addItemDecoration(DividerItemDecoration(act, DividerItemDecoration.VERTICAL))
                    }
                } else {
                    // 处理报错
                    toast("拉取推荐列表错误")
                    Log.d("avObject in error",e.toString())
                }
            }
        })

    }
}
