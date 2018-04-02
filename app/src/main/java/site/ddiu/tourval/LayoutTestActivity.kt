package site.ddiu.tourval

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import java.util.*





class LayoutTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_test)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
    }

    override fun onResume() {
        super.onResume()
        val lv = findViewById<ListView>(R.id.list_view_1)
        val sa:SimpleAdapter = SimpleAdapter(this,getData(),
                R.layout.list_view_item, arrayOf("img","title"), intArrayOf(R.id.image_view_1,R.id.text_view_1))
        lv.adapter = sa
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
