package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import com.blankj.utilcode.util.ResourceUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import  com.lingmiao.shop.business.main.presenter.ApplyShopAddressPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.presenter.impl.PopAddressPreImpl
import com.lingmiao.shop.business.tools.bean.RegionVo
import kotlinx.coroutines.launch

class ApplyShopAddressPresenterImpl(val context: Context, private var view: ApplyShopAddressPresenter.View) :
    BasePreImpl(view), ApplyShopAddressPresenter{

	var mList : List<RegionVo> = listOf();

	private val address : PopAddressPreImpl by lazy { PopAddressPreImpl(view); };
	override fun requestApplyShopAddressData(){
		//		mCoroutine.launch {
		//			val resp = MainRepository.apiService.getApplyShopAddress().awaitHiResponse()
		//			if (resp.isSuccess) {
		//				view.onApplyShopAddressSuccess(resp.data)
		//			}else{
		//				view.onApplyShopAddressError(resp.code)
		//			}
		//
		//		}
	}

	override fun showAddress(layout : android.view.View, old : List<RegionVo>?) {
//		val regionsType = object : TypeToken<List<RegionVo>>(){}.type;
//		var json = Gson().fromJson<MutableList<RegionVo>>(ResourceUtils.readAssets2String("areas.json"), regionsType);


		if(mList == null || mList.size == 0) {
			mCoroutine.launch {
				view?.showDialogLoading()
				val regionsType = object : TypeToken<List<RegionVo>>(){}.type;
				mList = Gson().fromJson<MutableList<RegionVo>>(ResourceUtils.readAssets2String("areas.json"), regionsType);
				view?.hideDialogLoading();
				show(layout, old);
			}
		} else {
			show(layout, old);
		}
	}

	fun show(layout : android.view.View,old : List<RegionVo>?) {
		address.showTypePop(context, layout, mList, old) { str, list ->
			if(list == null || list.size ==0) {
				return@showTypePop;
			}
			view.onSetAddress(list);
		}
	}
}