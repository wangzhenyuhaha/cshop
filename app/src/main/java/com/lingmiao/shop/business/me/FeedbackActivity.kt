package com.lingmiao.shop.business.me

import android.view.View
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.presenter.FeedbackPresenter
import com.lingmiao.shop.business.me.presenter.impl.FeedbackPresenterImpl
import kotlinx.android.synthetic.main.me_activity_feedback.*

/**
 *   建议反馈
 */
class FeedbackActivity : BaseActivity<FeedbackPresenter>(), FeedbackPresenter.View,
    View.OnClickListener {

    companion object {

        //软件问题
        const val APP_BUG = 1

        //需求建议
        const val BIZ_REQUIRE = 2

        //业务问题
        const val BIZ_PROBLEM = 3

        //其他问题
        const val OTHERS = 4

    }

    //当前选中的问题类型
    private var current = 0

    override fun getLayoutId() = R.layout.me_activity_feedback

    override fun useLightMode() = false

    private var nowWeChat: String? = null


    override fun initView() {

        //标题
        mToolBarDelegate.setMidTitle("建议反馈")
        //默认选中软件问题
        llFeedbackSoftQuestion.isSelected = true

        //软件问题
        llFeedbackSoftQuestion.setOnClickListener(this)

        //需求建议
        llFeedbackFeedback.setOnClickListener(this)

        //业务问题
        llFeedbackBusiness.setOnClickListener(this)

        //其他问题
        llFeedbackOther.setOnClickListener(this)

        //查看二维码
        //   feedback_wechat.setOnClickListener(this)

        //提交反馈
        tvFeedbackSubmit.setOnClickListener(this)

        //设置最大图片数量
        galleryRv.setCountLimit(1, 8)

        //获取企业微信
        mPresenter.getCompanyWeChat()
    }


    override fun createPresenter() = FeedbackPresenterImpl(this, this)


    override fun onFeedbackSuccess() {
        hideDialogLoading()
        showToast("反馈成功")
        finish()
    }

    override fun onFeedbackError() {
        hideDialogLoading()
        showToast("反馈失败")
    }

    override fun onSuccessWeChat() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //软件问题
            R.id.llFeedbackSoftQuestion -> {
                changeSelectedType(APP_BUG)
            }
            //需求建议
            R.id.llFeedbackFeedback -> {
                changeSelectedType(BIZ_REQUIRE)
            }
            //业务问题
            R.id.llFeedbackBusiness -> {
                changeSelectedType(BIZ_PROBLEM)
            }
            //其他问题
            R.id.llFeedbackOther -> {
                changeSelectedType(OTHERS)
            }
            //查看二维码
//            R.id.feedback_wechat -> {
//                DialogUtils.showDialog(context, R.mipmap.goods_selected_img)
//            }
            R.id.tvFeedbackSubmit -> {
                if (etFeedbackContent.text.toString().isEmpty()) {
                    showToast("请输入要反馈的内容")
                    return
                }
                //在这里需要上传图片，成功后方可继续提交反馈
                mPresenter.requestFeedbackData(
                    current,
                    etFeedbackContent.text.toString(),
                    galleryRv.getSelectPhotos()
                )
            }
        }
    }

    private fun changeSelectedType(position: Int) {
        llFeedbackSoftQuestion.isSelected = position == APP_BUG
        llFeedbackFeedback.isSelected = position == BIZ_REQUIRE
        llFeedbackBusiness.isSelected = position == BIZ_PROBLEM
        llFeedbackOther.isSelected = position == OTHERS
        current = position
    }

}