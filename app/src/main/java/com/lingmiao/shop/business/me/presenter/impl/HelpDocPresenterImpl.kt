package com.lingmiao.shop.business.me.presenter.impl

import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.HelpDocItemVo
import com.lingmiao.shop.business.me.bean.HelpDocReq
import com.lingmiao.shop.business.me.bean.HelpDocReqBody
import com.lingmiao.shop.business.me.presenter.HelpDocPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/24:08 PM
Auther      : Fox
Desc        :
 **/
class HelpDocPresenterImpl (val view : HelpDocPresenter.View) : BasePreImpl(view) ,
    HelpDocPresenter {

    override fun queryContentList(page: IPage, type: String, data: MutableList<HelpDocItemVo>) {
        mCoroutine.launch {
            var body = HelpDocReqBody();
            body.type = type;
            var req = HelpDocReq();
            req.body = body;
            req.pageNum = page.getPageIndex();
            req.pageSize = 20;

            val resp = MeRepository.apiService.queryContentList(req).awaitHiResponse();
            if (resp.isSuccess) {
                val list = resp.data.records;
                view.onLoadMoreSuccess(list, resp?.data.pageSize < page.getPageIndex())
            } else {
                view.onLoadMoreFailed()
            }
        }
    }

}