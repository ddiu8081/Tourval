package site.ddiu.tourval

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
    }

    fun switchGaode (view: View) {
        val intent = Intent(this, GaodeActivity::class.java)
        startActivity(intent) //启动地图界面
    }

    fun switchLayout (view: View) {
        val intent = Intent(this, LayoutTestActivity::class.java)
        startActivity(intent) //启动地图界面
    }

    fun testFun (view: View) {
        toast("fun16带动带7")
    }

}
