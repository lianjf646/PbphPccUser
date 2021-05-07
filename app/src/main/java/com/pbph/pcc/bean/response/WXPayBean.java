package com.pbph.pcc.bean.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/8 0008.
 */

public class WXPayBean extends BaseResponseBean {

    /**
     * code : 200
     * data : {"msg":"ok","sendUrl":"http://47.93.166.247:8084/notify.htm","appid":"wxb647db63d9873f4c","sign":"35BB716FB08D6703444E4220C813D240","orderCode":"1000000551439624","prepayid":"wx20171008084555ba66cd90400436432801","partnerid":"1480919252","packages":"Sign=WXPay","noncestr":"f3f27a324736617f20abbf2ffd806f6d","timestamp":"1507423554"}
     * msg : 成功
     */


    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean implements Serializable {
        /**
         * msg : ok
         * sendUrl : http://47.93.166.247:8084/notify.htm
         * appid : wxb647db63d9873f4c
         * sign : 35BB716FB08D6703444E4220C813D240
         * orderCode : 1000000551439624
         * prepayid : wx20171008084555ba66cd90400436432801
         * partnerid : 1480919252
         * packages : Sign=WXPay
         * noncestr : f3f27a324736617f20abbf2ffd806f6d
         * timestamp : 1507423554
         */

        private String msg;
        private String sendUrl;
        private String appid;
        private String sign;
        private String orderCode;
        private String prepayid;
        private String partnerid;
        private String packages;
        private String noncestr;
        private String timestamp;
        private String orderId;
        private String redcutionAmount;
        private String redcutionAmountText;

        public String getRedcutionAmountText() {
            return redcutionAmountText;
        }

        public void setRedcutionAmountText(String redcutionAmountText) {
            this.redcutionAmountText = redcutionAmountText;
        }

        public String getRedcutionAmount() {
            return redcutionAmount;
        }

        public void setRedcutionAmount(String redcutionAmount) {
            this.redcutionAmount = redcutionAmount;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSendUrl() {
            return sendUrl;
        }

        public void setSendUrl(String sendUrl) {
            this.sendUrl = sendUrl;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
