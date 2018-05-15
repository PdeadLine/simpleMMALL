package com.tmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * create by sintai
 */

public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Cart{
        int CHECKED=1;//即购物车选中状态
        int UN_CHECKED =0;//购物车未选中状态
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public interface Role{
        int ROLE_CUSTOMER=0;//普通用户
        int ROLE_ADMIN=1;//管理员用户
    }

    public enum ProductStatusEnum {
        ON_SALE(1,"在售");
        private int code;
        private String value;

        ProductStatusEnum(int code,String value){
            this.code=code;
            this.value=value;
        }
        public int getCode() {
            return code;
        }
        public String getValue() {
            return value;
        }
    }
    //订单状态枚举类
    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未付款"),
        PAID(20,"已支付"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单已完成"),
        ORDER_CLOSE(60,"订单关闭");
        OrderStatusEnum(int code,String value) {
            this.value=value;
            this.code=code;
        }
        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
        private String value;
        private int code;
    }

    //交易状态常量
    public interface AlipayCallBack{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }
    //付款平台枚举类
    public enum PayPlatformEnum{
        ALIPAY(1,"支付宝")
        ;
        PayPlatformEnum(int code,String value) {
            this.value=value;
            this.code=code;
        }
        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
        private String value;
        private int code;
    }

    public enum PaymentTypeEnum{
        ON_LINE_PAY(1,"在线支付")
        ;
        PaymentTypeEnum(int code,String value) {
            this.value=value;
            this.code=code;
        }
        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
        private String value;
        private int code;

    }

}
