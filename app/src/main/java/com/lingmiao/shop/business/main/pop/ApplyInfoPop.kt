package com.lingmiao.shop.business.main.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.adapter.ApplyInfoAdapter
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BasePopupWindow

class ApplyInfoPop(context: Context) :
    BasePopupWindow(context) {

    var liveData: MutableLiveData<Int>? = null

    //顶端的标题
    private var titleTv: TextView? = null

    //顶端的取消按钮
    private var closeIv: ImageView? = null

    //RecyclerView
    private var recyclerView: RecyclerView? = null

    //定义Adapter
    private var mAdapter: ApplyInfoAdapter? = null

    private var type: Int = 0

    fun getType(): Int {
        return type
    }

    fun setType(needType: Int) {
        type = needType
    }

    //设置数据
    fun setList(list: List<String>) {
        mAdapter?.submitList(list)
    }

    //设置标题
    fun setTitle(title: String) {
        titleTv?.text = title
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.main_pop_apply_info)
    }

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        super.onViewCreated(contentView)
        //标题
        titleTv = contentView.findViewById(R.id.mpai_tvTitle)
        //取消按钮
        closeIv = contentView.findViewById(R.id.mpai_closeIv)
        recyclerView = contentView.findViewById(R.id.mpai_recyclerView)

        val testLiveData: MutableLiveData<Int> = MutableLiveData()
        //自定义Adapter
        mAdapter = ApplyInfoAdapter(testLiveData)

        liveData = testLiveData

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        closeIv?.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateShowAnimation(): Animation {
        return showYTranslateAnim(300)
    }

    override fun getDismissAnimation(): Animation {
        return hideYTranslateAnim(300)
    }

}