package com.james.common.netcore.networking.http.core;


import androidx.annotation.Nullable;

/**
 * @author elson, email:liuqi2elson@163.com
 * @date 2020/7/9
 * @Desc 数据包装类
 */
public class HiResponse<T> {

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code = HiResponseCode.INIT_CODE;
    private String msg;

    /**
     * 有可能为null，因为是java写的没声明nullable，
     * 所以kotlin里使用不会添加问号。但是现在添加问号使用的地方可能需要修改，
     * 因此在kotlin协程中自行添加问号判断空。
     * <p>
     * 不过为null的情况，一般是接口不需要返回数据才会返回null，
     * 其他情况一般不会返回null，返回一个空对象{}。
     * 这个可以和后台沟通
     */
    private T data;

    private boolean isCanceled = false;
    private boolean isException = false;

    @Nullable
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HiResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isSuccess() {
        return this.code == 0;
    }


    public boolean isApiFailed() {
        return this.code != HiResponseCode.INIT_CODE && this.code != 0 && !isTokenError();
    }

    /**
     * 判断是否被取消，仅限协程使用
     */
    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    /**
     * 判断是否发生异常，仅限协程使用
     */
    public boolean isException() {
        return isException || this.code == HiResponseCode.INIT_CODE;
    }

    public void setException(boolean exception) {
        isException = exception;
    }

    public boolean isTokenError() {
        return this.code == HiResponseCode.TOKEN_EXPIRE || this.code == HiResponseCode.TOKEN_EXPIRE_INTERNAL;
    }

    public HiResponse() {
    }

    public HiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    private boolean isCache = false;

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }
}
