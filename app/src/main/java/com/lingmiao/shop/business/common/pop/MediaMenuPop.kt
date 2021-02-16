package com.lingmiao.shop.business.common.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import com.lingmiao.shop.R
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import com.james.common.utils.exts.show
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 相册、视频选择
 */
class MediaMenuPop(context: Context, var flags: Int = TYPE_SELECT_PHOTO) :
    BaseLazyPopupWindow(context) {

    companion object {
        const val TYPE_SELECT_VIDEO = 0x001
        const val TYPE_SELECT_PHOTO = 0x002
        const val TYPE_PLAY_PHOTO = 0x004
    }

    private var selectVideoLl: LinearLayout? = null
    private var selectPhotoLl: LinearLayout? = null
    private var playPhotoTv: TextView? = null

    private var listener: ((Int) -> Unit)? = null

    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.common_pop_media)
    }

    override fun onViewCreated(contentView: View) {
        // 显示的位置
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        selectVideoLl = contentView.findViewById<LinearLayout>(R.id.selectVideoLl).apply {
            show(flags and TYPE_SELECT_VIDEO != 0)
            setOnClickListener {
                listener?.invoke(TYPE_SELECT_VIDEO)
                dismiss()
            }
        }
        selectPhotoLl = contentView.findViewById<LinearLayout>(R.id.selectPhotoLl).apply {
            show(flags and TYPE_SELECT_PHOTO != 0)
            setOnClickListener {
                listener?.invoke(TYPE_SELECT_PHOTO)
                dismiss()}
        }
        playPhotoTv = contentView.findViewById<TextView>(R.id.playPhotoTv).apply {
            show(flags and TYPE_PLAY_PHOTO != 0)
            setOnClickListener {
                listener?.invoke(TYPE_PLAY_PHOTO)
                dismiss()
            }
        }
        contentView.findViewById<TextView>(R.id.cancelTv).apply {
            setOnClickListener { dismiss() }
        }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }
}