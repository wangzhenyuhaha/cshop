package com.lingmiao.shop.business.common.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R

class DialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        Log.d("WZYDDU", "这个Activity打开了")
        DialogUtils.showDialog(this, "测试", "乌拉", null, null)

        window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
    }
}