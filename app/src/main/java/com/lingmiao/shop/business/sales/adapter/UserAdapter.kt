package com.lingmiao.shop.business.sales.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.util.DATE_FORMAT
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.stampToDate

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class UserAdapter : BaseQuickAdapter<UserVo, BaseViewHolder>(R.layout.sales_adapter_user) {

    private var isBatchEditModel : Boolean = false;


    override fun convert(helper: BaseViewHolder, item: UserVo?) {
        item?.apply {


            GlideUtils.setImageUrl(helper.getView(R.id.userAvatar), item?.face, R.mipmap.main_shop_logo_default)
            //main_shop_logo_default
            helper.setGone(R.id.userCheckCb, isBatchEditModel);

            helper.setText(R.id.userNameTv, item?.nickname ?: "");
            helper.setText(R.id.userPhoneTv, item?.mobile ?: "");

            helper.setText(R.id.userInTypeTv, item?.sourceName ?: "");

            helper.setText(R.id.userOrderTv, String.format("%s", item?.orderNum));

            helper.setText(R.id.userAddressTv, item?.fullAddress ?: "");

            helper.setText(R.id.userInTimeTv, stampToDate(item?.createTime?:0, DATE_FORMAT));
            //helper.setText(R.id.userInTimeTv, TimeUtils.millis2String(item?.createTime?:0, "yyyy-MM-dd"));

            helper.addOnClickListener(R.id.userOrderDetailTv)
            helper.addOnClickListener(R.id.userPortraitTv)
            helper.addOnClickListener(R.id.userPhoneTv)
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