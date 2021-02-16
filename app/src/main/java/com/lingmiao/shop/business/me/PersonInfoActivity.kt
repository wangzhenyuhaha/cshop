package com.lingmiao.shop.business.me

import android.text.TextUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.me.bean.My
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.business.me.presenter.PersonInfoPresenter
import com.lingmiao.shop.business.me.presenter.impl.PersonInfoPresenterImpl
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.DialogUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.me_activity_person_info.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File


/**
 *   个人资料
 */
class PersonInfoActivity : BaseActivity<PersonInfoPresenter>(), PersonInfoPresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }
    private var id:String?=null

    override fun getLayoutId(): Int {
        return R.layout.me_activity_person_info
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {
//        id = intent.getStringExtra("id")
//        nickname = intent.getStringExtra("nickname")
        mToolBarDelegate.setMidTitle("个人资料")
        val my:My? = intent.getParcelableExtra<My>("bean")
        if(!TextUtils.isEmpty(my?.face)) GlideUtils.setImageUrl(ivPersonInfoHead, my?.face)
        id = my?.clerkId.toString()
        tvPersonInfoNickname.text = my?.uname
        tvPersonInfoPhone.text = my?.mobile


        rlPersonInfoNickname.setOnClickListener {
            DialogUtils.showInputDialog(this, "修改昵称", "昵称:", null) {
                tvPersonInfoNickname.text = it
                showDialogLoading()
                val bean = PersonInfoRequest()
                bean.uname = it
                bean.id = id
                mPresenter.requestPersonInfoData(bean)
            }
        }
        rlPersonInfoPhone.setOnClickListener { ActivityUtils.startActivity(UpdatePhoneActivity::class.java) }
        ivPersonInfoHead.setOnClickListener {
            //            val pop = MediaMenuPop(this)
//            pop.setOnClickListener { type -> LogUtils.d("type:" + type) }
//            pop.showPopupWindow()
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .loadImageEngine(GlideEngine.createGlideEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: List<LocalMedia?>) {
                        // 结果回调
                        val localMedia = result[0]

                        GlideUtils.setImageUrl(ivPersonInfoHead,OtherUtils.getImageFile(localMedia))
                        mCoroutine.launch {
                            showDialogLoading()
                            val uploadFile =
                                CommonRepository.uploadFile(OtherUtils.getImageFile(localMedia), true)
                            if (uploadFile.isSuccess) {
                                LogUtils.d(uploadFile.data.url)
                                val bean = PersonInfoRequest()
                                bean.face = uploadFile.data.url
                                bean.id = id
                                mPresenter.requestPersonInfoData(bean)
                            }else{
                                hideDialogLoading()
                            }
                        }
                    }

                    override fun onCancel() {
                        // 取消
                    }
                })
        }
    }

    override fun createPresenter(): PersonInfoPresenter {
        return PersonInfoPresenterImpl(this, this)
    }

    override fun onPersonInfoSuccess() {
        hideDialogLoading()
        ToastUtils.showShort("修改成功")
        EventBus.getDefault().post(PersonInfoRequest())
    }

    override fun onPersonInfoError() {
        hideDialogLoading()
    }

}