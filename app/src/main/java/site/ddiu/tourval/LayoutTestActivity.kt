package site.ddiu.tourval

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.SimpleAdapter
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_layout_test.*
import org.jetbrains.anko.toast
import java.util.*





class LayoutTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_test)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏

        val lv = findViewById<ListView>(R.id.list_view_1)
        lv.setOnClickListener {
            toast("You Clicked the item.")
        }

        button2.setOnClickListener {
            toast("hjhkj")
        }

    }

    override fun onResume() {
        super.onResume()
    }

    private fun getData():List<Map<String,Any>> {
        val list:MutableList<Map<String,Any>> = ArrayList ()
        var map = HashMap<String, Any>()
        map.put("img",R.drawable.lx)
        map.put("title","ghjghjjh")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.test)
        map.put("title","fghjklghuyioilknb")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.lx)
        map.put("title","ghjghjjh")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.lx)
        map.put("title","ghjghjjh")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.lx)
        map.put("title","ghjghjjh")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.test)
        map.put("title","fghjklghuyioilknb")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.lx)
        map.put("title","ghjghjjh")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.lx)
        map.put("title","ghjghjjh")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.lx)
        map.put("title","ghjghjjh")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.lx)
        map.put("title","ghjghjjh")
        list.add(map)

        map = HashMap<String, Any>()
        map.put("img",R.drawable.lx)
        map.put("title","ghjghjjh")
        list.add(map)

        return list
    }
}
