package site.ddiu.tourval

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import org.jetbrains.anko.act
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_login.*
import com.avos.avoscloud.AVException
import com.avos.avoscloud.RequestMobileCodeCallback
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.LogInCallback
import org.jetbrains.anko.toast


class LoginActivity : AppCompatActivity() {

    private var isSentCode = false
    private var user_phone:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标
    }

    fun btnOnClick (view: View) {
        if(!isSentCode) {
            user_phone = editText_phone.text.toString()
            // 请求发送验证码
            AVOSCloud.requestSMSCodeInBackground(user_phone, object : RequestMobileCodeCallback() {
                override fun done(e: AVException?) {
                    if (e == null) {
                        // 弹框提示
                        val tipDialog = QMUITipDialog.Builder(act)

                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord("验证码已发送")
                                .create()
                        tipDialog.show()
                        btn_login.postDelayed({ tipDialog.dismiss() }, 1200)

                        // 更改按钮文字
                        btn_login.text = "进入"

                        // 更改flag
                        code_layout.visibility = View.VISIBLE
                        editText_phone.isEnabled = false
                        isSentCode = true
                    } else {
                        Log.d("SMS", "Send failed!")
                        // 弹框提示
                        val tipDialog = QMUITipDialog.Builder(act)

                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord("验证码发送失败")
                                .create()
                        tipDialog.show()
                        btn_login.postDelayed({ tipDialog.dismiss() }, 1200)

                        // 更改按钮文字
                        btn_login.text = "进入"

                        // 更改flag
                        code_layout.visibility = View.VISIBLE
                        editText_phone.isEnabled = false
                        isSentCode = true
                    }
                }
            })
        }
        else {
            var user_code = editText_code.text.toString()
            AVUser.signUpOrLoginByMobilePhoneInBackground(user_phone, user_code, object : LogInCallback<AVUser>() {
                override fun done(avUser: AVUser?, e: AVException?) {
                    // 更改按钮文字
                    btn_login.text = "下一步"

                    // 更改flag
                    code_layout.visibility = View.GONE
                    editText_phone.isEnabled = true
                    isSentCode = false

                    // 如果 e 为空就可以表示登录成功了，并且 user 是一个全新的用户
                    if (e == null){
                        toast("登录成功")

                        val intent = Intent(act, MainActivity::class.java)
                        intent.putExtra("data","This is from MainActivity.")
                        startActivity(intent) //启动界面

                    }
                    else {
                        // 弹框提示
                        val tipDialog = QMUITipDialog.Builder(act)

                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord("登录失败")
                                .create()
                        tipDialog.show()
                        btn_login.postDelayed({ tipDialog.dismiss() }, 1200)
                    }
                }
            })
        }

    }
}
