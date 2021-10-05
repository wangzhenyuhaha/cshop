package com.lingmiao.shop.printer;

import static com.lingmiao.shop.util.DateUtilsKt.stampToDate;

import com.lingmiao.shop.R;
import com.lingmiao.shop.business.order.bean.OrderList;
import com.lingmiao.shop.business.order.bean.Sku;

import net.posprinter.utils.DataForSendToPrinterPos58;
import net.posprinter.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create Date : 2021/10/55:42 下午
 * Auther      : Fox
 * Desc        :
 **/
public class Printer {

    List<byte[]> list = new ArrayList();
    OrderList orderList;

    public Printer(OrderList orderList) {
        this.orderList = orderList;
    }

    public List<byte[]> getPrintList() {
        list.clear();
        // 标题
        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(0,00));//设置初始位置
//        list.add(DataForSendToPrinterPos58.selectCharacterSize(14));//字体放大一倍
        list.add(StringUtils.strTobytes(getNotNullStr(orderList.getSellerName())));
//        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(250,00));
//        list.add(StringUtils.strTobytes("#1"));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());


        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.selectAlignment(3));
        list.add(StringUtils.strTobytes(String.format("下单时间：%s", stampToDate(orderList.getCreateTime() == null ? 0 : orderList.getCreateTime()))));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());


        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.selectAlignment(3));
        list.add(StringUtils.strTobytes(String.format("订单编号：%s", getNotNullStr(orderList.getSn()))));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());

        for(Sku it : orderList.getSkuList()) {
            list.add(DataForSendToPrinterPos58.initializePrinter());
            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(0,00));
            list.add(StringUtils.strTobytes(getNotNullStr(it.getName())));
            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(250,00));
            list.add(StringUtils.strTobytes(String.format("x%s", it.getNum() == null ? 0 : it.getNum())));
            list.add(DataForSendToPrinterPos58.printAndFeedLine());
//            list.add(DataForSendToPrinterPos58.printAndFeedLine());

            list.add(DataForSendToPrinterPos58.initializePrinter());
            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(0,00));
            list.add(StringUtils.strTobytes(it.getSpecValues() == null || it.getSpecValues().isEmpty() ? "" : String.format("%s%s", "规格：",it.getSpecValues())));
            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(250,00));
            list.add(StringUtils.strTobytes(String.format("%s元", it.getSubtotal() == null ? 0 : it.getSubtotal())));
            list.add(DataForSendToPrinterPos58.printAndFeedLine());
            list.add(DataForSendToPrinterPos58.printAndFeedLine());
        }

        // 加购
        if(orderList.getReplenishRemark() != null && orderList.getReplenishRemark().length() > 0) {
            list.add(DataForSendToPrinterPos58.initializePrinter());
            list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(10));
            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(0,00));
            list.add(StringUtils.strTobytes(String.format("加购：%s", orderList.getReplenishRemark())));
            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(250,00));
            list.add(StringUtils.strTobytes(String.format("%s元", orderList.getReplenishPrice() == null ? 0 : orderList.getReplenishPrice())));
            list.add(DataForSendToPrinterPos58.printAndFeedLine());
            list.add(DataForSendToPrinterPos58.printAndFeedLine());
        }
        // 餐具
        if(orderList.getTableAwareHint() != null && orderList.getTableAwareHint().length() > 0) {
            list.add(DataForSendToPrinterPos58.initializePrinter());
            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(0,00));
            list.add(StringUtils.strTobytes(orderList.getTableAwareHint()));
            list.add(DataForSendToPrinterPos58.printAndFeedLine());
            list.add(DataForSendToPrinterPos58.printAndFeedLine());
        }
        // 打包费
        if(orderList.getPackagePrice() != null && orderList.getPackagePrice() > 0) {
            list.add(DataForSendToPrinterPos58.initializePrinter());
            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(0,00));
            list.add(StringUtils.strTobytes("打包费"));
            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(250,00));
            list.add(StringUtils.strTobytes(String.format("%s", orderList.getPackagePrice())));
            list.add(DataForSendToPrinterPos58.printAndFeedLine());
            list.add(DataForSendToPrinterPos58.printAndFeedLine());
        }

        // 总计
        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.selectAlignment(3));
        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(0,00));
        list.add(StringUtils.strTobytes(String.format("共%d件商品，实付款￥%s", orderList.getSkuList() == null ? 0 : orderList.getSkuList().size(), orderList.getOrderAmount() == null ? 0 : orderList.getOrderAmount())));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());

        // 隐私
        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(00,00));
        list.add(DataForSendToPrinterPos58.setLineSpaceing(12));
        list.add(DataForSendToPrinterPos58.selectOrCancelUnderlineModel(10));
        list.add(DataForSendToPrinterPos58.setDefultLineSpacing());
        list.add(StringUtils.strTobytes("为保护顾客隐私，顾客地址已隐藏，\n请使用C店商家APP或者配送端查看."));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());

        // 客户
        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.selectCharacterSize(17));
        list.add(StringUtils.strTobytes(getNotNullStr(orderList.getShipName())));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());

        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.selectCharacterSize(17));
        list.add(StringUtils.strTobytes(orderList.getShipMobile() == null ? "" : orderList.getShipMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());

        // 地址
        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.selectCharacterSize(17));
        list.add(StringUtils.strTobytes(getNotNullStr(orderList.getSimpleAddress())));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());

        // 备注
        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(00,00));
        list.add(DataForSendToPrinterPos58.setLineSpaceing(12));
        list.add(DataForSendToPrinterPos58.selectOrCancelUnderlineModel(10));
        list.add(DataForSendToPrinterPos58.setDefultLineSpacing());
        list.add(StringUtils.strTobytes(String.format("备注：%s", orderList.getRemark() == null ? "" : orderList.getRemark())));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());

        // 打印日期
        list.add(DataForSendToPrinterPos58.initializePrinter());
        list.add(DataForSendToPrinterPos58.selectAlignment(3));
        list.add(StringUtils.strTobytes(String.format("打印时间：%s", stampToDate(new Date().getTime()))));
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());

        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        list.add(DataForSendToPrinterPos58.printAndFeedLine());
        return list;
    }

    String getNotNullStr(String str) {
        return str == null ? "" : str;
    }
}
