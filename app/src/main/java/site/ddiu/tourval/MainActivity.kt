package site.ddiu.tourval

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import cn.hchstudio.kpermissions.KPermission
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import java.util.ArrayList


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


    }

    private fun initLike () {
        val list:MutableList<LocItem> = ArrayList ()
        list.add(LocItem("123","给初学者的RxJava2.0教程: Flowable"))
        list.add(LocItem("124","Using ThreadPoolExecutor in Android"))
        list.add(LocItem("125","Android 高质量录音库。"))
        list.add(LocItem("126","Android异步的姿势，你真的用对了吗？"))

        like_list.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        like_list.adapter = MainAdapter(list) {
            toast(it.desc)
        }
    }

    fun switchGaode (view: View) {
        val intent = Intent(this, GaodeActivity::class.java)
        intent.putExtra("data","This is from MainActivity.")
        startActivity(intent) //启动地图界面
    }

    fun switchLayout (view: View) {
        val intent = Intent(this, LayoutTestActivity::class.java)
        startActivity(intent) //启动地图界面
    }

    fun testFun (view: View) {
        toast("testFun")
    }

}
