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


class MainActivity : AppCompatActivity() {

    val items = listOf(
            "给初学者的RxJava2.0教程（七）: Flowable",
            "Android之View的诞生之谜",
            "Android之自定义View的死亡三部曲之Measure",
            "Using ThreadPoolExecutor in Android ",
            "Kotlin 泛型定义与 Java 类似，但有着更多特性支持。",
            "Android异步的姿势，你真的用对了吗？",
            "Android 高质量录音库。",
            "Android 边缘侧滑效果，支持多种场景下的侧滑退出。"
    )

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

        like_list.layoutManager = LinearLayoutManager(this)
        like_list.adapter = MainAdapter(items)
        initLike()


    }

    fun initLike () {

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
