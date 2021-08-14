package com.lingmiao.shop.business.me

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import com.danikula.videocache.HttpProxyCacheServer
import com.danikula.videocache.ProxyVideoCacheManager
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.me.presenter.HelpVideoPresenter
import com.lingmiao.shop.business.me.presenter.impl.HelpVideoPresenterImpl
import com.lingmiao.shop.databinding.MeActivityHelpVideoBinding
import xyz.doikki.videocontroller.StandardVideoController


/**
Create Date : 2021/8/811:00 下午
Auther      : Fox
Desc        :
 **/
class HelpVideoActivity : BaseVBActivity<MeActivityHelpVideoBinding, HelpVideoPresenter>(), HelpVideoPresenter.View {

    var url: String? = "";
    var title: String? = "";
    companion object {
        val TAG = "HelpViewActivity";

        fun openActivity(context: Context, url: String?, title: String?) {
            if (context is Activity) {
                val intent = Intent(context, HelpVideoActivity::class.java)
                intent.putExtra("url", url)
                intent.putExtra("title", title)
                context.startActivity(intent)
            }
        }
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

    override fun initBundles() {
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle(title ?:"帮助视频")

        val cacheServer: HttpProxyCacheServer = ProxyVideoCacheManager.getProxy(this)
        val proxyUrl = cacheServer.getProxyUrl(url)
        mBinding.player.setUrl(proxyUrl)
        val controller = StandardVideoController(this)
        controller.addDefaultControlComponent(title ?:"帮助视频", false)
        mBinding.player.setVideoController(controller)
        mBinding.player.start()
    }

    override fun onResume() {
        super.onResume()
        mBinding.player.resume();
    }

    override fun onPause() {
        super.onPause()
        mBinding.player.pause();
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val b: Boolean = mBinding.player.onKeyDown(keyCode, event);
        return b || super.onKeyDown(keyCode, event)
    }
}