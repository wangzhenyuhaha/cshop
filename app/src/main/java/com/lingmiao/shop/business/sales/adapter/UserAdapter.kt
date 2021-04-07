package com.lingmiao.shop.business.sales.adapter

import android.widget.CompoundButton
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.util.GlideUtils
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import org.greenrobot.eventbus.EventBus

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class UserAdapter() : BaseQuickAdapter<UserVo, BaseViewHolder>(R.layout.sales_adapter_user) {

    private var isBatchEditModel : Boolean = false;


    override fun convert(helper: BaseViewHolder, item: UserVo?) {
        item?.apply {


            helper.setGone(R.id.userCheckCb, isBatchEditModel);

            helper.setText(R.id.userNameTv, "张三");
            helper.setText(R.id.userPhoneTv, "18621774835");

            helper.setText(R.id.userAddressTv, "上海市");
            helper.setText(R.id.userInTypeTv, "扫码");

            helper.setText(R.id.userInTimeTv, "2020-10-01");

            helper.setText(R.id.userOrderTv, "0");
            helper.addOnClickListener(R.id.userOrderDetailTv)
            helper.addOnClickListener(R.id.userPortraitTv)
//            setOnCheckedChangeListener(helper.getView(R.id.userCheckCb), isChecked ?: false) { buttonView: CompoundButton?, isChecked: Boolean ->
//                item?.isChecked = isChecked;
//            }

            helper.setChecked(R.id.userCheckCb, item?.isChecked?:false);
            helper.addOnClickListener(R.id.userCheckCb);
        }
    }


    fun setBatchEditModel(flag : Boolean) {
        isBatchEditModel = flag;
        notifyDataSetChanged();
    }


}