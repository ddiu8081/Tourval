package site.ddiu.tourval

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.avos.avoscloud.*
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_place_info.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import java.util.*
import com.avos.avoscloud.AVException




class PlaceInfoActivity : AppCompatActivity() {

    private var objectId = ""
    private val userId = AVUser.getCurrentUser().mobilePhoneNumber
    private val userObjectId = AVUser.getCurrentUser().objectId
    var favLogId = ""
    var isFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_info)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarDarkMode(act) //设置状态栏黑色字体图标

        btn_back.setOnClickListener {
            finish()
        }

        val intent = intent
        objectId = intent.getStringExtra("objectId")


        val avQuery = AVQuery<AVObject>("PlaceInfo")
        avQuery.getInBackground(objectId, object : GetCallback<AVObject>() {
            override fun done(avObject: AVObject, e: AVException?) {
                textView_placeName.text = avObject.getString("name")
                textView_placeDesc.text = avObject.getString("desc")
                Picasso.get().load(avObject.getString("imgSrc")).into(imageView_placePic)
            }
        })

        getFavoriteState(objectId, userId)

    }

    fun switchMap (view: View) {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("objectId",objectId)
        startActivity(intent) //启动地图界面
    }

    fun favoriteThis (view: View) {

        if (isFav) {
            // 用户已经收藏，进行取消收藏操作
            if (favLogId != "") {
                val fav = AVObject.createWithoutData("FavoriteLog", favLogId)
                fav.deleteInBackground(object : DeleteCallback() {
                    override fun done(e: AVException?) {
                        if (e == null) {
                            isFav = false
                            btn_isFavPlace.text = "收藏"
                            btn_isFavPlace.setBackgroundColor(Color.parseColor("#eeffc408"))
                            toast("已取消收藏")
                        } else {
                            toast("错误")
                        }
                    }
                })
            }
            else {
                toast("失败")
            }

        }
        else {
            // 用户没有收藏，进行收藏操作

            val fav = AVObject("FavoriteLog")
            fav.put("type", 1) // 设置类型，1为place，2为POI
            fav.put("favId", objectId) // 景点id
            fav.put("user",userId) // 用户手机号
            fav.put("userObjectId",userObjectId) // 用户objectId
            fav.isFetchWhenSave = true
            fav.saveInBackground(object : SaveCallback() {
                override fun done(e: AVException?) {
                    if (e == null) {
                        isFav = true
                        favLogId = fav.objectId
                        btn_isFavPlace.text = "已收藏"
                        btn_isFavPlace.setBackgroundColor(Color.parseColor("#666666"))
                        toast("已收藏")
                    } else {
                        toast("错误")
                    }
                }
            })
        }
    }

    private fun getFavoriteState (thisObjectId: String, userId: String) {
        val query1: AVQuery<AVObject> = AVQuery("FavoriteLog")
        query1.whereEqualTo("favId", thisObjectId)
        val query2: AVQuery<AVObject> = AVQuery("FavoriteLog")
        query2.whereEqualTo("user", userId)
        val query = AVQuery.and(Arrays.asList(query1, query2))
        query.getFirstInBackground(object : GetCallback<AVObject>() {
            override fun done(avObject: AVObject?, e: AVException?) {
                if (avObject != null) {
                    favLogId = avObject.objectId
                    Log.d("favLogId",favLogId)
                    isFav = true
                    btn_isFavPlace.text = "已收藏"
                    btn_isFavPlace.setBackgroundColor(Color.parseColor("#666666"))
                }
                else {
                    isFav = false
                    btn_isFavPlace.text = "收藏"
                    btn_isFavPlace.setBackgroundColor(Color.parseColor("#eeffc408"))
                }
            }
        })
        Log.d("当前收藏状态",isFav.toString())
    }

}
