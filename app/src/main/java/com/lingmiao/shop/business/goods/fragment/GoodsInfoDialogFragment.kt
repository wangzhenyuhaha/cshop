package com.lingmiao.shop.business.goods.fragment

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.PhotoListAdapter

class GoodsInfoDialogFragment : DialogFragment() {

    companion object {
        fun nesInstance(): GoodsInfoDialogFragment {
            return GoodsInfoDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            //获取View
            val view = requireActivity().layoutInflater.inflate(R.layout.goods_dialog_picture, null)


            //create AlertDialog
            val dialog = AlertDialog.Builder(it)
                .setView(view)
                .create()


            val button: Button = view.findViewById(R.id.goods_dialog_picture_button)
            val textView: TextView = view.findViewById(R.id.goods_dialog_picture_textView)
            val viewPager2 = view.findViewById<ViewPager2>(R.id.goods_dialog_picture_viewpager2)
            val adapter = PhotoListAdapter()

            val list = listOf(
                R.drawable.what_is_goods_message1,
                R.drawable.what_is_goods_message2
            )



            viewPager2.adapter = adapter
            adapter.submitList(
                listOf(
                    R.drawable.what_is_goods_message1,
                    R.drawable.what_is_goods_message2
                )
            )
            viewPager2.apply {
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        val temp = "${position + 1} / ${list.size}"
                        textView.text = temp
                    }
                })
            }

            button.setOnClickListener {
                dialog.dismiss()
            }

            //设置多余的背景为透明
            val window = dialog.window
            window?.setBackgroundDrawable(ColorDrawable(0xffffff))


            dialog
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}