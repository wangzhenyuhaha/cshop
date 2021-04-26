package com.lingmiao.shop.business.me

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils
import com.lingmiao.shop.R
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.business.me.bean.VipType
import com.lingmiao.shop.business.me.presenter.ApplyVipPresenter
import com.lingmiao.shop.business.me.presenter.impl.ApplyVipPreImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.me_activity_apply_vip.*

/**
*   开通会员
*/
class ApplyVipActivity : BaseActivity<ApplyVipPresenter>(),ApplyVipPresenter.View {

    override fun getLayoutId(): Int {
        return R.layout.me_activity_apply_vip
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("开通会员")
        //mToolBarDelegate.setToolbarBackgroundOfOtherTheme(context!!, R.color.color_secondary, R.color.white);

        val list : MutableList<VipType> = mutableListOf();
        list.add(VipType());
        list.add(VipType());
        list.add(VipType());

        galleryRv.addDataList(list);

    }

    override fun createPresenter(): ApplyVipPresenter {
        return ApplyVipPreImpl(this)
    }

}