package com.lingmiao.shop.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.lingmiao.shop.R;
import com.lingmiao.shop.base.IWXConstant;
import com.lingmiao.shop.business.me.bean.RechargeReqVo;
import com.lingmiao.shop.business.me.event.PaySuccessEvent;
import com.lingmiao.shop.business.tools.api.JsonUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Create Date : 2021/5/117:55 AM
 * Auther      : Fox
 * Desc        :
 **/
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        api = WXAPIFactory.createWXAPI(this, IWXConstant.APP_ID, false);
        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        LogUtils.d("req-------->>>>>", JsonUtil.Companion.getInstance().toJson(req));
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                goToGetMsg((ShowMessageFromWX.Req) req);
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
        finish();
    }

    private void goToShowMsg(ShowMessageFromWX.Req req) {
        Toast.makeText(this, req.message.description, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void goToGetMsg(ShowMessageFromWX.Req req) {
        Toast.makeText(this, req.message.description, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        LogUtils.d("resp-------->>>>>", JsonUtil.Companion.getInstance().toJson(resp));
        if(resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    EventBus.getDefault().post(new PaySuccessEvent(RechargeReqVo.PAY_TRADE_CHANNEL_OF_WECHAT));
                    result = R.string.pay_success;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = R.string.pay_cancel;
                    break;
                default:
                    result = R.string.pay_unknown;
                    break;
            }
        }
        Toast.makeText(this, getString(result), Toast.LENGTH_SHORT).show();
        finish();
    }

}
