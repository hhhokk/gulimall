package com.gulimall.common.constant;

/**
 * @author zy
 * @create 2022-03-29-22:00
 */
public class WareConstant {

    public enum PurchaseStatusEnum{
        CRESTED(0,"新建"),
        ASSIGNED(1,"已分配"),
        RECEIVE(2,"已领取"),
        FINISH(3,"已完成"),
        HASERROR(4,"有异常");


        private int code;
        private String msg;

        PurchaseStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }


    public enum PurchaseDetailStatusEnum{
        CRESTED(0,"新建"),
        ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),
        FINISH(3,"已完成"),
        HASERROR(4,"采购失败");


        private int code;
        private String msg;

        PurchaseDetailStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }
}
