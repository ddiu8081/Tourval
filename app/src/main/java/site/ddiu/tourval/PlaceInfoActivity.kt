package site.ddiu.tourval

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import com.avos.avoscloud.GetCallback
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_place_info.*
import org.jetbrains.anko.act


class PlaceInfoActivity : AppCompatActivity() {

    private var objectId = ""

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
            }
        })

    }

    fun switchMap (view: View) {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("objectId",objectId)
        startActivity(intent) //启动地图界面
    }

}
