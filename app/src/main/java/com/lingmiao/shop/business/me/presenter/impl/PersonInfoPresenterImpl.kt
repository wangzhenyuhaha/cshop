package com.lingmiao.shop.business.me.presenter.impl

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.PersonInfo

import android.content.Context
import com.blankj.utilcode.util.GsonUtils
import  com.lingmiao.shop.business.me.presenter.PersonInfoPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.util.OtherUtils
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class PersonInfoPresenterImpl(context: Context, private var view: PersonInfoPresenter.View) :
    BasePreImpl(view), PersonInfoPresenter {
    override fun requestPersonInfoData(request: PersonInfoRequest) {
        mCoroutine.launch {
            try {
                val map = OtherUtils.tranPersonBeanToMap(request)
                val resp = MeRepository.apiService.updatePersonInfo(request.id ?: "", map)
                if (resp.isSuccessful) {
                    view.onPersonInfoSuccess()
                } else {
                    view.onPersonInfoError()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }


}