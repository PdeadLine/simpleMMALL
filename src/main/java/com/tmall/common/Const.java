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
}
