package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.*
import com.lingmiao.shop.business.goods.api.request.DeliveryRequest
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.business.goods.presenter.GoodsPublishPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsPublishPreImpl
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.isNotBlank
import com.james.common.utils.exts.show
import com.james.common.utils.exts.singleClick
import kotlinx.android.synthetic.main.goods_activity_publish.*
import kotlinx.android.synthetic.main.goods_include_publish_section1.*
import kotlinx.android.synthetic.main.goods_include_publish_section2.*
import kotlinx.android.synthetic.main.goods_include_publish_section3.*
import kotlinx.android.synthetic.main.goods_include_publish_section4.*
import kotlinx.android.synthetic.main.goods_include_publish_section5.*
import kotlinx.android.synthetic.main.goods_include_publish_section6.*
import kotlinx.android.synthetic.main.goods_include_publish_section7.*
import kotlinx.android.synthetic.main.goods_include_publish_section8.*
import kotlinx.android.synthetic.main.goods_include_publish_section_v_time.*

/**
 * Author : Elson
 * Date   : 2020/7/25
 * Desc   : 添加商品页面
 */
class GoodsPublishActivity : BaseActivity<GoodsPublishPre>(), GoodsPublishPre.PublishView {

    companion object {
        const val KEY_GOODS_ID = "KEY_GOODS_ID"

        const val REQUEST_CODE_VIDEO = 1000
        const val REQUEST_CODE_DELIVERY = 1001
        const val REQUEST_CODE_SKU = 1002
        const val REQUEST_CODE_DESC = 1003

        fun openActivity(context: Context, goodsId: String?) {
            val intent = Intent(context, GoodsPublishActivity::class.java)
            intent.putExtra(KEY_GOODS_ID, goodsId)
            context.startActivity(intent)
        }
    }

    private var isVirtualGoods = false;

    // 底部按钮是否展开
    private var isExpand = false

    // 编辑商品时，所携带的商品ID
    private var goodsId: String? = null

    // 添加/编辑商品 的数据实体
    private var goodsVO: GoodsVOWrapper = GoodsVOWrapper()

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_publish
    }

    override fun initBundles() {
        goodsId = intent.getStringExtra(KEY_GOODS_ID)
    }

    override fun createPresenter(): GoodsPublishPre {
        return GoodsPublishPreImpl(this, this)
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(if (goodsId.isNotBlank()) "编辑商品" else "发布商品")
        initSection1View()
        initSection2View()
        initSection3View()
        initSection4View()
        initSection5678View()
        initBottomView()

        mPresenter.loadGoodsInfo(goodsId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            // 视频
            REQUEST_CODE_VIDEO -> {
                data.getStringExtra(GoodsVideoActivity.KEY_VIDEO_URL)?.apply {
                    goodsVO.videoUrl = this
                }
                goodsVideoTv.text = if (goodsVO.videoUrl.isNotBlank()) "已添加" else "请选择"
            }
            // 发货方式
            REQUEST_CODE_DELIVERY -> {
                val deliveryVO =
                    data.getSerializableExtra(DeliverySettingActivity.KEY_Delivery) as? DeliveryRequest
                goodsVO.convert(deliveryVO)
                goodsDeliveryTv.text = if (goodsVO.isSelectDeliveryWay()) "已选择" else "请选择"
            }
            // 规格设置
            REQUEST_CODE_SKU -> {
                val specKeyList =
                    data.getSerializableExtra(SpecSettingActivity.KEY_SPEC_KEY_LIST) as? List<SpecKeyVO>
                val skuList =
                    data.getSerializableExtra(SpecSettingActivity.KEY_SKU_LIST) as? List<GoodsSkuVOWrapper>
                goodsVO.apply {
                    this.isAddSpec = true
                    this.skuList = skuList
                    this.specKeyList = specKeyList
                }
                if (isVirtualGoods) {
                    goodsVirtualSpecTv.text = if (skuList.isNullOrEmpty()) "请设置" else "已设置";
                } else {
                    goodsSpecTv.text = if (skuList.isNullOrEmpty()) "请设置规格" else "已设置"
                }
            }
            // 商品详情
            REQUEST_CODE_DESC -> {
                data.getStringExtra(GoodsDescH5Activity.KEY_DESC)?.apply {
                    goodsVO.intro = this
                    goodsDetailTv.text = if (goodsVO.intro.isNotBlank()) "已添加" else "未添加"
                }
            }
        }
    }

    override fun onSetGoodsType(isVirtual: Boolean) {
        isVirtualGoods = isVirtual;
        setVirtualGoodsView();
    }

    override fun onSetGoodsTypeNew(type: String?) {
        TODO("Not yet implemented")
    }

    override fun onSetUseTimeStr(useTime: String) {
        goodsVirtualUseTimeTv.text = useTime
    }

    override fun onLoadGoodsSuccess(goodsVO: GoodsVOWrapper, center: Boolean) {
        this.goodsVO = goodsVO
        goodsVO.apply {
            // 商品分类
//            goodsCategoryTv.text = Html.fromHtml(categoryName)
            goodsCategoryTv.text = categoryName?.replace("&gt;", "/")
            // 商品名称
            goodsNameEdt.setText(goodsName)
            galleryRv.addDataList(goodsGalleryList)
            // 发货方式
            if (isSelectDeliveryWay()) {
                goodsDeliveryTv.text = "已选择"
            }
            isVirtualGoods = isVirtualGoods() == 1
            if (isVirtualGoods) {
                // 虚拟商品
                setVirtualGoodsView();
                goodsVirtualExpireTv.text = expiryDayText;
                goodsVirtualUseTimeTv.text = availableDate;
                goodsVirtualSpecTv.text = if (this.hasMultiSpec()) "已设置" else "请设置";
            } else {
                // 规格
                if (this.hasMultiSpec()) {
                    goodsSpecTv.setText("已设置")
                    switchBtn.isChecked = true
                    showSection45WithStatus(switchBtn.isChecked, true)
                } else {
                    goodsPriceEdt.setText(price)
                    marketPriceEdt.setText(mktprice)
                    goodsQuantityEdt.setText(quantity)
                    goodsCostEdt.setText(cost)
                    goodsNoEdt.setText(sn)
                    goodsWeightEdt.setText(weight)
                    goodsSKUEdt.setText(upSkuId)
                    goodsIDEdt.setText(sn)
                    switchBtn.isChecked = false
                    if (!goodsId.isNullOrBlank()) {
                        goodsQuantityEdt.isEnabled = false;
                    }
                }
            }
            // 商品分组
            goodsGroupTv.text = shopCatName?.replace("&gt;", "/")
            // 商品视频
            if (videoUrl.isNotBlank()) {
                goodsVideoTv.text = "已选择"
            }
            // 商品详情
            if (intro.isNotBlank()) {
                goodsDetailTv.text = "已添加"
            }
            // 商品卖点
            goodsSellingDescEdt.setText(selling)
        }
    }

    override fun onUpdateCategory(categoryId: String?, categoryName: String?) {
        goodsCategoryTv.text = categoryName
        goodsVO.apply {
            this.categoryId = categoryId
            this.categoryName = categoryName
        }
    }

    override fun onUpdateGroup(groupId: String?, groupName: String?) {
        goodsGroupTv.text = groupName
        goodsVO.apply {
            this.shopCatId = groupId
            this.shopCatName = groupName
        }
    }

    private fun initSection1View() {
        // 切换商品类型
        goodsTypeTv.singleClick {
            mPresenter.showGoodsType(if (isVirtualGoods) 1 else 0)
        }
        // 分类
        goodsCategoryTv.singleClick {
            mPresenter.showCategoryPop()
        }
        // 有效期至
        goodsVirtualExpireTv.singleClick {
            mPresenter?.showExpirePop(goodsVO.expiryDay ?: "")
        }
        // 使用时间
        goodsVirtualUseTimeTv.singleClick {
            mPresenter?.showUseTimePop(goodsVO.availableDate ?: "")
        }
        // 商品套餐
        goodsVirtualSpecTv.singleClick {
            if (goodsVO.categoryId.isNotBlank()) {
                SpecSettingActivity.openActivity(this, REQUEST_CODE_SKU, isVirtualGoods, goodsVO)
            } else {
                showToast("请先选择商品分类")
            }
        }
    }

    override fun onUpdateExpire(item: GoodsUseExpireVo?) {
        goodsVO.expiryDay = item?.value;
        goodsVirtualExpireTv.setText(item?.label);
    }

    override fun onUpdateUseTime(items: List<MultiPickerItemBean>?) {
        var values = items?.map { it?.value }?.joinToString(separator = ",");
        var names = items?.map { it?.name }?.joinToString(separator = ",");
        goodsVO.availableDate = values;
        goodsVirtualUseTimeTv.text = if (names.isNotBlank()) names else "请设置";
    }

    fun setVirtualGoodsView() {
        goodsTypeTv.text = GoodsTypeVO.getName(if (isVirtualGoods) 1 else 0)
        section3RowDeliveryLL.show(!isVirtualGoods)
        // 有效期和使用时间
        sectionVirtualExpireTime.show(isVirtualGoods)
        showSection45WithStatus(false, isExpand)
    }

    private fun initSection2View() {
        galleryRv.setCountLimit(1, 20)
    }

    private fun initSection3View() {
        goodsDeliveryTv.singleClick {
            DeliverySettingActivity.openActivity(
                this,
                REQUEST_CODE_DELIVERY,
                DeliveryRequest.convert(goodsVO)
            )
        }
    }

    private fun initSection4View() {
        goodsSpecTv.singleClick {
            if (goodsVO.categoryId.isNotBlank()) {
                SpecSettingActivity.openActivity(this, REQUEST_CODE_SKU, isVirtualGoods, goodsVO)
            } else {
                showToast("请先选择商品分类")
            }
        }
        switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            showSection45WithStatus(isChecked, isExpand)
        }
    }

    private fun initSection5678View() {
        goodsGroupTv.singleClick {
            mPresenter.showGroupPop()
        }
        goodsVideoTv.singleClick {
            GoodsVideoActivity.openActivity(this, REQUEST_CODE_VIDEO, goodsVO.videoUrl)
        }
        goodsDetailTv.singleClick {
            GoodsDescH5Activity.openActivity(this, REQUEST_CODE_DESC, goodsVO.intro)
        }
    }

    private fun initBottomView() {
        moreTv.singleClick {
            isExpand = !isExpand
            showSection45WithStatus(switchBtn.isChecked, isExpand)
        }
        confirmTv.singleClick {
            clickConfirmView()
        }
    }

    private fun clickConfirmView() {
        goodsVO.apply {
            goodsName = goodsNameEdt.getViewText()
            selling = goodsSellingDescEdt.getViewText()
            goodsGalleryList = galleryRv.getSelectPhotos()

            goodsType = GoodsTypeVO.getValue(if (isVirtualGoods) 1 else 0)
            if (isVirtualGoods) {
                // 虚拟
                price = null
                mktprice = null
                quantity = null
                cost = null
                sn = null
                weight = null
                upSkuId = null
                upGoodsId = null
            } else {
                // 实物
                expiryDay = ""
                availableDate = ""

                if (switchBtn.isChecked) {
                    price = null
                    mktprice = null
                    quantity = null
                    cost = null
                    sn = null
                    weight = null
                    upSkuId = null
                    upGoodsId = null
                } else {
                    price = goodsPriceEdt.getViewText()
                    mktprice = marketPriceEdt.getViewText()
                    quantity = goodsQuantityEdt.getViewText()
                    cost = goodsCostEdt.getViewText()
                    sn = goodsNoEdt.getViewText()
                    weight = goodsWeightEdt.getViewText()
                    upSkuId = goodsSKUEdt.getViewText()
                    upGoodsId = goodsIDEdt.getViewText()
                    skuList = null
                }
            }

        }
        mPresenter.publish(goodsVO, isVirtualGoods, switchBtn.isChecked, false, 1, 0)
    }

    /**
     * 以下几个控件的显示隐藏和 "多规格的开关"、"底部展开/收起按钮"关联
     */
    private fun showSection45WithStatus(isChecked: Boolean, isExpand: Boolean) {
        if (isVirtualGoods) {
            section4Row1Ll.show(false)
            section4RowVirtualSpecLl.show(true)

            section4Row23Ll.show(false)
            section4Row45Ll.show(false)
            section4Row6Ll.show(false)
        } else {
            section4RowVirtualSpecLl.show(false)
            section4Row1Ll.show(isChecked)
            section4Row23Ll.show(!isChecked)
            section4Row45Ll.show(!isChecked && isExpand)
            section4Row6Ll.show(isChecked || isExpand)
        }
        section5678Ll.show(isExpand)

        moreTv.text = if (isExpand) "收起更多信息" else "展开更多信息"
        val drawable =
            if (isExpand) R.mipmap.goods_blue_arrow_up else R.mipmap.goods_blue_arrow_down
        moreTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }
}