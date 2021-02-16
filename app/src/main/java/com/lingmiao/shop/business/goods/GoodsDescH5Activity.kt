package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.FrameLayout
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.goods.presenter.GoodsDescPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsDescPreImpl
import com.lingmiao.shop.business.photo.PhotoHelper
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.isNotBlank
import com.james.common.utils.exts.singleClick
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebViewClient
import kotlinx.android.synthetic.main.goods_activity_h5_desc.*


class GoodsDescH5Activity : BaseActivity<GoodsDescPre>(), GoodsDescPre.DescView {

    companion object {
        const val KEY_DESC = "KEY_DESC"

        fun openActivity(context: Context, requestCode: Int, content: String?) {
            if (context is Activity) {
                val intent = Intent(context, GoodsDescH5Activity::class.java)
                intent.putExtra(KEY_DESC, content)
                context.startActivityForResult(intent, requestCode)
            }
        }
    }

    private var mHTMLContent: String? = null
    private var mAgentWeb: AgentWeb? = null

    private var firstEdit = true
    private var mWebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (mHTMLContent.isNotBlank() && firstEdit) {
                firstEdit = false
                mAgentWeb?.jsAccessEntrace?.quickCallJs("editShow", String(EncodeUtils.base64Encode(mHTMLContent)))
            }
        }
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_h5_desc
    }

    override fun initBundles() {
        mHTMLContent = intent.getStringExtra(KEY_DESC)
    }

    override fun createPresenter(): GoodsDescPre {
        return GoodsDescPreImpl(this)
    }

    override fun initView() {
        window.decorView.background = null
        toolbarView.setTitleContent("商品图文描述")
        toolbarView.setRightListener("保存", R.color.color_333333) {
            mAgentWeb?.jsAccessEntrace?.quickCallJs("editerSubmit",
                ValueCallback { value ->
                    var decodeValue:String = EncodeUtils.urlDecode(value)
                    if(decodeValue.startsWith("\"")){
                        decodeValue = decodeValue.substring(1)
                    }
                    if(decodeValue.endsWith("\"")){
                        decodeValue = decodeValue.substring(0,decodeValue.length-1)
                    }
                    LogUtils.d(decodeValue)
                    val intent = Intent()
                    intent.putExtra(KEY_DESC, decodeValue)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                })
        }

        initWebView()
        initBottomView()
    }

    private fun initWebView() {
//        mBridgeWebView = BridgeWebView(this)
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                webviewFl,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator()
            .setWebViewClient(mWebViewClient)
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) //打开其他应用时，弹窗咨询用户是否前往其他应用
            .createAgentWeb()
            .go(IConstant.getGoodsDetailH5() + "?" + System.currentTimeMillis())


    }

    private fun initBottomView() {
        phoneIv.singleClick {
            PhotoHelper.openAlbum(this, 9, null) { mediaList ->
                mPresenter?.uploadImages(mediaList)
            }
        }
    }

    override fun onUploadSuccess(images: String) {
        mAgentWeb?.jsAccessEntrace?.quickCallJs("showImage", images)
    }
}