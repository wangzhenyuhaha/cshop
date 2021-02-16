package com.lingmiao.shop.business.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.ExpressCompanyVo

class ExpressCompanyAdapter : BaseQuickAdapter<ExpressCompanyVo, BaseViewHolder>(R.layout.tools_adapter_express_company) {

    override fun convert(helper: BaseViewHolder, item: ExpressCompanyVo?) {
        helper.setText(R.id.tv_company_name, item?.logisticsCompanyDo?.name);
        helper.setText(R.id.tv_company_status, getStatusName(item?.open ?: false));
        helper.setChecked(R.id.sb_company_status_edit, item?.open ?:false);
        helper.addOnClickListener(R.id.sb_company_status_edit);
    }

    private fun getStatusName(flag : Boolean) : String {
        return if(flag ) "已选择" else "未选择";
    }

    fun onStatusEdit(flag : Boolean ?, position: Int) {
        val item = data[position];
        item.open = flag;
    }
}
