package site.ddiu.tourval

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.iflytek.cloud.*
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_place_info.*
import org.jetbrains.anko.act


class PlaceInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_info)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarDarkMode(act) //设置状态栏黑色字体图标

        btn_back.setOnClickListener {
            finish()
        }

        val intent = intent
        val data = intent.getStringExtra("data")
        Log.d("ReceiveData",data)
        textView_placeName.text = data

    }

    fun switchMap (view: View) {
        val intent = Intent(this, GaodeActivity::class.java)
        intent.putExtra("data","This is from MainActivity.")
        startActivity(intent) //启动地图界面
    }

}
