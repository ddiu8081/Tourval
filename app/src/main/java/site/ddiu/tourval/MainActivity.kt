package site.ddiu.tourval

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toast("sdasd")
    }

    fun switchGaode (view: View) {
        val intent = Intent(this, GaodeActivity::class.java)
//        val editText = findViewById(R.id.editText) as EditText
//        val message = editText.text.toString()
//        intent.putExtra(EXTRA_MESSAGE, message)
        startActivity(intent)
    }
}
