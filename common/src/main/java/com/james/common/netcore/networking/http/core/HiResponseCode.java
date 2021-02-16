package com.james.common.netcore.networking.http.core;

/**
 * @author elson, email:liuqi2elson@163.com
 * @date 2020/7/10
 * @Desc 状态码
 */
public class HiResponseCode {

    //token失效,内部使用
    public static final int TOKEN_EXPIRE_INTERNAL = -10003;
    //内部code
    public static final int UNKNOW_FAILED = -999999;
    public static final int NET_FAILED = -10001;
    public static final int DATA_ERROR = -10002;

    //code初始值，不能为0
    public static final int INIT_CODE = UNKNOW_FAILED;
    //code 取消
    public static final int CANCEL_CODE = -2;

    //token失效
    public static final int TOKEN_EXPIRE = 103;

    //系统内部错误
    public static final int SYSTEM_ERROR = 101;

    //缺少参数
    public static final int MISS_PARAM = 102;

    //参数非法
    public static final int INVALID_PARAM = 102;

}
