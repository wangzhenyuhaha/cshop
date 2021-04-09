package com.lingmiao.shop.business.main

import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.main.bean.MessageCenter
import com.lingmiao.shop.business.main.presenter.MessageCenterPresenter
import com.lingmiao.shop.business.main.presenter.impl.MessageCenterPresenterImpl
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import java.util.*

/**
 *   首页-消息中心
 */
class MessageCenterActivity : BaseLoadMoreActivity<MessageCenter, MessageCenterPresenter>(),
    MessageCenterPresenter.View {

    override fun initOthers() {
        mToolBarDelegate.setMidTitle("消息中心")
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun createPresenter(): MessageCenterPresenter {
        return MessageCenterPresenterImpl(this, this)
    }

    override fun onMessageCenterSuccess(bean: MessageCenter) {
        hidePageLoading()
    }

    override fun onMessageCenterError(code: Int) {
        hidePageLoading()
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.requestMessageCenterData(page.getPageIndex(), IConstant.PAGE_SIZE)
    }

    override fun initAdapter(): BaseQuickAdapter<MessageCenter, BaseViewHolder> {
        return object :
            BaseQuickAdapter<MessageCenter, BaseViewHolder>(R.layout.main_adapter_message_center) {
            override fun convert(helper: BaseViewHolder, item: MessageCenter) {
                helper.setText(R.id.tvMessageCenterTitle,item.typeText)
                helper.setText(R.id.tvMessageCenterContent,item.noticeContent)
                helper.setText(R.id.tvMessageCenterTime,TimeUtils.date2String(Date(item.sendTime!!*1000L),"yyyy-MM-dd HH:mm"))
            }

        }
    }

}