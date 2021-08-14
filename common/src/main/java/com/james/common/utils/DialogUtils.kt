package com.james.common.utils

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.blankj.utilcode.util.ToastUtils
import com.james.common.R
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.isNotBlank
import com.james.common.utils.exts.show


/**
 * 对话框工具类
 */
class DialogUtils {
    companion object {

        fun showDialog(
            context: Activity,
            title: String,
            content: String,
            leftClick: View.OnClickListener?,
            rightClick: View.OnClickListener?
        ) {
            showDialog(context, title, content, "取消", "确定", leftClick, rightClick)
        }


        fun showDialog(
            context: Activity,
            title: String,
            content: String,
            left: String,
            right: String,
            leftClick: View.OnClickListener?,
            rightClick: View.OnClickListener?
        ) {
            val dialog = AppCompatDialog(context, R.style.TransparentDialog)
            val defaultView =
                View.inflate(context, R.layout.dialog_common, null)
            dialog.setContentView(defaultView)
            val tvTitle = defaultView.findViewById<TextView>(R.id.tvTitle)
            val tvContent = defaultView.findViewById<TextView>(R.id.tvContent)
            val tvLeft = defaultView.findViewById<TextView>(R.id.tvLeft)
            val tvRight = defaultView.findViewById<TextView>(R.id.tvRight)
            tvTitle.text = title
            tvContent.text = content
            tvLeft.text = left
            tvRight.text = right
            tvLeft.setOnClickListener { v ->
                dialog.dismiss()
                leftClick?.onClick(v)
            }
            tvRight.setOnClickListener { v ->
                dialog.dismiss()
                rightClick?.onClick(v)
            }
            dialog.show()
        }

        // 展示1个带输入框的对话框
        fun showInputDialog(
            context: Activity,
            title: String,
            leftContent: String,
            leftClick: (() -> Unit)?,
            rightClick: ((String) -> Unit)?
//            leftClick: View.OnClickListener?,
//            rightClick: View.OnClickListener?
        ) {
            showInputDialog(
                context,
                title,
                leftContent,
                "请输入",
                null,
                "取消",
                "确定",
                leftClick,
                rightClick
            )
        }


        fun showInputDialog(
            context: Activity, title: String,
            leftContent: String, edtHint: String?,
            left: String, right: String,
            leftClick: (() -> Unit)?,
            rightClick: ((String) -> Unit)?
        ) {
            showInputDialog(
                context,
                title,
                leftContent,
                edtHint,
                null,
                left,
                right,
                leftClick,
                rightClick
            )
        }

        fun showInputDialog(
            value : String?,
            context: Activity, title: String,
            leftContent: String, edtHint: String?,
            edtText: String?,
            left: String, right: String,
            leftClick: (() -> Unit)?,
            rightClick: ((String) -> Unit)?
        ) {
            val dialog = AppCompatDialog(context, R.style.TransparentDialog)
            val defaultView =
                View.inflate(context, R.layout.dialog_common_input, null)
            dialog.setContentView(defaultView)
            val tvTitle = defaultView.findViewById<TextView>(R.id.tvTitle)
            val tvLeftContent = defaultView.findViewById<TextView>(R.id.tvLeftContent)
            val etRightContent = defaultView.findViewById<EditText>(R.id.etRightContent)
            val tvLeft = defaultView.findViewById<TextView>(R.id.tvLeft)
            val tvRight = defaultView.findViewById<TextView>(R.id.tvRight)
            tvTitle.text = title
            tvLeftContent.show(leftContent.isNotBlank())
            tvLeftContent.text = leftContent

            etRightContent.hint = edtHint
            etRightContent.setText(value)
            if (edtText != null) etRightContent.setText(edtText)
            tvLeft.text = left
            tvRight.text = right
            tvLeft.setOnClickListener { v ->
                dialog.dismiss()
                leftClick?.invoke()
            }
            tvRight.setOnClickListener { v ->
                if (TextUtils.isEmpty(etRightContent.getViewText())) {
                    ToastUtils.showShort("内容不能为空")
                    return@setOnClickListener
                }
                dialog.dismiss()
                v.tag = etRightContent.text.toString()
                rightClick?.invoke(etRightContent.getViewText())
            }
            dialog.show()
        }

        fun showInputDialog(
            context: Activity, title: String,
            leftContent: String, edtHint: String?,
            edtText: String?,
            left: String, right: String,
            leftClick: (() -> Unit)?,
            rightClick: ((String) -> Unit)?
        ) {
            val dialog = AppCompatDialog(context, R.style.TransparentDialog)
            val defaultView =
                View.inflate(context, R.layout.dialog_common_input, null)
            dialog.setContentView(defaultView)
            val tvTitle = defaultView.findViewById<TextView>(R.id.tvTitle)
            val tvLeftContent = defaultView.findViewById<TextView>(R.id.tvLeftContent)
            val etRightContent = defaultView.findViewById<EditText>(R.id.etRightContent)
            val tvLeft = defaultView.findViewById<TextView>(R.id.tvLeft)
            val tvRight = defaultView.findViewById<TextView>(R.id.tvRight)
            tvTitle.text = title
            tvLeftContent.show(leftContent.isNotBlank())
            tvLeftContent.text = leftContent

            etRightContent.hint = edtHint
            if (edtText != null) etRightContent.setText(edtText)
            tvLeft.text = left
            tvRight.text = right
            tvLeft.setOnClickListener { v ->
                dialog.dismiss()
                leftClick?.invoke()
            }
            tvRight.setOnClickListener { v ->
                if (TextUtils.isEmpty(etRightContent.getViewText())) {
                    ToastUtils.showShort("内容不能为空")
                    return@setOnClickListener
                }
                dialog.dismiss()
                v.tag = etRightContent.text.toString()
                rightClick?.invoke(etRightContent.getViewText())
            }
            dialog.show()
        }

        fun showMultInputDialog(
            context: Activity, title: String,
            leftContent: String, edtHint: String?,
            left: String, right: String,
            leftClick: (() -> Unit)?,
            rightClick: ((String) -> Unit)?
        ) {
            val dialog = AppCompatDialog(context, R.style.TransparentDialog)
            val defaultView =
                View.inflate(context, R.layout.dialog_common_mult_input, null)
            dialog.setContentView(defaultView)
            val tvTitle = defaultView.findViewById<TextView>(R.id.tvTitle)
            val tvLeftContent = defaultView.findViewById<TextView>(R.id.tvLeftContent)
            val etRightContent = defaultView.findViewById<EditText>(R.id.etRightContent)
            val tvLeft = defaultView.findViewById<TextView>(R.id.tvLeft)
            val tvRight = defaultView.findViewById<TextView>(R.id.tvRight)
            tvTitle.text = title

            etRightContent.hint = edtHint
            tvLeft.text = left
            tvRight.text = right
            tvLeft.setOnClickListener { v ->
                dialog.dismiss()
                leftClick?.invoke()
            }
            tvRight.setOnClickListener { v ->
                if (TextUtils.isEmpty(etRightContent.text.toString())) {
                    ToastUtils.showShort("内容不能为空")
                    return@setOnClickListener
                }
                dialog.dismiss()
                v.tag = etRightContent.text.toString()
                rightClick?.invoke(etRightContent.text.toString())
            }
            dialog.show()
        }

        fun showVersionUpdateDialog(
            context: Activity,
            title: String,
            content: String,
            leftClick: View.OnClickListener?,
            rightClick: View.OnClickListener?,
            force:Boolean = false
        ): AppCompatDialog {
            val dialog = AppCompatDialog(context, R.style.TransparentDialog)
            val defaultView =
                View.inflate(context, R.layout.dialog_common, null)
            dialog.setContentView(defaultView)
            val tvTitle = defaultView.findViewById<TextView>(R.id.tvTitle)
            val tvContent = defaultView.findViewById<TextView>(R.id.tvContent)
            val tvLeft = defaultView.findViewById<TextView>(R.id.tvLeft)
            val tvRight = defaultView.findViewById<TextView>(R.id.tvRight)
            val viBottomLine = defaultView.findViewById<View>(R.id.viBottomLine)
            tvTitle.text = title
            tvContent.text = content
            tvLeft.text = "取消"
            dialog.setCanceledOnTouchOutside(false)
            if(force){
                tvLeft.gone()
                viBottomLine.gone()
            }
            tvRight.text = "确定"
            tvLeft.setOnClickListener { v ->
                dialog.dismiss()
                leftClick?.onClick(v)
            }
            tvRight.setOnClickListener { v ->
                dialog.dismiss()
                rightClick?.onClick(v)
            }
            dialog.show()
            return dialog
        }


        fun showDialog(
            context: Activity,
            imageInt : Int
        ) {
            val dialog = AppCompatDialog(context, R.style.TransparentDialog)
            val defaultView =
                View.inflate(context, R.layout.dialog_image, null)
            dialog.setContentView(defaultView)
            val image = defaultView.findViewById<ImageView>(R.id.ivTipsImage)
            image?.setImageResource(imageInt);
            dialog.show()
        }


        fun showImageDialog(
            context: Activity,
            imageInt : Int,
            hint: String
        ) {
            val dialog = AppCompatDialog(context, R.style.TransparentDialog)
            val defaultView =
                View.inflate(context, R.layout.dialog_image_hint, null)
            dialog.setContentView(defaultView)
            val image = defaultView.findViewById<ImageView>(R.id.ivTipsImage)
            image?.setImageResource(imageInt);
            image.setOnClickListener {

            }
            val tvTipsHint = defaultView.findViewById<TextView>(R.id.tvTipsHint);
            tvTipsHint.text = hint;
            dialog.show()
        }

    }

}