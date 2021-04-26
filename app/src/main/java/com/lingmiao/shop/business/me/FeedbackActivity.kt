package com.lingmiao.shop.business.me

import android.view.View
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.bean.Feedback
import com.lingmiao.shop.business.me.presenter.FeedbackPresenter
import com.lingmiao.shop.business.me.presenter.impl.FeedbackPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import kotlinx.android.synthetic.main.me_activity_feedback.*

/**
*   建议反馈
*/
class FeedbackActivity : BaseActivity<FeedbackPresenter>(),FeedbackPresenter.View,
    View.OnClickListener {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }
    private var current = 0

//    /**     * 软件问题      */
//    val APP_BUG = 1
//
//
//    /**     * 需求建议      */
//    val BIZ_REQUIRE = 2
//
//
//    /**     * 业务问题      */
//    val BIZ_PROBLEM = 3
//
//
//    /**     * 其他问题      */
//    val OTHERS = 4

    override fun getLayoutId(): Int {
        return R.layout.me_activity_feedback
    }

    override fun useLightMode(): Boolean {
        return false
    }


    override fun initView() {
        mToolBarDelegate.setMidTitle("建议反馈")
        // mToolBarDelegate.setToolbarBackgroundOfOtherTheme(context!!, R.color.color_secondary, R.color.white);
        llFeedbackSoftQuestion.isSelected = true
//        showPageLoading()
//
//        mPresenter.requestFeedbackData()
        llFeedbackSoftQuestion.setOnClickListener(this)
        llFeedbackFeedback.setOnClickListener(this)
        llFeedbackBusiness.setOnClickListener(this)
        llFeedbackOther.setOnClickListener(this)
        tvFeedbackSubmit.setOnClickListener(this)
    }



    override fun createPresenter(): FeedbackPresenter {
        return FeedbackPresenterImpl(this, this)
    }

    override fun onFeedbackSuccess() {
        hideDialogLoading()
        showToast("反馈成功")
        finish()
    }

    override fun onFeedbackError() {
        hideDialogLoading()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.llFeedbackSoftQuestion->{changeSelectedType(0)}
            R.id.llFeedbackFeedback->{changeSelectedType(1)}
            R.id.llFeedbackBusiness->{changeSelectedType(2)}
            R.id.llFeedbackOther->{changeSelectedType(3)}
            R.id.tvFeedbackSubmit->{
                if(etFeedbackContent.text.toString().isEmpty()){
                    showToast("请输入要反馈的内容")
                    return
                }
                showDialogLoading()
                mPresenter.requestFeedbackData(current+1,etFeedbackContent.text.toString())
            }
        }
    }

    private fun changeSelectedType(position:Int){
        llFeedbackSoftQuestion.isSelected = position==0
        llFeedbackFeedback.isSelected = position==1
        llFeedbackBusiness.isSelected = position==2
        llFeedbackOther.isSelected = position==3
        current = position
    }

}