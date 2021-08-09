package com.lingmiao.shop.business.me

import android.view.KeyEvent
import android.view.WindowManager
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.me.presenter.HelpVideoPresenter
import com.lingmiao.shop.business.me.presenter.impl.HelpVideoPresenterImpl
import com.lingmiao.shop.databinding.MeActivityHelpVideoBinding


/**
Create Date : 2021/8/811:00 下午
Auther      : Fox
Desc        :
 **/
class HelpVideoActivity : BaseVBActivity<MeActivityHelpVideoBinding, HelpVideoPresenter>(), HelpVideoPresenter.View {

    companion object {
        val TAG = "HelpViewActivity";
    }
    override fun createPresenter(): HelpVideoPresenter {
        return HelpVideoPresenterImpl(this);
    }

    override fun getViewBinding(): MeActivityHelpVideoBinding {
        return MeActivityHelpVideoBinding.inflate(layoutInflater);
    }

    override fun useBaseLayout(): Boolean {
        return true;
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("帮助视频")
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mBinding.player.hideControls();
        mBinding.player.setChangeScreen(false);

        mBinding.player.setUp(this, "https://assets.mixkit.co/videos/preview/mixkit-a-girl-blowing-a-bubble-gum-at-an-amusement-park-1226-large.mp4", "");
        mBinding.player.start();
    }

    override fun onResume() {
        super.onResume()
        mBinding.player.resume();
    }

    override fun onPause() {
        super.onPause()
        mBinding.player.pause();
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.player.stopPlay();
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val b: Boolean = mBinding.player.onKeyDown(keyCode)
        return b || super.onKeyDown(keyCode, event)
    }
}